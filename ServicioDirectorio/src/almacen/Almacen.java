package almacen;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

import replicacion.IReplicacion;

import usuarios.Destinatario;

public class Almacen implements IEntrega, IRegistro, IActualizacion
{    
    private final int MAX_ESPERA = 15000; /* MILISEGUNDOS */
    
    private TreeMap<String, Destinatario> destinatarios;
    private HashMap<String, Date> fechasConexion;
    private IReplicacion replicacion;
    
    private static Almacen instance = null; /* Singleton */
    
    private Almacen()
    {
        super();
    }
 
    public static synchronized Almacen getInstance()
    {
        if (instance == null)
            instance = new Almacen();
        
        return instance;
    }
    
    @Override
    public synchronized boolean agregarNuevoDest(String nombre, String ip, String puerto){
        
        boolean res = false;
        Date timestamp = null;
                
        if (!this.destinatarios.containsKey(nombre))
        {
            this.destinatarios.put(nombre, new Destinatario(nombre, ip, puerto, true));
            timestamp = new Date();
            this.fechasConexion.put(nombre, timestamp);
            res = true;
        }
        else
        {
            if (ip.equals(this.destinatarios.get(nombre).getIp()) && !this.destinatarios.get(nombre).isOnline())
            {
                this.destinatarios.get(nombre).setOnline(true);
                this.destinatarios.get(nombre).setPuerto(puerto);
                timestamp = new Date();
                this.fechasConexion.put(nombre, timestamp);
                res = true;
            }
        }
        
        if (res)
        {
            this.replicacion.replicarDest(nombre, ip, puerto, timestamp.getTime());
        }
        
        return res;
    }
    
    @Override
    public synchronized void online(String nombreDest)
    {
        Date timestamp = new Date();
        
        this.fechasConexion.put(nombreDest, timestamp);
        
        this.replicacion.replicarOnline(nombreDest, timestamp.getTime());
    }
    
    @Override
    public synchronized void offline(String nombreDest)
    {

        GregorianCalendar fecha = new GregorianCalendar(2010, 12, 3);
        Date cambio = fecha.getTime();
        
        this.fechasConexion.put(nombreDest, cambio);
        this.destinatarios.get(nombreDest).setOnline(false);
        
        this.replicacion.replicarOffline(nombreDest);
    }

    private synchronized void actualizarEstadosConexion()
    {
        Iterator<Destinatario> iter;
        Destinatario proximo;
        long fechaActual, fechaProx;

        iter = this.destinatarios.values().iterator();
        while (iter.hasNext())
        {
            proximo = iter.next();
            fechaProx = this.fechasConexion.get(proximo.getNombre()).getTime();
            fechaActual = new Date().getTime();
            proximo.setOnline(((fechaActual -  fechaProx) <= MAX_ESPERA));
        }
    }
    
    @Override
    public synchronized TreeMap<String, Destinatario> getDestinatariosActualizados()
    {
        Destinatario proximo;
        TreeMap<String, Destinatario> copia = new TreeMap<String, Destinatario>();
        Iterator<Destinatario> iterDestinatarios;
        
        this.actualizarEstadosConexion();
        iterDestinatarios = this.destinatarios.values().iterator();
        
        while (iterDestinatarios.hasNext())
        {
            proximo = iterDestinatarios.next();
            copia.put(proximo.getNombre(), 
                      new Destinatario(proximo.getNombre(), proximo.getIp(), proximo.getPuerto(), proximo.isOnline()));
        }
        
        return copia;
    }
    
    public void setReplicacion(IReplicacion replicacion)
    {
        this.replicacion = replicacion;
        
        Object[] estado = this.replicacion.copiarEstado();
        this.destinatarios = (TreeMap<String, Destinatario>) estado[0];
        this.fechasConexion = (HashMap<String, Date>) estado[1];
    }
    
    @Override
    public synchronized void agregarCopiaDest(String nombre, String ip, String puerto, long timestamp)
    {
        // TODO Implement this method
    }

    @Override
    public synchronized void onlineCopia(String nombreDest, long timestamp)
    {
        // TODO Implement this method
    }

    @Override
    public synchronized void offlineCopia(String nombreDest)
    {
        // TODO Implement this method
    }

    @Override
    public synchronized Object[] getEstado()
    {
        // TODO Implement this method
        return new Object[0];
    }
}
