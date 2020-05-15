package confirmadora;

import almacen.Almacen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.net.InetSocketAddress;
import java.net.Socket;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

import java.util.TreeSet;

import mensaje.Mensaje;

import usuarios.Emisor;

public class Confirmadora implements Runnable
{
    @Override
    public void run()
    {
        final int PERIODO = 2000; /* milisegundos */
        
        TreeMap<String, Emisor> emisores;
        HashMap<String,TreeSet<String>> confirmaciones;
        Iterator<TreeSet<String>> iterConfirmaciones;
        Iterator<String> iterEmisoresIds;
        TreeSet<String> conjuntoEmisor;
        Emisor emisor;
        String emisorId = "";
        
        while (true)
        {
            try 
            {
                Thread.sleep(PERIODO);
            } 
            catch (InterruptedException e) 
            {
                System.out.println("Problema con sleep de confirmadora " + e.getMessage());
            }
            
            System.out.println("ALMACEN INTENTA ENVIAR CONFIRMACIONES");
            emisores = Almacen.getInstance().getEmisores();
            confirmaciones = Almacen.getInstance().getConfirmacionesPendientes();
            iterConfirmaciones = confirmaciones.values().iterator();
            iterEmisoresIds = confirmaciones.keySet().iterator();
            
            while (iterConfirmaciones.hasNext())
            {
                conjuntoEmisor = iterConfirmaciones.next();
                emisorId = iterEmisoresIds.next();
                emisor = emisores.get(emisorId);
                
                if (this.emitirConfirmacion(conjuntoEmisor, emisor.getIp(), Integer.parseInt(emisor.getPuerto())))
                    Almacen.getInstance().eliminarConfirmacion(emisorId);
            }
        }
    }
    
    private boolean emitirConfirmacion(TreeSet<String> mensaje, String ip, int puerto)
    {                   
        Socket socket;
        PrintWriter salida = null;
        
        boolean recibido = false;
        
        try
        {
            socket = new Socket();
            socket.connect(new InetSocketAddress(ip, puerto));
            salida = new PrintWriter(socket.getOutputStream(), true);
            
            salida.println(this.confirmacionAString(mensaje));
            salida.flush();
            recibido = true;
        }
        catch (IOException e)
        {
            System.out.println("Error al enviar confirmacion : " + e.getMessage());
        }
        finally
        {
            if (salida != null)
                salida.close();
        }
        
        return recibido;
    }
    
    private String confirmacionAString(TreeSet<String> confirmaciones)
    {
        final String SEPARADOR = ", ";
        
        StringBuilder sb = new StringBuilder();
        Iterator<String> iter = confirmaciones.iterator();
        
        while (iter.hasNext())
        {
            sb.append(iter.next());
            sb.append(SEPARADOR);
        }        
        sb.setLength(sb.length() - SEPARADOR.length());
        
        return sb.toString();
    }
}
