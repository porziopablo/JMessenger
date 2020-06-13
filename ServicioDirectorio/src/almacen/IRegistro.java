package almacen;

public interface IRegistro
{
    boolean agregarNuevoDest(String nombre, String ip, String puerto);

    void online(String nombreDest);

    void offline(String nombreDest);
}
