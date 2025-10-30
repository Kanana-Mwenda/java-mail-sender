package EmailSenderApp.src;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Scanner;

public class CryptoUtils {
    public static String GenerateAESKey() throws Exception {
        // Generate AES key
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);
        SecretKey secretKey = keyGen.generateKey();

        //convert to base64
        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());

        System.out.println("AES key generated:" + encodedKey);
        return encodedKey;
    }


    // Encrypt password method
    // AES key
    private static final String AES_KEY = "Hg9xCEbkjtuuZED8k1Y7uqftZTsfTwlDd4JYoaRVimc=";
    
    public static String encrypt(String clearPassword) {
        try {
            // Prepare AES key
            byte[] decodedKey = Base64.getDecoder().decode(AES_KEY);
            SecretKeySpec secretKey = new SecretKeySpec(decodedKey, "AES");

            // Encrypt password
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(clearPassword.getBytes(StandardCharsets.UTF_8));

            return Base64.getEncoder().encodeToString(encryptedBytes);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void encryptPassword() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter password to encrypt: ");
            String password = scanner.nextLine();

            String encryptedPassword = encrypt(password);

            // Update XML with encrypted password
            File xmlFile = new File("config.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = dbFactory.newDocumentBuilder();
            Document doc = documentBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            Element encryptedElement = doc.createElement("encryptedPassword");
            encryptedElement.appendChild(doc.createTextNode(encryptedPassword));
            doc.getDocumentElement().appendChild(encryptedElement);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(xmlFile);
            transformer.transform(source, result);

            System.out.println("Encrypted password saved to config.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Decrypt password method
    public static void decryptPassword(String passwordType) {
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

    //main method
    public static void main(String[] args) {
    try {
        // Step 1: Generate AES key
        // String aesKey = GenerateAESKey();
        // System.out.println("Generated AES Key: " + aesKey);

        // Step 2: Encrypt a password
        encryptPassword();

        // Step 3: Decrypt password from config.xml
        decryptPassword("SMTPPassword");

    } catch (Exception e) {
        e.printStackTrace();
    }
}

}
