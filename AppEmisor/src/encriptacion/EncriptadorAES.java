package encriptacion;

import java.io.UnsupportedEncodingException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class EncriptadorAES implements IEncriptacion
{
    private SecretKeySpec llave;
    private final String ALGORITMO = "AES";
    
    public EncriptadorAES(String llaveAUsar)
    {
        this.setLlave(llaveAUsar);
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
    public String encriptar(String texto) 
    {
        String textoEncriptado = "";
        
        try
        {
            Cipher cipher = Cipher.getInstance(ALGORITMO + "/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, this.llave);
            textoEncriptado = Base64.getEncoder().encodeToString(cipher.doFinal(texto.getBytes("UTF-8")));
        } 
        catch (Exception e) 
        {
            System.out.println("Error al encriptar: " + e.getMessage());
        }
        
        return textoEncriptado;
    }
    
    public String desencriptar(String texto) /* una vez que se copie a desencriptador borrar de ac� */
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
