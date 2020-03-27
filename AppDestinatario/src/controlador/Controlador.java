package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.net.InetAddress;

import java.net.UnknownHostException;

import java.util.Observable;
import java.util.Observer;

import mensaje.Mensaje;

import receptora.Receptora;

import usuarios.Destinatario;

import vista.IVista;

public class Controlador implements ActionListener, Observer{
    
    private IVista vista;
    private Receptora receptora; //observable
    
    public Controlador(IVista vista) {
        this.vista = vista;
        vista.addActionListener(this);
    }

    private void iniciarSesion(){
        
        try{
            String ip = InetAddress.getLocalHost().getHostAddress();
            String nombre = this.vista.getNombre();
            String puerto = this.vista.getPuerto();
            
            this.receptora = new Receptora( new Destinatario(nombre, ip, puerto)); 
            this.receptora.addObserver(this);    
            
        } catch (UnknownHostException e) {
            this.vista.informarDestinatario("No se pudo obtener IP, revisar conexion y reiniciar: " + e.getMessage());
        }

    }
    
    @Override
    public void actionPerformed(ActionEvent evento) {
        if (evento.getActionCommand().equals(IVista.COMANDO_INICIAR))
            this.iniciarSesion();
    }

    @Override                                   
    public void update(Observable observable, Object param) {
        
        if(observable == receptora){
            this.vista.agregarNuevoMensaje((Mensaje) param);  
        }
    }
    
}
