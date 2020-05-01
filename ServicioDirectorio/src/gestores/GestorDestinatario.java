package gestores;

import almacen.Almacen;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.net.ServerSocket;
import java.net.Socket;

public class GestorDestinatario implements Runnable
{    
    private final int REG_EXITOSO = 1;
    private final int REG_FALLIDO = 0;
    
    private int puertoDestinatario = 1234; /* valor por defecto */ 
    
    public GestorDestinatario()
    {
        super();
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
            }
            else
                throw new IOException("faltan o sobran datos.");
            
            if (! ((this.puertoDestinatario >= 0) && (this.puertoDestinatario <= 65535)))
                throw new IOException("el puerto para atender a los destinatarios debe ser un entero en el rango [0-65535].");
            if (datos[0] == datos[1])
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

    @Override
    public void run()
    {
        final String DESTINATARIO_LOG_UP = "Log Up";
        final String DESTINATARIO_ONLINE = "Online";
        final String DESTINATARIO_OFFLINE = "Offline";
        
        ServerSocket serverSocket;
        Socket socket;
        
        System.out.println("ESCUCHANDO DESTINATARIOS");
        try{
            serverSocket = new ServerSocket(puertoDestinatario);
            while(true){
                
                socket = serverSocket.accept();
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                                      
                String comando = in.readLine();
                String data = in.readLine();
            
                    if(comando.equals(DESTINATARIO_LOG_UP)){
                        if(Almacen.getInstance().agregarNuevoDest(data))
                            out.println(REG_EXITOSO);
                        else
                            out.println(REG_FALLIDO);
                    }
                    else if(comando.equals(DESTINATARIO_ONLINE)){
                        System.out.println("heartbeat de: " + data);
                        Almacen.getInstance().online(data);
                    }
                    else if(comando.equals(DESTINATARIO_OFFLINE)){
                        Almacen.getInstance().offline(data);
                    } 
                }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
