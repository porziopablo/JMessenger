package almacen;

import java.util.Iterator;
import java.util.TreeMap;

import mensaje.Mensaje;

public interface IGestionMensajes 
{
    void agregarMensaje(String mensaje, String[] listaDestinatarios);
    void eliminarMensaje(Iterator<String> idMensajes);
    TreeMap<String, Mensaje> getMensajesPendientes();    
}
