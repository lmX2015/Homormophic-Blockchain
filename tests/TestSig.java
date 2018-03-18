package tests;
import security.Ecdsa;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;

public class TestSig {
	public static void test1() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException, InvalidKeyException, SignatureException, UnsupportedEncodingException{
		KeyPair keys = Ecdsa.GenerateKeys();
		String message = "hello";
		byte[] sig = Ecdsa.GenerateSignature(message, keys);
		System.out.println(Ecdsa.ValidateSignature(message, keys.getPublic(), sig));
	}
	public static void main(String[] args) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException, SignatureException, UnsupportedEncodingException {
		test1();
	}
}
