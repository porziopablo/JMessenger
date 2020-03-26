package emisora;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.net.Socket;

import java.util.Iterator;
import java.util.Observable;

import mensaje.Mensaje;

import usuarios.Destinatario;

public class Emisora extends Observable
{
    public void emitirMensaje(Mensaje mensaje)
    {
        Iterator<Destinatario> destinatarios = mensaje.getDestinatarios().iterator();
        Socket socket;
        Destinatario proximo;
        PrintWriter salida;
        
        try 
        {
            while (destinatarios.hasNext())
            {
                proximo = destinatarios.next();
                socket = new Socket(proximo.getIp(), Integer.parseInt(proximo.getPuerto()));
                salida = new PrintWriter(socket.getOutputStream(), true);
                
                salida.close();
                socket.close();
            }
            
            /*Socket socket = new Socket(,Integer.parseInt());
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
            out.println(jTextArea1.getText());
            out.close();
            socket.close();*/
            
        } catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
    
    private void recibirConfirmacion()
    {
        
    }
    
}
