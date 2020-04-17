package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.WindowEvent;

import java.net.InetAddress;

import java.net.UnknownHostException;

import java.util.Observable;
import java.util.Observer;

import mensaje.Mensaje;

import notificadora.Notificadora;

import receptora.Receptora;

import usuarios.Destinatario;

import vista.IVista;

public class Controlador implements ActionListener, Observer{
    
    private IVista vista;
    private Receptora receptora; //observable
    private Notificadora notificadora;
    
    public Controlador(IVista vista) {
        this.vista = vista;
        vista.addActionListener(this);
    }

    private int iniciarSesion(){
        
        int correcto = 0;
        try{
            String ip = InetAddress.getLocalHost().getHostAddress();
            String nombre = this.vista.getNombre();
            String puerto = this.vista.getPuerto();
            
            this.notificadora = new Notificadora();
            int registro = this.notificadora.registrarDestinatario(nombre, ip, puerto);
            
            if(registro == 1){
                correcto = 1;
                this.receptora = new Receptora(new Destinatario(nombre, ip, puerto)); 
                this.receptora.addObserver(this);   
                this.receptora.recibirMensaje();
            }
            
        } catch (UnknownHostException e) {
            System.out.println("Error en la obtencion de la IP");
        }
    return correcto;
    }
    
    @Override
    public void actionPerformed(ActionEvent evento) {
        if (evento.getActionCommand().equals(IVista.COMANDO_INICIAR)){
            int res = this.iniciarSesion();
            this.vista.informarResultadoInicioSesion(res);
        }        
    }

    @Override                                   
    public void update(Observable observable, Object param) {
        
        if(observable == receptora){
            this.vista.agregarNuevoMensaje((Mensaje) param);  
        }
    }
    
// Para cuando cierro la app avisar al directorio que me fui y no que espere al heartbeat 
    public void windowClosing( WindowEvent evento){
        
    }
    
}
