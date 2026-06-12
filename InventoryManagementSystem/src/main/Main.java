
package main;
import dataaccess.InventoryDAO;
import dataaccess.SupplierDAO;
import model.Inventory;
import dataaccess.TransactionDAO;
import dataaccess.AlertDAO;


import java.util.Scanner;



public class Main {

	 static Scanner sc =new Scanner(System.in);
	 
	public static void main(String[] args) {
		   
		   int choice;
		  
		
		do {
			System.out.println("\n=======INVENTORY MANAGEMENT SYSTEM=========");
			System.out.println("1. INVENTORY MANAGEMENT");
			System.out.println("2. SUPPLIER MANAGEMENT");
			System.out.println("3. TRANSACTION AND REPORT MANAGEMENT ");
			System.out.println("4. ALERT MANAGEMENT");
			System.out.println("5. EXIT");
			System.out.println("ENTER CHOICE");
			

	        choice = sc.nextInt();
			
			switch(choice) {
			case 1:
				inventoryMenu();
				break;
			case 2:
				supplierMenu();
				break;
				
			case 3:
				reportMenu();
				break;
				
			case 4:
				alertMenu();
				break;
				
			case 5:
				System.out.println("THANK YOU");
				break;
				
			default:
				System.out.println("PLEASE SELECT A VALID CHOICE");
				
			}
			
			}while (choice!=5);
		
	}
	  static  void inventoryMenu() {
		System.out.println("\n====INVENTORY MANAGEMENT====");
		System.out.println("1. ADD ITEM");
		System.out.println("2. UPDATE ITEM");
		System.out.println("3. DELETE ITEM");
		System.out.println("4. SEARCH ITEM");
		System.out.println("5. DISPLAY ITEMS");
		System.out.println("ENTER YOUR CHOICE:");
		
		int choice=sc.nextInt();
		
		InventoryDAO inventoryDAO=new InventoryDAO();
		
		switch(choice) {
		case 1:
			inventoryDAO.addItem(); 
			break;
		case 2:
			inventoryDAO.updateItem();
			break;
			
		case 3:
			inventoryDAO.deleteItem();
			break;
		
		case 4:
			inventoryDAO.SearchItemByName();
			break;
			
		case 5:
			inventoryDAO.viewItem();
			break;
			
			
		default:
			System.out.println("INVALID CHOICE");
			
			
		
		}
		
	  }



static void supplierMenu() {
	System.out.println("\n====SUPPLIER  MANAGEMENT====");
	System.out.println("1. ADD SUPPLIER");
	System.out.println("2. UPDATE SUPPLIER");
	System.out.println("3. DELETE SUPPLIER");
	System.out.println("4. DISPLAY SUPPLIERS");
	System.out.println("ENTER YOUR CHOICE:");
	
	int choice=sc.nextInt();
	
	
	
	
	SupplierDAO supplierDAO=new SupplierDAO();
	switch(choice) {
	case 1:
		supplierDAO.addSupplier();
		break;
	case 2:
		supplierDAO.updateSupplier();
		break;
	case 3:
		supplierDAO.deleteSupplier();
		break;
	case 4:
		supplierDAO.displaySupplier();
		break;
		
	default:
		System.out.println("INVALID CHOICE");
		break;
		
	}
	
	
}
	

static void reportMenu() {
	System.out.println("\n====TRANSACTION AND REPORT MANAGEMENT====");
	System.out.println("1. ADD TRANSACTION DETAILS");
	System.out.println("2. VIEW TRANSACTION DETAILS");
	System.out.println("3. STOCK IN ");
	System.out.println("4. STOCK OUT");
	System.out.println("5. TRANSACTION LOG");
	System.out.println("6. STOCK REPORT ");
	System.out.println("7. SALES REPORT");
	System.out.println("8. PURCHASE REPORT");
	System.out.println("9. LOW STOCK REPORT");
	System.out.println("ENTER YOUR CHOICE:");
	
	int choice=sc.nextInt();
	
	
	
	
	TransactionDAO transactionDAO=new TransactionDAO();
	switch(choice) {
	case 1:
		transactionDAO.addTransaction();
		break;
	case 2:
		transactionDAO.viewTransaction();
		break;
	case 3:
		transactionDAO.stockIN( );
		break;
		
	case 4:
	    transactionDAO.stockOut( );
	    break;
	case 5:
		transactionDAO.viewTransactionLog();
		break;
	case 6:
		transactionDAO.stockReport();
		break;
	case 7:
		transactionDAO.salesReport();
		break;
	case 8:
		transactionDAO.purchaseReport();
		break;
	case 9:
		transactionDAO.lowStockReport();
		break;
	default:
		System.out.println("INVALID CHOICE");
	}
}
	
	

public static void alertMenu() {
	System.out.println("\n====ALERT  MANAGEMENT====");
	System.out.println("1. CHECK LOW STOCK");
	System.out.println("2.GENERATE ALERT MESSAGE");
	
	int choice=sc.nextInt();
	AlertDAO alertDAO=new AlertDAO();
	
	switch(choice) {
	case 1:
		alertDAO.checkLowStock();
		break;
	case 2:
		alertDAO.generateAlert();
		break;
	default:
		System.out.println("INVALID CHOICE");
	}
	sc.close();
	
}
	
}
