package agenda;

import directorio.Directorio;

import java.beans.XMLDecoder;

import java.io.BufferedInputStream;
import java.io.IOException;

import java.net.Socket;

import java.util.ArrayList;
import java.util.TreeMap;

import usuarios.Destinatario;

public class Agenda implements IActualizacionDestinatarios
{
    private ArrayList<Directorio> directorios;
    private int directorioActual;

    public Agenda(ArrayList<Directorio> directorios)
    {
        this.directorios = directorios;
        this.directorioActual = 0;
    }

    @Override
    public TreeMap<String, Destinatario> actualizarDestinatarios()
    {
        TreeMap<String, Destinatario> destinatarios = new TreeMap<String, Destinatario>();
        XMLDecoder entrada = null;
        Socket socket;
        boolean opRealizada = false;
        Directorio directorio;
        
        while (! opRealizada) /* pre-condicion: al menos un directorio activo en todo momento */
        {
            directorio = this.directorios.get(this.directorioActual);
            try
            {
                socket = new Socket(directorio.getIp(), directorio.getPuerto());
                entrada = new XMLDecoder(new BufferedInputStream(socket.getInputStream()));
                destinatarios = (TreeMap<String, Destinatario>) entrada.readObject();
                opRealizada = true;
            }  
            catch (IOException e)
            {
                this.directorioActual = ((this.directorioActual + 1) % this.directorios.size());
                System.out.println("cambio directorio a " + this.directorioActual);
            }
            finally
            {
                if (entrada != null)
                    entrada.close();
            }
        }

        return destinatarios;
    }
}
