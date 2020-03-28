package usuarios;

public class Destinatario extends Usuario implements Comparable
{
    public Destinatario(String nombre, String ip, String puerto)
    {
        super(nombre, ip, puerto);
    }

    @Override
    public int compareTo(Object otro)
    {
        return this.getNombre().compareTo(((Destinatario)otro).getNombre());
    }


    @Override
    public String toString()
    {
        return this.getNombre();
    }
}
