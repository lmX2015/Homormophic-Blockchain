package tests;

import security.*;

public class testsBlock {
	public static void main(String[] args) {
		//testTransaction();
		test1();
	}
	public static void testTransaction() {
		Transaction t = new Transaction("lm;b;10;1;sig;1;1");
		System.out.println(t);
	}
	public static void test1() {
		Block b = new Block(2);
		b.load();
		b.setHashPrec("test0");
		b.setSigMineur("hello");
		b.setIdMiner("rick");
		b.setPow("power");
		b.save();
		System.out.println(b);
	}
}
