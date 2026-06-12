package service;

import dataaccess.TransactionDAO;

public class ReportService {

    private TransactionDAO transactionDAO;

    public ReportService() {
        transactionDAO = new TransactionDAO();
    }

    // Total Sales Report
    public void  salesReport() {
          transactionDAO.salesReport();
    }

    // Total Purchase Report
    public void  purchaseReport() {
    	 transactionDAO.purchaseReport();
    }

    // Profit Report
    public void lowStockReport() {
          transactionDAO.lowStockReport();
          
    
   }
    public void stockReport()  {
        transactionDAO.stockReport() ;
        
  }
    

    // Display Sales Report
    public void displaySalesReport() {
        System.out.println("Total Sales :" + "salesReport()");
    }

    // Display Purchase Report
    public void displayPurchaseReport() {
        System.out.println("Total Purchase : " + " purchaseReport()");
        
    }

    // Display Profit Report
    public void displayLowStockReport() {
        System.out.println("Low Stock: " + "lowStockReport()");
        
    }
    public void displayStockReport() {
        System.out.println("Stock Report : " + "stockReport()");
        
    }

    // Display Complete Report
    public void displayCompleteReport() {
        System.out.println("\n===== INVENTORY REPORT =====");
        System.out.println("Total Sales     : " + "salesReport()");
        System.out.println("Total Purchase  : " + "purchaseReport()");
        System.out.println("Low Stock Report: " + "lowStockReport()");
        System.out.println("Stock Report: " + "    stockReport()");
        
    }
}