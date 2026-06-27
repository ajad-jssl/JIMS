package com.JIMS.integration.config;

import org.jasypt.util.text.BasicTextEncryptor;

public class EncryptUtil {
    public static void main(String[] args) {

        BasicTextEncryptor encryptor = new BasicTextEncryptor();
        encryptor.setPassword("0123456789ABCDEF");
        System.out.println("Username: " + encryptor.decrypt("jTkdXXw5CoP7oCBnvDzeTpjbhH5xCOh1OhVYOyURN6JnB7MnHC8kmcwPHQy6lybYHLLbeqPE9Amnx9zRDXAV0APlplozIQyIyoD3Fffj2FAwmcg47YAEDGzvG6G9ozIqrhbMMYAaHU+K5rSAgaY0ousoLZcZQSUp"));
        
    }
}