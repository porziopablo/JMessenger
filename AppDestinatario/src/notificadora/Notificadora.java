package notificadora;

import java.beans.XMLDecoder;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.net.Socket;

import java.util.TreeSet;

import usuarios.Destinatario;

public class Notificadora {
    
    public final static String DESTINATARIO_LOG_UP = "Log Up";
    public final static String DESTINATARIO_ONLINE = "Online";
    public final static String DESTINATARIO_OFFLINE = "Offline";
    public final static int FREC_AVISO = 5; //CHEQUEAR TIEMPO
    public final static int REG_EXITOSO = 1;
    public final static int REG_FALLIDO = 0;

    private String ipDirectorio;
    private int puertoDirectorio;
    private boolean encendido;
    private String nombreDestinatario; 
    
    
    public Notificadora() {
        this.ipDirectorio = "192.168.0.189";
        this.puertoDirectorio = 1234; /* valores por defecto */
        this.cargarConfiguracion();
    }
 
// El retorno sera 0 si el registro fue fallido y 1 si fue exitoso.   
    public int registrarDestinatario(String nombreDest, String ipDest, String puertoDest){
        
        Socket socket;
        PrintWriter salida;
        BufferedReader entrada;  
        int rta = 0;
        StringBuilder sb = new StringBuilder();
        final String SEPARADOR = "_###_";
        
        try
        {
            socket = new Socket(this.ipDirectorio, this.puertoDirectorio);
            salida = new PrintWriter(socket.getOutputStream(), true);
            entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            sb.append(DESTINATARIO_LOG_UP + "\n");
            sb.append(nombreDest);
            sb.append(SEPARADOR);
            sb.append(ipDest);
            sb.append(SEPARADOR);
            sb.append(puertoDest);
            salida.println(sb.toString()); //Mando datos del destinatario
            
            
            rta = Integer.parseInt(entrada.readLine());           
            if( rta == 1){ //registro exitoso
                this.nombreDestinatario = nombreDest;
                this.encendido = true;
            }
            
            salida.close();
            entrada.close();
             
        }  
        catch (IOException e)
        {
            System.out.println("Problema en conexion" + e.getMessage());
        }
        
        return rta;
    }
   
//Tanto en el metodo apagar como avisar mandamos el nombre del destinatario 
    public void apagar(){
        
    }
    
    private void avisar(){
        
    }
    
    private void cargarConfiguracion()
    {
        final String NOMBRE_ARCHIVO = "config_destinatario.txt";
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
    
}

