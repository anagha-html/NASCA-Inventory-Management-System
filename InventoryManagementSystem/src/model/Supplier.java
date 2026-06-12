/**
 * 
 */
package model;


public class Supplier {

	private int SupplierID;
	private String SupplierName;
	private int ContactNumber;
	private String Email;
	private String Address;
	
	public Supplier() {}
	public Supplier(int SupplierID,String SupplierName,int ContactNumber,String Email,String Address)
	{this.SupplierID=SupplierID;
	this.SupplierName=SupplierName;
	this.ContactNumber=ContactNumber;
	this.Email=Email;
	this.Address=Address;
	
	}
	public void addSupplier() {
		System.out.println("SUPPLIER ADDED SUCCESSFULLY");
		
	}
	public void updateSuplier() {
		System.out.println("SUPPLIER UPDATED SUCCESSFULLY");
		
	}
	public void deleteSupplier() {
		System.out.println("SUPPLIER DELETED SUCCESSFULLY");
		
	}
	public void viewSupplier() {
		System.out.println("SUPPLIER ID:" +SupplierID);
		System.out.println("SUPPLIER NAME :" +SupplierName);
		System.out.println("SUPPLIER PHONE :" +ContactNumber);
		System.out.println("SUPPLIER EMAIL :" +Email);
		System.out.println("SUPPLIER ADDRESS :" +Address);
	}
	
	public int getSupplierID() {
		return SupplierID;
	}
	public void setSupllierID() {
		this.SupplierID=SupplierID;
		
	}
	public String getSupplierName() {
		return SupplierName;
		
	}
	public void setSupplierName() {
		this.SupplierName=SupplierName;
		
	}
	public int getContactNumber() {
		return ContactNumber;
		
	}
	public void setContactNumber() {
		this.ContactNumber=ContactNumber;
		
	}
	public String getEmail() {
		return Email;
		
	}
	public void setEmail() {
		this.Email=Email;
		
	}
	public String getAddress() {
		return Address;
		
	}
	public void setAddress() {
		this.Address=Address;
	}
}
