package mensaje;

import java.util.List;

import usuarios.Destinatario;

public class Mensaje
{
    final static int MENSAJE_SIMPLE = 0;
    final static int MENSAJE_ALERTA = 1;
    final static int MENSAJE_RECEPCION = 2;
    
    private String asunto, cuerpo;
    private int tipo;
    private List<Destinatario> destinatarios;

    public Mensaje(String asunto, String cuerpo, int tipo, List<Destinatario> destinatarios)
    {
        this.asunto = asunto;
        this.cuerpo = cuerpo;
        this.tipo = tipo;
        this.destinatarios = destinatarios;
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

    public List<Destinatario> getDestinatarios()
    {
        return destinatarios;
    }
}
