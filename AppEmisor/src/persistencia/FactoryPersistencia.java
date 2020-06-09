package persistencia;

public class FactoryPersistencia implements ICreacionPersistencia
{
    private static FactoryPersistencia instance = null;
    public final static String XML = "XML";
    
    private FactoryPersistencia() 
    {
        super();
    }
    
    public static synchronized FactoryPersistencia getInstance()
    {
        if (instance == null)
            instance = new FactoryPersistencia();
        return instance;
    }

    @Override
    public IPersistencia getPersistencia(String tipo) 
    {
        IPersistencia persistencia = null;
        
        if (tipo.equalsIgnoreCase(XML))
            persistencia = new PersistenciaXML();

        return persistencia;
    }
}
