package replicacion;

import directorio.Directorio;

import java.util.ArrayList;

public class Replicacion implements IRecepecionCambios, IReplicacion
{
    private ArrayList<Directorio> directorios;
    private int puertoReplicacion;

    public Replicacion(ArrayList<Directorio> directorios, int puertoReplicacion)
    {
        this.directorios = directorios;
        this.puertoReplicacion = puertoReplicacion;
    }

    @Override
    public void recibirCambios()
    {
        // TODO Implement this method
    }

    @Override
    public void replicarDest(String nombre, String ip, String puerto, long timestamp)
    {
        // TODO Implement this method
    }

    @Override
    public void replicarOnline(String nombreDest, long timestamp)
    {
        // TODO Implement this method
    }

    @Override
    public void replicarOffline(String nombreDest)
    {
        // TODO Implement this method
    }

    @Override
    public Object[] copiarEstado()
    {
        // TODO Implement this method
        return new Object[0];
    }
}
