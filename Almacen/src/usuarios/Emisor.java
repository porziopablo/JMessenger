package usuarios;

public class Emisor extends Usuario implements Cloneable
{
    public Emisor(String nombre, String ip, String puerto)
    {
        super(nombre, ip, puerto);
    }
    
    public Emisor()
    {
        super("", "", "");
    }
    
    @Override
    public Object clone(){
        Emisor copia = null;
        try {
            copia = (Emisor) super.clone();
           
        } catch (CloneNotSupportedException e) {
            System.out.println(e.getMessage());
        }
        return copia;
    }    
}
