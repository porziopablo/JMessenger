package vista;

import java.awt.event.ActionListener;

import mensaje.Mensaje;

public class Vista implements IVista{
    public Vista() {
        super();
    }

    @Override
    public void addActionListener(ActionListener listener) {
        
    }

    @Override
    public void agregarNuevoMensaje(Mensaje mensaje) {
       
        Mensaje recibido = (Mensaje) mensaje; 
        StringBuilder builder = new StringBuilder();
        builder.append("De: " + recibido.getNombreEmisor() + "\n");
        builder.append("Asunto: " + recibido.getAsunto() + "\n");
        builder.append(recibido.getCuerpo() + "\n");
        
        //VER COMO AGREGARLO A LA VISTA
    }

    @Override
    public String getNombre() {
        
        return null;
    }

    @Override
    public String getPuerto() {
        
        return null;
    }

    @Override
    public void informarDestinatario(String error) {
        // TODO Implement this method
    }
}
