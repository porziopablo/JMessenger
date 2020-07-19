package persistencia;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.util.TreeMap;

import mensaje.Mensaje;

public class PersistenciaXML implements IPersistencia
{
    private static final String DIRECTORIO_MSJ = "persistencia_pendientes";
    private final static String TIPO_XML = ".xml";
    
    public PersistenciaXML() 
    {
        new File(DIRECTORIO_MSJ).mkdir();     /* crea directorio, si no existe */
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
}
