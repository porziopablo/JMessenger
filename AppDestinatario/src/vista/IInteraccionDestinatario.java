package vista;

import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import mensaje.Mensaje;

public interface IInteraccionDestinatario {
    
    String COMANDO_INICIAR = "iniciarSesion";
    
    public void addActionListener(ActionListener listener);
    public void addWindowListener(WindowListener w);
    public void agregarNuevoMensaje(Mensaje mensaje);
    public void informarResultadoInicioSesion(int resultado);
    public String getNombre();
    public String getPuerto();
}
