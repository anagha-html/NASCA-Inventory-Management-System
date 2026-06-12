package service;

import dataaccess.UserDAO;
import model.User;
import exception.InventoryException;

public class UserService {
	
	private UserDAO userDAO= new UserDAO();
	public boolean LoginUser(String username,String password) throws InventoryException{
		if(username==null||username.isEmpty()) {
			throw new InventoryException("USERNAME CANNOT BE EMTY");
		}
		
		if(password==null||password.isEmpty()) {
			throw new InventoryException("PASSWORD CANNOT BE EMPTY");
		
	}
	return userDAO.validateUser(username,password );

}
}
