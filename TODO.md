- [x] Modify insertEmailSender method to accept Connection conn parameter
- [x] Modify insertEmailLog method to accept Connection conn parameter
- [x] Modify sendEmail method to accept Connection conn and String htmlTemplate, remove insertEmailLog call and htmlTemplate loading
- [x] Update main method to call methods in sequence: initializeApp(), getConnection(), insertEmailSender(conn), load htmlTemplate, sendEmail(conn, htmlTemplate), insertEmailLog(conn, ...)

