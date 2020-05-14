package agenda;

import java.beans.XMLDecoder;

import java.io.BufferedInputStream;
import java.io.IOException;

import java.net.Socket;

import java.util.Iterator;

import java.util.TreeMap;

import usuarios.Destinatario;

public class Agenda implements IActualizacionDestinatarios
{
    private String ipDirectorio;
    private int puertoDirectorio;

    public Agenda(String ipDirectorio, int puertoDirectorio)
    {
        this.ipDirectorio = ipDirectorio;
        this.puertoDirectorio = puertoDirectorio;
    }

    @Override
    public TreeMap<String, Destinatario> actualizarDestinatarios()
    {
        TreeMap<String, Destinatario> destinatarios = new TreeMap<String, Destinatario>();
        XMLDecoder entrada;
        Socket socket;
                
        try
        {
            socket = new Socket(this.ipDirectorio, this.puertoDirectorio);
            entrada = new XMLDecoder(new BufferedInputStream(socket.getInputStream()));
            
            destinatarios = (TreeMap<String, Destinatario>) entrada.readObject();
            entrada.close();
        }  
        catch (IOException e)
        {
            System.out.println("Error al recibir agenda desde el Directorio: " + e.getMessage());
        }

        return destinatarios;
    }
}
