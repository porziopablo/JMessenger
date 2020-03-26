package main;

import java.util.Iterator;

import usuarios.Destinatario;
import usuarios.Emisor;

public class Main
{
    public static void main(String[] args)
    {   
        Iterator<Destinatario> iter = Emisor.getInstancia().getAgendaIterator();
        
        while (iter.hasNext())
        {
            Destinatario prox = iter.next();
            System.out.println(prox.getNombre() + " IP: " + prox.getIp() + " Puerto: " + prox.getPuerto());
        }
    }
}
