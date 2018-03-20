package memory;
import java.util.ArrayList;
import java.util.HashMap;
import config.config;
import security.*;
public class CacheBlockchain {
	private ArrayList<Block> b;
	private HashMap<String,Integer> balances;
	private HashMap<String,Integer> nonces;
	public CacheBlockchain() {
		b = new ArrayList<Block>();
		balances = new HashMap<String,Integer> ();
		nonces = new HashMap<String,Integer>() ;
	}
	public synchronized Block getBlock(int number) {
		if (number>=b.size())return null;
		return b.get(number);
	}
	public synchronized String lastHash() {
		if (b.size()<1 )return null;
		return b.get(b.size()-1).getHash();
	}
	private boolean regularTx(Transaction t) {
		if(balances.get(t.getSenderId())==null)balances.put(t.getSenderId(), 0);
		if(nonces.get(t.getSenderId())==null)nonces.put(t.getSenderId(), 0);
		if(balances.get(t.getReceiverId())==null)balances.put(t.getReceiverId(), 0);
		if(nonces.get(t.getReceiverId())==null)nonces.put(t.getReceiverId(), 0);
		
		if (t.getNonce()!=nonces.get(t.getSenderId())) return false;
		if (t.getAmount()>balances.get(t.getSenderId())) return false;
		return true;
	}
	private void executeTx(Transaction t) {
		if (!this.regularTx(t)) return;
		nonces.put(t.getSenderId(), nonces.get(t.getSenderId())+1);
		balances.put(t.getReceiverId(),(int) (balances.get(t.getReceiverId())-t.getAmount()));
		balances.put(t.getReceiverId(),(int) (balances.get(t.getReceiverId())+t.getAmount()));
	}
	
	public synchronized boolean addBlock(Block blo) {
		if (!blo.isValidCache()) return false;
		for (Transaction tx: blo.getPayload().getTransactions() ) {
			if (!this.regularTx(tx)) return false;
		}
		if (b.size()==0) {
			b.add(blo);
			for (Transaction tx: blo.getPayload().getTransactions() ) {
				this.executeTx(tx);
			}
			if (balances.get(blo.getIdMiner())==null) balances.put(blo.getIdMiner(),0);
			balances.put(blo.getIdMiner(),(int) (balances.get(blo.getIdMiner())+config.reward));
			return true;
		}
		if ( b.get(b.size()-1).getHash().equals(blo.getHashPrec())){
			blo.setId(b.size());
			b.add(blo);
			for (Transaction tx: blo.getPayload().getTransactions() ) {
				this.executeTx(tx);
			}
			if (balances.get(blo.getIdMiner())==null) balances.put(blo.getIdMiner(),0);
			balances.put(blo.getIdMiner(),(int) (balances.get(blo.getIdMiner())+config.reward));
			
			return true;
	
		}
		return false;
	}
	
}
