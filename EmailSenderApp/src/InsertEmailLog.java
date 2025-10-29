package EmailSenderApp.src;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
public class InsertEmailLog {
    public static void insertLog(
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

                String sql = "INSERT INTO email_logs "
                        + "(sender_id_fk, sender_name_fk, receiver_email, email_content_type, email_message_body, "
                        + "email_subject, status, status_date, status_description, email_attachment, date_created, date_modified) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?::email_status, ?, ?, ?, ?,?)";

            Timestamp now = new Timestamp(System.currentTimeMillis());

            try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            
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
            }}

