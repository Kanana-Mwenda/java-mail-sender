package EmailSenderApp.src;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import java.io.File;

public class DecryptPassword {

    // AES key
    private static final String AES_KEY = "Hg9xCEbkjtuuZED8k1Y7uqftZTsfTwlDd4JYoaRVimc=";
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java DecryptPassword <passwordType>");
            System.out.println("Example: java DecryptPassword encryptedAppPassword");
            return;
        }

        String passwordType = args[0]; //"SMTPPassword" or "DBPassword"
        String decryptedPassword = getDecryptedPassword(passwordType);

        if (decryptedPassword != null) {
            System.out.println("Decrypted Password (" + passwordType + "): " + decryptedPassword);
        } else {
            System.out.println("Failed to decrypt password.");
        }
    }

    // Method to decrypt a password based on its type
    public static String getDecryptedPassword(String passwordType) {
        try {
            // Load AES key from config.xml
            File xmlFile = new File("config.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = dbFactory.newDocumentBuilder();
            Document doc = documentBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            // Extract encrypted password
            String encryptedPassword = doc.getElementsByTagName(passwordType).item(0).getTextContent();

            // Decode AES key from base64
            byte[] decodedKey = Base64.getDecoder().decode(AES_KEY);
            SecretKeySpec secretKey = new SecretKeySpec(decodedKey, "AES");

            // Decrypt the password
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedPassword));
            
            return new String(decryptedBytes, StandardCharsets.UTF_8);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
