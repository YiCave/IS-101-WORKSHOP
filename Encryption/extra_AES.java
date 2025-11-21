import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.Cipher;

public class extra_AES {
     //symmetric encryption method
    //use SAME KEY to encrpyt and decrypt
    public static void main(String[] args) {
        String password="nice";
        byte[] encrypted=encrypt(password);
        for(byte nice:encrypted){
            System.out.println(nice+" ");
        }
    }
    public static byte[] encrypt(String password){
       
        try{
             KeyGenerator kg=KeyGenerator.getInstance("AES");
             kg.init(256); //128 bits. 192 bits, or 256 bits
             SecretKey sk=kg.generateKey();
             
             Cipher cipher=Cipher.getInstance("AES");
             cipher.init(Cipher.ENCRYPT_MODE, sk);
             byte[]encrypted=cipher.doFinal(password.getBytes());
             return encrypted;
        }catch(Exception e){
            System.out.println("Error");
        }
        return null;
    }
}
