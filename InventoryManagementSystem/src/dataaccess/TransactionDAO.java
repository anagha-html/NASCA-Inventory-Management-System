package dataaccess;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;


import database.DBConnection;
import model.TransactionReport;

public class TransactionDAO {
	public void addTransaction() {
		Scanner sc=new Scanner(System.in);
		try {
			
		
			System.out.println("ENTER TRANSACTION TYPE:");
			String TransactionType=sc.next();
			
			System.out.println("ENTER QUANTITY :");
			int Quantity=sc.nextInt();
			
			System.out.println("ENTER TRANSACTION DATE :");
			String TransactionDate=sc.next();
			
			
			
			
			Connection con=DBConnection.getConnection();
			
			
			String query="INSERT INTO TransactionDetails(TransactionType,Quantity,TransactionDate)"
					+ "VALUES(?,?,?)";
			
			
			PreparedStatement ps= con.prepareStatement(query);
			
		
			ps.setString(1, TransactionType);
			ps.setInt(2, Quantity);
			ps.setString(3, TransactionDate);
			
			
			int rows=ps.executeUpdate();
			if(rows>0) {
				System.out.println("TRANSACTION DETAILS ADDED SUCCESSFULLY ");
				
			}else {
				System.out.println("SOMETHING WENT WRONG");
				
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

//VIEW TRANSACTION DETAILS
	public void viewTransaction() {
	  try {
		  
		  String query ="SELECT * FROM TransactionDetails";
		 
		  Connection con=DBConnection.getConnection();
		  PreparedStatement ps=con.prepareStatement(query);
		  
		  ResultSet rs =ps.executeQuery();
		  while(rs.next()) {

              System.out.println(
                  rs.getInt("transactionid") +        " | " +
                  rs.getInt("ItemID") +               " | " +
                  rs.getString("transactiontype")   + " | " +
                  rs.getInt("quantity")             + " | " 
                  
              );
          }
		  
	  }catch(Exception e) {
		  e.printStackTrace();
	  }
	  
	}

	  
// STOCKin fn
public boolean stockIN() {
	Scanner sc=new Scanner(System.in);
	
	 System.out.println("ENTER ITEM ID:");
     int ItemID = sc.nextInt();

     System.out.println("ENTER QUANTITY TO ADD:");
     int Quantity = sc.nextInt();

	String updateStock="UPDATE InventoryItem SET QuantityAvailable=QuantityAvailable + ? WHERE ItemID=?";
	String logTransaction = "INSERT INTO TransactionDetails(ItemID, TransactionType, Quantity) VALUES (?, 'STOCK_IN', ?)";

	try {
		Connection con=DBConnection.getConnection();
		PreparedStatement ps1 = con.prepareStatement(updateStock);
		ps1.setInt(1, ItemID);
		ps1.setInt(2, Quantity);
		
		int rows=ps1.executeUpdate();
		
		if (rows>0) {
			PreparedStatement ps2=con.prepareStatement(logTransaction);
			ps2.setInt(1, ItemID);
			ps2.setInt(2, Quantity);
			ps2.executeUpdate();
			return true;
			
		}
		}catch(Exception e) {
			e.printStackTrace();
		}
	return false;
	
		
		
	}

//Stock out 
public boolean stockOut() {
	Scanner sc=new Scanner(System.in);
	
	 System.out.println("ENTER ITEM ID:");
     int ItemID = sc.nextInt();

     System.out.println("ENTER QUANTITY TO SELL:");
     int Quantity = sc.nextInt();
	String updateStock="UPDATE InventoryItem SET QuantityAvailable=QuantityAvailable - ? WHERE ItemID=?";
	 String logTransaction = "INSERT INTO TransactionDetails(ItemID, TransactionType, Quantity) VALUES (?, 'STOCK_OUT', ?)";

	 try {
		 Connection con=DBConnection.getConnection();
		 PreparedStatement ps1=con.prepareStatement(updateStock);
		 ps1.setInt(1, Quantity);
		 ps1.setInt(2, ItemID);
		 
		 
		 int rows=ps1.executeUpdate();
		 
		 if(rows>0) {
			 PreparedStatement ps2=con.prepareStatement(logTransaction);
			 ps2.setInt(1, ItemID);
			 ps2.setInt(2, Quantity);
			 ps2.executeUpdate();
			 return true;
			 
		 }

		 
		 
		 }catch(Exception e) {
			 e.printStackTrace();
		 }
	 return false;
	 
		 
}
	

//tranaction log
public void viewTransactionLog() {
    String query = "SELECT * FROM TransactionDetails";

    try {
        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(query);

        ResultSet rs = ps.executeQuery();

        System.out.println("\n===== TRANSACTION LOG =====");

        while(rs.next()) {
            System.out.println("Transaction ID : " + rs.getInt("TransactionID"));
            System.out.println("Item ID        : " + rs.getInt("ItemID"));
            System.out.println("Type           : " + rs.getString("TransactionType"));
            System.out.println("Quantity       : " + rs.getInt("Quantity"));
            System.out.println("DATE           : " + rs.getDate("TransactionDate"));
            System.out.println("TOTAL SALES    : " + rs.getDouble("totalSales"));
            System.out.println("TOTAL PURCHASE : " + rs.getDouble("totalPurchase"));
            System.out.println("PROFIT         : " + rs.getDouble("Profit"));
            System.out.println("--------------------------------");
        }

    } catch(Exception e) {
        e.printStackTrace();
    }
	  
}

//stock report
public void stockReport() {
    String query = "SELECT * FROM InventoryItem";

    try {
        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(query);
        ResultSet rs = ps.executeQuery();

        System.out.println("\n===== STOCK REPORT =====");
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

//sales report
public void salesReport() {
    String query = "SELECT * FROM TransactionDetails WHERE TransactionType='STOCK_OUT'";

    try {
        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(query);
        ResultSet rs = ps.executeQuery();

        System.out.println("\n===== SALES REPORT =====");
        while(rs.next()) {
            System.out.println("Transaction ID : " + rs.getInt("TransactionID"));
            System.out.println("Item ID        : " + rs.getInt("ItemID"));
            System.out.println("Type           : " + rs.getString("TransactionType"));
            System.out.println("Quantity       : " + rs.getInt("Quantity"));
            System.out.println("DATE           : " + rs.getDate("TransactionDate"));
            System.out.println("TOTAL SALES    : " + rs.getDouble("totalSales"));
            System.out.println("TOTAL PURCHASE : " + rs.getDouble("totalPurchase"));
            System.out.println("PROFIT         : " + rs.getDouble("Profit"));
            System.out.println("--------------------------------");
        }
    }catch(Exception e) {
    	e.printStackTrace();
    }
}

//purchase report
public void purchaseReport() {
    String query = "SELECT * FROM TransactionDetails WHERE TransactionType='STOCK_IN'";

    try {
        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(query);
        ResultSet rs = ps.executeQuery();

        System.out.println("\n===== PURCHASE REPORT =====");
        while(rs.next()) {
            System.out.println("Transaction ID : " + rs.getInt("TransactionID"));
            System.out.println("Item ID        : " + rs.getInt("ItemID"));
            System.out.println("Type           : " + rs.getString("TransactionType"));
            System.out.println("Quantity       : " + rs.getInt("Quantity"));
            System.out.println("DATE           : " + rs.getDate("TransactionDate"));
            System.out.println("TOTAL SALES    : " + rs.getDouble("totalSales"));
            System.out.println("TOTAL PURCHASE : " + rs.getDouble("totalPurchase"));
            System.out.println("PROFIT         : " + rs.getDouble("Profit"));
            System.out.println("--------------------------------");
        }
    }catch(Exception e) {
    	e.printStackTrace();
    }
}

//low stock report
public void lowStockReport() {
    String query = "SELECT * FROM InventoryItem WHERE QuantityAvailable <= MinimumStockLevel";

    try {
        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(query);
        ResultSet rs = ps.executeQuery();

        System.out.println("\n===== LOW STOCK REPORT =====");
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

    }
