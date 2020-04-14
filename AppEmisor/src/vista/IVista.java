package vista;

import java.awt.event.ActionListener;

import java.util.Iterator;
import java.util.List;

import usuarios.Destinatario;

public interface IVista
{
    String COMANDO_ENVIAR = "enviar";
    String COMANDO_INICIAR = "iniciarSesion";
    String COMANDO_ACTUALIZAR = "actualizar";
    
    public List<Destinatario> getDestinatarios();
    public String getAsunto();
    public String getCuerpo();
    public int getTipoMensaje();
    public String getNombre();
    public void addActionListener(ActionListener listener);
    public void informarEmisor(String mensaje);
    public void actualizarAgenda(Iterator<Destinatario> destinatarios);
    public void mostrarCarga();
}