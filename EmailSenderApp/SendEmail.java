package EmailSenderApp;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Base64;
import javax.mail.Transport;
import java.nio.file.Files;
import java.nio.file.Paths;


public class SendEmail {
    public static void main(String[] args) {

        // Recipient and sender emails
        String recipient = "lizamwenda95@gmail.com";
        String sender = "kananamwenda20@gmail.com";

        // Gmail SMTP host
        String host = "smtp.gmail.com";

        // Gmail authentication credentials
        final String username = "kananamwenda20@gmail.com"; 
        final String password = "sfoq hwop njvp qsqt";

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
            
            byte[] imageBytes = Files.readAllBytes(Paths.get("EmailSenderApp/assets/tatua-logo.png"));
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
            
            //html template
            String htmlTemplate = new String(Files.readAllBytes(Paths.get("EmailSenderApp/templates/welcome.html")));
   
            htmlTemplate = htmlTemplate.replace("{{base64Image}}", base64Image);
            message.setContent(htmlTemplate, "text/html; charset=utf-8");

            // Send the message
            Transport.send(message);
            System.out.println(" Mail successfully sent!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
