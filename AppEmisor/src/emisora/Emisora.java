package emisora;

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

import persistencia.IPersistencia;

public class Emisora extends Observable implements IEmisionMensaje
{
    private String ipAlmacen = "192.168.0.9";
    private int puertoAlmacen = 1234;       /* valores por defecto */
    private int puertoConfirmacion = 1234;
    private IEncriptacion encriptador;
    private IPersistencia persistencia;

    public Emisora(String ipAlmacen, int puertoAlmacen, int puertoConfirmacion, IEncriptacion encriptador, 
                   IPersistencia persistencia)
    {
        this.ipAlmacen = ipAlmacen;
        this.puertoAlmacen = puertoAlmacen;
        this.puertoConfirmacion = puertoConfirmacion;
        this.encriptador = encriptador;
        this.persistencia = persistencia;
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
        Socket socket;
        PrintWriter salida = null;
        
        try
        {
            socket = new Socket();
            socket.connect(new InetSocketAddress(this.ipAlmacen, this.puertoAlmacen));
            salida = new PrintWriter(socket.getOutputStream(), true);
            
            salida.println(this.mensajeAString(mensaje));
            salida.flush();
        }
        catch (IOException e)
        {
            setChanged();
            notifyObservers("Error al enviar mensaje, reintentar luego.");
            System.out.println("Error al enviar mensaje al Almacén: " + e.getMessage());
        }
        finally
        {
            if (salida != null)
                salida.close();
        }
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
                            notifyObservers(nombreDestinatario + " recibió/recibieron tu mensaje.");
                        }
                        catch (IOException e) 
                        {
                            System.out.println("Error al recibir confirmación: " + e.getMessage());
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
                                System.out.println("Error al recibir confirmación: " + e.getMessage());
                            }
                        }
                    }
                } 
                catch (IOException e) 
                {
                    System.out.println("Error al abrir socket de confirmación: " + e.getMessage());
                }
            }
        }.start();
    }  
}