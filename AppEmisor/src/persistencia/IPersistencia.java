package persistencia;

import java.util.TreeMap;

import mensaje.Mensaje;

public interface IPersistencia
{
    void persistirMensaje(Mensaje mensaje) throws PersistenciaException;
    void eliminarMensaje(String idMensaje) throws PersistenciaException;
    TreeMap<String, Mensaje> cargarMensajesExistentes() throws PersistenciaException;
}
