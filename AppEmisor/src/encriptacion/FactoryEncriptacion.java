package encriptacion;

public class FactoryEncriptacion implements ICreacionEncriptacion
{
    private static FactoryEncriptacion instance = null;
    public static final String AES = "AES";
    
    private FactoryEncriptacion()
    {
        super();
    }
    
    public static synchronized FactoryEncriptacion getInstance()
    {
        if (instance == null)
            instance = new FactoryEncriptacion();
        
        return instance;
    }
    
    @Override
    public IEncriptacion getEncriptacion(String tipo, String llave)
    {
        IEncriptacion encriptacion = null;
        
        if (tipo.equalsIgnoreCase(AES))
            encriptacion = new EncriptadorAES(llave);
        
        return encriptacion;
    }
}
