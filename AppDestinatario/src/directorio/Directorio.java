package directorio;

public class Directorio {
    
    private String ip;
    private int puerto;

    public Directorio(String ip, int puerto)
    {
        this.ip = ip;
        this.puerto = puerto;
    }

    public String getIp()
    {
        return ip;
    }

    public int getPuerto()
    {
        return puerto;
    }
    
}
