package replicacion;

public interface IReplicacion
{
    public void replicarDest(String nombre, String ip, String puerto, long timestamp);
    public void replicarOnline(String nombreDest, long timestamp);
    public void replicarOffline(String nombreDest);
    public Object[] copiarEstado();
}
