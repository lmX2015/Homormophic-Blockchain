package config;

public class config {
	// parameters 
	public static int helloInt = 255;
	public static String ID = "lmX2015";
	public static int dirRefresh = 10;
	public static int firstSeq= 0;
	public static String pathtoDir ="C:\\Users\\Louis\\eclipse-workspace\\INF557_td67\\src\\Myfiles";
	public static String pathPeerFile = "C:\\Users\\Louis\\eclipse-workspace\\INF557_td67\\src\\peerFiles";
	public static String defaultHash= "HashTest";
	
	//functions enabling
	public static boolean blockchainEnabled = false; // will simulate blockchain part
	
	
	// debug options (what is printed in the console)
	public static boolean pmessageIn = true;
	public static boolean pmessageOut =true;
	public static boolean pgl =false; //globalListHandler debug
	public static boolean phello = false ;// print hello handler debugging
	public static boolean psyn =true;
	public static boolean psimlist= false;
	public static boolean pserving =false;
	
	// commandline
	public static boolean cmlineon = true;
	public static boolean interrupthandler = true;
}
