/**
 * 
 */
package model;

/**
 * 
 */
public class Inventory {

	private int ItemID;
	private String ItemName;
	private String Category;
	private int QuantityAvailable;
	private double Price;
	private int MinimumStockLevel;
	private int SupplierID;
	
	public Inventory() {}
	
	public Inventory(int ItemID,String itemName,String Category,int QuantityAvailable,double Price,int MinimumStockLevel,int SupplierID) 
	{this.ItemID=ItemID;
	this.ItemName=ItemName;
	this.Category=Category;
	this.QuantityAvailable=QuantityAvailable;
	this.Price=Price;
	this.MinimumStockLevel=MinimumStockLevel;
	this.SupplierID=SupplierID;
	
	}
	
	public void addItem() {
		System.out.println("ITEM ADDED SUCCESSFULLY");
	}
	public void updateItem() {
		System.out.println("ITEM UPDATED SUCCESSFULLY");
	}
	public void deleteItem() {
		System.out.println("ITEM DELETED SUCCESSFULLY");
	}
	public void searchItem() {
		System.out.println("Searching item......");
	}
	public void displayItem() {
		System.out.println("ITEM ID :" +  ItemID);
		System.out.println("ITEM NAME :" + ItemName);
		System.out.println("CATEGORY :" + Category);
		System.out.println("QUANTITY AVAILABLE  :" + QuantityAvailable);
		System.out.println("PRICE :" + Price);
		System.out.println("MINIMUM STOCK :" +MinimumStockLevel);
	}
	public int getItemID() {
		return ItemID;
	
	}
	public void setItemID( int ItemID) {
		this.ItemID=ItemID;
		
	}
	public String getItemName() {
		return ItemName;
		
	}
	public void setItemName(String ItemName) {
		this.ItemName=ItemName;
		
	}
	public String getCategory() {
		return Category;
		
	}
	public void setCategory(String Category) {
		this.Category=Category;
		
		
	}
	public int getQuantityAvailable() {
		return QuantityAvailable;
	}
	public void setQuantityAvailable(int QuantityAvailable) {
		this.QuantityAvailable=QuantityAvailable;
		
	}
	public  double getPrice() {
		return Price;
		
	}
	public void setPrice(double Price) {
		this.Price=Price;
		
	}
	public int getMinStockLevel() {
		return MinimumStockLevel;
		
	}
	public void setMinStock(int minStock) {
		this.MinimumStockLevel=MinimumStockLevel;
		
	}
	public int getSupplierID() {
		return SupplierID;
	}
	public void setSupllierID() {
		this.SupplierID=SupplierID;
		
	}
	

}
