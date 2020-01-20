package main;

import java.sql.*;
import java.util.Optional;
import java.util.logging.Logger;

import dao.UserDao;

public class DatabaseJDBC {
	private static final Logger logger = Logger.getLogger(DatabaseJDBC.class.getName());
	private static String driver = "com.mysql.cj.jdbc.Driver";
	private static String url = "jdbc:mysql://localhost:3306/pwjj_lab?characterEncoding=UTF-8";
	
	public static void main(String[] args) {
		loadDriver();
		
		UserDao userDao = new UserDao();
		Optional<Integer> id = userDao.getUser("Przemyslaw");
		
		id.ifPresent((i) -> logger.info("id: " + i));
	}
	
	public static Connection getConnection() {
		return connectToDatabase("localhost", "pwjj_lab", "user", "H@slo123");
	}
	
	private static boolean loadDriver() {
	    try {
	        Class.forName(driver);
	        return true;
	    } catch (Exception e) {
	    	logger.severe(e.getMessage());
	        return false;
	    }
	}
	
	private static Connection connectToDatabase(String adress, String dataBaseName, String userName, String password) {
	    String baza = String.format("jdbc:mysql://%s:3306/%s", adress, dataBaseName);
	    Connection connection = null;
	    try {
	        connection = DriverManager.getConnection(baza, userName, password);
	    } catch (SQLException e) {
	    	logger.severe(e.getMessage());
	        System.exit(1);
	    }
	    return connection;
	}
}
