package fatwallet;
import java.util.*;
import java.sql.*;


public class DatabaseInit {
	 
	  
	 public static void main(String args[])throws Exception
	 {
		 Connection connect = null;
		 Statement statement = null;
		 ResultSet resultSet = null;
		 
		 try {
		      // This will load the MySQL driver, each DB has its own driver
		      Class.forName("com.mysql.jdbc.Driver");
		      // Setup the connection with the DB
		      connect = DriverManager.getConnection("jdbc:mysql://localhost/?user=yliu0&password=yl1234");
		      // Statements allow to issue SQL queries to the database
		      statement = connect.createStatement();
		      // Result set get the result of the SQL query
		      int result = statement.executeUpdate("CREATE DATABASE IF NOT EXISTS FatWallet1");
		      
		      connect = DriverManager.getConnection("jdbc:mysql://localhost/FatWallet1?user=yliu0&password=yl1234");
			  statement = connect.createStatement();
			  result = statement.executeUpdate("CREATE TABLE IF NOT EXISTS users(user_name varchar(50),user_title varchar(50),registered_date varchar(50));");
			  result = statement.executeUpdate("ALTER IGNORE TABLE users ADD UNIQUE (user_name);");
			  
			  //result = statement.executeUpdate("CREATE TABLE IF NOT EXISTS items();");
			  result = statement.executeUpdate("CREATE TABLE IF NOT EXISTS deals(deal_id  varchar(20), publisher_name varchar(50), deal_name varchar(200),deal_category  varchar(50), deal_other_category  varchar(150), post_date varchar(50), description varchar(1000));");
			  result = statement.executeUpdate("ALTER IGNORE TABLE deals ADD UNIQUE (deal_id);");
			  
			  result = statement.executeUpdate("CREATE TABLE IF NOT EXISTS deal_replies(deal_id varchar(20),description varchar(15000), reply_user varchar(50),reply_date varchar(50));");
			  result = statement.executeUpdate("ALTER IGNORE TABLE deal_replies ADD UNIQUE INDEX (deal_id, reply_date);");
			  
			  
			  result = statement.executeUpdate("CREATE TABLE IF NOT EXISTS deal_change(deal_id varchar(20),deal_rating INT, views INT, replies INT, days INT,  record_order INT, record_date DATETIME);");
			  
			  result = statement.executeUpdate("CREATE TABLE IF NOT EXISTS rating(deal_id varchar(20), message_id varchar(20), rating_type ENUM('positive','negative'),rating_user varchar(50),record_date DATETIME );");
			  result = statement.executeUpdate("ALTER IGNORE TABLE rating ADD UNIQUE INDEX (deal_id, rating_user);");
			  
			  result = statement.executeUpdate("CREATE TABLE IF NOT EXISTS deals_queue(deal_id  varchar(20), days INT, record_order INT);");
			  result = statement.executeUpdate("ALTER IGNORE TABLE deals_queue ADD UNIQUE (deal_id);");
			  
			  //resultSet.close();
	    	  statement.close();
	    	  connect.close();
		    } catch (Exception e) {
		      throw e;
		    } 
	 }
	
	
	

}
