package security;
import config.* ;

public class Pow {
	private String miner;
	private String hashBlock;
	private String pow;
	
	public Pow(String miner, String hashBlock, String pow) {
		super();
		this.miner = miner;
		this.hashBlock = hashBlock;
		this.pow = pow;
	}
	public String getMiner() {
		return miner;
	}
	public void setMiner(String miner) {
		this.miner = miner;
	}
	public synchronized String getHashBlock() {
		return hashBlock;
	}
	public synchronized void setHashBlock(String hashBlock) {
		this.hashBlock = hashBlock;
	}
	public synchronized String getPow() {
		return pow;
	}
	public synchronized void setPow(String pow) {
		this.pow = pow;
	}
	// check pow
	public synchronized boolean check() {
		String conc = miner+hashBlock+pow;
		String hash = Hash.hash(conc);
		for (int i=0;i<config.difficulty;++i) {
			if (conc.charAt(i)!='0') {
				return false;
			}
		}
		return true;
	}
	
}
