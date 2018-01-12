
package security;
//import javax.crypto.*;
import org.bouncycastle.jcajce.provider.digest.SHA3;
import org.bouncycastle.util.encoders.Hex;
//import java.security.Security;

public class Hash {
	public static String hash(String Message) {
		SHA3.DigestSHA3 digestSHA3 = new SHA3.Digest512();
		byte[] digest = digestSHA3.digest(Message.getBytes());
		//System.out.println("SHA3-512 = " + Hex.toHexString(digest));
		return Hex.toHexString(digest);
	}
	public static String hashs(String[]input) {
		String buf ="";
		for (String s: input) {
			buf+=s;
		}
		SHA3.DigestSHA3 digestSHA3 = new SHA3.Digest512();
		byte[] digest = digestSHA3.digest(buf.getBytes());
		return Hex.toHexString(digest);
		
	}
	/*public static String hash(String[] Message) {
		if (Message.length==0)return "";
		String res =hash(Message[0]);
		for (int i=1;i<Message.length;++i) {
			res= 
		}
	}*/

}
