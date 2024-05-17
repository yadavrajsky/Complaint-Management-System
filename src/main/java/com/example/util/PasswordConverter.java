package com.example.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class PasswordConverter implements AttributeConverter<String, String> {

    @Override
    public String convertToDatabaseColumn(String attribute) {
        return hashPassword(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        // No need to convert back to plain text
        return dbData;
    }

    public static String hashPassword(String password) {
        try {
            // Create MessageDigest instance for SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            // Add password bytes to digest
            byte[] bytes = md.digest(password.getBytes());
            // Convert byte array to hexadecimal string
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            // Return hashed password
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // e.printStackTrace();
            return null;
        }
    }
}
