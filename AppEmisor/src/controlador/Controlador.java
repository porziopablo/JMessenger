package controlador;

import agenda.IActualizacionDestinatarios;

import emisora.Emisora;

import emisora.IEmisionMensaje;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import java.util.Timer;

import mensaje.Mensaje;

import usuarios.Destinatario;

import vista.IInteraccionEmisor;

public class Controlador implements Observer, ActionListener
{
    private IInteraccionEmisor vista;
    private IEmisionMensaje emisora;
    private IActualizacionDestinatarios agenda;
    
    public Controlador(IInteraccionEmisor vista, IActualizacionDestinatarios agenda)
    {
        this.agenda = agenda;
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
        if (evento.getActionCommand().equals(IInteraccionEmisor.COMANDO_INICIAR))
            this.iniciarSesion();
        else if (evento.getActionCommand().equals(IInteraccionEmisor.COMANDO_ENVIAR))
            this.enviarMensaje();
        else if (evento.getActionCommand().equals(IInteraccionEmisor.COMANDO_ACTUALIZAR))
            this.actualizarDestinatarios();
    }

    private void iniciarSesion()
    {
        this.emisora = new Emisora(this.vista.getNombre()); /* en ITER 3 ya no se necesita el nombre dentro de emisora */
        this.emisora.addObserver(this);                     /* new emisora no se hará dentro del controlador */
        this.actualizarDestinatarios();
    }

    private void enviarMensaje()
    {
        Timer timer =  new java.util.Timer();
        String asunto = this.vista.getAsunto();
        String cuerpo = this.vista.getCuerpo();
        int tipo = this.vista.getTipoMensaje();
        List<Destinatario> destinatarios = this.vista.getDestinatarios();
        Iterator<Destinatario> iter;
        String nombres = "";
        
        iter = destinatarios.iterator();
        nombres = iter.next().getNombre();
        while (iter.hasNext())
            nombres = nombres + ", " + iter.next().getNombre();
        this.vista.informarEmisor("Enviando mensaje a " + nombres + ".");
        
        timer.schedule
        ( 
            new java.util.TimerTask() 
            {
                @Override
                public void run() 
                {
                    emisora.emitirMensaje(new Mensaje(asunto, cuerpo, tipo, destinatarios));
                    timer.cancel();
                }
            }, 
            500 
        );
    }
    
    private void actualizarDestinatarios()
    {
        Timer timer =  new java.util.Timer();
        
        this.vista.mostrarCarga();
        timer.schedule
        ( 
            new java.util.TimerTask() 
            {
                @Override
                public void run() 
                {
                    vista.actualizarAgenda(agenda.actualizarDestinatarios());
                    timer.cancel();
                }
            }, 
            500 
        );
    }
}
