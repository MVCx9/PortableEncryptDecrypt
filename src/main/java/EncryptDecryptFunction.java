import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;

public class EncryptDecryptFunction {

	public static final String ENCRYPTION_ALGORITHM = "RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING";

	EncryptDecryptFunction(){

	}

	public static byte[] encrypt(String message, PublicKey publicKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
	    return cipher.doFinal(message.getBytes());
	}
	
	public static String decrypt(String encryptedMessage, PrivateKey privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
	    Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
	    cipher.init(Cipher.DECRYPT_MODE, privateKey);
	    byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedMessage));
	    return new String(decryptedBytes);
	}
	
	public static PublicKey getPublicKey(String filename) throws IOException, CertificateException {
        try (FileInputStream fis = new FileInputStream(filename)) {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) cf.generateCertificate(fis);
            return cert.getPublicKey();
        }
	}
	
	public static PrivateKey getPrivateKey(String filename, String password) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException {
	    KeyStore ks = KeyStore.getInstance("PKCS12");
	    FileInputStream fis = new FileInputStream(filename);
	    ks.load(fis, password.toCharArray());
	    String alias = ks.aliases().nextElement();
	    return (PrivateKey) ks.getKey(alias, password.toCharArray());
	}

}
