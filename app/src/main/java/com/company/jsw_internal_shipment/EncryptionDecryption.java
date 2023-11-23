package com.company.jsw_internal_shipment;


import android.util.Base64;

import androidx.annotation.Nullable;

import java.nio.ByteBuffer;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
public class EncryptionDecryption {
    public static final int INIT_VECTOR_LENGTH = 16;
    /**
     * @see <a href="https://stackoverflow.com/questions/9655181/how-to-convert-a-byte-array-to-a-hex-string-in-java">how-to-convert-a-byte-array-to-a-hex-string</a>
     */
    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();


    protected String data;

    protected String initVector;

    protected String errorMessage;

    private EncryptionDecryption() {
        super();
    }


    private EncryptionDecryption(@Nullable String initVector, @Nullable String data, @Nullable String errorMessage) {
        super();

        this.initVector = initVector;
        this.data = data;
        this.errorMessage = errorMessage;
    }


    public static EncryptionDecryption encrypt(String secretKey, String plainText) {
        String initVector = null;
        try {
            // Check secret length
            if (!isKeyLengthValid(secretKey)) {
                throw new Exception("Secret key's length must be 128, 192 or 256 bits");
            }

            // Get random initialization vector
            SecureRandom secureRandom = new SecureRandom();
            byte[] initVectorBytes = new byte[INIT_VECTOR_LENGTH / 2];
            secureRandom.nextBytes(initVectorBytes);
            initVector = bytesToHex(initVectorBytes);
            initVectorBytes = initVector.getBytes("UTF-8");

            IvParameterSpec ivParameterSpec = new IvParameterSpec(initVectorBytes);
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

            // Encrypt input text
            byte[] encrypted = cipher.doFinal(plainText.getBytes("UTF-8"));

            ByteBuffer byteBuffer = ByteBuffer.allocate(initVectorBytes.length + encrypted.length);
            byteBuffer.put(initVectorBytes);
            byteBuffer.put(encrypted);

            // Result is base64-encoded string: initVector + encrypted result
            String result = Base64.encodeToString(byteBuffer.array(), Base64.DEFAULT);

            // Return successful encoded object
            return new EncryptionDecryption(initVector, result, null);
        } catch (Throwable t) {
            t.printStackTrace();
            // Operation failed
            return new EncryptionDecryption(initVector, null, t.getMessage());
        }
    }


    public static EncryptionDecryption decrypt(String secretKey, String cipherText) {
        try {
            // Check secret length
            if (!isKeyLengthValid(secretKey)) {
                throw new Exception("Secret key's length must be 128, 192 or 256 bits");
            }

            // Get raw encoded data
            byte[] encrypted = Base64.decode(cipherText, Base64.DEFAULT);

            // Slice initialization vector
            IvParameterSpec ivParameterSpec = new IvParameterSpec(encrypted, 0, INIT_VECTOR_LENGTH);
            // Set secret password
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

            // Trying to get decrypted text
            String result = new String(cipher.doFinal(encrypted, INIT_VECTOR_LENGTH, encrypted.length - INIT_VECTOR_LENGTH));

            // Return successful decoded object
            return new EncryptionDecryption(bytesToHex(ivParameterSpec.getIV()), result, null);
        } catch (Throwable t) {
            t.printStackTrace();
            // Operation failed
            return new EncryptionDecryption(null, null, t.getMessage());
        }
    }


    public static boolean isKeyLengthValid(String key) {
        return key.length() == 16 || key.length() == 24 || key.length() == 32;
    }


    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }


    public String getData() {
        return data;
    }


    public String getInitVector() {
        return initVector;
    }


    public String getErrorMessage() {
        return errorMessage;
    }


    public boolean hasError() {
        return this.errorMessage != null;
    }


    public String toString() {
        return getData();
    }
}
