package configuracion;

import java.io.IOException;

public interface IConfiguracion
{
    Object[] cargarConfiguracion(String origen) throws IOException;
}
