package gestores;

import almacen.Almacen;

import java.beans.XMLEncoder;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.io.InputStreamReader;

import java.net.ServerSocket;
import java.net.Socket;

public class GestorEmisor implements Runnable
{
    private int puertoEmisor = 1235; /* valor por defecto */
    
    public GestorEmisor()
    {
        super();
        this.cargarConfiguracion();
    }
    
    private void cargarConfiguracion()
    {
        final String NOMBRE_ARCHIVO = "config_directorio.txt";
        final String SEPARADOR = ", *"; /* regex */
        final String ENCODING = "UTF-8";
        final int CANT_DATOS = 2;
        
        BufferedReader lector;
        String ruta = System.getProperty("user.dir") + File.separator + NOMBRE_ARCHIVO, linea;
        String[] datos;

        try
        {
            lector = new BufferedReader(new InputStreamReader(new FileInputStream(ruta), ENCODING));
            linea = lector.readLine();
            lector.close();
            
            datos = linea.split(SEPARADOR);
            if ((datos.length == CANT_DATOS))
                this.puertoEmisor = Integer.parseInt(datos[1]);
            else
                throw new IOException("faltan o sobran datos.");
            
            if (! ((this.puertoEmisor >= 0) && (this.puertoEmisor <= 65535)))
                throw new IOException("el puerto para atender a los emisores debe ser un entero en el rango [0-65535].");
            if (datos[0] == datos[1])
                throw new IOException("El puerto para atender destinatarios debe ser diferente al utilizado para atender emisores.");
        }
        catch(IOException e)
        {
            System.out.println("Error al cargar configuracion: " + e.getMessage());
        }
        catch(NumberFormatException e)
        {
            System.out.println("Error al cargar configuracion: " + e.getMessage());
        }
    }

    @Override
    public void run()
    {
        ServerSocket serverSocket;
        Socket socket;
        XMLEncoder salida;
        
        System.out.println("ATENDIENDO EMISORES");
        try
        {
            serverSocket = new ServerSocket(this.puertoEmisor);
            while (true)
            {
                socket = serverSocket.accept();
                salida = new XMLEncoder(new BufferedOutputStream(socket.getOutputStream()));
                
                salida.writeObject(Almacen.getInstance().getDestinatariosActualizados());
                salida.close();
            }
        } 
        catch (IOException e)
        {
            System.out.println("Error al enviar destinararios: " + e.getMessage());
        }
    }
}
