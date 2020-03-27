package emisora;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.net.ServerSocket;
import java.net.Socket;

import java.util.Iterator;
import java.util.Observable;

import mensaje.Mensaje;

import usuarios.Destinatario;
import usuarios.Emisor;

public class Emisora extends Observable
{
    private Emisor emisor;

    public Emisora(Emisor emisor)
    {
        super();
        this.emisor = emisor;
    }

    public Emisor getEmisor()
    {
        return emisor;
    }
    
    public String mensajeAString(Mensaje mensaje)
    {
        final String SEPARADOR = "_###_";
        
        StringBuilder sb = new StringBuilder();
        
        /* nombre emisor */
        sb.append(this.emisor.getNombre());
        
        /* asunto */
        sb.append(SEPARADOR);
        sb.append(mensaje.getAsunto());
        
        /* cuerpo */
        sb.append(SEPARADOR);
        sb.append(mensaje.getCuerpo());
        
        
        /* tipo mensaje */
        sb.append(SEPARADOR);
        sb.append(mensaje.getTipo());
        
        /* ip y puerto emisor para confirmar recepcion */
        if (mensaje.getTipo() == Mensaje.MENSAJE_RECEPCION)
        {
            sb.append(SEPARADOR);
            sb.append(this.emisor.getIp());
            sb.append(SEPARADOR);
            sb.append(this.emisor.getPuerto());
        }
        
        return sb.toString();
    }

    public void emitirMensaje(Mensaje mensaje)
    {
        Iterator<Destinatario> destinatarios = mensaje.getDestinatarios().iterator();
        Socket socket;
        Destinatario proximo;
        PrintWriter salida;
        String msj = this.mensajeAString(mensaje);

        while (destinatarios.hasNext())
        {
            proximo = destinatarios.next();
            try
            {
                socket = new Socket(proximo.getIp(), Integer.parseInt(proximo.getPuerto()));
                salida = new PrintWriter(socket.getOutputStream(), true);
                
                salida.print(msj);
                
                salida.close();
                socket.close();
            }
            catch (IOException e)
            {
                System.out.println("Error al enviar mensaje a " + proximo.getNombre() + " : " + e.getMessage());
            }
        }             
    }
    
    public void recibirConfirmacion()
    {
        new Thread() 
        {
            public void run() 
            {
                String confirmacion = "";
                
                try 
                {
                    ServerSocket serverSocket = new ServerSocket(Integer.parseInt(emisor.getPuerto()));
                    while (true) 
                    {
                        Socket socket = serverSocket.accept();
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                        confirmacion = in.readLine();
                        setChanged();
                        notifyObservers(confirmacion);
                    }
                } 
                catch (IOException e) 
                {
                    System.out.println("Error al recibir confirmación: " + e.getMessage());
                }
            }
        }.start();
    }  
}