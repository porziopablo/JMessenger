package controlador;

import emisora.Emisora;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.net.InetAddress;

import java.net.UnknownHostException;

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
            String ip = InetAddress.getLocalHost().getHostAddress();
            String nombre = this.vista.getNombre();
            String puerto = this.vista.getPuerto();
            
            this.emisora = new Emisora(new Emisor(nombre, ip, puerto));
            this.emisora.addObserver(this);
            this.emisora.recibirConfirmacion();
        }
        catch (UnknownHostException e)
        {
            this.vista.informarEmisor("NO SE PUDO OBTENER IP, REVISAR CONEXION Y REINICIAR: " + e.getMessage());       
        }
    }

    private void enviarMensaje()
    {
        String asunto = this.vista.getAsunto();
        String cuerpo = this.vista.getCuerpo();
        int tipo = this.vista.getTipoMensaje();
        List<Destinatario> destinatarios = this.vista.getDestinatarios();
        
        this.emisora.emitirMensaje(new Mensaje(asunto, cuerpo, tipo, destinatarios));
    }
}