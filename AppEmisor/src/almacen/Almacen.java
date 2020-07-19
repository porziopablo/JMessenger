package almacen;

import java.time.Instant;

import java.util.TreeMap;

import mensaje.Mensaje;

import persistencia.IPersistencia;
import persistencia.PersistenciaException;

public class Almacen implements IGestionPendientes
{
    private IPersistencia persistencia;
    private TreeMap<String, Mensaje> mensajesPendientes;
    private static Almacen instance = null;
    
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
    public synchronized void almacenarMensaje(Mensaje mensaje)
    {
        mensaje.setId(mensaje.getDestinatarios().get(0) + "_" + Instant.now().toEpochMilli());
        this.mensajesPendientes.put(mensaje.getId(), mensaje);
        try
        {
            this.persistencia.persistirMensaje(mensaje);
        } 
        catch (PersistenciaException e)
        {
            System.out.println("Error al persistir mensaje pendiente: " + e.getMessage());
        }
    }
    
    @Override
    public synchronized void eliminarMensaje(String id)
    {
        this.mensajesPendientes.remove(id);
        try
        {
            this.persistencia.eliminarMensaje(id);
        } 
        catch (PersistenciaException e)
        {
            System.out.println("Error al persistir mensaje pendiente: " + e.getMessage());
        }
    }
    
    @Override
    public synchronized TreeMap<String, Mensaje> getCopiaPendientes()
    {
        return (TreeMap<String, Mensaje>) this.mensajesPendientes.clone();
    }
    
    public synchronized void setPersistencia(IPersistencia persistencia)
    {
        this.persistencia = persistencia;
        try
        {
            this.mensajesPendientes = this.persistencia.cargarMensajesExistentes();
        } 
        catch (PersistenciaException e)
        {
           this.mensajesPendientes = new TreeMap<String, Mensaje>(); 
        }
    }
}
