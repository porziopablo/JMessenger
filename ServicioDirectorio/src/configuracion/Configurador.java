package configuracion;

import directorio.Directorio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.ArrayList;

public class Configurador implements IConfiguracion
{
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
        final int CANT_DATOS = 4;
        final int MIN_DIRECTORIOS = 2;
        final int CANT_DATOS_DIRECTORIO = 2;
        
        BufferedReader lector;
        String ruta = System.getProperty("user.dir") + File.separator + origen, linea, ipDirectorio;
        String[] datos;
        Object[] configuracion = new Object[CANT_DATOS];
        int puertoDestinatario, puertoEmisor, puertoReplicacion, cantidadDirectorios;
        ArrayList<Directorio> directorios = new ArrayList<Directorio>();
        
        lector = new BufferedReader(new InputStreamReader(new FileInputStream(ruta), ENCODING));
        linea = lector.readLine();
        if (linea != null)
            datos = linea.split(SEPARADOR);
        else
            throw new IOException("El archivo de configuración se encuentra vacío.");
        
        if (datos.length == CANT_DATOS)
        {                    
            try
            {
                puertoDestinatario = Integer.parseInt(datos[0]);  /* puerto destinatario */
                puertoEmisor = Integer.parseInt(datos[1]);        /* puerto emisor */
                puertoReplicacion = Integer.parseInt(datos[2]);   /* puerto replicacion */  
                cantidadDirectorios = Integer.parseInt(datos[3]); /* cantidad directorios */
            }
            catch (NumberFormatException e)
            {
                throw new IOException("El puerto ingresado o la cant. de directorios contiene caracteres no numéricos: " 
                                      + e.getMessage());
            }
        }
        else
            throw new IOException("faltan o sobran datos.");
        
        if (! ((puertoEmisor >= 0) && (puertoEmisor <= 65535)))
            throw new IOException("el puerto para enviar mensajes al Almacen debe ser un entero en el rango [0-65535].");
        if (! ((puertoDestinatario >= 0) && (puertoDestinatario <= 65535)))
            throw new IOException("el puerto para recibir confirmaciones de recepción debe ser un entero en el rango [0-65535].");
        if (! ((puertoReplicacion >= 0) && (puertoReplicacion <= 65535)))
            throw new IOException("el puerto para replicacion debe ser un entero en el rango [0-65535].");        
        if (puertoEmisor == puertoDestinatario)
            throw new IOException("El puerto para atender destinatarios debe ser diferente al utilizado para atender emisores.");
        if (puertoEmisor == puertoReplicacion)
            throw new IOException("El puerto para atender emisores debe ser diferente al de replicacion.");
        if (puertoDestinatario == puertoReplicacion)
            throw new IOException("El puerto para atender destinatarios debe ser diferente al de replicacion.");
        if (cantidadDirectorios < MIN_DIRECTORIOS)
            throw new IOException("Debe haber al menos datos de " + MIN_DIRECTORIOS + " directorio/s.");
        
        configuracion[0] = puertoDestinatario;
        configuracion[1] = puertoEmisor;
        configuracion[2] = puertoReplicacion;
        
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
                    ipDirectorio = datos[0];                        /* IP directorio */
                    puertoReplicacion = Integer.parseInt(datos[1]); /* puerto replicacion */
                }
                catch (NumberFormatException e)
                {
                    throw new IOException("El puerto del directorio nro " + (i+1) 
                                          + " contiene caracteres no numéricos: " + e.getMessage());
                }
            }
            else
                throw new IOException("faltan o sobran datos del directorio nro " + (i+1) + ".");
            if (! ((puertoReplicacion >= 0) && (puertoReplicacion <= 65535)))
                throw new IOException("el puerto del directorio nro " + (i+1) + " debe ser un entero en el rango [0-65535].");
            directorios.add(new Directorio(ipDirectorio, puertoReplicacion));
        }
        lector.close();
        configuracion[3] = directorios;
        
        return configuracion;
    }
}