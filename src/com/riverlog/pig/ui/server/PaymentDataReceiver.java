package com.riverlog.pig.ui.server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PaymentDataReceiver extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	@Override  
	public void doPost(HttpServletRequest request, HttpServletResponse response)  
	throws ServletException, IOException  
	{   
	String username = request.getParameter("somevalue");  //the directories will be under the username  
	System.out.println("Here is the post value " + username);
		  
	}  
	
	@Override  
	public void doGet(HttpServletRequest request, HttpServletResponse response)  
	throws ServletException, IOException  
	{   
	String username = request.getParameter("somevalue");  //the directories will be under the username  
	System.out.println("Here is the GET value " + username);
		  
	}  


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
