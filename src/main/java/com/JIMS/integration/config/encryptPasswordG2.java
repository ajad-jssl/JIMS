package com.JIMS.integration.config;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
public class encryptPasswordG2 {
public static String encryptPassword(String plainText) throws Exception {

    byte[] keyBytes = new byte[]{
            1,2,3,4,5,6,7,8,9,10,11,12,
            13,14,15,16,17,18,19,20,21,22,23,24
    };

    byte[] ivBytes = new byte[]{
            65,110,68,26,69,(byte)178,(byte)200,(byte)219
    };

    SecretKeySpec key = new SecretKeySpec(keyBytes, "DESede");
    IvParameterSpec iv = new IvParameterSpec(ivBytes);

    Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, key, iv);

    byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

    return Base64.getEncoder().encodeToString(encrypted);
}

}