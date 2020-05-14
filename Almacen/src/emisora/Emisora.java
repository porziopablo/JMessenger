package emisora;

import agenda.IActualizacionDestinatarios;

import almacen.Almacen;

import java.io.BufferedReader;
import java.io.IOException;

import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.net.InetSocketAddress;
import java.net.Socket;

import java.util.ArrayList;
import java.util.Iterator;

import java.util.TreeMap;

import mensaje.Mensaje;

import usuarios.Destinatario;

public class Emisora implements Runnable
{
    private IActualizacionDestinatarios agenda;

    public Emisora(IActualizacionDestinatarios agenda)
    {
        this.agenda = agenda;
    }
    
    private String mensajeAString(Mensaje mensaje)
    {
        final String SEPARADOR = "_###_";
        final String FINAL = "##FIN##";
        
        StringBuilder sb = new StringBuilder();
        
        /* nombre emisor */
        sb.append(mensaje.getNombreEmisor());
        
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

    @Override
    public void run()
    {
        final int PERIODO = 2000; /* milisegundos */
        
        TreeMap<String, Destinatario> destinatarios;
        Iterator<Mensaje> mensajes;
        ArrayList<String> mensajesABorrar;
        Mensaje mensaje;
        Destinatario destinatario;
        boolean recibido;
        
        while (true)
        {
            try 
            {
                Thread.sleep(PERIODO);
            } 
            catch (InterruptedException e) 
            {
                System.out.println("Problema con sleep del hilo " + e.getMessage());
            }
            
            mensajes = Almacen.getInstance().getMensajesPendientes().values().iterator();
            destinatarios = this.agenda.actualizarDestinatarios();
            mensajesABorrar= new ArrayList<String>();
            
            while (mensajes.hasNext())
            {
                mensaje = mensajes.next();
                destinatario = destinatarios.get(mensaje.getNombreDestinatario());
                
                if (destinatario.isOnline())
                {
                    recibido = this.emitirMensaje(mensaje, destinatario.getIp(), Integer.parseInt(destinatario.getPuerto()));
                    if (recibido)
                    {
                        mensajesABorrar.add(mensaje.getId());
                        if (mensaje.getTipo() == Mensaje.MENSAJE_RECEPCION)
                            Almacen.getInstance().agregarConfirmacion(mensaje.getNombreEmisor(), 
                                                                      mensaje.getNombreDestinatario());
                    }
                }
            }
            Almacen.getInstance().eliminarMensaje(mensajesABorrar.iterator());
        }
    }
    
    private boolean emitirMensaje(Mensaje mensaje, String ip, int puerto)
    {
        final int TIMEOUT = 5000;
                   
        Socket socket;
        PrintWriter salida = null;
        BufferedReader entrada = null;
        
        boolean recibido = false;
        
        try
        {
            socket = new Socket();
            socket.connect(new InetSocketAddress(ip, puerto), TIMEOUT);
            socket.setSoTimeout(TIMEOUT);
            salida = new PrintWriter(socket.getOutputStream(), true);
            entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            salida.println(this.mensajeAString(mensaje));
            salida.flush();
            
            if (mensaje.getTipo() == Mensaje.MENSAJE_RECEPCION) /* espera confirmacion de recepcion */
                recibido = (entrada.readLine() != null); /* si hay confirmacion, se recibio, sino no */
            else
                recibido = true; /* asume que se recibió pues no hubo errores para los otros tipos */
        }
        catch (IOException e)
        {
            System.out.println("Error al enviar mensaje a " +  mensaje.getNombreDestinatario()  + " : " + e.getMessage());
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
                System.out.println("Error al cerrar flujo con " + mensaje.getNombreDestinatario() + " : " + e.getMessage());
            }
        }
        
        return recibido;
    }
}
