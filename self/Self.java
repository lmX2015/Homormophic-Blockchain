package self;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import memory.Db;
import security.*;
public class Self {
	
	private  static String id;
	private static KeyPair keys;
	private static Block currentBlock;
	
	
	public static void init() {
		try {
			Ecdsa.init();
			keys=Ecdsa.GenerateKeys();
			id = Ecdsa.ExportPublic(keys.getPublic());
		} catch (InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchProviderException e) {
			e.printStackTrace();
		}
		currentBlock=Db.getAll()[0];
		//currentBlock.selfForge();
	}
	public static synchronized Block getCurrentBlock() {
		return currentBlock;
	}
	public static synchronized void setCurrentBlock(Block b) {
		currentBlock=b;
	}
	

	public synchronized static String getId() {
		return id;
	}

	public synchronized static void setId(String id2) {
		id = id2;
	}

	public synchronized static KeyPair getKeys() {
		return keys;
	}

	public static synchronized void setKeys(KeyPair keys2) {
		keys = keys2;
	}
	
	public static String selfSign(String message) {
		try {
			return Ecdsa.bytesToHex(Ecdsa.sign(message, keys));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
