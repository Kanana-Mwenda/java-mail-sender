package EmailSenderApp.src;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;



public class SendEmail {
    public static void main(String[] args) {

        // Recipient and sender emails
        String recipient = "lizamwenda95@gmail.com";
        String sender = "kananamwenda20@gmail.com";

        // Gmail SMTP host
        String host = "smtp.gmail.com";

        // Gmail authentication credentials
        final String username = "kananamwenda20@gmail.com"; 
        final String password = DecryptPassword.getDecryptedPassword();

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

        String htmlTemplate = "";
        try {
            // Load HTML template
            htmlTemplate = new String(Files.readAllBytes(Paths.get("EmailSenderApp/templates/welcome.html")));
        } catch (Exception e) {
            System.out.println("Failed to read email template: " + e.getMessage());
        }

        Timestamp now = new Timestamp(System.currentTimeMillis());

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

              InsertEmailLog.insertLog(
                1,                               // sender_id_fk
                "Kanana",                      // sender_name_fk
                recipient,                                  // receiver_email
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


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
