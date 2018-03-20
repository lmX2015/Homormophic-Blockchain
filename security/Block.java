package security;

import memory.Db;
import memory.User;
import config.config;

import self.Self;

public class Block {
	
	private int id;
	private String hash;
	private Payload payload;
	private String idMiner;
	private String hashPrec;
	private String sigMineur;
	private String pow;
	
	
	public Block() {}
	
	public Block(int id){
		this.id=id;
		
	}
	
	
	public synchronized String getIdMiner() {
		return idMiner;
	}

	public synchronized void setIdMiner(String idMiner) {
		this.idMiner = idMiner;
	}

	public synchronized String getHashPrec() {
		return hashPrec;
	}

	public synchronized void setHashPrec(String hashPrec) {
		this.hashPrec = hashPrec;
	}

	public synchronized String getSigMineur() {
		return sigMineur;
	}

	public synchronized void setSigMineur(String sigMineur) {
		this.sigMineur = sigMineur;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	public Payload getPayload() {
		return payload;
	}
	public void setPayload(Payload payload) {
		this.payload = payload;
	}
	
	
	public synchronized String getPow() {
		return pow;
	}

	public synchronized void setPow(String pow) {
		this.pow = pow;
	}

	public synchronized  String toString() {
		return "id:"+id+"\n"+"hash:"+hash+"\n"+hashPrec+"\n"+"miner: "+idMiner+"\n"+"sig :"+sigMineur+"\n"+"pow :"+pow+"\n"+"data:"+payload;
	}
	public synchronized void load() {
		Block temp = Db.getFromId(this.id);
		this.id=temp.id;
		this.payload = temp.payload;
		this.hash =temp.hash;
		this.pow =temp.pow;
	}
	public synchronized void save() {
		Db.exportBlock(this);
	}
	private String calculateHash() {
		String[] ToHash = {hashPrec,idMiner,payload.toString(),pow};
		String hashTheorical = Hash.hashs(ToHash);
		return hashTheorical;
	}
	
	public synchronized boolean isValid() {
		// TODO add genesis check if id=0;
		if (id>0) {
			Block pred = new Block(id-1);
			pred.load();
			if (!pred.getHash().equals(hashPrec)) return false;
		} 
		if (!payload.isValid())return false;
		User miner = new User(idMiner);
		if(!miner.checkSig(hash, this.sigMineur)) return false;
		String hashTheorical = calculateHash();
		if (!hash.equals(hashTheorical)) return false;
		Pow p = new Pow(idMiner,hashPrec,pow);
		if (!p.check()) return false;		
		return true;
	}
	public synchronized boolean isValidCache() {
		if (!payload.isValidCache())return false;
		User miner = new User(idMiner);
		if(!miner.checkSig(hash, this.sigMineur)) return false;
		String hashTheorical = calculateHash();
		if (!hash.equals(hashTheorical)) return false;
		Pow p = new Pow(idMiner,hashPrec,pow);
		if (!p.check()) return false;		
		return true;
	}
	
	public synchronized void updateHash() {
		hash =calculateHash();
	}
	public synchronized void AddTransaction(Transaction tx) {
		payload.addTransaction(tx);
		hash =calculateHash();
	} 
	
	public synchronized void make() {
		for (Transaction tx: payload.getTransactions()) {
			tx.make();
		}
		User miner = new User (idMiner);
		miner.load();
		miner.setBalance(miner.getBalance()+config.reward);
		miner.save();
	}
	public synchronized void selfForge() {
		Pow p = new Pow(Self.getId(),hashPrec);
		this.pow= p.mine();
		this.idMiner=Self.getId();
		hash=calculateHash();
		this.sigMineur=Self.selfSign(hash);
		this.make();
		this.save();
		Self.setCurrentBlock(new Block(this.id+1));
		
		
	}

	

}
