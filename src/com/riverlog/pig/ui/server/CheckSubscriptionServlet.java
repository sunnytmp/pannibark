package com.riverlog.pig.ui.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

public class CheckSubscriptionServlet extends HttpServlet {
   
	private static final long serialVersionUID = 1L;

	@Override  
	public void doPost(HttpServletRequest request, HttpServletResponse response)  
	throws ServletException, IOException  
	{   
	String noOfDaysRemaining = null;
	PrintWriter out = response.getWriter();	
	String username = request.getParameter("username");  //the directories will be under the username  

	try {
		//Fire db command to check susbcription
		CheckSubscriptionValidityDB chdb = new CheckSubscriptionValidityDB();
		noOfDaysRemaining = chdb.verifyLogin(username);

	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		out.write("Validity Check Failed.. Please see if your subscription has expired.");	
		return;
	}
		out.write(noOfDaysRemaining);		  
	}  
	
}


