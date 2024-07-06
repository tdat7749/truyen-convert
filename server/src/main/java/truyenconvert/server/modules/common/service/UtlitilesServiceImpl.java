package truyenconvert.server.modules.common.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;

@Service
public class UtlitilesServiceImpl implements UtilitiesService{

    @Value("${truyencv.salt}")
    private String salt;

    @Value("${truyencv.aes}")
    private String AES;

    @Value("${truyencv.aes-cipher-algorithm}")
    private String AES_CIPHER_ALGORITHM;

    @Override
    public String randomKeyNumber() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for(int i = 1;i<=9;i++){
            int digit = random.nextInt(10);
            sb.append(digit);
        }
        return sb.toString();
    }

    @Override
    public String AesEncrypt(String text,String hashCode) throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM);

        String key = Base64.getEncoder().encodeToString((salt + hashCode).getBytes()).substring(0,16);

        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), AES);
        IvParameterSpec ivSpec = new IvParameterSpec(key.getBytes());

        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        byte[] encryptedBytes = cipher.doFinal(text.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    @Override
    public String AesDecrypt(String encryptedText, String hashCode) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM);

        String key = Base64.getEncoder().encodeToString((salt + hashCode).getBytes()).substring(0,16);

        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), AES);
        IvParameterSpec ivSpec = new IvParameterSpec(key.getBytes());

        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

        byte[] decodedBytes = Base64.getDecoder().decode(encryptedText);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes);
    }
}
