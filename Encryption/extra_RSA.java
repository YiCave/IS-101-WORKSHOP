import java.security.KeyPair;
import java.security.KeyPairGenerator;
import javax.crypto.Cipher;

public class extra_RSA {
     public static void main(String[] args) {
        String password="nice";
        byte[] encrypted=encrypt(password);
        for(byte nice:encrypted){
            System.out.println(nice+" ");
        }
    }
    //have public key and private key (asymmetric)
    //only private key can be used to decrypt
    //used widely in WIFI, HTTPS, and password managers
    public static byte[] encrypt(String password){
        try{
            KeyPairGenerator kpg=KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048); //2048 or 4096
            KeyPair kp=kpg.generateKeyPair();
            
            Cipher cipher=Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, kp.getPublic());
            byte[] encrypted=cipher.doFinal(password.getBytes());
            return encrypted;
        }catch(Exception e){
            System.out.println("Error");
        }
        return null;
    }
}
