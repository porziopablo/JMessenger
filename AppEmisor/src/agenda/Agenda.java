package agenda;

import java.beans.XMLDecoder;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.Socket;

import java.util.Iterator;

import java.util.TreeMap;

import usuarios.Destinatario;

public class Agenda implements IActualizacionDestinatarios
{
    private String ipDirectorio;
    private int puertoDirectorio;
    
    public Agenda()
    {
        this.ipDirectorio = "192.168.0.192";
        this.puertoDirectorio = 1235; /* valores por defecto */
        this.cargarConfiguracion();
    }
    
    private void cargarConfiguracion()
    {
        final String NOMBRE_ARCHIVO = "config_emisor.txt";
        final String SEPARADOR = ", *"; /* regex */
        final String ENCODING = "UTF-8";
        final int CANT_DATOS = 2;
        
        BufferedReader lector;
        String ruta = System.getProperty("user.dir") + File.separator + NOMBRE_ARCHIVO, linea;
        String[] datos;

        try
        {
            lector = new BufferedReader(new InputStreamReader(new FileInputStream(ruta), ENCODING));
            linea = lector.readLine();
            lector.close();
            
            datos = linea.split(SEPARADOR);
            if ((datos.length == CANT_DATOS))
            {
                this.ipDirectorio = datos[0];
                this.puertoDirectorio = Integer.parseInt(datos[1]);
            }
            else
                throw new IOException("faltan o sobran datos.");
            
            if (! ((this.puertoDirectorio >= 0) && (this.puertoDirectorio <= 65535)))
                throw new IOException("el puerto para comunicarse con el Directorio debe ser un entero en el rango [0-65535].");
        }
        catch(IOException e)
        {
            System.out.println("Error al cargar configuracion: " + e.getMessage());
        }
        catch(NumberFormatException e)
        {
            System.out.println("Error al cargar configuracion: " + e.getMessage());
        }
    }
    
    @Override
    public Iterator<Destinatario> actualizarDestinatarios()
    {
        TreeMap<String, Destinatario> destinatarios = new TreeMap<String, Destinatario>();
        XMLDecoder entrada;
        Socket socket;
                
        try
        {
            socket = new Socket(this.ipDirectorio, this.puertoDirectorio);
            entrada = new XMLDecoder(new BufferedInputStream(socket.getInputStream()));
            
            destinatarios = (TreeMap<String, Destinatario>) entrada.readObject();
            entrada.close();
        }  
        catch (IOException e)
        {
            System.out.println("Error al recibir agenda desde el Directorio: " + e.getMessage());
        }

        return destinatarios.values().iterator();
    }
}
