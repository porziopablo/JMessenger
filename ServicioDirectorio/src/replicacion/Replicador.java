package replicacion;

import almacen.Almacen;

import directorio.Directorio;

import gestores.GestorDestinatario;

import java.beans.XMLDecoder;

import java.beans.XMLEncoder;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

import usuarios.Destinatario;

public class Replicador implements IRecepecionCambios, IReplicacion
{
    private ArrayList<Directorio> directorios;
    private int puertoReplicacion;
    private static final String ESTADO_COPIA = "copia"; 

    public Replicador(ArrayList<Directorio> directorios, int puertoReplicacion)
    {
        this.directorios = directorios;
        this.puertoReplicacion = puertoReplicacion;
    }

    @Override
    public void recibirCambios()
    {
        new Thread()
        {
            public void run() 
            {
                ServerSocket serverSocket;
                Socket socket;
                String text[];
                XMLEncoder salida;
                Object[] estado;
                     
                System.out.println("ESCUCHANDO DIRECTORIOS");
                try
                {
                    serverSocket = new ServerSocket(puertoReplicacion);
                    while(true)
                    {
                        socket = serverSocket.accept();
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                                              
                        String comando = in.readLine();
                    
                        if(comando.equals(GestorDestinatario.DESTINATARIO_LOG_UP))
                        {
                            text = in.readLine().split(GestorDestinatario.SEPARADOR); // nombre - ip - puerto - timestamp
                            Almacen.getInstance().agregarCopiaDest(text[0], text[1], text[2], Long.parseLong(text[3]));
                        }
                        else if(comando.equals(GestorDestinatario.DESTINATARIO_ONLINE))
                        {
                            text = in.readLine().split(GestorDestinatario.SEPARADOR); // nombre - timestamp
                            Almacen.getInstance().onlineCopia(text[0], Long.parseLong(text[1]));
                        }
                        else if(comando.equals(GestorDestinatario.DESTINATARIO_OFFLINE))
                        {
                            Almacen.getInstance().offlineCopia(in.readLine());
                        } 
                        else if(comando.equals(ESTADO_COPIA)) 
                        {
                            salida = new XMLEncoder(new BufferedOutputStream(socket.getOutputStream()));
                            
                            estado = Almacen.getInstance().getEstado();
                            salida.writeObject(estado[0]);
                            salida.writeObject(estado[1]);
                            salida.close();
                        }
                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void replicar(String cambio)
    {
        Iterator<Directorio> iter = this.directorios.iterator();
        Directorio dir;
        Socket socket;
        PrintWriter salida = null;
        
        final int TIMEOUT = 1000; /* milisegundos */
        
        while(iter.hasNext())
        {
            dir = iter.next();

            try 
            {
                socket = new Socket();
                socket.connect(new InetSocketAddress(dir.getIp(), dir.getPuerto()), TIMEOUT);
                salida = new PrintWriter(socket.getOutputStream(), true);
                  
                salida.println(cambio);             
            } 
            catch (IOException e) 
            {
                System.out.println("Directorio en ip "+ dir.getIp()+" esta desconectado");
            }
            finally
            {
                if( salida!= null)
                    salida.close();
            }
        }
    }
    
    @Override
    public void replicarDest(String nombre, String ip, String puerto, long timestamp)
    {
        StringBuilder sb = new StringBuilder();
        
        sb.append(GestorDestinatario.DESTINATARIO_LOG_UP + "\n");
        sb.append(nombre);
        sb.append(GestorDestinatario.SEPARADOR);
        sb.append(ip);
        sb.append(GestorDestinatario.SEPARADOR);
        sb.append(puerto);
        sb.append(GestorDestinatario.SEPARADOR);
        sb.append(timestamp);
        
        this.replicar(sb.toString());
        
    }

    @Override
    public void replicarOnline(String nombreDest, long timestamp)
    {
        StringBuilder sb = new StringBuilder();
        
        sb.append(GestorDestinatario.DESTINATARIO_ONLINE + "\n");
        sb.append(nombreDest);
        sb.append(GestorDestinatario.SEPARADOR);
        sb.append(timestamp);
        
        this.replicar(sb.toString());
    }

    @Override
    public void replicarOffline(String nombreDest)
    {
        StringBuilder sb = new StringBuilder();
        
        sb.append(GestorDestinatario.DESTINATARIO_OFFLINE + "\n");
        sb.append(nombreDest);
        
        this.replicar(sb.toString());
    }

    @Override
    public Object[] copiarEstado()
    {
        Object[] estado = new Object[2];
        estado[0] = new TreeMap<String, Destinatario>();
        estado[1] = new HashMap<String, Date>();
        
        boolean opRealizada = false;
        Directorio directorio;
        int directorioActual = 0;
        XMLDecoder entrada = null;
        PrintWriter salida = null;
        Socket socket;
        
                  
        while (! opRealizada) /* pre-condicion: al menos un directorio activo en todo momento */
        {
            directorio = this.directorios.get(directorioActual);
            try
            {
                socket = new Socket(directorio.getIp(), directorio.getPuerto());
                salida = new PrintWriter(socket.getOutputStream(), true);           
                entrada = new XMLDecoder(new BufferedInputStream(socket.getInputStream())); 
                    
                salida.println(ESTADO_COPIA);  
                estado[0] = entrada.readObject();
                estado[1] = entrada.readObject();
                opRealizada = true;
            }  
            catch (IOException e)
            {
                directorioActual = ((directorioActual + 1) % this.directorios.size());
                opRealizada = (directorioActual == 0); /* para el caso del 1er directorio existente, 
                                                        * no habria otro listo, por lo que debe inicializarse vacio */
                System.out.println("cambio directorio a " + directorioActual);
            }
            finally
            {
                if (entrada != null)
                    entrada.close();
                if (salida != null)
                    salida.close();
            }
        }
        
        return estado;
    }
}
