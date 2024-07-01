package com.example.money_manager.utils;

import android.util.Base64;
import android.util.Log;

import java.security.KeyStore;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class AESUtil {
    private static final String AES_MODE = "AES/ECB/PKCS7Padding";
    private static final String CHARSET = "UTF-8";
    private static final String ANDROID_KEYSTORE = "AndroidKeyStoreMoneyManager";
    private static final String KEY_ALIAS = "MyKeyAlias";

    // Generate AES key
    public static SecretKey generateKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256); // AES key size 256 bits
        return keyGenerator.generateKey();
    }
    public static SecretKey getSecretKey() {
        try {
            KeyStore keyStore = KeyStore.getInstance(ANDROID_KEYSTORE);
            keyStore.load(null);
            return (SecretKey) keyStore.getKey(KEY_ALIAS, null);
        } catch (Exception e) {
            Log.e("KeyStoreManager", "Error getting secret key: " + e.getMessage());
            return null;
        }
    }
    // Encrypt data using AES
    public static String encrypt(String input, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_MODE);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(input.getBytes(CHARSET));
        return Base64.encodeToString(encryptedBytes, Base64.DEFAULT);
    }

    // Decrypt data using AES
    public static String decrypt(String encryptedInput, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_MODE);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] encryptedBytes = Base64.decode(encryptedInput, Base64.DEFAULT);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes, CHARSET);
    }
}
