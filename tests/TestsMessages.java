package tests;
import messages.*;
public class TestsMessages {

	public static void main(String[] args) {
		testHello();
		testListRes();
			}
	public static void testHello() {
		HelloMessage h =new HelloMessage("HELLO;A;B;10;hashtest");
		System.out.println(h);
		assert((h.toString()).equals("HELLO;A;B;10;hashtest"));

	}
	public static void testListRes() {
		ListResMessage m = new ListResMessage("LISTRES;a;b;3;epc;epo;epx;");
		System.out.println(m);
	}

}
