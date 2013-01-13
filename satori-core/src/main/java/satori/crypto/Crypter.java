package satori.crypto;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import satori.config.Configuration;

/**
 * Simple and fast AES cipher utility.
 * 
 */
public class Crypter {

    private static final String DEFAULT_TRANSFORM = "AES/ECB/PKCS5Padding";

    private static final Logger logger = LoggerFactory.getLogger(Crypter.class);

    private final ThreadLocal<Cipher> ciphers;
    private final SecretKey secret;
    private final String transformation;

    private final static Crypter _ = new Crypter();

    public static String decrypt(String string) {

        try {
            Cipher cipher = _.getCipher(Cipher.DECRYPT_MODE);
            return new String(cipher.doFinal(DatatypeConverter.parseBase64Binary(string)), "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static String encrypt(String string) {

        try {
            Cipher cipher = _.getCipher(Cipher.ENCRYPT_MODE);
            byte[] doFinal = cipher.doFinal(string.getBytes("UTF-8"));
            return DatatypeConverter.printBase64Binary(doFinal);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private Crypter() {

        try {
            this.ciphers = new ThreadLocal<Cipher>();
            this.transformation = DEFAULT_TRANSFORM;
            this.secret = new SecretKeySpec(getPassword(), "AES");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private Cipher getCipher(int mode) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {

        if (this.ciphers.get() == null) {
            this.ciphers.set(Cipher.getInstance(transformation));
        }

        Cipher cipher = ciphers.get();
        cipher.init(mode, secret);
        return cipher;

    }

    /**
     * @return 16 bytes password
     */
    private byte[] getPassword() {
        Configuration cfg = Configuration.satoriYaml();
        if (!cfg.has("app.password")) {
            logger.warn("Please, set the key 'app.password: [16 bytes password]' in satori.yml configuration file.");
        }

        return String.format("%-16s", cfg.get("app.password", "0123456789012345")).substring(0, 16).replace(' ', '_')
                .getBytes();
    }

}
