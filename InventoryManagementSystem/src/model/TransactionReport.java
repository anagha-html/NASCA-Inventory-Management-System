package model;

public class TransactionReport {
	private int  TransactionID;
	private int ItemID;
	private String TransactionType;
	private int  Quantity ;
	private String  TransactionDate;
	private double totalSales;
	private double totalPurchase;
	private double Profit;
	
	
	
	
	public TransactionReport() {}
		public TransactionReport(int  TransactionID, int ItemID,
				String TransactionType,int  Quantity,
				String  TransactionDate,double totalSales,double totalPurchase,double Profit
				)
		{
		this.TransactionID=TransactionID;
		this.ItemID=ItemID;
		this.TransactionType=TransactionType;
		this.Quantity=Quantity;
		this.TransactionDate=TransactionDate;
		this.totalSales=totalSales;
		this.totalPurchase=totalPurchase;
		this.Profit=Profit;
		
	}
		public int getTransactionID() {
			return TransactionID;
		}
		public void setTransactionID(int TransactionID) {
			this.TransactionID=TransactionID;
			
		}
		 public int getItemId() {
		        return ItemID;
		    }

		    public void setItemId(int ItemID) {
		        this.ItemID = ItemID;
		    }

		    public String getTransactionType() {
		        return TransactionType;
		        
		    }
		    public void setTransactionType(String TransactionType) {
		    	this.TransactionType=TransactionType;
		    	
		    }
		
		    public int getQuantity() {
		        return Quantity;
		    }

		    public void setQuantity(int Quantity) {
		        this.Quantity = Quantity;
		    }

		    public String getTransactionDate() {
		        return TransactionDate;
		    }

		    public void setTransactionDate(String TransactionDate) {
		        this.TransactionDate = TransactionDate;
		    }
		
		    public double gettotalSales() {
		    	return totalSales;
		    	
		    }
		    public void settotalSales(double totalSales) {
		    	this.totalSales=totalSales;
		    	
		    }
		    public double gettotalPurchase() {
		    	return totalPurchase;
		    	
		    }
		    public void settotalPurchase(double totalPurchase) {
		    	this.totalPurchase=totalPurchase;
		    	
		    }
		    public double getProfit() {
		    	return Profit;
		    	
		    }
		    public void setProfit(double Profit) {
		    	this.Profit=Profit;
		    	
		    }
		

}
