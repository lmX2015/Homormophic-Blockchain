package tests;
import memory.Db;
import security.*;
public class TestsDb {

	public static void main(String[] args) {
		Block[] blocks= Db.getAll();
		for (Block b: blocks) {
			System.out.println(b);
		}
	}

}
