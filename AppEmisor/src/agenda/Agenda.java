package agenda;

import java.beans.XMLDecoder;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.io.PrintWriter;

import java.net.Socket;

import java.util.Iterator;

import java.util.TreeSet;

import usuarios.Destinatario;

public class Agenda
{
    private String ipDirectorio;
    private int puertoDirectorio;
    
    public Agenda()
    {
        this.ipDirectorio = "192.168.0.1";
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
    
    public Iterator<Destinatario> actualizarDestinatarios()
    {
        final String ACTUALIZAR_AGENDA = "actualizar_agenda";
        
        TreeSet<Destinatario> destinatarios = new TreeSet<Destinatario>();
        XMLDecoder entrada;
        Socket socket;
        PrintWriter salida;
        
        /* SOLO PARA TESTEAR UI */
        
//        destinatarios.add(new Destinatario("A", "192.168.0.9", "1234", true));
//        destinatarios.add(new Destinatario("B", "192.168.0.192", "1234", false));
//        destinatarios.add(new Destinatario("C", "234.168.0.9", "1234", false));
//        destinatarios.add(new Destinatario("D", "300.168.0.9", "1234", true));
//        destinatarios.add(new Destinatario("E", "200.168.0.9", "1234", true));
        
        /* FIN CODIGO TEST UI */
        
        try
        {
            socket = new Socket(this.ipDirectorio, this.puertoDirectorio);
            salida = new PrintWriter(socket.getOutputStream(), true);
            entrada = new XMLDecoder(new BufferedInputStream(socket.getInputStream()));
            
            salida.println(ACTUALIZAR_AGENDA);
            destinatarios = (TreeSet<Destinatario>) entrada.readObject();
            entrada.close();
        }  
        catch (IOException e)
        {
            System.out.println("Error al recibir agenda desde el Directorio: " + e.getMessage());
        }

        return destinatarios.iterator();
    }
}
