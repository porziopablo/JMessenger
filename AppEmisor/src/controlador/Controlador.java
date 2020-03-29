package controlador;

import emisora.Emisora;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;

import java.net.InetAddress;

import java.net.URL;
import java.net.UnknownHostException;

import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import mensaje.Mensaje;

import usuarios.Destinatario;
import usuarios.Emisor;

import vista.IVista;

public class Controlador implements Observer, ActionListener
{
    private IVista vista;
    private Emisora emisora;
    
    public Controlador(IVista vista)
    {
        this.vista = vista;
        vista.addActionListener(this);
    }

    @Override
    public void update(Observable observado, Object arg)
    {
        if (observado == this.emisora)
        {
            String nombreDestinatario = (String) arg;
            this.vista.informarEmisor(nombreDestinatario + " ha recibido tu mensaje.");
        }
    }

    @Override
    public void actionPerformed(ActionEvent evento)
    {
        if (evento.getActionCommand().equals(IVista.COMANDO_INICIAR))
            this.iniciarSesion();
        else if (evento.getActionCommand().equals(IVista.COMANDO_ENVIAR))
            this.enviarMensaje();
    }

    private void iniciarSesion()
    {
        try
        {
//            URL averiguarIP = new URL("http://checkip.amazonaws.com");
//            BufferedReader lector = new BufferedReader(new InputStreamReader(averiguarIP.openStream()));
//            String ip = lector.readLine(); /* IP publica */
//            lector.close();
            
            String ip = InetAddress.getLocalHost().getHostAddress(); /* IP local */
            String nombre = this.vista.getNombre();
            String puerto = this.vista.getPuerto();
            
            this.emisora = new Emisora(new Emisor(nombre, ip, puerto));
            this.emisora.addObserver(this);
            this.emisora.recibirConfirmacion();
            
            this.vista.actualizarAgenda(this.emisora.getEmisor().getAgendaIterator());
        }
        catch (IOException e)
        {
            this.vista.informarEmisor("NO SE PUDO OBTENER IP, REVISAR CONEXION Y REINICIAR.");       
        }
    }

    private void enviarMensaje()
    {
        String asunto = this.vista.getAsunto();
        String cuerpo = this.vista.getCuerpo();
        int tipo = this.vista.getTipoMensaje();
        List<Destinatario> destinatarios = this.vista.getDestinatarios();
        Iterator<Destinatario> iter;
        String nombres = "";
        
        this.emisora.emitirMensaje(new Mensaje(asunto, cuerpo, tipo, destinatarios));
        
        iter = destinatarios.iterator();
        nombres = iter.next().getNombre();
        while (iter.hasNext())
            nombres = nombres + ", " + iter.next().getNombre();
        this.vista.informarEmisor("Enviando mensaje a " + nombres + ".");
    }
}
