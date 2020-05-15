package almacen;

import configurador.Configurador;

import java.time.Instant;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;

import mensaje.Mensaje;

import persistencia.IPersistencia;

import persistencia.PersistenciaException;

import usuarios.Destinatario;
import usuarios.Emisor;

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

    @Override
    public synchronized void agregarMensaje(String nombreEmisor, String asunto, String cuerpo, String tipo, String[] listaDestinatarios) {
        for(int i=0; i<listaDestinatarios.length;i++){
            Mensaje mensaje = new Mensaje(nombreEmisor, listaDestinatarios[i], asunto, cuerpo, listaDestinatarios[i]+"_"+Instant.now().toEpochMilli() ,Integer.parseInt(tipo));
            this.mensajesPendientes.put(listaDestinatarios[i]+"_"+Instant.now().toEpochMilli(), mensaje);
            try {
                this.persistencia.persistirMensaje(mensaje);
            } catch (PersistenciaException e) {
                System.out.println("No se pudo persistir el mensaje");
            }
        }   
    }
    
    
    @Override
    public synchronized void agregarEmisor(String nombreEmisor, String ip, String puerto)
    {
        Emisor emisor = new Emisor(nombreEmisor, ip, puerto);
        this.emisores.put(nombreEmisor, emisor);    
        try {
            this.persistencia.persistirEmisor(emisor);
        } catch (PersistenciaException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public synchronized void agregarConfirmacion(String nombreEmisor, String nombreDestinatario)
    {
        TreeSet<String> confirm = this.confirmacionesPendientes.get(nombreEmisor);
        if(confirm == null){
            confirm = new TreeSet<String>();
        }
        confirm.add(nombreDestinatario);
        this.confirmacionesPendientes.put(nombreEmisor, confirm);
        try {
            this.persistencia.persistirConfirmacion(nombreEmisor, confirm);
        } catch (PersistenciaException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public synchronized void eliminarConfirmacion(String nombreEmisor)
    {
        this.confirmacionesPendientes.remove(nombreEmisor);
        try {
            this.persistencia.eliminarConfirmaciones(nombreEmisor);
        } catch (PersistenciaException e) {
            System.out.println(e.getMessage());
        }
        this.eliminarEmisor(nombreEmisor);
    }
    
    private synchronized void eliminarEmisor(String nombreEmisor) {
        
        if(!this.confirmacionesPendientes.containsKey(nombreEmisor) && !this.existeMsjRecepcion(nombreEmisor)){
            this.emisores.remove(nombreEmisor);
            try {
                this.persistencia.eliminarEmisor(nombreEmisor);
            } catch (PersistenciaException e) {
                System.out.println(e.getMessage());
            }
        }
        
    }
    
    private synchronized boolean existeMsjRecepcion(String nombreEmisor) {
        boolean existe = false;
       
        
        return existe;
    }
    
    @Override
    public synchronized void eliminarMensaje(Iterator<String> idMensajes)
    {
        
    }

    @Override
    public synchronized HashMap<String, TreeSet<String>> getConfirmacionesPendientes()
    {
        return (HashMap<String, TreeSet<String>>) this.confirmacionesPendientes.clone();
    } 
    
    @Override
    public synchronized TreeMap<String, Mensaje> getMensajesPendientes()
    {       
        return (TreeMap<String, Mensaje>) this.mensajesPendientes.clone();
    }

    @Override
    public synchronized TreeMap<String, Emisor> getEmisores()
    {
        return (TreeMap<String, Emisor>) this.emisores.clone();
    }

    public void setPersistencia(IPersistencia persistencia)
    {
        this.persistencia = persistencia;
        try {
            this.confirmacionesPendientes = this.persistencia.cargarConfirmacionesExistentes();
            this.emisores = this.persistencia.cargarEmisoresExistentes();
            this.mensajesPendientes = this.persistencia.cargarMensajesExistentes();
        } catch (PersistenciaException e) {
            System.out.println(e.getMessage());
        }
    }

}
