package com.riverlog.pig.ui.server;

import java.io.File;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.pig.PigServer;
import org.apache.pig.piggybank.*;
import org.apache.pig.builtin.*;
import org.apache.pig.impl.*;
import org.apache.pig.impl.builtin.GFAny;
import org.apache.pig.impl.builtin.SampleLoader;
import org.apache.pig.piggybank.evaluation.string.UPPER;

import com.mysql.jdbc.PreparedStatement;
import com.riverlog.pig.ui.client.LogJobExecution;
import com.riverlog.pig.ui.client.PigJobExecution;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**         
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class LoginServiceImpl extends RemoteServiceServlet implements
		LogJobExecution {

	public String greetServer(String input) throws IllegalArgumentException {
		// Verify that the input is valid. 
	 
		String serverInfo = getServletContext().getServerInfo();
		String userAgent = getThreadLocalRequest().getHeader("User-Agent");

		// Escape data from the client to avoid cross-site script vulnerabilities.
		input = escapeHtml(input);
		userAgent = escapeHtml(userAgent);
		
		String executionResult = null;

		executionResult = verifyLogin(input);
		//return "Hello, " + input + "!<br><br>I am running " + serverInfo
			//	+ ".<br><br>It looks like you are using:<br>" + userAgent;
		int usernamedelimiter = input.indexOf(":");
   	    String username = input.substring(0,usernamedelimiter);
		if (executionResult != null && executionResult.trim().equals("SuccessFul")){
			FieldVerifier.makeExampleDataForUser(username);
		}
		return executionResult.toString();
	}

	/**
	 * Escape an html string. Escaping data received from the client helps to
	 * prevent cross-site script vulnerabilities.
	 * 
	 * @param html the html string to escape
	 * @return the escaped string
	 */
	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;");
	}
	
	
	public String verifyLogin(String latin){
		  String EXECUTION_RESULT = new String();
		  String username = null;
		  String userpwd= null;
		  Connection con;
		  String success = null;
			  
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
		            	 username = latin.substring(0,usernamedelimiter);
		            	userpwd = latin.substring(usernamedelimiter+1);
		            	 success  = checkUserCredentials(con,username, userpwd);
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
	
	public String checkUserCredentials(Connection con, String username, String password) {
		Statement stmt;
		try {
			stmt = con.createStatement();
	      String query = "select * from users where username='"+ username +"' and password=" + "MD5('"+password+"')";
	      System.out.println(query);
	      ResultSet rs =  stmt.executeQuery(query);
	      
	      while (rs.next()) {
	     /** Create space <User Directory> for this user */
	    	  File directory = new File(username);
	    	  directory.mkdirs();
	         return "SuccessFul";
	      } 
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "NothingCameThrough";
		
	}
}
