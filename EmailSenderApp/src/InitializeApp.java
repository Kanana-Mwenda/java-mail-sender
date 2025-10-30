package EmailSenderApp.src;

import java.io.File;
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


                //SMTP credentials
                Element smtpPassword = (Element) doc.getElementsByTagName("SMTPPassword").item(0);
                if (smtpPassword != null){
                    String passwordType = smtpPassword.getAttribute("type");

                //encryption
                    if (passwordType.equals("cleartext")){
                        String clearPassword = smtpPassword.getTextContent().trim();
                        System.out.println("Encrypting SMTP password...");
                        
                        String encryptedPassword =  EncryptPassword.encrypt(clearPassword);

                        //update XML
                        smtpPassword.setTextContent(encryptedPassword);
                        smtpPassword.setAttribute("type", "encrypted");
                } else {
                    System.out.println("SMTPPassword is already encrypted.");
                }
        }

                //Database credentials
                Element dbPassword = (Element) doc.getElementsByTagName("DBPassword").item(0);
                if (dbPassword != null){
                    String PasswordType = dbPassword.getAttribute("type");
                    //encryption
                    if (PasswordType.equals("cleartext")){
                        String clearPassword = dbPassword.getTextContent().trim();
                        System.out.println("Encrypting DB password...");

                        String encryptedPassword = EncryptPassword.encrypt(clearPassword);
   
                        //update XML
                        dbPassword.setTextContent(encryptedPassword);
                        dbPassword.setAttribute("type", "encrypted");
                    } else {
                        System.out.println("DBPassword is already encrypted.");
                    }
                }
                     
                    //save updated XML
                    TransformerFactory transformerFactory = TransformerFactory.newInstance();
                    Transformer transformer = transformerFactory.newTransformer();
                    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                    DOMSource source = new DOMSource(doc);
                    StreamResult result = new StreamResult(configFile);
                    transformer.transform(source, result); 

                    System.out.println("Config.xml file updated successfully.");
     
            } catch (Exception e){
                System.out.println("Initialization failed: " + e.getMessage());         
        }
    }

    public static void main(String[] args) {
        run();
    }
}
