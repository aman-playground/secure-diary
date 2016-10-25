package com.ajain.securediary;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class Encrypt {
    private String password;
    private String encryptedPassword;

    Encrypt(String password) {
        this.password = password;
        encryptedPassword = encryptPassword();
    }

    private String encryptPassword() {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(password.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    String getEncryptedPassword() {
        return encryptedPassword;
    }
}
