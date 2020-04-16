package usuarios;

import java.text.Collator;

import java.util.Locale;

public class Destinatario extends Usuario implements Comparable
{
    private boolean online;
    
    public Destinatario(String nombre, String ip, String puerto, boolean online)
    {
        super(nombre, ip, puerto);
        this.online = online;
    }
    
    public Destinatario()
    {
        super("", "", "");
    }

    public void setOnline(boolean online)
    {
        this.online = online;
    }

    public boolean isOnline()
    {
        return online;
    }

    @Override
    public int compareTo(Object otro)
    {
        String nombreActual = this.getNombre().toLowerCase(Locale.forLanguageTag("es-ES"));
        String nombreOtro = ((Destinatario)otro).getNombre().toLowerCase(Locale.forLanguageTag("es-ES"));
        
        return Collator.getInstance(Locale.forLanguageTag("es-ES")).compare(nombreActual, nombreOtro);
    }

    @Override
    public String toString()
    {
        return this.getNombre();
    }
    
}
