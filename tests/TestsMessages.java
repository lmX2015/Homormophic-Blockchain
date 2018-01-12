package tests;
import network.HelloMessage;
public class TestsMessages {

	public static void main(String[] args) {
		HelloMessage h =new HelloMessage("HELLO;robin;10;azerty;8;caerbannog;dexter;bengallou;DEXTERMOTAFUCKA;JBiot93;SPAAAAAM2;PickleRick;hell");
		System.out.println(h);
		assert((h.toString()).equals("HELLO;robin;10;azerty;8;caerbannog;dexter;bengallou;DEXTERMOTAFUCKA;JBiot93;SPAAAAAM2;PickleRick;hell"));
	}

}
