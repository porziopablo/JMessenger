package persistencia;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;

import mensaje.Mensaje;

import usuarios.Emisor;

public class PersistenciaXML implements IPersistencia 
{
    private static final String DIRECTORIO_MSJ = "persistencia_mensajes";
    private static final String DIRECTORIO_EMISOR = "persistencia_emisores";
    private static final String DIRECTORIO_CONFIRM = "persistencia_confirmaciones";
    private final static String TIPO_XML = ".xml";
    
    public PersistenciaXML() 
    {
        new File(DIRECTORIO_MSJ).mkdir();     /* crea directorios, si no existen */
        new File(DIRECTORIO_EMISOR).mkdir();
        new File(DIRECTORIO_CONFIRM).mkdir();
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
        } 
        catch (FileNotFoundException e)
        {
            throw new PersistenciaException("No se pudo persistir mensaje con id: " + mensaje.getId());
        }
        finally
        {
            encoder.close();
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
    public TreeMap<String, Mensaje> cargarMensajesExistentes() throws PersistenciaException
    {
        TreeMap<String, Mensaje> mensajes = new TreeMap<String, Mensaje>();
        String nombreArchivo, ruta = System.getProperty("user.dir") + File.separator + DIRECTORIO_MSJ; 
        File[] archivos = new File(ruta).listFiles();
        XMLDecoder decoder = null;
        Mensaje mensaje;
        
        for (int i = 0; i < archivos.length; i++) 
        {
          if (archivos[i].isFile()) 
          {
              nombreArchivo = ruta + File.separator + archivos[i].getName();
              try
              {
                  decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(nombreArchivo)));
                  mensaje = (Mensaje) decoder.readObject(); 
                  mensajes.put(mensaje.getId(), mensaje);
              } 
              catch (FileNotFoundException e)
              {
                  throw new PersistenciaException("No se pudo cargar mensaje con id: " + archivos[i].getName());
              }
              finally
              {
                  decoder.close();
              }
          } 
        }
        
        return mensajes;
    }

    @Override
    public void persistirEmisor(Emisor emisor) throws PersistenciaException
    {
        String nombreArchivo, ruta = System.getProperty("user.dir") + File.separator + DIRECTORIO_EMISOR+File.separator;
        XMLEncoder encoder = null;
        
        nombreArchivo = ruta + emisor.getNombre() + TIPO_XML;
        try
        {
            encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(nombreArchivo)));
            encoder.writeObject(emisor);
        } 
        catch (FileNotFoundException e)
        {
            throw new PersistenciaException("No se pudo persistir emisor con id: " + emisor.getNombre());
        }
        finally
        {
            encoder.close();
        }
    }

    @Override
    public void eliminarEmisor(String nombreEmisor) throws PersistenciaException
    {
        String ruta = System.getProperty("user.dir") + File.separator + DIRECTORIO_EMISOR + File.separator;
        
        if (!new File(ruta + nombreEmisor + TIPO_XML).delete())
            throw new PersistenciaException("No se pudo eliminar emisor con id: " + nombreEmisor);
    }

    @Override
    public TreeMap<String, Emisor> cargarEmisoresExistentes() throws PersistenciaException
    {
        TreeMap<String, Emisor> emisores = new TreeMap<String, Emisor>();
        String nombreArchivo, ruta = System.getProperty("user.dir") + File.separator + DIRECTORIO_EMISOR; 
        File[] archivos = new File(ruta).listFiles();
        XMLDecoder decoder = null;
        Emisor emisor;
        
        for (int i = 0; i < archivos.length; i++) 
        {
          if (archivos[i].isFile()) 
          {
              nombreArchivo = ruta + File.separator + archivos[i].getName();
              try
              {
                  decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(nombreArchivo)));
                  emisor = (Emisor) decoder.readObject(); 
                  emisores.put(emisor.getNombre(), emisor);
              } 
              catch (FileNotFoundException e)
              {
                  throw new PersistenciaException("No se pudo cargar emisor con id: " + archivos[i].getName());
              }
              finally
              {
                  decoder.close();
              }
          } 
        }
        
        return emisores;
    }

    @Override
    public void persistirConfirmacion(String nombreEmisor, TreeSet<String> confirmaciones) throws PersistenciaException
    {
        String nombreArchivo, ruta = System.getProperty("user.dir") + File.separator +DIRECTORIO_CONFIRM+File.separator;
        XMLEncoder encoder = null;
        
        nombreArchivo = ruta + nombreEmisor + TIPO_XML;
        try
        {
            encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(nombreArchivo)));
            encoder.writeObject(confirmaciones);
        } 
        catch (FileNotFoundException e)
        {
            throw new PersistenciaException("No se pudo persistir confirmaciones de emisor con id: " + nombreEmisor);
        }
        finally
        {
            encoder.close();
        }
    }

    @Override
    public void eliminarConfirmaciones(String nombreEmisor) throws PersistenciaException
    {
        String ruta = System.getProperty("user.dir") + File.separator + DIRECTORIO_CONFIRM + File.separator;
        
        if (!new File(ruta + nombreEmisor + TIPO_XML).delete())
            throw new PersistenciaException("No se pudo eliminar confirmaciones de emisor con id: " + nombreEmisor);
    }

    @Override
    public HashMap<String, TreeSet<String>> cargarConfirmacionesExistentes() throws PersistenciaException
    {
        HashMap<String, TreeSet<String>> confirmaciones = new HashMap<String, TreeSet<String>>();
        String nombreArchivo, nombreEmisor, ruta = System.getProperty("user.dir") + File.separator + DIRECTORIO_CONFIRM; 
        File[] archivos = new File(ruta).listFiles();
        XMLDecoder decoder = null;
        TreeSet<String> conjuntoEmisor;
        
        for (int i = 0; i < archivos.length; i++) 
        {
          if (archivos[i].isFile()) 
          {
              nombreArchivo = ruta + File.separator + archivos[i].getName();
              nombreEmisor = archivos[i].getName().replaceFirst("[.][^.]+$", ""); /* para eliminar extension */
              try
              {
                  decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(nombreArchivo)));
                  conjuntoEmisor = (TreeSet<String>) decoder.readObject(); 
                  confirmaciones.put(nombreEmisor, conjuntoEmisor);
              } 
              catch (FileNotFoundException e)
              {
                  throw new PersistenciaException("No se pudo cargar confirmaciones de emisor con id: " + nombreEmisor);
              }
              finally
              {
                  decoder.close();
              }
          } 
        }
        
        return confirmaciones;
    }
}
