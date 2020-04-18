package vista;

import java.awt.event.ActionListener;

import java.awt.event.WindowAdapter;

import mensaje.Mensaje;

public interface IVista {
    
    String COMANDO_INICIAR = "iniciarSesion";
    
    public void addActionListener(ActionListener listener);
    public void agregarNuevoMensaje(Mensaje mensaje);
    public void informarResultadoInicioSesion(int resultado);
    public String getNombre();
    public String getPuerto();
}
