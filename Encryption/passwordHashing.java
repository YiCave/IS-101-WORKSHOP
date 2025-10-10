import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class passwordHashing {
    
    public static String hash(String password){
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            
            byte[] hashBytes = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashBytes);
        }
        catch(NoSuchAlgorithmException e){
            throw new RuntimeException(e);
        }
    }
    
    public static boolean verifyPassword(String inputPassword, String storedHash){
        String inputHash = hash(inputPassword);
        return inputHash.equals(storedHash);
    }
}
