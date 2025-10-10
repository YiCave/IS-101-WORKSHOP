
public class caesarCipher {
    
    public static String encrypt(String text, int shift){
        StringBuffer result = new StringBuffer();
        
        for (int i = 0; i < text.length(); i++){
            if(Character.isAlphabetic(text.charAt(i))){
                if (Character.isUpperCase(text.charAt(i))){
                    char c = (char)((text.charAt(i) + shift - 65) % 26 + 65);
                result.append(c);
                }
                else{
                    char c = (char)((text.charAt(i) + shift - 97) % 26 + 97);
                result.append(c);
                }
            }
            else{
                result.append(text.charAt(i));
            }            
        }
        return result.toString();
    }
    
    public static String decrypt(String text, int shift){
        StringBuffer result = new StringBuffer();
        
        for (char c : text.toCharArray()){
            if (Character.isAlphabetic(c)){
                if (Character.isUpperCase(c)){
                    char ch = (char)((c + (26 - shift) - 65) % 26 + 65);
                    result.append(ch);
                }
                else{
                    char ch = (char)((c + (26 - shift) - 97) % 26 + 97);
                    result.append(ch);
                }
            }
            else{
                result.append(c);
            }
        }
        return result.toString();
    }
}
