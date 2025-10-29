package EmailSenderApp.src;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    public static Connection getConnection() {
        String url = "jdbc:postgresql://localhost:5432/emails_db";
        String user = "kanana";

        String password = DecryptPassword.getDecryptedPassword("encryptedDBPassword");

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
}
