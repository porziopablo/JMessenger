package emisora;

import agenda.IActualizacionDestinatarios;

import java.util.Iterator;

import java.util.TreeMap;

import usuario.Destinatario;

public class Emisora implements Runnable
{
    private IActualizacionDestinatarios agenda;

    public Emisora(IActualizacionDestinatarios agenda)
    {
        this.agenda = agenda;
    }

    @Override
    public void run()
    {
        TreeMap<String, Destinatario> destinatarios;
        
        while (true)
        {
            destinatarios = this.agenda.actualizarDestinatarios();
            
        }
    }
}
