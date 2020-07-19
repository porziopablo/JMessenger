package configurador;

import directorio.Directorio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.ArrayList;

import persistencia.FactoryPersistencia;

public class Configurador implements IConfiguracion{
    
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
    public Object[] cargarConfiguracion(String origen) throws IOException 
    {
        final String SEPARADOR = ", *"; /* regex */
        final String ENCODING = "UTF-8";
        final int CANT_DATOS = 3;
        final int MIN_DIRECTORIOS = 2;
        final int CANT_DATOS_DIRECTORIO = 2;
        
        BufferedReader lector;
        String ruta = System.getProperty("user.dir") + File.separator + origen, linea, tipoPersistencia, ipDirectorio;
        String[] datos;
        Object[] configuracion = new Object[CANT_DATOS];
        int puertoDirectorio, puertoEmisor, cantidadDirectorios;
        ArrayList<Directorio> directorios = new ArrayList<Directorio>();
        
        lector = new BufferedReader(new InputStreamReader(new FileInputStream(ruta), ENCODING));
        linea = lector.readLine();
        if (linea != null)
            datos = linea.split(SEPARADOR);
        else
            throw new IOException("El archivo de configuración se encuentra vacío.");
        
        if ((datos.length == CANT_DATOS))
        {                    
            try
            {
                puertoEmisor = Integer.parseInt(datos[0]);        /* puerto emisor */
                tipoPersistencia = datos[1];                      /* tipo de persistencia */
                cantidadDirectorios = Integer.parseInt(datos[2]); /* cantidad directorios */
            }
            catch (NumberFormatException e)
            {
                throw new IOException("El puerto ingresado o la cantidad de directorios contienen caracteres no numéricos: " 
                                      + e.getMessage());
            }
        }
        else
            throw new IOException("faltan o sobran datos.");
        
        if (! ((puertoEmisor >= 0) && (puertoEmisor <= 65535)))
            throw new IOException("el puerto para escuchar al Emisor debe ser un entero en el rango [0-65535].");
        if (! tipoPersistencia.equalsIgnoreCase(FactoryPersistencia.TIPO1))
            throw new IOException("el tipo de persistencia no es valido");
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
        
        configuracion[0] = puertoEmisor;
        configuracion[1] = tipoPersistencia;
        configuracion[2] = directorios;
        
        return configuracion;
    }
}
