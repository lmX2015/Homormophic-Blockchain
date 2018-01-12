package tests;

import security.*;

public class testsBlock {
	public static void main(String[] args) {
		testTransaction();
	}
	public static void testTransaction() {
		Transaction t = new Transaction("lm;b;10;1;sig;1;1");
		System.out.println(t);
	}
}
