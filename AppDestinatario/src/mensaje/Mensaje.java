package mensaje;

public class Mensaje {
    
    final static int MENSAJE_SIMPLE = 0;
    final static int MENSAJE_ALERTA = 1;
    final static int MENSAJE_RECEPCION = 2;
    
    private String asunto, cuerpo, nombreEmisor;
    private int tipo;
    
    public Mensaje(String asunto, String cuerpo, int tipo, String nombreEmisor) {
        this.asunto = asunto;
        this.cuerpo = cuerpo;
        this.tipo = tipo;
        this.nombreEmisor = nombreEmisor;
    }

//getters
    public String getAsunto() {
        return asunto;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public String getNombreEmisor() {
        return nombreEmisor;
    }

    public int getTipo() {
        return tipo;
    }
}
