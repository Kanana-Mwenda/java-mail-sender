package EmailSenderApp.src;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;   

public class InsertEmailSender {
    public static void main(String[] args) {
        try(Connection conn = DBConnection.getConnection()) {
        
            String sql = "INSERT INTO email_senders (sender_name, sender_username, sender_password, date_created, date_modified) VALUES (?, ?, ?, ?, ?)";
            
            PreparedStatement stmt = conn.prepareStatement(sql);

            String senderName = "Kanana";
            String senderUsername = "kananamwenda20@gmail.com";
            String senderPassword = "TGEz827ilG7jLyUTCH113PWxChyU0HKeD/gs5tvCRfk=";
            
            Timestamp now = new Timestamp(System.currentTimeMillis());

            stmt.setString(1, senderName);
            stmt.setString(2, senderUsername);
            stmt.setString(3, senderPassword);
            stmt.setTimestamp(4, now);
            stmt.setTimestamp(5, now);

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
}
