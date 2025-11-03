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
    
    public static String encrypt(String tagName,String clearPassword) {
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

    public static void encryptPassword(String tagName) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter password to encrypt: ");
            String clearPassword = scanner.nextLine();

            String encryptedPassword = encrypt(tagName, clearPassword);
            

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
        }}

        //encrypt username
        public static void encryptUsername(String tagName) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter username to encrypt: ");
            String clearUsername = scanner.nextLine();

            String encryptedUsername = encrypt(tagName, clearUsername);
            

            // Update XML with encrypted password
            File xmlFile = new File("config.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = dbFactory.newDocumentBuilder();
            Document doc = documentBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            Element encryptedElement = doc.createElement("encryptedUsername");
            encryptedElement.appendChild(doc.createTextNode(encryptedUsername));
            doc.getDocumentElement().appendChild(encryptedElement);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(xmlFile);
            transformer.transform(source, result);

            System.out.println("Encrypted username saved to config.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

        //encrypt db name
        public static void encryptDBname(String tagName) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter database name to encrypt: ");
            String clearDBname = scanner.nextLine();

            String encryptedDBname= encrypt(tagName, clearDBname);
            

            // Update XML with encrypted password
            File xmlFile = new File("config.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = dbFactory.newDocumentBuilder();
            Document doc = documentBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            Element encryptedElement = doc.createElement("encryptedDBname");
            encryptedElement.appendChild(doc.createTextNode(encryptedDBname));
            doc.getDocumentElement().appendChild(encryptedElement);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(xmlFile);
            transformer.transform(source, result);

            System.out.println("Encrypted database name saved to config.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

    // Method to get encrypted password from config.xml
    public static String getEncryptedPassword(String tagName) {
    try {
        File configFile = new File("config.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(configFile);
        doc.getDocumentElement().normalize();

        return doc.getElementsByTagName(tagName).item(0).getTextContent();

    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}

    // Method to get encrypted username from config.xml
    public static String getEncryptedUsername(String tagName) {
    try {
        File configFile = new File("config.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(configFile);
        doc.getDocumentElement().normalize();

        return doc.getElementsByTagName(tagName).item(0).getTextContent();

    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}


    // Decrypt password method
    public static void decryptPassword(String tagName) {
        String decryptedPassword = getDecryptedPassword(tagName);

        if (decryptedPassword != null) {
            System.out.println("Decrypted Password (" + tagName + "): " + decryptedPassword);
        } else {
            System.out.println("Failed to decrypt password.");
        }
    }

    // Method to decrypt a password based on its type
    public static String getDecryptedPassword(String tagName) {
        try {
            File xmlFile = new File("config.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = dbFactory.newDocumentBuilder();
            Document doc = documentBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            // Extract encrypted password
            String encryptedPassword = doc.getElementsByTagName(tagName).item(0).getTextContent();

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

        // Method to decrypt a username based on its type
    public static String getDecryptedUsername(String tagName) {
        try {
            File xmlFile = new File("config.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = dbFactory.newDocumentBuilder();
            Document doc = documentBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            // Extract encrypted password
            String encryptedUsername = doc.getElementsByTagName(tagName).item(0).getTextContent();

            // Decode AES key from base64
            byte[] decodedKey = Base64.getDecoder().decode(AES_KEY);
            SecretKeySpec secretKey = new SecretKeySpec(decodedKey, "AES");

            // Decrypt the password
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedUsername));

            return new String(decryptedBytes, StandardCharsets.UTF_8);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to decrypt a dbname based on its type
    public static String getDecryptedDBname(String tagName) {
        try {
            File xmlFile = new File("config.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = dbFactory.newDocumentBuilder();
            Document doc = documentBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            // Extract encrypted password
            String encryptedDBname = doc.getElementsByTagName(tagName).item(0).getTextContent();

            // Decode AES key from base64
            byte[] decodedKey = Base64.getDecoder().decode(AES_KEY);
            SecretKeySpec secretKey = new SecretKeySpec(decodedKey, "AES");

            // Decrypt the password
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedDBname));

            return new String(decryptedBytes, StandardCharsets.UTF_8);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    // Method to get url value 
        public static String getUrlValue(String tagName) {
            try {
                File xmlFile = new File("config.xml");
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(xmlFile);
                doc.getDocumentElement().normalize();

                return doc.getElementsByTagName(tagName).item(0).getTextContent().trim();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }


    //main method
    public static void main(String[] args) {
    try {
        // Step 1: Generate AES key
        String aesKey = GenerateAESKey();
        System.out.println("Generated AES Key: " + aesKey);

        // Step 2: Encrypt a password
        encryptPassword("SMTPPassword");
        encryptPassword("DBPassword");

        //Step 3: Encrypt a username
        encryptUsername("SMTPUsername");
        encryptUsername("DBUsername");

        //Step 4: Encrypt a database name
        encryptDBname("DBName");

        // Step 3: Decrypt password from config.xml
        decryptPassword("SMTPPassword");
        decryptPassword("DBPassword");
        decryptPassword("DBUsername");

    } catch (Exception e) {
        e.printStackTrace();
    }
}

}
