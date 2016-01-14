package com.riverlog.pig.ui.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.riverlog.pig.ui.server.EmailPigScripts;
import com.google.gson.Gson;

public class SaveScriptServlet extends HttpServlet {
   
	private static final long serialVersionUID = 1L;

	@Override  
	public void doPost(HttpServletRequest request, HttpServletResponse response)  
	throws ServletException, IOException  
	{   
	PrintWriter out = response.getWriter();	
	String username = request.getParameter("username");  //the directories will be under the username  
	
	String scriptFileName = request.getParameter("scriptFileName");
	
	//EmailPigScripts emailpigscript = new EmailPigScripts(); 
	String[] datafiles = new String[1];
	StringBuilder theScript = new StringBuilder(request.getParameter("theScript"));
	
//	FieldVerifier.savePigScriptFile(username, scriptFileName, theScript);
	try {
		//save file method
		
		FieldVerifier.savePigScriptFile(username, scriptFileName, theScript);
		
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	out.write("Script File Save " + "successFul");		  
	}  
	
}


