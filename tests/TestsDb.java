package tests;
import memory.Db;
import security.*;
public class TestsDb {
	public static void testBlocks() {
		Block[] blocks= Db.getAll();
		for (Block b: blocks) {
			System.out.println(b);
		}
	}
	public static void testAccount() {
		System.out.println(Db.getAmountUser("test"));
	}
	public static void main(String[] args) {
		testBlocks();
		testAccount();
	}


}
