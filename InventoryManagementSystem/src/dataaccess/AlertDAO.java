package dataaccess;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;




import database.DBConnection;

public class AlertDAO {
	public void checkLowStock() {
	    String query = "SELECT ItemID, ItemName, Category,QuantityAvailable, MinimumStockLevel,SupplierID" +
	                   "FROM InventoryItem " +
	                   "WHERE QuantityAvailable <= MinimumStockLevel";

	    try {
	        Connection con = DBConnection.getConnection();
	        PreparedStatement ps = con.prepareStatement(query);
	        ResultSet rs = ps.executeQuery();

	        boolean lowStockFound = false;

	        System.out.println("\n====LOW STOCK ALERT======");
	        while (rs.next()) {
	            lowStockFound = true;
	            System.out.println("⚠ ALERT!");
	            System.out.println("Item ID: " + rs.getInt("ItemID"));
	            System.out.println("Item Name: " + rs.getString("ItemName"));
	            System.out.println("Category : " +rs.getString("Category"));
	            System.out.println("Available Stock: " + rs.getInt("QuantityAvailable"));
	            System.out.println("Minimum Stock Level: " + rs.getInt("MinimumStockLevel"));
	            System.out.println("SUPPLIER ID : "+ rs.getInt("SupplierID"));
	            System.out.println("--------------------------------");
	        }
	        		
	        
	    }catch(Exception e) {
	    	e.printStackTrace();
	    }
	}
	
	// alert message
	public void generateAlert() {
	    String query = "SELECT ItemName, QuantityAvailable FROM InventoryItem " +
	                   "WHERE QuantityAvailable <= MinimumStockLevel";

	    try {
	        Connection con = DBConnection.getConnection();
	        PreparedStatement ps = con.prepareStatement(query);
	        ResultSet rs = ps.executeQuery();

	        boolean alertFound = false;
	        while (rs.next()) {
	            alertFound = true;

	            System.out.println("⚠ LOW STOCK ALERT");
	            System.out.println("Item: " + rs.getString("ItemName"));
	            System.out.println("Remaining Stock: " + rs.getInt("QuantityAvailable"));
	            System.out.println();
	        }

	        if (!alertFound) {
	            System.out.println("No Low Stock Alerts.");
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	

	        	
	        		

}
