
package model;

public class User {

	private int userID;
	private String username;
	private String password;
	private String role;
	
	
	//default constructor
	public User() {
	}
		public User(int userID,String username,String paasword,String role)
		{
			this.userID=userID;
			this.username=username;
			this.password=password;
			this.role=role;
			
		}
		
		
		//getter and setter
		public int getUserID() {
			return userID;
			
		}
		public void setUserID(int userID) {
			this.userID=userID;
			
		}
		
		public String getUsername() {
			return username;
			
		}
		public void setUsername(String username) {
			this.username=username;
			
		}
		
		public String getPassword() {
			return password;
			
		}
		
		public void setPassword(String password) {
			this.password=password;
			
		}
		public String getRole() {
			return role;
			
		}
		public void setRole(String role) {
			this.role=role;
		}
		
		@Override
		public String toString() {
			return "User[userID=" +userID + ", Username=" +username +", role=" +role +"]";
			
		}
			
			
			
	public static void main(String[] args) {
		// TODO Auto-generated method stu

	}

}
