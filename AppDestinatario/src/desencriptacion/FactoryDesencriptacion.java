package desencriptacion;

public class FactoryDesencriptacion implements ICreacionDesencriptacion{
    
    private static FactoryDesencriptacion instance = null;
    public static final String AES = "AES";
    
    private FactoryDesencriptacion() {
        super();
    }

    public static synchronized FactoryDesencriptacion getInstance(){
        
        if (instance == null)
            instance = new FactoryDesencriptacion();
        
        return instance;
    }
    
    @Override
    public IDesencriptacion getDesencriptacion(String tipo, String llave) {
        
        IDesencriptacion desencriptacion = null;
        
        if (tipo.equalsIgnoreCase(AES))
            desencriptacion = new DesencriptadorAES(llave);
        
        return desencriptacion;
    }
}
