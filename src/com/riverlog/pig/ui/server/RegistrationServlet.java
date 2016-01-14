package com.riverlog.pig.ui.server;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gwt.core.client.GWT;
import com.riverlog.pig.ui.server.businessobjects.PaymentUpdate;


public class RegistrationServlet extends HttpServlet {
   
	private static final long serialVersionUID = 1L;
	private String Licence = null;
	private String fname =null;
	private String lname =null;
	private String username=null;
	private String emailid = null;
	private String confemailid = null;
	private String password = null;
	private String confpassword = null;
	private String country = null;
	private String enterprise = null;
	private String developer = null;
	private String student = null;
	private String agreement = null;
	private String username4email = null;
	private String password4email =null;
	private String payeridt = null;
	private String paymentamountt=null;
	private String subscriptiondurationt=null;
	private String itemamountt=null;
	private String tokent=null;
	private String evalid=null;
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)  
			throws ServletException, IOException  
			{ 
		
			}
	
	@Override  
	public void doPost(HttpServletRequest request, HttpServletResponse response)  
	throws ServletException, IOException  
	{   
	PrintWriter out = response.getWriter();	
	 fname = request.getParameter("firstname");
	 lname = request.getParameter("lastname");  
	 emailid = request.getParameter("emailid");
	 confemailid = request.getParameter("confemailid");
	 password = request.getParameter("password");
	 confpassword = request.getParameter("confpassword");
	 country = request.getParameter("choice");
	 
     paymentamountt= request.getParameter("paymentamountt");
     subscriptiondurationt= request.getParameter("subscriptiondurationt");	
     itemamountt = request.getParameter("itemamountt");
     tokent = request.getParameter("tokent");
     payeridt = request.getParameter("payeridt");
	 
	// enterprise = request.getParameter("license");
	 
	 agreement = request.getParameter("agreement");
	 evalid = request.getParameter("evalid");
	 EmailPigScripts emailpigscript = new EmailPigScripts();
   
	String resultOnUpdateAndEmail=null;
	try {
		 resultOnUpdateAndEmail = verifyLogin(null);
		if(resultOnUpdateAndEmail.equalsIgnoreCase("SuccessFul")) {
		 emailpigscript.main1(null, emailid.toString().trim() , emailid.toString(), "Thank you. Your Registration Details below: Your username is " + username4email + "and your password is: " + confpassword +
				 "Please remember that the password is casesensitive.", "Registration Confirmation Email",null);
		}
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	//request.getRequestDispatcher("../Login.html?name=sunny&country=usa").forward(request, response);
	//response.sendRedirect("../Login.html?name=sunny&country=usa");
	 // out.write("Email Sent to email ID " + fname + emailid +":" + password + ":" + enterprise);	
	if(resultOnUpdateAndEmail.equalsIgnoreCase("SuccessFul")) {
	  out.write("<html>");
      out.write("<body bgcolor=\"white\">");
      out.write("<head>");

	out.write("<title> Registration Successful! </title>");
      out.write("</head>");
      out.write("<body>");

	out.write("<h1>Your Registration was successfull!</h1>");
	out.write("Please login to the email account provided during registration and get your UserName password and login in to the system!" +
			"Once again, thank you and congratulations!<BR>Go Back to login page. <a href="+getBaseUrl(request)+"/Login.html>Click here for Login Page</a> .<BR> The Team @ Webgrunt");
	}else{
		  out.write("<html>");
	      out.write("<body bgcolor=\"blue\">");
	      out.write("<head>");

		out.write("<title> Registration Attempt Message! </title>");
	      out.write("</head>");
	      out.write("<body>");

		out.write("<h1>Your Registration was NOT successfull!</h1>");
		out.write("Perhaps you entered an email that already existed. Please try with another email and try register again. Click the Back <BR>" +
				"button in your browser.");
		
	}
	}
	public static String getBaseUrl( HttpServletRequest request ) {
	    if ( ( request.getServerPort() == 80 ) ||
	         ( request.getServerPort() == 443 ) ) {
	      return request.getScheme() + "://" +
	             request.getServerName() +
	             request.getContextPath();
	    } else {
	      return request.getScheme() + "://" +
	             request.getServerName() + ":" + request.getServerPort() +
	             request.getContextPath();
	    }
	}
	/**
	 * 
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
		            	 success  = updateRegistration(con,fname,lname, password, emailid, Licence,country,evalid);
		            }
		            if(success.equalsIgnoreCase("SuccessFul")){
		              return success ;
		            }
		  
		  
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
	public String updateRegistration(Connection con, String fname,String lname, String password, String emailid, String licence, String country, String evalid) {
		Statement stmt;
		try {
			stmt = con.createStatement();
			// * from users where username='"+ username +"' and password=" + "MD5('"+password+"')";
			 //Now generate unique userid code
			boolean isEmailExisting = checkIfEmailExistsInDB(con, emailid);
			if (isEmailExisting && payeridt == null && payeridt.isEmpty()){
				//Here the email exists and the user has registered earlier for EVALUATION
				// version then his condition becomes true because NO PAYER ID.
				//So he has clicked TRIAL button
				return "EmailExists";
			}
		    //Here even though the user is coming back and his email exists
			// A new username is created and therefore new update will be done.
			// He or she can register using the same email id.
			String uniqueUserName =  generateUserName(con, fname,lname);
		    username4email = uniqueUserName;
		    //set the isuserevaluating variable to 1 or 0. 1 means evaluating, 0 means proper registration.
		    //Either case only one email can be utilized.
		    int isuserevaluating = 0;
		    if (evalid.trim().equals("true")){
		    	isuserevaluating= 1;
		    } else{
		    	isuserevaluating = 0;
		    }
			String query = "insert into users (firstname, lastname, username,password,email,license, country, isuserevaluating) VALUES(" + "'" + fname + "'," + "'" + lname + "'," + "'" + uniqueUserName + "'," +"MD5('" + password + "')," +
	    		  
	    		  "'" + emailid + "'," + "'" + "Allow" + "'," + "'" + country + "'" + isuserevaluating +")";
	      boolean rs =  stmt.execute(query);
	      	//Now update payment information received by calling paymentUpdate.
	      //At this point the user has already been created in the DB and now just update with payment info.
	      PaymentUpdate paymentupdate = new PaymentUpdate();
	      if (evalid != null && !evalid.isEmpty() ){
	    	  subscriptiondurationt = "7";
	      }
	      paymentupdate.InitializePaymentVariable(uniqueUserName, payeridt, paymentamountt, subscriptiondurationt, itemamountt, tokent);
	      
	      //This means that the updation was successful and so do anythere here as a part of initiatlization if required.
	      return "SuccessFul";
	      
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "NothingCameThrough";
		}
		
		
	}
	public String generateUserName(Connection conn, String finame, String liname){
		Statement stmt;
		String username_i = finame + liname.substring(0,1); 
		try {
			stmt = conn.createStatement();
			// * from users where username='"+ username +"' and password=" + "MD5('"+password+"')";
	      String query = "select username from users where username = " + "'" + username_i.toString().trim() + "'" ;
	      System.out.println(query);
	      ResultSet rs =  stmt.executeQuery(query);
	     
	   //This means that the updation was successful and so do anythere here as a part of initiatlization if required.
	    //Now generate unique userid code
	     while (rs.next()){
	    	String query2 = "select count (*) from users where username like " + "'" + username_i.toString().trim() + "%'" ;
	    	ResultSet rs2 =  stmt.executeQuery(query);
	    	int numberofsimilaruserids =0;
	    	while(rs2.next()) {
	    		//here get the record count
	    		rs2.last();
	    		numberofsimilaruserids = rs2.getRow();
	    		rs2.beforeFirst();
	    		
	    	}
	    	if (numberofsimilaruserids == 0){
	    		rs=null;
	    		rs2=null;
	    		username4email = username_i;
	    	  return username_i;
	    	}
	    	rs=null;
    		rs2=null;
    		username4email = username_i + numberofsimilaruserids+1;
    		username4email = username_i + numberofsimilaruserids+1;
	    	return username_i + numberofsimilaruserids+1;
	     }
	      
	      
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "NothingCameThrough";
		}
		
		
		  return username_i;
	}
	
	public boolean checkIfEmailExistsInDB(Connection conn, String emailid) {
		Statement stmt;
		 
		try {
          stmt = conn.createStatement();
	      String query = "select email from users where email = " + "'" + emailid.toString().trim() + "'" ;
	      System.out.println(query);
	      ResultSet rs =  stmt.executeQuery(query);
	     
	   //This means that the updation was successful and so do anythere here as a part of initiatlization if required.
	    //Now generate unique userid code
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


