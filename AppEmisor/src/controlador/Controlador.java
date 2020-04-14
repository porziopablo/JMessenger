package controlador;

import agenda.Agenda;

import emisora.Emisora;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import java.util.TreeSet;

import mensaje.Mensaje;

import usuarios.Destinatario;

import vista.IVista;

public class Controlador implements Observer, ActionListener
{
    private IVista vista;
    private Emisora emisora;
    private Agenda agenda;
    
    public Controlador(IVista vista)
    {
        this.agenda = new Agenda();
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
        else if (evento.getActionCommand().equals(IVista.COMANDO_ACTUALIZAR))
            this.actualizarDestinatarios();
    }

    private void iniciarSesion()
    {
        this.emisora = new Emisora(this.vista.getNombre());
        this.emisora.addObserver(this);
        
        /* SOLO PARA TESTEAR UI */
        
//        TreeSet<Destinatario> destinatarios = new TreeSet<Destinatario>();
//        
//        destinatarios.add(new Destinatario("DELL", "192.168.0.9", "1234", true));
//        destinatarios.add(new Destinatario("TOSHIBA", "192.168.0.192", "1234", false));
//        destinatarios.add(new Destinatario("Ivan", "234.168.0.9", "1234", false));
//        destinatarios.add(new Destinatario("Martín", "300.168.0.9", "1234", true));
//        destinatarios.add(new Destinatario("Mariquena", "200.168.0.9", "1234", true));
//        
//        this.vista.actualizarAgenda(destinatarios.iterator());
        
        /* FIN CODIGO TEST UI */
    }

    private void enviarMensaje()
    {
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
        
        this.emisora.emitirMensaje(new Mensaje(asunto, cuerpo, tipo, destinatarios));
    }
    
    private void actualizarDestinatarios()
    {
        this.vista.mostrarCarga();
        this.vista.actualizarAgenda(this.agenda.actualizarDestinatarios());
    }
}
