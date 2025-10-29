package EmailSenderApp.src;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.Scanner;

public class EncryptPassword {

    public static String encrypt(String clearPassword, SecretKeySpec secretKey) {
        try {
            // Load AES key from config.xml
            File xmlFile = new File("config.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = dbFactory.newDocumentBuilder();
            Document doc = documentBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            // Read AES key
            String keyString = doc.getElementsByTagName("aesKey").item(0).getTextContent();
            byte[] decodedKey = Base64.getDecoder().decode(keyString);
            SecretKeySpec secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

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

    public static void main(String[] args) {
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
}
