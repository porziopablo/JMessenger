package almacen;

import java.util.TreeMap;

import usuarios.Destinatario;

public interface IEntrega
{
    TreeMap<String, Destinatario> getDestinatariosActualizados();
}
