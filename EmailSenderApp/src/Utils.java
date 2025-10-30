package EmailSenderApp.src;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Scanner;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Utils {

    // Generate AES key
    private static final String AES_KEY = "Hg9xCEbkjtuuZED8k1Y7uqftZTsfTwlDd4JYoaRVimc=";

    public static void generateAESKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);
        SecretKey secretKey = keyGen.generateKey();

        //convert to base64
        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());

        try(FileWriter writer = new FileWriter("config.xml")) {
            writer.write(encodedKey);
        }
        System.out.println("AES key generated and saved to aes.key");
    }

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
    public static void decryptPassword(String[] args) {
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
            // Load encrypted password from config.xml
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

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java Utils <command> [args]");
            System.out.println("Commands:");
            System.out.println("  generate - Generate AES key");
            System.out.println("  encrypt - Encrypt password");
            System.out.println("  decrypt <passwordType> - Decrypt password");
            return;
        }

        String command = args[0].toLowerCase();

        switch (command) {
            case "generate":
                try {
                    generateAESKey();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "encrypt":
                encryptPassword();
                break;
            case "decrypt":
                if (args.length < 2) {
                    System.out.println("Usage: java Utils decrypt <passwordType>");
                    return;
                }
                decryptPassword(java.util.Arrays.copyOfRange(args, 1, args.length));
                break;
            default:
                System.out.println("Unknown command: " + command);
                break;
        }
    }
}
