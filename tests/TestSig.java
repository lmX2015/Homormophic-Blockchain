package tests;
import security.Ecdsa;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

public class TestSig {
	public static void test1() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException, InvalidKeyException, SignatureException, UnsupportedEncodingException{
		Ecdsa.init();
		KeyPair keys = Ecdsa.GenerateKeys();
		String message = "hello";
		byte[] sig = Ecdsa.sign(message, keys);
		System.out.println(Ecdsa.verify(message, keys.getPublic(), sig));
	}
	public static void test2() throws InvalidKeyException, SignatureException, UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException, InvalidKeySpecException {
		Ecdsa.init();
		KeyPair keys = Ecdsa.GenerateKeys();
		String message = "hello";
		byte[] sig = Ecdsa.sign(message, keys);
		
		String[] keysS = Ecdsa.ExportKeyPair(keys);
		keys = Ecdsa.LoadKeyPair(keysS[1],keysS[0] );
		System.out.println(Ecdsa.verify(message, keys.getPublic(), sig));

		
	}
	public static void main(String[] args) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException, SignatureException, UnsupportedEncodingException, InvalidKeySpecException {
		test1();
		test2();
	}
}
