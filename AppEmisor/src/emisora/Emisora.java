package emisora;

import almacen.Almacen;

import encriptacion.IEncriptacion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import java.net.UnknownHostException;

import java.util.Iterator;
import java.util.Observable;

import mensaje.Mensaje;

public class Emisora extends Observable implements IEmisionMensaje
{
    private String ipAlmacen = "192.168.0.9";
    private int puertoAlmacen = 1234;       /* valores por defecto */
    private int puertoConfirmacion = 1234;
    private IEncriptacion encriptador;

    public Emisora(String ipAlmacen, int puertoAlmacen, int puertoConfirmacion, IEncriptacion encriptador)
    {
        this.ipAlmacen = ipAlmacen;
        this.puertoAlmacen = puertoAlmacen;
        this.puertoConfirmacion = puertoConfirmacion;
        this.encriptador = encriptador;
        
        this.emitirMensajesPendientes();
    }

    private String mensajeAString(Mensaje mensaje) throws UnknownHostException
    {
        final String SEPARADOR = "_###_";
        final String FINAL = "##FIN##";
        final String SEPARADOR_DEST = "_@@_";
        
        StringBuilder sb = new StringBuilder();
        Iterator<String> destinatarios = mensaje.getDestinatarios().iterator();
        
        /* nombre emisor */
        sb.append(mensaje.getNombreEmisor());
        
        /* asunto */
        sb.append(SEPARADOR);
        sb.append(this.encriptador.encriptar(mensaje.getAsunto()));
        
        /* cuerpo */
        sb.append(SEPARADOR);
        sb.append(this.encriptador.encriptar(mensaje.getCuerpo()));
        
        /* tipo mensaje */
        sb.append(SEPARADOR);
        sb.append(mensaje.getTipo());
        
        /* destinatarios */
        sb.append(SEPARADOR);
        while (destinatarios.hasNext())
        {
            sb.append(destinatarios.next());
            sb.append(SEPARADOR_DEST);
        }
                
        /* ip y puerto emisor para confirmar recepcion */
        if (mensaje.getTipo() == Mensaje.MENSAJE_RECEPCION)
        {
            sb.append(SEPARADOR);
            sb.append(InetAddress.getLocalHost().getHostAddress());
            sb.append(SEPARADOR);
            sb.append(this.puertoConfirmacion);
        }
        
        /* para finalizar mensaje */
        sb.append("\n" + FINAL);
                
        return sb.toString();
    }

    @Override
    public void emitirMensaje(Mensaje mensaje)
    {        
        if (! this.intentarEmision(mensaje))
        {
            setChanged();
            notifyObservers("Error al enviar mensaje, se reintentar� luego.");
            Almacen.getInstance().almacenarMensaje(mensaje);
        }
    }
    
    private synchronized boolean intentarEmision(Mensaje mensaje)
    {
        Socket socket;
        PrintWriter salida = null;
        boolean emitido = false;
        
        try
        {
            socket = new Socket();
            socket.connect(new InetSocketAddress(this.ipAlmacen, this.puertoAlmacen));
            salida = new PrintWriter(socket.getOutputStream(), true);
            
            salida.println(this.mensajeAString(mensaje));
            salida.flush();
            emitido = true;
        }
        catch (IOException e)
        {
            emitido = false;
        }
        finally
        {
            if (salida != null)
                salida.close();
        }
        
        return emitido;
    }
    
    @Override
    public void recibirConfirmacion()
    {
        new Thread() 
        {
            public void run() 
            {
                String nombreDestinatario = "";
                ServerSocket serverSocket;
                Socket socket;
                BufferedReader lector = null;
                
                try 
                {
                    serverSocket = new ServerSocket(puertoConfirmacion);
                    while (true) 
                    {
                        try
                        {
                            socket = serverSocket.accept();
                            lector = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    
                            nombreDestinatario = lector.readLine();
                            
                            setChanged();
                            notifyObservers(nombreDestinatario + " recibi�/recibieron tu mensaje.");
                        }
                        catch (IOException e) 
                        {
                            System.out.println("Error al recibir confirmaci�n: " + e.getMessage());
                        }
                        finally
                        {
                            try
                            {
                                if (lector != null)
                                    lector.close();
                            }
                            catch (IOException e) /* errores importantes fueron atrapados en catch anterior */
                            {
                                System.out.println("Error al recibir confirmaci�n: " + e.getMessage());
                            }
                        }
                    }
                } 
                catch (IOException e) 
                {
                    System.out.println("Error al abrir socket de confirmaci�n: " + e.getMessage());
                }
            }
        }.start();
    }
        
    private void emitirMensajesPendientes()
    {
        new Thread()
        {
            public void run()
            {
                final int PERIODO = 10000; /* milisegundos */
                
                Iterator<Mensaje> mensajes;
                Mensaje mensaje;
                
                while (true)
                {
                    try 
                    {
                        Thread.sleep(PERIODO);
                    } 
                    catch (InterruptedException e) 
                    {
                        System.out.println("Problema con sleep de emisora " + e.getMessage());
                    }
                    
                    mensajes = Almacen.getInstance().getCopiaPendientes().values().iterator();
                    
                    while (mensajes.hasNext())
                    {
                        mensaje = mensajes.next();                        
                        if (intentarEmision(mensaje))
                            Almacen.getInstance().eliminarMensaje(mensaje.getId());
                    }
                }
            }
        }.start();
    }
}