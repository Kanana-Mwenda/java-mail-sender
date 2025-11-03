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

public class Utils {
    // AES key
    private static final String AES_KEY = "Hg9xCEbkjtuuZED8k1Y7uqftZTsfTwlDd4JYoaRVimc=";

    // Generic encrypt method
    public static String encrypt(String value) {
        try {
            byte[] decodedKey = Base64.getDecoder().decode(AES_KEY);
            SecretKeySpec secretKey = new SecretKeySpec(decodedKey, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Generic decrypt method
    public static String decrypt(String encryptedValue) {
        try {
            byte[] decodedKey = Base64.getDecoder().decode(AES_KEY);
            SecretKeySpec secretKey = new SecretKeySpec(decodedKey, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedValue));
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Save value to XML
    public static void saveToXml(String tag, String value) {
        try {
            File xmlFile = new File("config.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = dbFactory.newDocumentBuilder();
            Document doc = documentBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            Element element = doc.createElement(tag);
            element.appendChild(doc.createTextNode(value));
            doc.getDocumentElement().appendChild(element);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(xmlFile);
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Get value from XML
    public static String getFromXml(String tag) {
        try {
            File xmlFile = new File("config.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            return doc.getElementsByTagName(tag).item(0).getTextContent();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Encrypt and save with prompt
    public static void encryptAndSave(String tag, String prompt) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            String encrypted = encrypt(input);
            saveToXml(tag, encrypted);
            System.out.println("Encrypted " + tag + " saved to config.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Encrypt password
    public static void encryptPassword(String tag) {
        encryptAndSave(tag, "Enter password to encrypt: ");
    }

    // Encrypt username
    public static void encryptUsername(String tag) {
        encryptAndSave(tag, "Enter username to encrypt: ");
    }

    // Encrypt DB name
    public static void encryptDBname(String tag) {
        encryptAndSave(tag, "Enter database name to encrypt: ");
    }

    // Get decrypted value
    public static String getDecrypted(String tag) {
        String encrypted = getFromXml(tag);
        return (encrypted != null) ? decrypt(encrypted) : null;
    }

    // Print decrypted value
    public static void printDecrypted(String tag) {
        String decrypted = getDecrypted(tag);
        if (decrypted != null) {
            System.out.println("Decrypted " + tag + ": " + decrypted);
        } else {
            System.out.println("Failed to decrypt " + tag);
        }
    }

    // Get URL value (assuming not encrypted)
    public static String getUrlValue(String tag) {
        return getFromXml(tag);
    }

    // Generate AES key
    public static String generateAESKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);
        SecretKey secretKey = keyGen.generateKey();
        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        System.out.println("AES key generated: " + encodedKey);
        return encodedKey;
    }

    // Main method
    public static void main(String[] args) {
        try {
            // Generate AES key
            String aesKey = generateAESKey();
            System.out.println("Generated AES Key: " + aesKey);

            // Encrypt passwords
            encryptPassword("SMTPPassword");
            encryptPassword("DBPassword");

            // Encrypt usernames
            encryptUsername("SMTPUsername");
            encryptUsername("DBUsername");

            // Encrypt DB name
            encryptDBname("DBName");

            // Print decrypted values
            printDecrypted("SMTPPassword");
            printDecrypted("DBPassword");
            printDecrypted("SMTPUsername");
            printDecrypted("DBUsername");
            printDecrypted("DBName");

            // Example: getDecryptedUsername
            String decryptedUsername = getDecrypted("SMTPUsername");
            System.out.println("Direct getDecryptedUsername: " + decryptedUsername);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
