package almacen;

public interface IRegistro
{
    boolean agregarNuevoDest(String data);

    void online(String data);

    void offline(String data);
}
