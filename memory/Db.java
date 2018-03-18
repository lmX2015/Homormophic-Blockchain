package memory;
import java.sql.*;
import security.*;
public class Db {

	public static synchronized Block[] getAll() {
		try (
				Connection conn = DriverManager.getConnection(
						"jdbc:mysql://localhost:3306/blockchain?useSSL=false", "root", "");
				Statement stmt = conn.createStatement();
				){
			String request = "select * from blocks order by id";
			ResultSet rset = stmt.executeQuery(request);
			int rowcount = 0;
			if (rset.last()) {
				rowcount = rset.getRow();
				rset.beforeFirst(); // not rs.first() because the rs.next() below will move on, missing the first element
			}
			Block[]  res = new Block[rowcount];
			//Block res = null; 
			rowcount =0;
			while(rset.next()) {
				res[rowcount]= new Block();
				res[rowcount].setHash(rset.getString("hash"));
				res[rowcount].setId(rset.getInt("id"));
				res[rowcount].setPayload(new Payload((rset).getString("content")));
				rowcount ++;
			}
			return res;


		}
		catch(SQLException ex) {
			ex.printStackTrace();
			return null;
		}  


	}
	public static synchronized Block getFromId(int id) {
		try (
				Connection conn = DriverManager.getConnection(
						"jdbc:mysql://localhost:3306/blockchain?useSSL=false", "root", "");
				Statement stmt = conn.createStatement();
				){
			String request = "select * from blocks where id="+id;
			ResultSet rset = stmt.executeQuery(request);
			Block res = null; 
			while(rset.next()) {
				res.setHash(rset.getString("hash"));
				res.setId(id);
				res.setPayload(new Payload((rset).getString("content")));
			}
			return res;


		}
		catch(SQLException ex) {
			ex.printStackTrace();
			return null;
		}  

	}
	public static synchronized long getAmountUser(String pubkey ) {
		try (
				Connection conn = DriverManager.getConnection(
						"jdbc:mysql://localhost:3306/blockchain?useSSL=false", "root", "");
				Statement stmt = conn.createStatement();
				){
			String request = "select amount from accounts where pubkey =\'"+pubkey+"\'";
			ResultSet rset = stmt.executeQuery(request);
			long res =0;
			while(rset.next()) {
				res = rset.getLong("amount");
			}
			return res;


		}
		catch(SQLException ex) {
			ex.printStackTrace();
			return 0;
		}  

	}

	/*  public static void main(String[] args) {
	      try (
	         // Step 1: Allocate a database 'Connection' object
	         Connection conn = DriverManager.getConnection(
	               "jdbc:mysql://localhost:3306/blockchain?useSSL=false", "root", "");
	               // MySQL: "jdbc:mysql://hostname:port/databaseName", "username", "password"

	         // Step 2: Allocate a 'Statement' object in the Connection
	         Statement stmt = conn.createStatement();
	      ) {
	         // Step 3: Execute a SQL SELECT query, the query result
	         //  is returned in a 'ResultSet' object.
	         String strSelect = "select id, hash, content from blocks";
	         System.out.println("The SQL query is: " + strSelect); // Echo For debugging
	         System.out.println();

	         ResultSet rset = stmt.executeQuery(strSelect);

	         // Step 4: Process the ResultSet by scrolling the cursor forward via next().
	         //  For each row, retrieve the contents of the cells with getXxx(columnName).
	         System.out.println("The records selected are:");
	         int rowCount = 0;
	         while(rset.next()) {   // Move the cursor to the next row, return false if no more row
	            //String title = rset.getString("title");
	            //double price = rset.getDouble("price");
	            //int    qty   = rset.getInt("qty");

	        	 int id = rset.getInt("id");
	        	 String hash = rset.getString("hash");
	        	 String content = rset.getString("content");
	            System.out.println(id + ", " + hash +" ,"+content);
	            ++rowCount;
	         }
	         System.out.println("Total number of records = " + rowCount);

	      } catch(SQLException ex) {
	         ex.printStackTrace();
	      }
	      // Step 5: Close the resources - Done automatically by try-with-resources
	   }*/
}
