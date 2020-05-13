package configuracion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Configurador implements IConfiguracion
{
    private static Configurador instance = null;
    
    private Configurador()
    {
        super();
    }
    
    public static synchronized Configurador getInstance()
    {
        if (instance == null)
            instance = new Configurador();
        
        return instance;
    }
    
    @Override
    public Object[] cargarConfiguracion(String origen) throws IOException
    {
        final String SEPARADOR = ", *"; /* regex */
        final String ENCODING = "UTF-8";
        final int CANT_DATOS = 2;
        
        BufferedReader lector;
        String ruta = System.getProperty("user.dir") + File.separator + origen, linea;
        String[] datos;
        Object[] configuracion = new Object[CANT_DATOS];
        int puertoDestinatario, puertoEmisor;
        
        lector = new BufferedReader(new InputStreamReader(new FileInputStream(ruta), ENCODING));
        linea = lector.readLine();
        lector.close();
        datos = linea.split(SEPARADOR);
        
        if ((datos.length == CANT_DATOS))
        {                    
            try
            {
                puertoDestinatario = Integer.parseInt(datos[0]); /* puerto destinatario */
                puertoEmisor = Integer.parseInt(datos[1]);       /* puerto emisor */
            }
            catch (NumberFormatException e)
            {
                throw new IOException("El puerto ingresado contiene caracteres no numéricos: " + e.getMessage());
            }
        }
        else
            throw new IOException("faltan o sobran datos.");
        if (! ((puertoEmisor >= 0) && (puertoEmisor <= 65535)))
            throw new IOException("el puerto para enviar mensajes al Almacen debe ser un entero en el rango [0-65535].");
        if (! ((puertoDestinatario >= 0) && (puertoDestinatario <= 65535)))
            throw new IOException("el puerto para recibir confirmaciones de recepción debe ser un entero en el rango [0-65535].");
        if (puertoEmisor == puertoDestinatario)
            throw new IOException("El puerto para atender destinatarios debe ser diferente al utilizado para atender emisores.");
        
        configuracion[0] = puertoDestinatario;
        configuracion[1] = puertoEmisor;
        
        return configuracion;
    }
}