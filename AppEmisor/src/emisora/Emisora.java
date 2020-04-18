package emisora;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.net.InetSocketAddress;
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
        final int TIMEOUT = 5000;
        
        Iterator<Destinatario> destinatarios = mensaje.getDestinatarios().iterator();
        Socket socket;
        Destinatario proximo;
        PrintWriter salida = null;
        BufferedReader entrada = null;
        String msj = this.mensajeAString(mensaje), confirmacion;
        
        while (destinatarios.hasNext())
        {
            proximo = destinatarios.next();
            try
            {
                socket = new Socket();
                socket.connect(new InetSocketAddress(proximo.getIp(), Integer.parseInt(proximo.getPuerto())), TIMEOUT);
                socket.setSoTimeout(TIMEOUT);
                salida = new PrintWriter(socket.getOutputStream(), true);
                entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                
                salida.println(msj);
                
                if (mensaje.getTipo() == Mensaje.MENSAJE_RECEPCION) /* espero confirmacion de recepcion */
                {
                    confirmacion = entrada.readLine();
                    if (confirmacion != null)
                    {
                        setChanged();
                        notifyObservers(confirmacion);
                    }
                }
                salida.flush();
            }
            catch (IOException e)
            {
                System.out.println("Error al enviar mensaje a " + proximo.getNombre() + " : " + e.getMessage());
            }
            finally
            {
                try
                {
                    if (salida != null)
                        salida.close();
                    if (entrada != null)
                        entrada.close(); 
                }
                catch (IOException e) /* errores importantes fueron atrapados en catch anterior */
                {
                    System.out.println("Error al enviar mensaje a " + proximo.getNombre() + " : " + e.getMessage());
                }

            }
        }             
    }
}