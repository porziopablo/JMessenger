package almacen;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

import usuarios.Destinatario;

public class Almacen implements IEntrega, IRegistro
{    
    private final int MAX_ESPERA = 15000; /* MILISEGUNDOS */
    
    private TreeMap<String, Destinatario> destinatarios;
    private HashMap<String, Date> fechasConexion;
    
    private static Almacen instance = null; /* Singleton */
    
    private Almacen()
    {
        this.destinatarios = new TreeMap<String, Destinatario>();
        this.fechasConexion = new HashMap<String, Date>();
    }
 
    public static synchronized Almacen getInstance()
    {
        if (instance == null)
            instance = new Almacen();
        
        return instance;
    }
    
    @Override
    public synchronized boolean agregarNuevoDest(String data){
        
        boolean res = false;
        final String SEPARADOR = "_###_";
        
        String text[];
        text = data.split(SEPARADOR); // nombre - ip - puerto
        Destinatario nuevo = new Destinatario(text[0], text[1], text[2], true);
        
        if(!this.destinatarios.containsKey(text[0])){
            this.destinatarios.put(text[0], nuevo);
            this.fechasConexion.put(text[0], new Date());
            res =true;
        }
        else{
            if(text[1].equals(this.destinatarios.get(text[0]).getIp()) && !this.destinatarios.get(text[0]).isOnline()){
                this.destinatarios.get(text[0]).setOnline(true);
                this.destinatarios.get(text[0]).setPuerto(text[2]);
                this.fechasConexion.put(text[0], new Date());
                res = true;
            }
        }
        
        return res;
    }
    
    @Override
    public synchronized void online(String data){
        this.fechasConexion.put(data, new Date());
    }
    
    @Override
    public synchronized void offline(String data){

        GregorianCalendar fecha = new GregorianCalendar(2010, 12, 3);
        Date cambio = fecha.getTime();
        this.fechasConexion.put(data, cambio);
        this.destinatarios.get(data).setOnline(false);
    }
            
    private synchronized void actualizarEstados()
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
        
        this.actualizarEstados();
        iterDestinatarios = this.destinatarios.values().iterator();
        
        while (iterDestinatarios.hasNext())
        {
            proximo = iterDestinatarios.next();
            copia.put(proximo.getNombre(), 
                      new Destinatario(proximo.getNombre(), proximo.getIp(), proximo.getPuerto(), proximo.isOnline()));
        }
        
        return copia;
    }
}
