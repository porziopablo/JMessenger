package mensaje;

import usuarios.Emisor;

public class Mensaje implements Cloneable
{
    public final static int MENSAJE_SIMPLE = 0;
    public final static int MENSAJE_ALERTA = 1;
    public final static int MENSAJE_RECEPCION = 2;
    
    private String nombreEmisor, nombreDestinatario, asunto, cuerpo, id;
    private int tipo;

    public Mensaje(String nombreEmisor, String nombreDestinatario, String asunto, String cuerpo, String id, int tipo)
    {
        this.nombreEmisor = nombreEmisor;
        this.nombreDestinatario = nombreDestinatario;
        this.asunto = asunto;
        this.cuerpo = cuerpo;
        this.id = id;
        this.tipo = tipo;
    }

    public Mensaje()
    {
        super();
    }

    public void setNombreEmisor(String nombreEmisor)
    {
        this.nombreEmisor = nombreEmisor;
    }

    public String getNombreEmisor()
    {
        return nombreEmisor;
    }

    public void setNombreDestinatario(String nombreDestinatario)
    {
        this.nombreDestinatario = nombreDestinatario;
    }

    public String getNombreDestinatario()
    {
        return nombreDestinatario;
    }

    public void setAsunto(String asunto)
    {
        this.asunto = asunto;
    }

    public String getAsunto()
    {
        return asunto;
    }

    public void setCuerpo(String cuerpo)
    {
        this.cuerpo = cuerpo;
    }

    public String getCuerpo()
    {
        return cuerpo;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return id;
    }

    public void setTipo(int tipo)
    {
        this.tipo = tipo;
    }

    public int getTipo()
    {
        return tipo;
    }
    
    @Override
    public Object clone(){
        Mensaje copia = null;
        try {
            copia = (Mensaje) super.clone();
           
        } catch (CloneNotSupportedException e) {
            System.out.println(e.getMessage());
        }
        return copia;
    }    
}
