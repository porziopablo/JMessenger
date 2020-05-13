package gestores;

import almacen.Almacen;

import java.beans.XMLEncoder;

import java.io.BufferedOutputStream;

import java.io.IOException;


import java.net.ServerSocket;
import java.net.Socket;

public class GestorEmisor implements Runnable
{
    private int puertoEmisor = 1235; /* valor por defecto */

    public GestorEmisor(int puertoEmisor)
    {
        this.puertoEmisor = puertoEmisor;
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
