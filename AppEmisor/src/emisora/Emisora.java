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

public class Emisora extends Observable
{
    private String nombreEmisor;

    public Emisora(String nombreEmisor)
    {
        super();
        this.nombreEmisor = nombreEmisor;
    }
    
    private String mensajeAString(Mensaje mensaje)
    {
        final String SEPARADOR = "_###_";
        final String FINAL = "##FIN##";
        
        StringBuilder sb = new StringBuilder();
        
        /* nombre emisor */
        sb.append(this.nombreEmisor);
        
        /* asunto */
        sb.append(SEPARADOR);
        sb.append(mensaje.getAsunto());
        
        /* cuerpo */
        sb.append(SEPARADOR);
        sb.append(mensaje.getCuerpo());
        
        /* tipo mensaje */
        sb.append(SEPARADOR);
        sb.append(mensaje.getTipo());
        
        /* para finalizar mensaje */
        sb.append("\n" + FINAL);
                
        return sb.toString();
    }

    public void emitirMensaje(Mensaje mensaje)
    {
        Iterator<Destinatario> destinatarios = mensaje.getDestinatarios().iterator();
        Socket socket;
        Destinatario proximo;
        PrintWriter salida;
        BufferedReader entrada;
        String msj = this.mensajeAString(mensaje), confirmacion;

        while (destinatarios.hasNext())
        {
            proximo = destinatarios.next();
            try
            {
                socket = new Socket(proximo.getIp(), Integer.parseInt(proximo.getPuerto()));
                salida = new PrintWriter(socket.getOutputStream(), true);
                entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                
                salida.println(msj);
                
                if (mensaje.getTipo() == Mensaje.MENSAJE_RECEPCION) /* espero confirmacion de recepcion */
                {
                    confirmacion = entrada.readLine();
                    setChanged();
                    notifyObservers(confirmacion);
                }
                
                salida.close();
                entrada.close();
            }
            catch (IOException e)
            {
                System.out.println("Error al enviar mensaje a " + proximo.getNombre() + " : " + e.getMessage());
            }
        }             
    }
}