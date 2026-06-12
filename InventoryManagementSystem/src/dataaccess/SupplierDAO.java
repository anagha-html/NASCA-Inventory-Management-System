/**
 * 
 */
package dataaccess;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;




import database.DBConnection;
import model.Supplier;


public class SupplierDAO {
	public void addSupplier() {
		Scanner sc=new Scanner(System.in);
		try {
			System.out.println("ENTER SUPPLIER ID TO BE UPDATED:");
			int SupplierID=sc.nextInt();
			
			System.out.println("ENTER SUPPLIER NAME :");
			String SupplierName=sc.next();
			
			
		System.out.println(" ENTER CONTACT NUMBER :");
		String ContactNumber=sc.next();
		
		System.out.println("ENTER EMAIL :");
		String Email=sc.next();
		
		System.out.println("ENTER ADDRESS");
		String Address=sc.next();
		
		String query ="INSERT INTO SupplierDetails(SupplierName,ContactNumber,Email,Address)VALUES(?,?,?,?)";
		Connection con=DBConnection.getConnection();
		PreparedStatement ps= con.prepareStatement(query);
		
		
		ps.setString(1, SupplierName);
		ps.setString(2, ContactNumber);
		ps.setString(3, Email);
		ps.setString(4, Address);
		
		int rows=ps.executeUpdate();
		if(rows>0){
			System.out.println("SUPPLIER ADDED SUCCESSFULLY");
			
			
		}else {
			System.out.println("SOMETHING WENT WRONG");
		}
		
		
		
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}

//To update supplier
	public boolean updateSupplier() {
		Scanner sc=new Scanner(System.in);
		
		System.out.println("ENTER NEW NAME:");
		String SupplierName=sc.next();
		
	    System.out.println("ENTER NEW CONTACT NUMBER:");
	    String ContactNumber=sc.next();
	    
	    System.out.println("ENTER  SUPPLIERID:");
	    int SupplierID=sc.nextInt();

	    String query =
	        "UPDATE SupplierDetails SET SupplierName=?, ContactNumber=? WHERE SupplierID=?";

	    try {
	        Connection con = DBConnection.getConnection();

	        PreparedStatement ps = con.prepareStatement(query);

	        ps.setString(1, SupplierName);
	        ps.setString(2, ContactNumber);
	        ps.setInt(3,SupplierID);
	        
	        

	        return ps.executeUpdate() > 0;

	    } catch(Exception e) {
	        e.printStackTrace();
	    }

	    return false;
	}

// to delete a supplier
public void deleteSupplier() {
	Scanner sc=new Scanner(System.in);
	try {
		System.out.println("ENTER SUPPLIER ID:");
		int SupplierID=sc.nextInt();
		
		String query="DELETE FROM  SupplierDetails WHERE SupplierID=?";
		Connection con =DBConnection.getConnection();
		PreparedStatement ps =con.prepareStatement(query);
		
		ps.setInt(1, SupplierID);
		
		int rows=ps.executeUpdate();
		if(rows>0) {
			System.out.println("SUPPLIER DELETED SUCCESSFULLY");
			
		}else {
			System.out.println("SOMETHING WENT  WRONG");
			
		}
	}catch(Exception e) {
		e.printStackTrace();
	}
	
}
	
//to VIEW SUPPLIER
public void displaySupplier() {
	Scanner sc=new Scanner(System.in);
	try {
		
		String query="SELECT * FROM SupplierDetails ";
		Connection con =DBConnection.getConnection();
		PreparedStatement ps=con.prepareStatement(query);
		
		ResultSet rs=ps.executeQuery();
		while(rs.next()) {
			System.out.println(rs.getInt("SupplierID")+ " "
					+ rs.getString("SupplierName")+ " "
					+ rs.getString("ContactNumber")+" "
					+rs.getString("Email")+ " "
					+rs.getString("Address")
					);
		}
	}catch(Exception e) {
		e.printStackTrace();
	}
}
	
}

	
	

