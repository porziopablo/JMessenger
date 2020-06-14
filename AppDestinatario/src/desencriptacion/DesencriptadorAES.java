package desencriptacion;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class DesencriptadorAES implements IDesencriptacion {
    
    private final String ALGORITMO = "AES";
    private SecretKeySpec llave;
    
    public DesencriptadorAES(String llave) {
        this.setLlave(llave);
    }

    private void setLlave(String llaveAUsar) 
    {
        MessageDigest sha = null;
        byte[] llaveString;
                
        try 
        {
            llaveString = llaveAUsar.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            llaveString = sha.digest(llaveString);
            llaveString = Arrays.copyOf(llaveString, 16); 
            this.llave = new SecretKeySpec(llaveString, ALGORITMO);
        } 
        catch (Exception e) 
        {
            System.out.println("Error al setear llave: " + e.getMessage());
        }
    }
    
    @Override
    public String desencriptar(String texto) 
    {
        String textoDesencriptado = "";
        try
        {
            Cipher cipher = Cipher.getInstance(ALGORITMO + "/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, this.llave);
            textoDesencriptado = new String(cipher.doFinal(Base64.getDecoder().decode(texto)));
        } 
        catch (Exception e) 
        {
            System.out.println("Error al desencriptar: " + e.getMessage());
        }
        
        return textoDesencriptado;
    }
    
}
