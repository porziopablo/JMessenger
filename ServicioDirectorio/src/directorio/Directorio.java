package directorio;

import java.beans.XMLEncoder;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.io.PrintWriter;

import java.net.ServerSocket;

import java.net.Socket;

import java.util.Date;
import java.util.HashMap;
import java.util.TreeSet;

import usuarios.Destinatario;

public class Directorio
{
    public static final String DESTINATARIO_LOG_UP = "Log Up";
    public static final String DESTINATARIO_ONLINE = "Online";
    public static final String DESTINATARIO_OFFLINE = "Offline";
    
    public static final int REG_EXITOSO = 1;
    public static final int REG_FALLIDO = 0;
    
    public static final int MAX_ESPERA = 5; /* SEGUNDOS */ /* probar y ajustar */
    
    private TreeSet<Destinatario> listado;
    private HashMap<String, Date> fechasConexion;
    private int puertoDestinatario, puertoEmisor;
    private Object lock;
    
    public Directorio()
    {
        this.listado = new TreeSet<Destinatario>();
        this.fechasConexion = new HashMap<String, Date>();
        this.puertoDestinatario = 1234; /* valores por defecto */
        this.puertoEmisor = 1235;
        this.lock = new Object();
        this.cargarConfiguracion();
    }
    
    private void cargarConfiguracion()
    {
        final String NOMBRE_ARCHIVO = "config_directorio.txt";
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
                this.puertoDestinatario = Integer.parseInt(datos[0]);
                this.puertoEmisor = Integer.parseInt(datos[1]);
            }
            else
                throw new IOException("faltan o sobran datos.");
            
            if (! ((this.puertoDestinatario >= 0) && (this.puertoDestinatario <= 65535)))
                throw new IOException("el puerto para atender a los destinatarios debe ser un entero en el rango [0-65535].");
            if (! ((this.puertoEmisor >= 0) && (this.puertoEmisor <= 65535)))
                throw new IOException("el puerto para atender a los emisores debe ser un entero en el rango [0-65535].");
            if (this.puertoEmisor == this.puertoDestinatario)
                throw new IOException("El puerto para atender destinatarios debe ser diferente al utilizado para atender emisores.");
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
    
    public void escucharDestinatarios()
    {
        /* hacer */
    }
    
    public void atenderEmisores()
    {
        new Thread()
        {
            public void run()
            {
                ServerSocket serverSocket;
                Socket socket;
                XMLEncoder salida;
                BufferedReader entrada;
                
                try
                {
                    serverSocket = new ServerSocket(puertoEmisor);
                    while (true)
                    {
                        socket = serverSocket.accept();
                        salida = new XMLEncoder(new BufferedOutputStream(socket.getOutputStream()));
                        entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        
                    }
                } 
                catch (IOException e)
                {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        }.run();
    }
}
