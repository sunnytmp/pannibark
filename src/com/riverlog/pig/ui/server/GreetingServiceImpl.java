package com.riverlog.pig.ui.server;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.pig.PigServer;
import org.apache.pig.piggybank.*;
import org.apache.pig.builtin.*;
import org.apache.pig.impl.*;
import org.apache.pig.impl.builtin.GFAny;
import org.apache.pig.impl.builtin.SampleLoader;
import org.apache.pig.piggybank.evaluation.string.UPPER;

import com.riverlog.pig.ui.client.PigJobExecution;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
		PigJobExecution {
	PigServer pigServer=null;
	String username = null;
	boolean isStore = false;
	static Logger log = Logger.getLogger(GreetingServiceImpl.class.getName());
	public String greetServer(String input) throws IllegalArgumentException {
		log.info("The incoming pig text is ..." + input);
		if (input.toUpperCase().matches("(.*)(&LT)(.*)")){
			input.replace("&lt", "<");
			input.replace("&LT", "<");
		}else if(input.toUpperCase().matches("(.*)(&GT)(.*)")){
			input.replace("&gt", ">");
			input.replace("&GT", ">");
		}
		log.info("The converted pig text is ...."+input);
    	username = input.substring(0,input.indexOf(":"));
    	log.info("The user name retrieved is .." + username);
    	input = input.substring(input.indexOf(":") +1);
		// Verify that the input is valid. 
		if (!FieldVerifier.isValidName(input)) {
			// If the input is not valid, throw an IllegalArgumentException back to
			// the client.
			throw new IllegalArgumentException(
					"Is it really an oink oink? Please check the syntax");
		}
		 log.info("Input got verified...");      
		String serverInfo = getServletContext().getServerInfo();
		String userAgent = getThreadLocalRequest().getHeader("User-Agent");

		// Escape data from the client to avoid cross-site script vulnerabilities.
	//	input = escapeHtml(input);
		userAgent = escapeHtml(userAgent);
		
		StringBuilder executionResult = null;
		input = input.substring(input.indexOf("-")+1);
		isStore = FieldVerifier.checkIfStoreStatement(input);
		log.info("CheckIfStoreStatement returned " + isStore);
		if (isStore){
			/* We have already converted the statement from dump to store. So 
			 * Simply store it first.*/
			//input = FieldVerifier.convertDumpToStore(input,username);
			
		} 
		if (FieldVerifier.checkIfFetchStatement(input.substring(input.indexOf("-")+1))){
			/* Since input contains character "-' with username embedded. Plug out
			 * the actual pig statement. If the dump statement would have been there
			 * then the statement would have been converted above already above the else. */
			
			    StringBuilder fileContent = new StringBuilder( FieldVerifier.fetchFile(input,username));
				executionResult = fileContent;
				log.info("FieldVerifieer.checkIfFetchStatement method got executed and returned true, Sending execution result" + executionResult.toString());
				return executionResult.toString();
				
		} 
		
		
		
			//pigServer.registerQuery("set pig.import.search.path " + 	username );
		/* Now execute the pig latin by calling the executePig method */
		   executionResult = executePig(input);
		 
		   /* Now that the statement if it was Dump or no dump, had been cleanly
		  * executed. send back the result appropriately. Check if dump then read the files
		  * and send back response. We have already stored the file. So now we read all the 
		  * files and send the whole string back to client.*/
		   if (isStore){
		    StringBuilder fileContent = new StringBuilder( FieldVerifier.readDumpFile(input,username));
			executionResult = executionResult.append("\n"+fileContent);
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
	
	
	public StringBuilder executePig(String latin){
		  StringBuilder EXECUTION_RESULT = new StringBuilder("");
		  try{
			  log.info("Entered executePig method..");
			  org.apache.pig.builtin.ABS abs = new ABS();
			  org.apache.pig.impl.builtin.GFAny sl = new GFAny();
			    
		 /*here before execute.. GET LIST OF DIRECTORIES AND DELETE IT ALSO HERE.. systempig*/
			  String[] systempigdirs= FieldVerifier.getListOfDumpDirectories(username);
			  log.info("FieldVerfier.getListOfDumpDirectories returned successfully with " + systempigdirs.length );
		  pigServer = new PigServer("local"); // Or "local" for local mode
		  //System.out.println("A = LOAD '" + "data';");
		  log.info("PigServer got created sucessfully..");
		  if (FieldVerifier.ifRegisterJarThenExecute(username, latin, pigServer)){
				log.debug("This bunch includes Register Jar statement. So registered all Jars. ");
				latin = FieldVerifier.stripOutREGISTERword(latin);
			}
		//Local Specific
		// pigServer.registerJar("/home/ec2-user/jetty-distribution-9.0.4.v20130625/webapps/piggyrun/WEB-INF/lib/piggybank.jar");
		//  pigServer.registerJar("/Users/US-SMenon/Documents/wingrunt/WebGrunt_lib/piggybank.jar"); 

		  log.info("PigServer registered jar");
		 // Now plug in username in front of the statement
		 latin = prependUserName(latin, username);
		  log.info("Latin modified with prepended username..." + latin);
		
		  
		  pigServer.registerQuery(latin );
		  log.info("Pig Server registeredQuery");
		  
		  }catch(Exception pigException){
			 //pigException.printStackTrace();
			 EXECUTION_RESULT= EXECUTION_RESULT.append("Exception "); 
					EXECUTION_RESULT.append(pigException.getMessage()+ pigException.getStackTrace());
					log.info("ERROR-" + pigException.getMessage());
			pigException.printStackTrace();		
		  }
		  if (EXECUTION_RESULT.toString().trim().equals("") || EXECUTION_RESULT.toString().trim().isEmpty() ||
				  EXECUTION_RESULT.toString().length() == 0 ) {
			 // EXECUTION_RESULT = EXECUTION_RESULT.append("EXECUTION SUCCESSFUL!");
			 log.info("Final conditional check before returning the Execution Result which is .." + EXECUTION_RESULT);
		  }
		  return EXECUTION_RESULT;
	  }

	       public  String prependUserName(String orgStr,String username) throws Exception {

	              //do for LOAD

	              String[] commands = orgStr.split(";");

	              int i=0;

	              String fixedStr="";

	              String newStr ="";

	           for (i=0 ;i < commands.length ; i++){

	                  int index =0;

	                  int qindex =0;

	                  fixedStr = commands[i].toUpperCase();

	                  if (fixedStr.contains("LOAD"))

	                            {

	                             index = fixedStr.indexOf("LOAD",index);

	                          qindex = fixedStr.indexOf("'",index);

	                          commands[i] = commands[i].substring(0,qindex+1) +username+"/"+commands[i].substring(qindex+1);

	                            //latin.substring(0,first+1) +username+"/"+latin.substring(first+1);

	            }

	              if (i<=0)  newStr = commands[i]; else newStr = newStr + ";" + commands[i] + ";";

	          }      

	          return newStr;

	       }

	
}
