package com.vidasoft.magman.security;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import jakarta.enterprise.context.ApplicationScoped;
import java.security.Key;
import java.util.Random;

@ApplicationScoped
public class PasswordService {

    private static final String ENCRYPTION_KEY = "$oME$anD0mKey!@#";
    private static final int SALT_LENGTH = 8;

    public String encryptPassword(String password, String salt) {
        var saltedPassword = password + salt;

        Key aesKey = new SecretKeySpec(ENCRYPTION_KEY.getBytes(), "AES");
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            byte[] encrypted = cipher.doFinal(saltedPassword.getBytes());
            return new String(encrypted);
        } catch (Exception e) {
            return password;
        }
    }

    public String generateSalt() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < SALT_LENGTH; i++) {
            sb.append((char) random.nextInt());
        }

        return sb.toString();
    }

}
