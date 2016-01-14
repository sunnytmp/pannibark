package com.riverlog.pig.ui.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import com.google.gson.Gson; 
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;




public class GetPigScriptFilesServlet	extends HttpServlet {  
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

		@Override  
		public void doPost(HttpServletRequest request, HttpServletResponse response)  
		throws ServletException, IOException  
		{   
		PrintWriter out = response.getWriter();	
		String username = request.getParameter("username");  //the directories will be under the username  
		
		@SuppressWarnings("rawtypes")
		ArrayList fileList = FieldVerifier.checkIfFilesExist(username);
        
		Gson gson = new Gson();
		String fileListStr = gson.toJson(fileList);
		
		out.write(fileListStr);		  
		}  

}
