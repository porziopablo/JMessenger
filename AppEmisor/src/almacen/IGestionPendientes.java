package almacen;

import java.util.TreeMap;

import mensaje.Mensaje;

public interface IGestionPendientes
{
    public void almacenarMensaje(Mensaje mensaje);
    public void eliminarMensaje(String id);
    public TreeMap<String, Mensaje> getCopiaPendientes();
}
