package memory;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import security.Ecdsa;


public class User {
	private String pKey;
	private long nonce;
	private long balance;

	public synchronized String getpKey() {
		return pKey;
	}
	public synchronized void setpKey(String pKey) {
		this.pKey = pKey;
	}
	public synchronized long getNonce() {
		return nonce;
	}
	public synchronized void setNonce(long l) {
		this.nonce = l;
	}
	public synchronized long getBalance() {
		return balance;
	}
	public synchronized void setBalance(long balance) {
		this.balance = balance;
	}

	public User(String pKey) {
		super();
		this.pKey = pKey;
		this.balance=0;
		this.nonce = 0;
	}
	public synchronized void load() {
		if (this.pKey==null) return;
		try (Connection conn = DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/blockchain?useSSL=false", "root", "");
				Statement stmt = conn.createStatement();
				)
		{
			String request = "select * from accounts where pubkey =\'"+this.pKey+"\'";
			ResultSet rset = stmt.executeQuery(request);
			while(rset.next()) {
				
				this.setNonce(rset.getLong("nonce"));
				this.setBalance(rset.getLong("amount"));
			}
			conn.close();
			
		}
		catch(SQLException ex) {
			ex.printStackTrace();
		}  


	}
	
	public synchronized void save() {
		try {
			Connection conn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/blockchain?useSSL=false", "root", "");
			PreparedStatement ps = conn.prepareStatement(
					"UPDATE accounts SET amount = ?, nonce =? WHERE pubkey = ?");

			// set the preparedstatement parameters
			ps.setString(3,pKey);
			ps.setLong(1,balance);
			ps.setLong(2, nonce);

			// call executeUpdate to execute our sql update statement
			if (ps.executeUpdate()==1){
				ps.close();

			}
			else {
				ps.close();
				String query=" insert into accounts (pubkey, amount, nonce)"
				        + " values (?, ?, ?)";

				ps = conn.prepareStatement(query);						
				ps.setString(1,pKey);
				ps.setLong(2,balance);
				ps.setLong(3, nonce);
				ps.execute();
				ps.close();

			}
			conn.close();
			


		}
		catch(SQLException ex) {
			ex.printStackTrace();

		}  
	}

	public String toString() {
		return this.pKey+";"+this.nonce+";"+this.balance+";;";
	}
	public boolean checkSig(String message,String sig) {
		try {
			Ecdsa.init();
			PublicKey key = Ecdsa.loadPublic(pKey);
			return Ecdsa.verify(message, key, Ecdsa.hexStringToByteArray(sig));
		} catch (InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchProviderException | InvalidKeySpecException | InvalidKeyException | SignatureException | UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		}
	}




}
