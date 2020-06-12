package configuracion;

import desencriptacion.FactoryDesencriptacion;

import directorio.Directorio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.ArrayList;

public class Configurador implements IConfiguracion {
    
    private static Configurador instance = null;
    
    private Configurador()
    {
        super();
    }
    
    public static synchronized Configurador getInstance()
    {
        if (instance == null)
            instance = new Configurador();
        
        return instance;
    }

    @Override
    public Object[] cargarConfiguracion(String origen) throws IOException{
        
        final String SEPARADOR = ", *"; /* regex */
        final String ENCODING = "UTF-8";
        final int CANT_DATOS = 3;
        final int MIN_DIRECTORIOS = 2;
        final int CANT_DATOS_DIRECTORIO = 2;
        
        BufferedReader lector;
        String ruta = System.getProperty("user.dir") + File.separator + origen, linea, ipDirectorio;
        String[] datos;
        Object[] configuracion = new Object[CANT_DATOS];
        int puertoDirectorio, cantidadDirectorios = 0;
        ArrayList<Directorio> directorios = new ArrayList<Directorio>();
        
        lector = new BufferedReader(new InputStreamReader(new FileInputStream(ruta), ENCODING));
        linea = lector.readLine();          
        datos = linea.split(SEPARADOR);
        
        if (datos.length == CANT_DATOS)
        {
            try
            {
                configuracion[0] = datos[0].toUpperCase();        /* algoritmo encriptacion */
                configuracion[1] = datos[1];                      /* llave encriptacion */
                cantidadDirectorios = Integer.parseInt(datos[2]);                      /* cant. de directorios */
            }
            catch (NumberFormatException e)
            {
                throw new IOException("La cantidad de directorios contiene caracteres no numéricos: " + e.getMessage());
            }
        }
        else
            throw new IOException("faltan o sobran datos.");
        if (! ((String) configuracion[0]).equalsIgnoreCase(FactoryDesencriptacion.AES))
            throw new IOException("el algorimto de encriptación elegido no está soportado.");
        if (((String) configuracion[1]).length() < 1)
            throw new IOException("la llave de encriptación debe tener longitud mayor o igual a 1.");  
        if (cantidadDirectorios < MIN_DIRECTORIOS)
            throw new IOException("Debe haber al menos datos de " + MIN_DIRECTORIOS + " directorio/s.");
        
        for (int i = 0; i < cantidadDirectorios; i++)
        {
            linea = lector.readLine();
            if (linea == null)
                throw new IOException("Faltan datos de " + (cantidadDirectorios - i) + " directorio/s.");
            datos = linea.split(SEPARADOR);
            
            if (datos.length == CANT_DATOS_DIRECTORIO)
            {
                try
                {
                    ipDirectorio = datos[0];                       /* IP directorio */
                    puertoDirectorio = Integer.parseInt(datos[1]); /* puerto directorio */
                }
                catch (NumberFormatException e)
                {
                    throw new IOException("El puerto del directorio nro " + (i+1) 
                                          + " contiene caracteres no numéricos: " + e.getMessage());
                }
            }
            else
                throw new IOException("faltan o sobran datos del directorio nro " + (i+1) + ".");
            if (! ((puertoDirectorio >= 0) && (puertoDirectorio <= 65535)))
                throw new IOException("el puerto del directorio nro " + (i+1) + " debe ser un entero en el rango [0-65535].");
            directorios.add(new Directorio(ipDirectorio, puertoDirectorio));
        }
        
        lector.close();
        configuracion[2] = directorios;
        
        return configuracion;
    }
}
