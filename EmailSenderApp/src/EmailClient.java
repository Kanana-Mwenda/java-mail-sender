package EmailSenderApp.src;

import EmailSenderApp.src.CryptoUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class EmailClient {

    // InitializeApp function
    public static void initializeApp(){
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

                        String encryptedPassword =  CryptoUtils.encrypt("SMTPPassword",clearPassword);

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

                        String encryptedPassword = CryptoUtils.encrypt("DBPassword",clearPassword);

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

    // DBConnection function
    public static Connection getConnection() {
        String url = "jdbc:postgresql://localhost:5432/emails_db";
        String user = "kanana";

        String password = CryptoUtils.getDecryptedPassword("DBPassword");

        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the database!");
            return conn;

        } catch (SQLException e) {
            System.out.println("Failed to connect to the database!");
            e.printStackTrace();
            return null;
        }
    }


    // InsertEmailSender function
    public static void insertEmailSender(Connection conn) {
        try {

            //SQL Query
            String sql = "INSERT INTO email_senders (sender_name, sender_username, sender_password, date_created, date_modified) VALUES (?, ?, ?, ?, ?)";
            //SQL Statement
            PreparedStatement stmt = conn.prepareStatement(sql);

            String senderName = "Kanana";
            String senderUsername = "kananamwenda20@gmail.com";
            String senderPassword = "TGEz827ilG7jLyUTCH113PWxChyU0HKeD/gs5tvCRfk=";

            Timestamp now = new Timestamp(System.currentTimeMillis());

            //Set parameters
            stmt.setString(1, senderName);
            stmt.setString(2, senderUsername);
            stmt.setString(3, senderPassword);
            stmt.setTimestamp(4, now);
            stmt.setTimestamp(5, now);

            //Execute the insertion
            int rows = stmt.executeUpdate();

            if (rows>0){
                System.out.println(" A new sender has been inserted successfully!");
            } else {
                System.out.println(" Insertion failed!");
            }
            } catch (SQLException e){
                e.printStackTrace();
        }

    }


    // SendEmail function
    public static void sendEmail(Connection conn, String htmlTemplate) {
        // Recipient and sender emails
        String recipient = "lizamwenda95@gmail.com";
        String sender = "kananamwenda20@gmail.com";

        // Gmail SMTP host
        String host = "smtp.gmail.com";

        // Gmail authentication credentials
        final String username = "kananamwenda20@gmail.com";
        final String password = CryptoUtils.getDecryptedPassword("SMTPPassword");

        // Mail properties
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true"); //authentication
        properties.put("mail.smtp.starttls.enable", "true");//security protocol
        properties.put("mail.smtp.host", host);//gmail server
        properties.put("mail.smtp.port", "587");//port number

        // Session with authentication
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Mime message object
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(sender)); //senders email
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient)); //recipients email
            message.setSubject("Test Email using Gmail SMTP"); //subject
            message.setContent(htmlTemplate, "text/html;");

            // Send the message
            Transport.send(message);
            System.out.println(" Mail successfully sent!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

        // InsertEmailLog function
    public static void insertEmailLog(
            Connection conn,
            int senderIdFk,
            String senderNameFk,
            String receiverEmail,
            String emailContentType,
            String emailMessageBody,
            String emailSubject,
            String status,
            Timestamp statusDate,
            String statusDescription,
            String emailAttachment,
            Timestamp dateCreated,
            Timestamp dateModified)
            {

                // SQL Query
                String sql = "INSERT INTO email_logs "
                        + "(sender_id_fk, sender_name_fk, receiver_email, email_content_type, email_message_body, "
                        + "email_subject, status, status_date, status_description, email_attachment, date_created, date_modified) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?::email_status, ?, ?, ?, ?,?)";

            Timestamp now = new Timestamp(System.currentTimeMillis());

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set parameters
            stmt.setInt(1, senderIdFk);
            stmt.setString(2, senderNameFk);
            stmt.setString(3, receiverEmail);
            stmt.setString(4, emailContentType);
            stmt.setString(5, emailMessageBody);
            stmt.setString(6, emailSubject);
            stmt.setString(7, status);
            stmt.setTimestamp(8, statusDate);
            stmt.setString(9, statusDescription);
            stmt.setString(10, emailAttachment);
            stmt.setTimestamp(11, now);
            stmt.setTimestamp(12, now);

            // Execute the insertion
            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("Email log inserted successfully!");
            } else {
                System.out.println("Failed to insert the email log.");
            }

        } catch (SQLException e){
                System.out.println("Database error occurred while inserting email log.");
                e.printStackTrace();
        }
            }

    public static void main(String[] args) {
        initializeApp();
        Connection conn = getConnection();
        insertEmailSender(conn);
        String htmlTemplate = "";
        try {
            // Load HTML template
            htmlTemplate = new String(Files.readAllBytes(Paths.get("EmailSenderApp/templates/welcome.html")));
        } catch (Exception e) {
            System.out.println("Failed to read email template: " + e.getMessage());
        }
        sendEmail(conn, htmlTemplate);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        insertEmailLog(
            conn,
            1,                               // sender_id_fk
            "Kanana",                      // sender_name_fk
            "lizamwenda95@gmail.com",                                  // receiver_email
            "text/html",               // email_content_type
            htmlTemplate,                               // email_message_body
            "Test Email using Gmail SMTP", // email_subject
            "sent",                              // status
            now,                                        // status_date
            "Email sent successfully",// status_description
            null,                       // email_attachment
            now,                                        // date_created
            now                                         // date_modified
        );
    }
}

