package configurador;

import java.io.IOException;

public interface IConfiguracion {
    
    public Object[] cargarConfiguracion(String origen) throws IOException;
}
