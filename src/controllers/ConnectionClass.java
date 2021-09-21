package controllers;
import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionClass {
	
	public Connection connection;
	
	public Connection getConnection() {
		
		String dbName = "accountdb";
		String userName = "root";
		String password = "LinkinP4rk1!";
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			connection = DriverManager.getConnection("jdbc:mysql://localhost/" + dbName, userName, password);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return connection;	
	}
	
}
