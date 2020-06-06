package gestores;

import almacen.Almacen;

import java.io.BufferedReader;

import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.net.ServerSocket;
import java.net.Socket;

public class GestorDestinatario implements Runnable
{    
    private final int REG_EXITOSO = 1;
    private final int REG_FALLIDO = 0;
    
    private int puertoDestinatario = 1234; /* valor por defecto */

    public GestorDestinatario(int puertoDestinatario)
    {
        this.puertoDestinatario = puertoDestinatario;
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
        try
        {
            serverSocket = new ServerSocket(puertoDestinatario);
            while(true)
            {
                socket = serverSocket.accept();
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                                      
                String comando = in.readLine();
                String data = in.readLine();
            
                if(comando.equals(DESTINATARIO_LOG_UP))
                {
                    if(Almacen.getInstance().agregarNuevoDest(data))
                        out.println(REG_EXITOSO);
                    else
                        out.println(REG_FALLIDO);
                }
                else if(comando.equals(DESTINATARIO_ONLINE))
                {
                    System.out.println("heartbeat de: " + data);
                    Almacen.getInstance().online(data);
                }
                else if(comando.equals(DESTINATARIO_OFFLINE))
                {
                    Almacen.getInstance().offline(data);
                } 
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}