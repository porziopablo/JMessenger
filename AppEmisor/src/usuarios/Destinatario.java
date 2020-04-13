package usuarios;

public class Destinatario extends Usuario implements Comparable
{
    private boolean online;
    
    public Destinatario(String nombre, String ip, String puerto, boolean online)
    {
        super(nombre, ip, puerto);
        this.online = online;
    }
    
    public Destinatario()
    {
        super("", "", "");
    }

    public void setOnline(boolean online)
    {
        this.online = online;
    }

    public boolean isOnline()
    {
        return online;
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
