
package dataaccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import database.DBConnection;

import database.DBConnection;
import model.User;


public class UserDAO {
	public boolean validateUser(String username,String password) {
		String query="SELECT * FROM login WHERE username=? AND password =?";
		
		
	try {
		Connection con=DBConnection.getConnection();
		PreparedStatement ps = con.prepareCall(query);
		
		ps.setString(1,username);
		ps.setString(2, password);
		
		ResultSet rs= ps.executeQuery();
		
		return rs.next();
		
	
	}catch(SQLException e) {
		e.printStackTrace();
	}
	return false;
	}
	

}
	
	
	


