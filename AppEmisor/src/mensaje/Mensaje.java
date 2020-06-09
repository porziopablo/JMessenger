package mensaje;

import java.util.ArrayList;
import java.util.List;

import usuarios.Destinatario;

public class Mensaje
{
    public final static int MENSAJE_SIMPLE = 0;
    public final static int MENSAJE_ALERTA = 1;
    public final static int MENSAJE_RECEPCION = 2;
    
    private String asunto, cuerpo, nombreEmisor, id;
    private int tipo;
    private ArrayList<String> destinatarios;

    public Mensaje(String nombreEmisor, String asunto, String cuerpo, int tipo, ArrayList<String> destinatarios)
    {
        this.nombreEmisor = nombreEmisor;
        this.asunto = asunto;
        this.cuerpo = cuerpo;
        this.tipo = tipo;
        this.destinatarios = destinatarios;
        this.id = "";
    }

    public String getAsunto()
    {
        return asunto;
    }

    public String getCuerpo()
    {
        return cuerpo;
    }

    public int getTipo()
    {
        return tipo;
    }

    public ArrayList<String> getDestinatarios()
    {
        return destinatarios;
    }

    public String getNombreEmisor()
    {
        return nombreEmisor;
    }

    public String getId()
    {
        return id;
    }

    public void setAsunto(String asunto)
    {
        this.asunto = asunto;
    }

    public void setCuerpo(String cuerpo)
    {
        this.cuerpo = cuerpo;
    }

    public void setNombreEmisor(String nombreEmisor)
    {
        this.nombreEmisor = nombreEmisor;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public void setTipo(int tipo)
    {
        this.tipo = tipo;
    }

    public void setDestinatarios(ArrayList<String> destinatarios)
    {
        this.destinatarios = destinatarios;
    }
}
