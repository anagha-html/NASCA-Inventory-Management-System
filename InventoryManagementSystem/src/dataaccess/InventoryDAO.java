package dataaccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

import model.Inventory;
import database.DBConnection;
import dataaccess.SupplierDAO;
public class InventoryDAO {
	//ADD ITEM 
	public void addItem() {

    Scanner sc = new Scanner(System.in);{

    try {
        

        System.out.print("Enter Item Name: ");
        String ItemName = sc.nextLine();
        
        System.out.println("ENTER CATEGORY :");
        String Category=sc.next();
        

        System.out.print("Enter Quantity: ");
        int quantity = sc.nextInt();

        System.out.print("Enter Price: ");
        double price = sc.nextDouble();
        
        System.out.println("ENTER MINIMUM STOCK LEVEL");
        int MinimumStockLevel=sc.nextInt();
        
       
        Connection con = DBConnection.getConnection();

        String query = "INSERT INTO InventoryItem(ItemName,Category,QuantityAvailable,Price,MinimumStockLevel,SupplierID) VALUES(?,?,?,?,?,?)";

        PreparedStatement ps = con.prepareStatement(query);

         
         ps.setString(1, ItemName);
         ps.setString(2, Category);
         ps.setInt(3, quantity);
         ps.setDouble(4, price);
         ps.setInt(5, MinimumStockLevel);
        
         
        int rows = ps.executeUpdate();

        if(rows > 0) {
            System.out.println("Inventory Added Successfully");
        }

        con.close();

    } catch(Exception e) {
        e.printStackTrace();
    }
}
	}




//update inventory item
    public void updateItem(){
    	Scanner sc=new Scanner(System.in);
    	try {
    	System.out.println("ENTER ITEM ID:");
    	int ItemId=sc.nextInt();
    	
    	
    	System.out.println("ENTER NEW QUANTITY:");
    	int Quantity=sc.nextInt();
    	
    	  Connection con = DBConnection.getConnection();
    	  String query= "UPDATE InventoryItem SET QuantityAvailable=? WHERE ItemID=?";
    	  
    	  PreparedStatement ps= con.prepareStatement(query);
    	  
    	  ps.setInt(1, Quantity);
    	  ps.setInt(2, ItemId);
    	  
    	  int rows=ps.executeUpdate();
    	  
    	  if(rows>0) {
    		  System.out.println("INVENTORY UPDATED SUCCESSFULLY");
    		  
    	  }else {
    		  System.out.println("SOMETHING WENT WRONG");
    		  
    	  }
    	  con.close();
   
    	
	
}catch(Exception e) {
	e.printStackTrace();
}
    
    	
}


//Delete inventory
public void deleteItem() {
	Scanner sc=new Scanner(System.in);
	try {
		System.out.println("ENTER ITEM ID:");
		int ItemID=sc.nextInt();
		
		String query="DELETE FROM InventoryItem WHERE ItemID=?";
		Connection con=DBConnection.getConnection();
		PreparedStatement ps= con.prepareStatement(query);
		
		ps.setInt(1, ItemID);
		
		int rows=ps.executeUpdate();
		
		if(rows>0) {
			System.out.println("ITEM DELETED SUCCESSFULLY");
			
		}else {
			System.out.println("SOMETHING WENT WRONG");
		}
	}catch(Exception e) {
		e.printStackTrace();
		
	}
}


//TO SEARCH AN ITEM
public void SearchItemByName() {
	Scanner sc=new Scanner(System.in);
	try {
		System.out.println("ENTER ITEM NAME:");
		String ItemName=sc.next();
		
		String query="SELECT * FROM InventoryItem WHERE ItemName=?";
		Connection con=DBConnection.getConnection();
		PreparedStatement ps=con.prepareStatement(query);
		
		ps.setString(1, ItemName);
		
		
		
		ResultSet rs=ps.executeQuery();
		

        while (rs.next()) {

            System.out.println("Item ID : " + rs.getInt("itemid"));
            System.out.println("Name    : " + rs.getString("itemname"));
            System.out.println("Category: " + rs.getString("category"));
            System.out.println("Qty     : " + rs.getInt("quantityavailable"));
            System.out.println("Price   : " + rs.getDouble("price"));
      System.out.println("Minimum Stock : " + rs.getInt("MinimumStockLevel") );
      System.out.println("Supplier ID   : " + rs.getInt("SupplierID"));
        }

		}catch(Exception e) {
			e.printStackTrace();
			
	}
}
	

//DISPLAY ITEM
public void viewItem() {
	Scanner sc=new Scanner(System.in);
	try {
		String query="SELECT * FROM InventoryItem ";
		Connection con=DBConnection.getConnection();
		PreparedStatement ps = con.prepareStatement(query);
		
		ResultSet rs=ps.executeQuery();
		
		 while(rs.next()) {

             System.out.println(
                     rs.getInt("itemid") + " "
                     + rs.getString("itemname") + " "
                     + rs.getString("category") + " "
                     + rs.getInt("quantityavailable") + " "
                     + rs.getDouble("price")+ " "
                     +rs.getInt("MinimumStockLevel")+ " "
                     +rs.getInt("SupplierID")
             );
		
	}
}catch(Exception e) {
	e.printStackTrace();
}
}
}
	
