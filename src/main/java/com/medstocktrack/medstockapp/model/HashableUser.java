package com.medstocktrack.medstockapp.model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public interface HashableUser {
    int isExisting(String username, String password);

    static String hash(String string) {
        byte[] bytes = string.getBytes();
        StringBuilder sb = new StringBuilder();
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(bytes);
            for (byte b : hash) {
                sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return sb.toString();
    }
}
