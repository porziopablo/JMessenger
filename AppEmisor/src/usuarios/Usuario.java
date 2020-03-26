package usuarios;

public abstract class Usuario
{
    private String nombre;
    private String ip;
    private String puerto;

    public Usuario(String nombre, String ip, String puerto)
    {
        this.nombre = nombre;
        this.ip = ip;
        this.puerto = puerto;
    }

    public String getNombre()
    {
        return nombre;
    }

    public String getIp()
    {
        return ip;
    }

    public String getPuerto()
    {
        return puerto;
    }
}
