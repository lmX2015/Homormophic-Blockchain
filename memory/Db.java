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
				res = new Block();
				res.setHash(rset.getString("hash"));
				res.setId(id);
				res.setPayload(new Payload((rset).getString("content")));
				res.setHashPrec(rset.getString("hashprev"));
				res.setIdMiner(rset.getString("miner"));
				res.setSigMineur(rset.getString("signature"));
				res.setPow(rset.getString("pow"));
			}
			return res;


		}
		catch(SQLException ex) {
			ex.printStackTrace();
			return null;
		}  

	}
	public static synchronized void exportBlock(Block b) {
		
		try {
			Connection conn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/blockchain?useSSL=false", "root", "");
			PreparedStatement ps = conn.prepareStatement(
					"UPDATE blocks SET  hash= ?, content =?, miner=?, hashprev=?, signature=?,pow=? WHERE id =?");

			// set the preparedstatement parameters
			ps.setString(1,b.getHash());
			ps.setString(2,b.getPayload().toString());
			ps.setString(3,b.getIdMiner() );
			ps.setString(4,b.getHashPrec());
			ps.setString(5,b.getSigMineur());
			ps.setInt(7, b.getId());
			ps.setString(6,b.getPow());
			
			// call executeUpdate to execute our sql update statement
			if (ps.executeUpdate()==1){
				ps.close();

			}
			else {
				ps.close();
				String query=" insert into blocks (hash, content, miner, hashprev,signature,id,pow)"
				        + " values (?,?,?,?,?,?,?)";

				ps = conn.prepareStatement(query);						
				ps.setString(1,b.getHash());
				ps.setString(2,b.getPayload().toString());
				ps.setString(3,b.getIdMiner() );
				ps.setString(4,b.getHashPrec());
				ps.setString(5,b.getSigMineur());
				ps.setInt(6, b.getId());
				ps.setString(7,b.getPow());
				
				ps.execute();
				ps.close();

			}
			conn.close();
			


		}
		catch(SQLException ex) {
			ex.printStackTrace();

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
