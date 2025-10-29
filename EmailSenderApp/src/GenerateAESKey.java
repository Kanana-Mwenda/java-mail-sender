package EmailSenderApp.src;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;
import java.io.FileWriter;

public class GenerateAESKey {
    public static void main(String[] args) throws Exception {
        // Generate AES key
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);
        SecretKey secretKey = keyGen.generateKey();

        //convert to base64
        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());

        try(FileWriter writer = new FileWriter("config.xml")) {
            writer.write(encodedKey);
        }
        System.out.println("AES key generated and saved to aes.key");
        }
    }

