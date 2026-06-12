/**
 * 
 */
package service;

import dataaccess.UserDAO;

public class LoginService {
	
	private UserDAO userDAO =new UserDAO();
	
	public boolean login(String username,String password) {
		
		return userDAO.validateUser(username,password);
		
		
	}
	



	}


