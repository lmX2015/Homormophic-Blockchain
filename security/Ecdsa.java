package security;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


public class Ecdsa {
	
	private static KeyPairGenerator keyGen;
	
	public static KeyPair GenerateKeys() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException{
		return keyGen.generateKeyPair();
	}
	
	public static void	init() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
		keyGen = KeyPairGenerator.getInstance("DSA"); 
		keyGen.initialize(1024);
	}
	
	private final static char[] hexArray = "0123456789abcdef".toCharArray();
	
	public static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}
	public static byte[] hexStringToByteArray(String s) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}
	
	public static String[] ExportKeyPair(KeyPair keyPair){
		PrivateKey privateKey = keyPair.getPrivate();
		PublicKey publicKey = keyPair.getPublic();
		String[] res = new String[2];
		// Store Public Key.
		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(
				publicKey.getEncoded());
		res[0]=bytesToHex(x509EncodedKeySpec.getEncoded());
		// Store Private Key.
		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(
				privateKey.getEncoded());
		res[1]=bytesToHex(pkcs8EncodedKeySpec.getEncoded());
		return res;
	}
	public static String ExportPublic(PublicKey publicKey){
		// Store Public Key.
		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(
				publicKey.getEncoded());
		return bytesToHex(x509EncodedKeySpec.getEncoded());
	}
	

	public static KeyPair LoadKeyPair(String pvk, String pbk)
			 throws NoSuchAlgorithmException,
			InvalidKeySpecException {
		byte[] encodedPublicKey= hexStringToByteArray(pbk);
		byte[] encodedPrivateKey = hexStringToByteArray(pvk) ;
		KeyFactory keyFactory = KeyFactory.getInstance("DSA");
		X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(
				encodedPublicKey);
		PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
 
		PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(
				encodedPrivateKey);
		PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
 
		return new KeyPair(publicKey, privateKey);
	}
	
	public static PublicKey loadPublic(String pbk) throws NoSuchAlgorithmException, InvalidKeySpecException {
		byte[] encodedPublicKey= hexStringToByteArray(pbk);
		
		KeyFactory keyFactory = KeyFactory.getInstance("DSA");
		X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(
				encodedPublicKey);
		PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
  
		return publicKey;
	
	}
	
 
	public static byte[] sign(String plaintext, KeyPair keys) throws SignatureException, UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException{
	    Signature signature = Signature.getInstance("DSA");
	    signature.initSign(keys.getPrivate(), new SecureRandom());

	    byte[] message = plaintext.getBytes();
	    signature.update(message);

	    byte[] sigBytes = signature.sign();
		return sigBytes;
	}
	
	public static boolean verify(String plaintext, PublicKey key, byte[] sigBytes )throws SignatureException, InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchProviderException{
		Signature signature = Signature.getInstance("DSA");
		    
		signature.initVerify(key);
	    signature.update(plaintext.getBytes());
	    return signature.verify(sigBytes);
	}
	

		
	}