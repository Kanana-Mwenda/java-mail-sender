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
    public static void main(String[] args) {
        try (
            //password to encrypt
            Scanner scanner = new Scanner(System.in)) {;
            System.out.print("Enter password to encrypt: ");
            String password = scanner.nextLine();
            
            // Load AES key from config.xml
            File xmlFile = new File("config.xml");

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = dbFactory.newDocumentBuilder();

            //parse XML file
            Document doc = documentBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            //read AES key
            String keyString = doc.getElementsByTagName("aesKey").item(0).getTextContent();
            
            // Decode base64 key
            byte[] decodedKey = Base64.getDecoder().decode(keyString);
            SecretKeySpec secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");


            // Encrypt the password
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(password.getBytes(StandardCharsets.UTF_8));
            String encryptedPassword = Base64.getEncoder().encodeToString(encryptedBytes);

            // Update XML with encrypted password
            Element encryptedElement = doc.createElement("encryptedPassword");
            encryptedElement.appendChild(doc.createTextNode(encryptedPassword));
            doc.getDocumentElement().appendChild(encryptedElement);

            //Save updated XML File
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(xmlFile);
            transformer.transform(source,result);

            System.out.println("Encrypted password saved to config.xml");


    } catch (Exception e) {
        e.printStackTrace();
    }
}}
