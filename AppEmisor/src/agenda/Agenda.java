package agenda;

import directorio.Directorio;

import java.beans.XMLDecoder;

import java.io.BufferedInputStream;

import java.io.IOException;

import java.net.Socket;

import java.util.ArrayList;
import java.util.Iterator;

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
    public Iterator<Destinatario> actualizarDestinatarios()
    {
        TreeMap<String, Destinatario> destinatarios = new TreeMap<String, Destinatario>();
        XMLDecoder entrada;
        Socket socket;
                
        try
        {
            socket = new Socket("", 0);
            entrada = new XMLDecoder(new BufferedInputStream(socket.getInputStream()));
            
            destinatarios = (TreeMap<String, Destinatario>) entrada.readObject();
            entrada.close();
        }  
        catch (IOException e)
        {
            System.out.println("Error al recibir agenda desde el Directorio: " + e.getMessage());
        }

        return destinatarios.values().iterator();
    }
}
