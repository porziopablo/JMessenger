package notificadora;

public interface IConexion {

    public int registrarDestinatario(String nombreDest, String ipDest, String puertoDest);
    public void apagar();
    
}
