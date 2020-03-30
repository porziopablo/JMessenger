package vista;

import java.awt.event.ActionListener;

import mensaje.Mensaje;

public interface IVista {
    
    String COMANDO_INICIAR = "iniciarSesion";
    
    public void addActionListener(ActionListener listener);
    public void agregarNuevoMensaje(Mensaje mensaje);
    public String getNombre();
    public String getPuerto();
}
