package almacen;

import java.util.HashMap;
import java.util.TreeMap;
import java.util.TreeSet;

import usuarios.Emisor;

public interface IGestionConfirmaciones
{
    void agregarConfirmacion(String nombreEmisor, String nombreDestinatario);
    void eliminarConfirmacion(String nombreEmisor);
    void agregarEmisor(String nombreEmisor, String ip, String puerto);
    HashMap<String, TreeSet<String>> getConfirmacionesPendientes();
    TreeMap<String, Emisor> getEmisores();  
}
