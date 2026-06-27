package com.JIMS.integration.config;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

public class AesEncryptionUtil {
	  // Must be 16+ characters. Store this in application.properties in real apps
    private static final String SECRET_KEY = "MySuperSecretKey123";
    private static final String SALT = "MySalt123";
 
    // =================== ENCRYPT ===================
    public static String encrypt(String plainText) {
        try {
            byte[] iv = new byte[16];
            new SecureRandom().nextBytes(iv);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
 
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(SECRET_KEY.toCharArray(), SALT.getBytes(), 65536, 256);
            SecretKey secret = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
 
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secret, ivSpec);
 
            byte[] encrypted = cipher.doFinal(plainText.getBytes("UTF-8"));
 
            // Combine IV + Encrypted text
            byte[] encryptedWithIv = new byte[iv.length + encrypted.length];
            System.arraycopy(iv, 0, encryptedWithIv, 0, iv.length);
            System.arraycopy(encrypted, 0, encryptedWithIv, iv.length, encrypted.length);
 
            return Base64.getEncoder().encodeToString(encryptedWithIv);
 
        } catch (Exception e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }
 
    // =================== DECRYPT ===================
    public static String decrypt(String encryptedText) {
        try {
            byte[] decoded = Base64.getDecoder().decode(encryptedText);
 
            byte[] iv = new byte[16];
            byte[] encrypted = new byte[decoded.length - 16];
 
            System.arraycopy(decoded, 0, iv, 0, 16);
            System.arraycopy(decoded, 16, encrypted, 0, encrypted.length);
 
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
 
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(SECRET_KEY.toCharArray(), SALT.getBytes(), 65536, 256);
            SecretKey secret = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
 
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secret, ivSpec);
 
            byte[] decrypted = cipher.doFinal(encrypted);
            return new String(decrypted, "UTF-8");
 
        } catch (Exception e) {
            throw new RuntimeException("Decryption failed", e);
        }
    }
}

