/**
 * 
 */
package dataaccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


import database.DBConnection;
import model.Customer;

public class CustomerDAO {

	public boolean addCustomer(Customer customer) {
		 
		String query="INSERT INTO customer(cust_name,phone,email,address)VALUES(?,?,?,?)";
		
		
		try {
			Connection con=DBConnection.getConnection();
			
			PreparedStatement ps= con.prepareStatement(query);
			
			ps.setString(1, customer.getCustomerName());
			ps.setInt(2, customer.getPhoneNumber());
			ps.setString(3, customer.getEmail());
			ps.setString(4, customer.getAddress());
			
			int rows =ps.executeUpdate();
			
			return rows>0;
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
	return false;
	}
}