package almacen;

import java.util.Iterator;
import java.util.TreeMap;

import mensaje.Mensaje;

public interface IGestionMensajes 
{
    void agregarMensaje(String nombreEmisor, String asunto, String cuerpo, String tipo, String[] listaDestinatarios);
    void eliminarMensaje(Iterator<String> idMensajes);
    TreeMap<String, Mensaje> getMensajesPendientes();    
}
