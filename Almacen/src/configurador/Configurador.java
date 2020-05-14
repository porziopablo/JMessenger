package configurador;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import persistencia.FactoryPersistencia;

public class Configurador implements IConfiguracion{
    
    private static Configurador instance = null;
    
    private Configurador() {
        super();
    }

    public static synchronized Configurador getInstance()
    {
        if (instance == null)
            instance = new Configurador();
        
        return instance;
    }
    
    @Override
    public Object[] cargarConfiguracion(String origen) throws IOException {
        
        final String SEPARADOR = ", *"; /* regex */
        final String ENCODING = "UTF-8";
        final int CANT_DATOS = 4;
        
        BufferedReader lector;
        String ruta = System.getProperty("user.dir") + File.separator + origen, linea, tipoPersistencia, ipDirectorio;
        String[] datos;
        Object[] configuracion = new Object[CANT_DATOS];
        int puertoDirectorio, puertoEmisor;
        
        lector = new BufferedReader(new InputStreamReader(new FileInputStream(ruta), ENCODING));
        linea = lector.readLine();
        lector.close();
        datos = linea.split(SEPARADOR);
        
        if ((datos.length == CANT_DATOS))
        {                    
            try
            {
                ipDirectorio = datos[0];                         /* ip directorio */
                puertoDirectorio = Integer.parseInt(datos[1]);   /* puerto directorio */
                puertoEmisor = Integer.parseInt(datos[2]);       /* puerto emisor */
                tipoPersistencia = datos[3];                     /* tipo de persistencia */
            }
            catch (NumberFormatException e)
            {
                throw new IOException("Uno de los puertos ingresados contienen caracteres no numéricos: " + e.getMessage());
            }
        }
        else
            throw new IOException("faltan o sobran datos.");
        
        if (! ((puertoEmisor >= 0) && (puertoEmisor <= 65535)))
            throw new IOException("el puerto para escuchar al Emisor debe ser un entero en el rango [0-65535].");
        if (! ((puertoDirectorio >= 0) && (puertoDirectorio <= 65535)))
            throw new IOException("el puerto para interactuar con el Directorio debe ser un entero en el rango [0-65535].");
        if (! tipoPersistencia.equalsIgnoreCase(FactoryPersistencia.TIPO1))
            throw new IOException("el tipo de persistencia no es valido");
        
        configuracion[0] = ipDirectorio;
        configuracion[1] = puertoDirectorio;
        configuracion[2] = puertoEmisor;
        configuracion[3] = tipoPersistencia;
        
        return configuracion;
    }
}
