package com.riverlog.pig.ui.server;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CheckSubscriptionValidityDB {

	public String verifyLogin(String latin){
		  String EXECUTION_RESULT = new String();
		  String username = null;
		  String userpwd= null;
		  Connection con;
		  String success = null;  // This is the number of days remaining.
			  
			  try
		        {
		            String host          = "jdbc:mysql://localhost:3306/";
		            String db           = "pig_gwt_3";
		            String driver       = "com.mysql.jdbc.Driver";
		            String user         = "root";
		            String pass         = "pwd";

		            Class.forName(driver).newInstance();
		            con = DriverManager.getConnection(host+db, user, pass);

		         
		            if (con != null || !con.equals(null)){
		            	int usernamedelimiter = latin.indexOf(":");
		            	if (usernamedelimiter >0){   //This colon will not be there because
		            		// there is no password. It is only for password. Let this be here for
		            		//future use may be.
		            		 username = latin.substring(0,usernamedelimiter);
		            	 userpwd = latin.substring(usernamedelimiter+1);
		            	}else{
		            		 username = latin.trim();
		            	}
		            	 
		            	 success  = checkUserSubscriptionValidityDB(con,username, userpwd);
		            }
		            
		            return success ;
		  
		  
		  }catch(Exception pigException){
			 //pigException.printStackTrace();
			 EXECUTION_RESULT= EXECUTION_RESULT + "Exception  "; 
			 EXECUTION_RESULT =	EXECUTION_RESULT + pigException.getMessage();
			 pigException.printStackTrace();
		  }
		  if (EXECUTION_RESULT.toString().trim().equals("") || EXECUTION_RESULT.toString().trim().isEmpty() ||
				  EXECUTION_RESULT.toString().length() == 0 ) {
			  EXECUTION_RESULT = EXECUTION_RESULT + "EXECUTION SUCCESSFUL!";
		  }
		  return EXECUTION_RESULT;
	}
	
	public String checkUserSubscriptionValidityDB(Connection con, String username, String password) {
		Statement stmt;
		try {
			stmt = con.createStatement();
	      String query = "select subscriptionduration, datediff(curdate(),dateRegistered) as daysRemaining from users where username = " + "'" + username + "'" +
			" and isuserevaluating=1"; 

	    //  String query2 = "select subscriptionduration from users where username = " + "'" + username + "'" ;
	      ResultSet rs =  stmt.executeQuery(query);
	   //   ResultSet rs2 = stmt.executeQuery(query2);
	     while (rs.next()) {
	    	 int dateDiff = new Integer(rs.getString("daysRemaining")).intValue();
	    	 int subscriptionDuration = new Integer(rs.getString("subscriptionduration")).intValue();
	    	 switch(subscriptionDuration){
	    	 case 7:
	    		 //This is evaluation for 30 days  free
	    		 if (dateDiff > 30){
	    			 return "subscriptionEnded";
	    		 } else{
	    			 return "subscriptionValid";
	    		 }
	    	 case 1:
	    		 //one months
	    		 if (dateDiff > 30){
	    			 return "subscriptionEnded";
	    		 }else{
	    			 return "subscriptionValid";
	    		 }
	    	 case 3:
	    		 //three months
	    		 if (dateDiff > 90){
	    			 return "subscriptionEnded";
	    		 }else{
	    			 return "subscriptionValid";
	    		 }
	    	 case 4:
	    		 if (dateDiff > 120){
	    			 return "subscriptionEnded";
	    		 }else{
	    			 return "subscriptionValid";
	    		 }
	    	 }
	    	 //return rs.getString("daysRemaining");
	      } 
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "SomethingWrongWithDBAccess";
		
	}
	
}
