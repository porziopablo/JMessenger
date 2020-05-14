package almacen;

import configurador.Configurador;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;

import mensaje.Mensaje;

import persistencia.IPersistencia;

import usuario.Emisor;

public class Almacen implements IGestionConfirmaciones, IGestionMensajes
{
    
    private TreeMap<String, Mensaje> mensajesPendientes;
    private HashMap<String, TreeSet<String>> confirmacionesPendientes;
    private TreeMap<String, Emisor> emisores;
    private IPersistencia persistencia;
    private static Almacen instance = null; 
        
    private Almacen()
    {
        super();
        this.mensajesPendientes = new TreeMap<String, Mensaje>();
        this.confirmacionesPendientes = new HashMap<String, TreeSet<String>>();
        this.emisores = new TreeMap<String, Emisor>();
    }
    
    public static synchronized Almacen getInstance()
    {
        if (instance == null)
            instance = new Almacen();
        
        return instance;
    }
    
    public void setPersistencia(IPersistencia persistencia)
    {
        this.persistencia = persistencia;
    }

    @Override
    public void agregarConfirmacion(String nombreEmisor, String nombreDestinatario)
    {
        // TODO Implement this method
    }

    @Override
    public void eliminarConfirmacion(String nombreEmisor)
    {
        // TODO Implement this method
    }

    @Override
    public void agregarEmisor(String nombreEmisor, String ip, String puerto)
    {
        // TODO Implement this method
    }

    @Override
    public HashMap<String, TreeSet<String>> getConfirmacionesPendientes()
    {
        // TODO Implement this method
        return null;
    }

    @Override
    public TreeMap<String, Emisor> getEmisores()
    {
        // TODO Implement this method
        return null;
    }

    @Override
    public void agregarMensaje(String mensaje, String[] listaDestinatarios)
    {
        // TODO Implement this method
    }

    @Override
    public void eliminarMensaje(Iterator<String> idMensajes)
    {
        // TODO Implement this method
    }

    @Override
    public TreeMap<String, Mensaje> getMensajesPendientes()
    {
        // TODO Implement this method
        return null;
    }
}
