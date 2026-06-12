/**
 * 
 */
package model;

/**
 * 
 */
public class Customer {

	
	private int custID;
	private String custName;
	private int phoneNumber;
	private String email;
	private String address;
	
	public Customer() {}
	public Customer(int custID,String custName,int phoneNumber,String email,String address)
	{this.custID=custID;
	this.custName=custName;
	this.phoneNumber=phoneNumber;
	this.email=email;
	this.address=address;
	
	}
	public void addCustomer() {
		System.out.println("CUSTOMER ADDED SUCCESSFULLY");
	
	}
	public void updateCustomer() {
		System.out.println("CUSTOMER UPDATED SUCCESSFULLY");
	}
	public void deleteCustomer() {
		System.out.println("CUSTOMER DELETED SUCCESSFULLY");
	}
	public void searchCustomer() {
		System.out.println("Searching.......");
	}
	public void displayCustomer() {
		System.out.println("CUSTOMER ID :" +custID);
		System.out.println("CUSTOMER NAME :" +custName);
		System.out.println("PHONE NUMBER :" +phoneNumber);
		System.out.println("EMAIL :" +email);
		System.out.println("ADDRESS :" +address);
		
	}
	
	public int getCustomerID() {
		return custID;
	}
	public void setCustomerID(int custID) {
		this.custID=custID;
		
	}
	public String getCustomerName() {
		return custName;
		
	}
	public void setCustomerName() {
		this.custName=custName;
		
	}
	public int getPhoneNumber() {
		return phoneNumber;
		
	}
	public void setPhoneNumber() {
		this.phoneNumber=phoneNumber;
		
	}
	public String getEmail() {
		return email;
		
	}
	public void setEmail() {
		this.email=email;
		
	}
	public String getAddress() {
		return address;
		
	}
	public void setAddress() {
		this.address=address;
	}
	

}
