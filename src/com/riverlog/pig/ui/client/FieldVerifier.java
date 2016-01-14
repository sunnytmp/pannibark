package com.riverlog.pig.ui.client;



import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.google.gwt.dev.asm.Label;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * <p>
 * FieldVerifier validates that the name the user enters is valid.
 * </p>
 * <p>
 * This class is in the <code>shared</code> package because we use it in both
 * the client code and on the server. On the client, we verify that the name is
 * valid before sending an RPC request so the user doesn't have to wait for a
 * network round trip to get feedback. On the server, we verify that the name is
 * correct to ensure that the input is correct regardless of where the RPC
 * originates.
 * </p>
 * <p>
 * When creating a class that is used on both the client and the server, be sure
 * that all code is translatable and does not use native JavaScript. Code that
 * is not translatable (such as code that interacts with a database or the file
 * system) cannot be compiled into client side JavaScript. Code that uses native
 * JavaScript (such as Widgets) cannot be run on the server.
 * </p>
 */
public class FieldVerifier {

	/**
	 * Verifies that the specified name is valid for our service.
	 * 
	 * In this example, we only require that the name is at least four
	 * characters. In your application, you can use more complex checks to ensure
	 * that usernames, passwords, email addresses, URLs, and other fields have the
	 * proper syntax.
	 * 
	 * @param name the name to validate
	 * @return true if valid, false if invalid
	 */
	public static boolean isValidName(String name) {
		if (name == null) {
			return false;
		}
		return name.length() > 3;
	}
	public static boolean isDumpCommand(String statement) {
		statement = statement.replace("\n", "");
		if (statement.toUpperCase().matches("(.*)DUMP(.*)") ) {
			 return true;
		   } else {
			   return false;
		   }
	    	  
	}
	public static boolean checkIfFetchStatement(String statement){
		statement = statement.replace("\n", "");
		if (statement.toUpperCase().matches("(.*)FETCH(.*)") ) {
			 return true;
		   } else {
			   return false;
		   }
	}
	
	public static String convertDumpToStore(String scriptCommands,String username){
		 String alltokensfull = new String("");
		try{
		scriptCommands = scriptCommands.replace("\n", "");
		String[] alltokens = null;
       
        String splittedColls[] = scriptCommands.split(";");
        int fileXtn = 0;
        for (int i = 0;i<= splittedColls.length-1;i++){
        	if (splittedColls[i].toUpperCase().matches("(.*)DUMP(.*)")){
        		fileXtn = fileXtn +1 ;
        		String replacedString = replaceDumpWithStore(splittedColls[i].toString());
        		alltokensfull = alltokensfull + ";" + replacedString;
        		alltokensfull = alltokensfull + " INTO '" + username + "/systempig" + fileXtn + "';" ;
        	//	Window.alert("Returned the whole string from replaceDump.... What is here now ?  " + splittedColls[i]);
        	}else {
        		if (i==0){
        		alltokensfull = alltokensfull + splittedColls[i].toString();
        		}else {
        			alltokensfull = alltokensfull + ";" + splittedColls[i].toString();
        		}
        	}
        }
       // Window.alert("Here is the all string..." + alltokensfull);
      }catch(Exception ets){
    	  ets.printStackTrace();
      }
		return alltokensfull;
    }

	public static String replaceDumpWithStore(String theStringToMatch){
		String replacedString = new String("");
		replacedString = "Store "+ theStringToMatch.substring(4);
		return replacedString;
	}
	
	public static boolean addItemToText(ListBox listitem, String item) {
		boolean found = false;
		if (listitem.getItemCount() == 0){
			listitem.addItem(item.substring(item.indexOf("/")+1));
			return true;
		}
		for (int i = 0;i<=listitem.getItemCount()-1;i++){
			if (listitem.getItemText(i).toUpperCase().equalsIgnoreCase(item.substring(item.indexOf("/")+1))) {
				// do nothing
				found = true;
			}	
		}
		if (!found){
			listitem.addItem(item.substring(item.indexOf("/")+1));
		}
		return true;
	}
	
	
	public static boolean checkIfStoreStatement(String statement){
		statement = statement.replace("\n", "");
		statement = statement.toUpperCase();
		if (statement.matches("(.*)(STORE)(.*)") ) {
			return true;
	   } else {
		   return false;
		
	   }
	}
	
	public static String getFileName(String statement){
		statement = statement.replace("\n", "");
		
		String fname = null;
		ArrayList<String> matches = new ArrayList<String>();
		RegExp pattern = RegExp.compile("('(.*?)')", "g");
		for (MatchResult result = pattern.exec(statement); result != null; result = pattern.exec(statement)) {
		   matches.add(result.getGroup(0));
		   fname = result.getGroup(0);
		}
		return fname.replaceAll("'", "");
	}
    
	public static void populateListBox(StringBuilder pigScriptFiles, ListBox listbox, String username){
		String files = null;
		String[] fileListRetrieved = pigScriptFiles.toString().split(",");
		
		for (int i=0;i<=fileListRetrieved.length-1;i++){
		    files = fileListRetrieved[i].toString();
		    files = files.replace("[","");
		    files = files.replace("]","");
		    files = files.replace("\"", "");
		    if (!files.trim().substring(0,8).equals("systempig")){
		     listbox.addItem(files);
		    }
		}
	}
	
}
