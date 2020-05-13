package configuracion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Configurador implements IConfiguracion
{
    private static Configurador instance;
    
    private Configurador()
    {
        super();
    }
    
    public static synchronized Configurador getInstance()
    {
        if (instance == null)
            new Configurador();
        
        return instance;
    }
    
    @Override
    public Object[] cargarConfiguracion(String origen) throws IOException
    {
        final String SEPARADOR = ", *"; /* regex */
        final String ENCODING = "UTF-8";
        final int CANT_DATOS = 5;
        
        BufferedReader lector;
        String ruta = System.getProperty("user.dir") + File.separator + origen, linea;
        String[] datos;
        Object[] configuracion = new Object[CANT_DATOS];
        int puertoDirectorio, puertoConfirmacion, puertoAlmacen;
        
        lector = new BufferedReader(new InputStreamReader(new FileInputStream(ruta), ENCODING));
        linea = lector.readLine();
        lector.close();
        datos = linea.split(SEPARADOR);
        
        if ((datos.length == CANT_DATOS))
        {
            configuracion[0] = datos[0];                     /* IP directorio */
            puertoDirectorio = Integer.parseInt(datos[1]);   /* puerto directorio */
            configuracion[2] = datos[2];                     /* IP almacen */
            puertoAlmacen = Integer.parseInt(datos[3]);      /* puerto almacen */
            puertoConfirmacion = Integer.parseInt(datos[4]); /* puerto confirmacion */
        }
        else
            throw new IOException("faltan o sobran datos.");
        if (! ((puertoDirectorio >= 0) && (puertoDirectorio <= 65535)))
            throw new IOException("el puerto para recibir destinatarios del Directorio debe ser un entero en el rango [0-65535].");
        if (! ((puertoAlmacen >= 0) && (puertoAlmacen <= 65535)))
            throw new IOException("el puerto para enviar mensajes al Almacen debe ser un entero en el rango [0-65535].");
        if (! ((puertoConfirmacion >= 0) && (puertoConfirmacion <= 65535)))
            throw new IOException("el puerto para recibir confirmaciones de recepción debe ser un entero en el rango [0-65535].");
        
        configuracion[1] = puertoDirectorio;
        configuracion[3] = puertoAlmacen;
        configuracion[4] = puertoConfirmacion;
        
        return configuracion;
    }
}
