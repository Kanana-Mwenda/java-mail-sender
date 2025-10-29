package EmailSenderApp.src;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Base64;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Element;
import org.w3c.dom.Document;


public class InitializeApp {
    public static void run(){
            try{
                File configFile = new File("config.xml");

                //check if config.xml exists
                if (!configFile.exists()){
                    System.out.println("Configuration file not found.");
                    return;
                } 

                //parse XML file
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(configFile);
                doc.getDocumentElement().normalize();


                //check smtpuser and smtppassword elements
                Element smtpUser =(Element) doc.getElementsByTagName("SMTPUser").item(0);
                Element smtpPassword = (Element) doc.getElementsByTagName("SMTPPassword").item(0);

                if (smtpUser == null || smtpPassword== null){
                    System.out.println("SMTPUser or SMTPPassword elements missing in config.xml");
                    return;
                }

                //check the type attribute
                String userType = smtpUser.getAttribute("type");
                String passwordType = smtpPassword.getAttribute("type");

                System.out.println("SMTP User Type: " + userType);
                System.out.println("SMTP Password Type: " + passwordType);

                // load aes key
                File keyFile = new File("aes.key");
                if (!keyFile.exists()) {
                    System.out.println("AES key file not found (aes.key)");
                    return;
                }

                String base64Key = new String(Files.readAllBytes(keyFile.toPath()), StandardCharsets.UTF_8);
                byte[] decodedKey = Base64.getDecoder().decode(base64Key);
                SecretKeySpec secretKey = new SecretKeySpec(decodedKey, "AES");


                //encryption
                if (passwordType.equals("cleartext")){
                    String clearPassword = smtpPassword.getTextContent().trim();
                    System.out.println("Encrypting SMTP password...");
                    
                    String encryptedPassword =  EncryptPassword.encrypt(clearPassword);

                    //update XML
                    smtpPassword.setTextContent(encryptedPassword);
                    smtpPassword.setAttribute("type", "encrypted");

                    //save updated XML
                    TransformerFactory transformerFactory = TransformerFactory.newInstance();
                    Transformer transformer = transformerFactory.newTransformer();
                    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                    DOMSource source = new DOMSource(doc);
                    StreamResult result = new StreamResult(configFile);
                    transformer.transform(source, result); 

                    System.out.println("SMTPPassword encrypted and config.xml updated.");
                    } else {
                    System.out.println("SMTPPassword is already encrypted.");

                }

            } catch (Exception e){
                System.out.println("Initialization failed: " + e.getMessage());         
        }
    }
}
