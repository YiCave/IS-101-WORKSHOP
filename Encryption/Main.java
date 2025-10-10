
public class Main {

    public static void main(String[] args) {
        int shift = 18;
        String text = caesarCipher.encrypt("Skibidi Toilet Rizz", shift);
        System.out.println("Encrypted text: " + text);
        String decrypt = caesarCipher.decrypt(text, shift);
        System.out.println("Decrypted text: " + decrypt);
        String x = passwordHashing.hash(decrypt);
        System.out.println(x);
        if (passwordHashing.verifyPassword("Skibidi Toilet Rizz", x))
            System.out.println("Correct password, Welcome!");
        else
            System.out.println("Wrong passord, Please try again");
    }   
}
