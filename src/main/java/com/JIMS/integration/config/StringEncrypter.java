package com.JIMS.integration.config;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.springframework.stereotype.Component;

@Component
public class StringEncrypter {
	private final Cipher ecipher;
	private final Cipher dcipher;

	public StringEncrypter(SecretKey key, String algorthim) {

		try {
			ecipher = Cipher.getInstance(algorthim);
			dcipher = Cipher.getInstance(algorthim);
			ecipher.init(Cipher.ENCRYPT_MODE, key);
			dcipher.init(Cipher.ENCRYPT_MODE, key);
		} catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException e) {
			throw new RuntimeException("Error Initializing cipher", e);
		}
	}

	public StringEncrypter(String passPhrase) {
		byte[] salt = { (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32, (byte) 0x56, (byte) 0x34, (byte) 0xE3,
				(byte) 0x03 };
		int iterationCount = 19;

		try {
			KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount);
			SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);

			ecipher = Cipher.getInstance("PBEWithMD5AndDES");
			dcipher = Cipher.getInstance("PBEWithMD5AndDES");

			AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);
			ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
			dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
		} catch (InvalidAlgorithmParameterException | InvalidKeySpecException | NoSuchPaddingException
				| NoSuchAlgorithmException | InvalidKeyException e) {
			throw new RuntimeException("Error initializing ciphers", e);
		}
	}

	public String encrypt(String str) {
		try {
			byte[] uft8 = str.getBytes("UTF-8");
			byte[] enc = ecipher.doFinal(uft8);
			return Base64.getEncoder().encodeToString(enc);

		} catch (BadPaddingException | IllegalBlockSizeException | UnsupportedEncodingException e) {
			throw new RuntimeException("Encryption error", e);
		}
	}

	public String decrypt(String str) {
		try {
			byte[] dec = Base64.getDecoder().decode(str);
			byte[] utf8 = dcipher.doFinal(dec);
			return new String(utf8, "UTF-8");
		} catch (BadPaddingException | IllegalBlockSizeException | UnsupportedEncodingException e) {
			throw new RuntimeException("Decryption error", e);
		}
	}

	public static String testEncrypt(String encryptValue) {
		String passPharse = "akd89343My Pass Phrase";
		StringEncrypter encrypter = new StringEncrypter(passPharse);
		return encrypter.encrypt(encryptValue);
	}

	public static String testEncryptJnlp(String key, String pass) {
		StringEncrypter encrypter = new StringEncrypter(key);
		return encrypter.encrypt(pass);
	}

	public static String testDecryptJnlp(String key, String pass) {
		StringEncrypter encrypter = new StringEncrypter(key);
		return encrypter.decrypt(pass);
	}

	public static String testDecrypt(String pEncrypted) {
		String passPhrase = "akd89343My Pass Phrase";
		StringEncrypter encrypter = new StringEncrypter(passPhrase);
		return encrypter.decrypt(pEncrypted);
	}

	public static void main(String[] args) {
		String encrypted = testEncrypt("maftab");
		System.out.println("Encrypted: " + encrypted);
		String decrypted = testDecrypt("1G3sV8YsH3QsDrXx7fpTYw==");
		System.out.println("Decrypted: " + decrypted);
		//String val = testEncryptJnlp("arun", "Rama");
		//System.out.println("val :" + val);
// val1 = testDecryptJnlp("arun", val);
		//System.out.println("val :" + val1);
	}
}
