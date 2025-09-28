package com.example.auth.service;

import com.example.auth.exception.EncryptionException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
public class EncryptionService {
    @Value("${app.encryptionSecret}")
    private String secretKey;
    private Cipher cipher;
    private SecretKey signInKey;

    @PostConstruct
    public void init() throws Exception {
        byte[] decodedKey = Base64.getDecoder().decode(secretKey);
        if (decodedKey.length != 16 && decodedKey.length != 24 && decodedKey.length != 32) {
            throw new IllegalArgumentException("Invalid AES key length: must be 16, 24, or 32 bytes.");
        }
        signInKey = new SecretKeySpec(decodedKey, "AES");
        cipher = Cipher.getInstance("AES");
    }

    public String encrypt(String plainText)  {
        try {
            byte[] plainTextByte = plainText.getBytes();
            cipher.init(Cipher.ENCRYPT_MODE, signInKey);
            byte[] encryptedByte = cipher.doFinal(plainTextByte);
            Base64.Encoder encoder = Base64.getEncoder();
            return encoder.encodeToString(encryptedByte);
        } catch (Exception e) {
            throw new EncryptionException("Text cannot be encrypted");
        }
    }

    public String decrypt(String encryptedText) {
        try {
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] encryptedTextByte = decoder.decode(encryptedText);
            cipher.init(Cipher.DECRYPT_MODE, signInKey);
            byte[] decryptedByte = cipher.doFinal(encryptedTextByte);
            return new String(decryptedByte);
        } catch (Exception e) {
            throw new EncryptionException("Text cannot be decrypted");
        }
    }
}
