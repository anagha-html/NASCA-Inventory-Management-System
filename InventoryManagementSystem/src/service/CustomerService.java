package service;


import dataaccess.CustomerDAO;
import model.Customer;
import exception.InventoryException;


public class CustomerService {
	
	private CustomerDAO customerDAO =new CustomerDAO();
	
	public boolean addCustomer(Customer customer) throws InventoryException  {
		if (customer.getCustomerName()==null|| customer.getCustomerName().isEmpty()) {
			throw new InventoryException("CUSTOMER NAME CANNOT BE EMPTY");
			
		}
		return customerDAO.addCustomer(customer);
		
		
	}
	

}
