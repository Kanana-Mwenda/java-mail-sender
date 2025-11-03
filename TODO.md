# Refactor CryptoUtils.java into Utils.java

## Steps to Complete:
- [x] Create generic helper methods: encrypt(String value), decrypt(String encryptedValue), saveToXml(String tag, String value), getFromXml(String tag)
- [x] Create encryptAndSave(String tag, String prompt) method that prompts for input, encrypts, and saves to XML
- [x] Create specific encrypt methods: encryptPassword(String tag), encryptUsername(String tag), encryptDBname(String tag) that call encryptAndSave with appropriate prompts
- [x] Create getDecrypted(String tag) method that retrieves from XML and decrypts
- [x] Create printDecrypted(String tag) method that calls getDecrypted and prints the result
- [x] Retain getUrlValue(String tag) method as is
- [x] Implement main method with calls to generateAESKey, encrypt methods, and printDecrypted for various tags including getDecryptedUsername
- [x] Create Utils.java file with all refactored code
