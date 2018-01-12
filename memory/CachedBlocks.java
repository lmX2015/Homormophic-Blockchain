package memory;
import java.util.HashMap;

import security.*;
public class CachedBlocks {
	private int blockCapacity=100;
	private Block[]blocks;
	private HashMap<Integer,Integer> cache;
	public CachedBlocks() {
		cache = new HashMap<Integer,Integer>();
		blocks = new Block[blockCapacity];
	}
	public synchronized Block getBlock(int number) {
		Integer index = cache.get(number);
		if (index==null) return null;
		return blocks[index];
	}
}
