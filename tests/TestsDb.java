package tests;
import memory.*;
import security.*;
public class TestsDb {
	public static void testBlocks() {
		Block[] blocks= Db.getAll();
		for (Block b: blocks) {
			System.out.println(b);
		}
	}
	public static void test2() {
		User utest = new User("test");
		utest.load();
		System.out.println(utest);
	}
	public static void test3() {
		User utest = new User("test");		
		utest.load();
		utest.setBalance(10);
		utest.save();
		utest.load();
		System.out.println(utest);
	}
	public static void test4() {
		User utest = new User("not_an_user");
		utest.setBalance(0);
		utest.setNonce(0);
		utest.save();
	}

	public static void main(String[] args) {
		//testBlocks();
		test2();
		test3();
		test4();
	}


}
