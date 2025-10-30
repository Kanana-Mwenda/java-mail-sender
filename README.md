# JavaMailSender Project

This project is a Java-based email sender application that securely handles email credentials using AES encryption, sends emails via Gmail SMTP, and logs email activities to a PostgreSQL database.

## Features

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

### 1. Initialize App

The `InitializeApp` class checks if `config.xml` exists and verifies whether the credentials (SMTP and database passwords) are stored as cleartext or encrypted. If a password is in cleartext, it will automatically encrypt it and update `config.xml`.

**Compile**
```bash
javac -cp "lib/*" EmailSenderApp/src/InitializeApp.java
```

**Run**:
```bash
java -cp ".:lib/*" EmailSenderApp.src.InitializeApp
```

### 2. Generate AES Key

Run the `GenerateAESKey` class to generate a new AES key.All password encryption and decryption will use this key

**Note**: Ensure the `EncryptPassword` and `DecryptPassword` classes have the same hardcoded AES key to maintain consistency.

**Compile**:
```bash
javac -cp "lib/*" EmailSenderApp/src/GenerateAESKey.java
```

**Run**:
```bash
java -cp ".:lib/*" EmailSenderApp.src.GenerateAESKey
```

This will create/update `config.xml` with a new AES key.

### 3. Encrypt Password

Run the `EncryptPassword` class to encrypt a password using the generated AES key and update `config.xml`.

**Compile**
```bash
javac -cp "lib/*" EmailSenderApp/src/EncryptPassword.java
```

**Run**:
```bash
java -cp ".:lib/*" EmailSenderApp.src.EncryptPassword
```

This will encrypt the password and add the encrypted value to `config.xml`.

### 4. Decrypt Password

Run the `DecryptPassword` class to decrypt and display the password from `config.xml`.

**Compile** 
```bash
javac -cp "lib/*" EmailSenderApp/src/DecryptPassword.java
```

**Run**:
```bash
java -cp ".:lib/*" EmailSenderApp.src.DecryptPassword
```

This will print the decrypted password to the console.

### 5. Insert Email Sender Data

Run the `InsertEmailSender` class to insert sender information into the database.

**Note**: The sender details are hardcoded in the code. Update as needed.

**Compile** :
```bash
javac -cp "lib/*" EmailSenderApp/src/InsertEmailSender.java
```

**Run**:
```bash
java -cp ".:lib/*" EmailSenderApp.src.InsertEmailSender
```

This will insert a new sender record into the `email_senders` table.

### 6. Send Email and Insert Log

Run the `SendEmail` class to send an email using the decrypted password and automatically log the activity.

**Note**: Update the recipient, sender and other details in the code as needed. Ensure Gmail App Password is used for authentication.

**Compile** 
```bash
javac -cp "lib/*" EmailSenderApp/src/SendEmail.java
```

**Run**:
```bash
java -cp ".:lib/*" EmailSenderApp.src.SendEmail
```

This will send the email using the HTML template from `EmailSenderApp/templates/welcome.html` and insert a log entry into the `email_logs` table.

## Configuration

- `config.xml`: Stores the AES key and encrypted password.
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

