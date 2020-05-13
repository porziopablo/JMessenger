package usuario;

public class Emisor extends Usuario
{
    public Emisor(String nombre, String ip, String puerto)
    {
        super(nombre, ip, puerto);
    }
    
    public Emisor()
    {
        super("", "", "");
    }
}
