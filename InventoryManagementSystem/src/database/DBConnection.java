
package database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;



public class DBConnection {
	
	private static final String URL="jdbc:mysql://localhost:3306/inventorys_schema";
	private static final String USERNAME="root";
	private static final String PASSWORD="1234";
	
	
	public static Connection getConnection() {
		Connection con =null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			con =DriverManager.getConnection(URL,USERNAME,PASSWORD);
			 
			System.out.println("DATABASE CONNECTED SUCCESSFULLY");
			
					
		}catch (ClassNotFoundException e) {
			System.out.println("MYSQL DRIVER NOT  FOUND");
			e.printStackTrace();
			
		}catch(SQLException e) {
			System.out.println("Connection failed");
			e.printStackTrace();
		}
		return con;
		
	}
	public static void closeConnection(Connection con) {
		
		try {
			if(con!=null) {
				con.close();
				System.out.println("CONNECTION CLOSED");
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
