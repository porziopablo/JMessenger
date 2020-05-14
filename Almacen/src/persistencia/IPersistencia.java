package persistencia;

import java.util.HashMap;
import java.util.Iterator;

import java.util.TreeMap;

import java.util.TreeSet;

import mensaje.Mensaje;

import usuario.Emisor;

public interface IPersistencia
{
    void persistirMensaje(Mensaje mensaje) throws PersistenciaException;
    void eliminarMensaje(String idMensaje) throws PersistenciaException;
    TreeMap<String, Mensaje> cargarMensajesExistentes() throws PersistenciaException; 
    void persistirEmisor(Emisor emisor) throws PersistenciaException;
    void eliminarEmisor(String nombreEmisor) throws PersistenciaException;
    TreeMap<String, Emisor> cargarEmisoresExistentes() throws PersistenciaException;
    void persistirConfirmacion(String nombreEmisor, Iterator<String> confirmaciones) throws PersistenciaException;
    void eliminarConfirmaciones(String nombreEmisor) throws PersistenciaException;
    HashMap<String, TreeSet<String>> cargarConfirmacionesExistentes() throws PersistenciaException;
}
