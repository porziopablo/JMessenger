package vista;

import java.awt.event.ActionListener;

import java.util.List;

import usuarios.Destinatario;

public interface IVista
{
    String COMANDO_ENVIAR = "enviar";
    String COMANDO_INICIAR = "iniciarSesion";
    
    public List<Destinatario> getDestinatarios();
    public String getAsunto();
    public String getCuerpo();
    public int getTipoMensaje();
    public String getNombre();
    public String getPuerto();
    public void addActionListener(ActionListener listener);
    public void informarEmisor(String mensaje);
}