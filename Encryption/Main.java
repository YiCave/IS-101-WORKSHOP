import java.util.Scanner;

public class Main {
    
    public static void main(String[] args) {
            App.launch(); //launching the program
    }   
}

//App class to handle sessions for this program
class App{
    private static final String PASSWORD = "Skibidi Toilet Rizz";
    private static final int SHIFT = 18;
    
    public static void launch(){
        System.out.println("Welcome to IS 101 Workshop Encryption demo session!\n");
        Scanner sc = new Scanner(System.in);
        STOP:
        while (true){
            System.out.println("Choose an action: \n 1.caesar cipher\n 2.SHA-256 hashing\n 3.End program\n");
            int option = sc.nextInt();
            switch(option){
                case 1 -> {
                    System.out.println("Using caesar cipher...");
                    caesarSession(PASSWORD);
                }
                case 2 -> {
                    System.out.println("Using SHA-256 hashing");
                    sha256Hashing(PASSWORD);
                }
                case 3 -> {
                    System.out.println("Ending program...");
                    break STOP;
                }
                default -> System.out.println("Invalid input, please try again");
            }
        }

    }
    
    //method that handles caesar cipher encryption sessions
    public static void caesarSession(String password){ 
        Scanner s1 = new Scanner(System.in);
        String encryptedPw = "";
        STOP:
        while (true){
            System.out.println("Choose an action: \n 1.Encrypt\n 2.Login\n 3.Decrypt for fun\n 4.Back\n");
            int option = s1.nextInt();
            switch(option){
                case 1 -> {
                    System.out.println("Encrypting using caesar cipher...");
                    encryptedPw = caesarCipher.encrypt(password, SHIFT);
                    System.out.println("Encrypted password: " + encryptedPw + "\n");
                }
                case 2 -> {
                    if (encryptedPw.equals("")){
                        System.out.println("Password was not encrypted, please encrypt the password first\n");
                        break;
                    }
                    System.out.println("Enter your password to login");
                    Scanner s2 = new Scanner(System.in);
                    String input = s2.nextLine();
                    String pw = caesarCipher.decrypt(encryptedPw, SHIFT);
                    if (pw.equals(input))
                        System.out.println("=== Correct password. Welcome back! ===\n");
                    else
                        System.out.println("Wrong password!\n");                             
                }
                case 3 -> {
                    if (encryptedPw.equals("")){
                        System.out.println("Password was not encrypted, please encrypt the password first\n");
                        break;
                    }
                    System.out.println("Decrypting stored password for checking...");
                    System.out.println("Decrypted password: " + caesarCipher.decrypt(encryptedPw, SHIFT) + "\n");
                }
                case 4 -> {
                    System.out.println("Going back...\n");
                    break STOP;
                }
                default -> System.out.println("Invalid input, please try again\n");
            }
        }
    }
    
    //method that handles SHA-256 password hashing sessions
    public static void sha256Hashing(String password){
        Scanner s3 = new Scanner(System.in);
        String encryptedPw = "";
        STOP:
        while (true){
            System.out.println("Choose an action: \n 1.Encrypt\n 2.Login\n 3.Back\n");
            int option = s3.nextInt();
            switch(option){
                case 1 -> {
                    System.out.println("Hashing password using SHA-256...");
                    encryptedPw = passwordHashing.hash(password);
                    System.out.println("Hashed password: " + encryptedPw + "\n");
                }
                case 2 -> {
                    if (encryptedPw.equals("")){
                        System.out.println("Password was not encrypted, please encrypt the password first");
                        break;
                    }
                    else{
                        System.out.println("Enter your password to login");
                        Scanner s4 = new Scanner(System.in);
                        String input = s4.nextLine();
                        if (passwordHashing.verifyPassword(input, encryptedPw))
                            System.out.println("=== Correct password. Welcome back! ===\n");
                        else
                            System.out.println("Wrong password!\n");
                    }
                }
                case 3 -> {
                    System.out.println("Going back...\n");
                    break STOP;
                }
                default -> System.out.println("Invalid input, please try again");
            }
        }
    }
}