package almacen;

public interface IActualizacion
{
    public void agregarCopiaDest(String nombre, String ip, String puerto, long timestamp);
    public void onlineCopia(String nombreDest, long timestamp);
    public void offlineCopia(String nombreDest);
    public Object[] getEstado();
}
