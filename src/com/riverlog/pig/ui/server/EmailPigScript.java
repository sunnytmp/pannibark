package com.riverlog.pig.ui.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

public class EmailPigScript extends HttpServlet {
   
	private static final long serialVersionUID = 1L;

	@Override  
	public void doPost(HttpServletRequest request, HttpServletResponse response)  
	throws ServletException, IOException  
	{   
	PrintWriter out = response.getWriter();	
	String username = request.getParameter("username");  //the directories will be under the username  
	String toEmail = request.getParameter("toEmail");
	String dataFileName = request.getParameter("dataFileName");
	
	EmailPigScripts emailpigscript = new EmailPigScripts();   
	String[] datafiles = new String[1];
	datafiles[0] = dataFileName;
	try {
		emailpigscript.main1(datafiles, toEmail.toString().trim() , toEmail.toString(), "Please see your attached file", "Pig File From WebGrunt",username);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	out.write("Email Sent to email ID " + toEmail);		  
	}  
	
}


