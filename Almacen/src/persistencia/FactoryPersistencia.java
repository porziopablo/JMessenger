package persistencia;


public class FactoryPersistencia implements ICreacionPersistencia{
    
    private static FactoryPersistencia instance = null;
    public final static String TIPO1 = "XML";
    
    private FactoryPersistencia() {
        super();
    }
    
    public static synchronized FactoryPersistencia getInstance(){
        if (instance == null)
            instance = new FactoryPersistencia();
        return instance;
    }

    @Override
    public IPersistencia getPersistencia(String tipo) {
        IPersistencia p = null;
        if(tipo.equalsIgnoreCase(TIPO1)){
            p = new PersistenciaXML();
        }
        return p;
        
    }
}
