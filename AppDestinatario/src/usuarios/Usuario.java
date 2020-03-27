package usuarios;

public class Usuario {
    
    private String nombre, ip, puerto;
    
    public Usuario(String nombre, String ip, String puerto) {
        this.nombre = nombre;
        this.ip =ip;
        this.puerto =puerto;
    }

//getters
    public String getNombre() {
        return nombre;
    }

    public String getIp() {
        return ip;
    }

    public String getPuerto() {
        return puerto;
    }
}
