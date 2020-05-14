package persistencia;

import java.beans.XMLEncoder;

import java.io.BufferedOutputStream;
import java.io.File;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;

import mensaje.Mensaje;

import usuario.Emisor;

public class PersistenciaXML implements IPersistencia 
{
    private final static String DIRECTORIO_MSJ = "persistencia_mensajes";
    private final static String TIPO_XML = ".xml";
    
    public PersistenciaXML() 
    {
        new File(DIRECTORIO_MSJ).mkdir();
    }

    @Override
    public void persistirMensaje(Mensaje mensaje) throws PersistenciaException
    {
        String nombreArchivo, ruta = System.getProperty("user.dir") + File.separator + DIRECTORIO_MSJ + File.separator;
        XMLEncoder encoder = null;
    
        nombreArchivo = ruta + mensaje.getId() + TIPO_XML;
        try
        {
            encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(nombreArchivo)));
            encoder.writeObject(mensaje);
            encoder.close();
        } 
        catch (FileNotFoundException e)
        {
            throw new PersistenciaException("No se pudo persistir mensaje con id: " + mensaje.getId());
        }
    }

    @Override
    public void eliminarMensaje(String idMensaje) throws PersistenciaException
    {
        String ruta = System.getProperty("user.dir") + File.separator + DIRECTORIO_MSJ + File.separator;
        
        if (!new File(ruta + idMensaje + TIPO_XML).delete())
            throw new PersistenciaException("No se pudo eliminar mensaje con id: " + idMensaje);
    }

    @Override
    public TreeMap<String, Mensaje> cargarMensajesExistentes()
    {
        // TODO Implement this method
        return null;
    }

    @Override
    public void persistirEmisor(Emisor emisor)
    {
        // TODO Implement this method
    }

    @Override
    public void eliminarEmisor(String nombreEmisor)
    {
        // TODO Implement this method
    }

    @Override
    public TreeMap<String, Emisor> cargarEmisoresExistentes()
    {
        // TODO Implement this method
        return null;
    }

    @Override
    public void persistirConfirmacion(String nombreEmisor, Iterator<String> confirmaciones)
    {
        // TODO Implement this method
    }

    @Override
    public void eliminarConfirmaciones(String nombreEmisor)
    {
        // TODO Implement this method
    }

    @Override
    public HashMap<String, TreeSet<String>> cargarConfirmacionesExistentes()
    {
        // TODO Implement this method
        return null;
    }
}
