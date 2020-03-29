package usuarios;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileInputStream;;
import java.io.IOException;

import java.io.InputStreamReader;

import java.util.Iterator;
import java.util.TreeSet;

public class Emisor extends Usuario
{
    private TreeSet<Destinatario> agenda;
    
    public Emisor(String nombre, String ip, String puerto)
    {
        super(nombre, ip, puerto);
        this.agenda = new TreeSet<Destinatario>();
        this.agendarDestinatarios();
    }
    
    private void agendarDestinatarios()
    {       
        final String NOMBRE_ARCHIVO = "agenda.txt";
        final String SEPARADOR = ", *"; /* regex */
        final String ENCODING = "UTF-8";
        final int CANT_DATOS = 3;
        
        BufferedReader lector;
        String ruta = System.getProperty("user.dir") + File.separator + NOMBRE_ARCHIVO, linea;
        String[] datos;

        try
        {
            lector = new BufferedReader(new InputStreamReader(new FileInputStream(ruta), ENCODING));
            linea = lector.readLine();
            while (linea != null)
            {
                datos = linea.split(SEPARADOR);
                if ((datos.length == CANT_DATOS))
                    this.agenda.add(new Destinatario(datos[0], datos[1], datos[2]));   
                linea = lector.readLine();
            }
            lector.close();
        }
        catch(IOException e)
        {
            System.out.println("Error al levantar agenda: " + e.getMessage());
        }
    }
    
    public Iterator<Destinatario> getAgendaIterator()
    {
        return this.agenda.iterator();
    }
}
