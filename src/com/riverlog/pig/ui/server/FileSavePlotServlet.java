package com.riverlog.pig.ui.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

public class FileSavePlotServlet extends HttpServlet {
   
	private static final long serialVersionUID = 1L;

	@Override  
	public void doPost(HttpServletRequest request, HttpServletResponse response)  
	throws ServletException, IOException  
	{   
	PrintWriter out = response.getWriter();	
	String username = request.getParameter("username");  //the directories will be under the username  
	
	String plotFileName = request.getParameter("plotFileName");
	
 

	StringBuilder theData = new StringBuilder(request.getParameter("theData"));

	try {
		//save file method
		FieldVerifier.barPloterPDF(username, plotFileName, theData);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		out.write("Script File Save " + "Failed...");
		return;
	}
		out.write("Script File Save " + "successFul");		  
	}  
	
}


