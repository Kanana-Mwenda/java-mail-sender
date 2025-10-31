# JavaMailSender Project

This project is a Java-based email sender application that securely handles email credentials using AES encryption, sends emails via Gmail SMTP, and logs email activities to a PostgreSQL database.

## Features

The application consists of two main classes:

- **EmailClient**: Handles application initialization, database connections, email sending, sender data insertion, and email logging.
- **CryptoUtils**: Manages AES key generation, password encryption, and decryption.

Key functionalities include:
- Generate AES encryption keys
- Encrypt and decrypt email passwords
- Send HTML emails using Gmail SMTP
- Insert and manage email sender data in the database
- Log email sending activities

## Prerequisites

- Java 8 or higher
- PostgreSQL database
- Git (for cloning the repository)
- Gmail account with App Password (for SMTP authentication)

## Installation

1. **Fork the Repository**:
   - Go to the GitHub repository page.
   - Click the "Fork" button in the top-right corner to create your own copy of the repository.

2. **Clone the Repository**:
   ```bash
   git clone https://github.com/your-username/java-mail-sender.git
   cd java-mail-sender
   ```

3. **Set Up PostgreSQL Database**:
   - Create a database named `emails_db`.
   - Create a user with your credentials(username and password).
   - Ensure the database has the necessary tables: `email_senders` and `email_logs`. 

## Setup

1. **Compile All Java Files**:
   ```bash
   javac -cp "lib/*" EmailSenderApp/src/*.java
   ```

## Usage

The application consists of two main classes: `EmailClient` and `CryptoUtils`. Run the respective main methods to perform operations.

### Running CryptoUtils

The `CryptoUtils` class handles AES key generation, password encryption, and decryption. Running its main method will prompt for passwords to encrypt and display decrypted passwords.

**Compile**:
```bash
javac -cp "lib/*" EmailSenderApp/src/CryptoUtils.java
```

**Run**:
```bash
java -cp ".:lib/*" EmailSenderApp.src.CryptoUtils
```

This will:
- Encrypt SMTP and DB passwords (prompts for input).
- Decrypt and display the SMTP password.

**Note**: The AES key is hardcoded in `CryptoUtils.java`. Ensure consistency across operations.

### Running EmailClient

The `EmailClient` class handles application initialization, database connections, email sending, sender data insertion, and email logging. Running its main method will execute all these operations in sequence.

**Compile**:
```bash
javac -cp "lib/*" EmailSenderApp/src/EmailClient.java
```

**Run**:
```bash
java -cp ".:lib/*" EmailSenderApp.src.EmailClient
```

This will:
- Initialize the app.
- Connect to the database.
- Insert email sender data.
- Send an email using the HTML template from `EmailSenderApp/templates/welcome.html`.
- Log the email activity.

**Note**: Update hardcoded values (e.g., recipient, sender details) in `EmailClient.java` as needed. Ensure Gmail App Password is used for authentication.

## Configuration

- `config.xml`: Stores the credentails.
- Database connection details are in `DBConnection.java`.
- Email templates are in `EmailSenderApp/templates/`.

## Notes

- Ensure all JAR files in the `lib/` directory are included in the classpath.
- For Gmail SMTP, enable "Less secure app access" or use an App Password.
- Update hardcoded values in the Java files for your specific use case.
- The project assumes a PostgreSQL database is running locally on port 5432.

## Troubleshooting

- If compilation fails, ensure Java is installed and the classpath includes all JARs.
- For database connection issues, verify PostgreSQL is running and credentials are correct.
- Email sending failures may be due to Gmail security settings; use App Passwords.

