package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;
import mensaje.Mensaje;
import notificadora.IConexion;
import receptora.Receptora;
import usuarios.Destinatario;
import vista.IInteraccionDestinatario;

public class Controlador extends WindowAdapter implements ActionListener, Observer {
    
    private IInteraccionDestinatario vista;
    private Receptora receptora; //observable
    private IConexion notificadora;
    
    public Controlador(IInteraccionDestinatario vista, IConexion notificadora, Receptora receptora) {
        this.vista = vista;
        this.vista.addActionListener(this);
        this.vista.addWindowListener(this);
        this.notificadora = notificadora;
        this.receptora = receptora;
    }

    private int iniciarSesion(){
       
        int correcto = 0;
        try{
            String ip = InetAddress.getLocalHost().getHostAddress();
            String nombre = this.vista.getNombre();
            String puerto = this.vista.getPuerto();
            
            int registro = this.notificadora.registrarDestinatario(nombre, ip, puerto);
            
            if(registro == 1){
                correcto = 1;
                this.receptora.setDestinatario(new Destinatario(nombre, ip, puerto)); 
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
        if (evento.getActionCommand().equals(IInteraccionDestinatario.COMANDO_INICIAR)){
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
    
   
    @Override
    public void windowClosing(WindowEvent e)
    {
        this.notificadora.apagar();
    }
    
}
