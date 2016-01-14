package com.riverlog.pig.ui.server.dataobjects;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PaymentUpdate {
String payerid = null;
String paymentamount = null;
String subscriptionduration =null;
String itemamount = null; 
String token = null;
String uniqueUserName = null;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public void InitializePaymentVariable(String UniqueUserName,String payeridt, String paymentamountt, String subscriptiondurationt, String itemamountt, String tokent){
		payerid = payeridt;
		paymentamount = paymentamountt;
		subscriptionduration = subscriptiondurationt;
		itemamount = itemamountt;
		token = tokent;
		uniqueUserName = UniqueUserName;
		ConnectToDbAndUpdate();
	}
	public String ConnectToDbAndUpdate(){
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
		            	 success  = updateRegistration(con, uniqueUserName, payerid, paymentamount, subscriptionduration, itemamount, token);
		            }
		            if(success.equalsIgnoreCase("SuccessFul")){
		              return success ;
		            }
		  
		  
		  }catch(Exception connectException){
			 //pigException.printStackTrace();
			 EXECUTION_RESULT= EXECUTION_RESULT + "Exception  "; 
			 EXECUTION_RESULT =	EXECUTION_RESULT +connectException.getMessage();
			 connectException.printStackTrace();
		  }
		  if (EXECUTION_RESULT.toString().trim().equals("") || EXECUTION_RESULT.toString().trim().isEmpty() ||
				  EXECUTION_RESULT.toString().length() == 0 ) {
			  EXECUTION_RESULT = EXECUTION_RESULT + "EXECUTION SUCCESSFUL!";
		  }
		  return EXECUTION_RESULT;
	}
	
	public String updateRegistration(Connection con,String username, String payerid,String paymentamount, String subscriptionduration, String itemamount, String token) {
		Statement stmt;
		try {
			stmt = con.createStatement();
			// * from users where username='"+ username +"' and password=" + "MD5('"+password+"')";
			 //Now generate unique userid code
			boolean isPayerIDExisting = checkIfPayerIDExistsInDB(con, payerid);
			String query = null;
		//	if (isPayerIDExisting){
				//Update on payerid
				 query = "update users set PAYERID=" + "'" + payerid +"'," +" PAYERAMOUNT="+ "'" + paymentamount + "'," +" SUBSCRIPTIONDURATION=" + "'" + subscriptionduration + "'," + " ITEMAMOUNT=" + "'" + itemamount + "'," + " TOKEN=" + "'" + token + "'," + 
					" dateRegistered= current_date " + 	" WHERE " + " USERNAME=" + "'" + username + "'" ;
				
		//	}else{
		//		return "UnSuccessFul Payment";
		//	}
	   
	      boolean rs =  stmt.execute(query);
	   //This means that the updation was successful and so do anythere here as a part of initiatlization if required.
	      return "SuccessFul";
	      
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "NothingCameThrough";
		}
		
		
	}
	public boolean checkIfPayerIDExistsInDB(Connection conn, String payerid) {
		Statement stmt;
		 
		try {
          stmt = conn.createStatement();
          
	      String query = "select payerid from users where payerid = " + "'" + payerid.toString().trim() + "'" ;
	      System.out.println(query);
	      ResultSet rs =  stmt.executeQuery(query);
	     
	  //This means that payer id exists if true. So retrun true .. if not return  false
	     while (rs.next()){
	    	rs=null;
 
	    	return true;
	     }
	    
	      
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		 return false;
		
		
	}
}
