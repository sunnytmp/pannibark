package com.riverlog.pig.ui.server;

import java.io.BufferedReader;
import java.io.File; 
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.apache.pig.PigServer;

import com.google.gwt.user.client.ui.ListBox;

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
	 * 
	 * 
	 * @param name the name to validate
	 * @return true if valid, false if invalid
	 */
	static Logger log = Logger.getLogger(FieldVerifier.class.getName());
	
	public static boolean ifRegisterJarThenExecute(String username , String statement, PigServer pigServer){
		
		statement = statement.replace("\n", "");
		if (statement.toUpperCase().matches("(.*)REGISTER(.*)") ) {
		log.debug("It is a REGISTER statement, send it to register method");
		String statements[] = statement.split(";");
		for (int i = 0;i<= statements.length-1; i++){
			if (statements[i].trim().toUpperCase().matches("(.*)REGISTER(.*)") ){
				String jarPath = statements[i].toString().trim().substring(8);
				log.info("Path for the jar to be registered is " + jarPath);
				registerJar(jarPath,pigServer);
			}
		}
		 return true;
	   } else {
		   log.debug("NOT A REGISTER STATEMENT.....");
		   return false;
	   }
		
	}
	public static boolean registerJar(String jarPath, PigServer pigServer){
		try {
			pigServer.registerJar(jarPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	public static String stripOutREGISTERword(String input){
		String strippedOutRegisterStr = new String("");
		String[] splitInput = input.split(";");
		for (int i = 0;i <= splitInput.length-1;i++){
			if (splitInput[i].trim().toUpperCase().matches("(.*)REGISTER(.*)") ){
				//Don't put that into the array.
			}else{
				if (strippedOutRegisterStr != null && !strippedOutRegisterStr.isEmpty() &&
				       !strippedOutRegisterStr.trim().equals("") ) {
				 strippedOutRegisterStr = strippedOutRegisterStr + splitInput[i].toString().trim() + ";"; 		
				}else{
					strippedOutRegisterStr =  splitInput[i].toString().trim() + ";";
				}
			}
		}
		log.info("The stripped out latin looks like this: " + strippedOutRegisterStr);
		return strippedOutRegisterStr;
	}
	
	public static boolean makeExampleDataForUser(String username){
		InputStream inStream = null;
		OutputStream outStream = null;
		try {
		     	    File afile =new File("/home/ubuntu/application.log");
		    	    File bfile =new File("/home/ubuntu/jetty-distribution-8.1.17.v20150415/"+ username + "/application.log");
		 
				    inStream = new FileInputStream(afile);
		    	    outStream = new FileOutputStream(bfile);
		 
		    	    byte[] buffer = new byte[1024];
		 
		    	    int length;
		    	    //copy the file content in bytes 
		    	    while ((length = inStream.read(buffer)) > 0){
		    	    	outStream.write(buffer, 0, length);
		    	    }
		    	    inStream.close();
		    	    outStream.close();	    
		 
		   } catch (IOException e) {
			    e.printStackTrace();
			}
	
		return true;
	}
	public static boolean isValidName(String name) {
		if (name == null) {
			return false;
		}
		return name.length() > 3;
	}
	
	public static boolean addItemToText(ListBox listitem, String item) {
		
		listitem.addItem(item);
		return true;
	}
	     
	public static String[] getListOfDumpDirectories(String username){
		/* DELETING FILES RIGHT HERE TOO */
		File file = new File(username);
		String[] directories = file.list(new FilenameFilter() {
		  @Override
		  public boolean accept(File dir, String name) {
		    return new File(dir, name).isDirectory();
		  }
		});
		String path = new File("").getAbsolutePath();


		File systempigfile = null;
		for(int i = 0;i<=directories.length-1;i++){
			systempigfile = new File(path+"/"+ username + "/" + directories[i].toString()); 
			if (directories[i].matches(("(.*)(systempig)(.*)") )){
			//	File directory = new File(directories[i]);
				 
		    	//make sure directory exists
		    	if(!systempigfile.exists()){
		 
		           System.out.println(systempigfile+ "Directory does not exist.");
		          // System.exit(0);
		 
		        }else{
		 
		           try{
		 
		               delete(systempigfile);
		 
		           }catch(IOException e){
		               e.printStackTrace();
		               System.exit(0);
		           }
		        }
				// Files.deleteIfExists(new Path(systempigfile.getAbsolutePath())); 
				//systempigfile.setWritable(true);
				//systempigfile.delete();
		    }
		}
		
		return directories;
	}
	
	public static void delete(File file)
	    	throws IOException{
	 
	    	if(file.isDirectory()){
	 
	    		//directory is empty, then delete it
	    		if(file.list().length==0){
	 
	    		   file.delete();
	    		   System.out.println("Directory is deleted : " 
	                                                 + file.getAbsolutePath());
	 
	    		}else{
	 
	    		   //list all the directory contents
	        	   String files[] = file.list();
	 
	        	   for (String temp : files) {
	        	      //construct the file structure
	        	      File fileDelete = new File(file, temp);
	 
	        	      //recursive delete
	        	     delete(fileDelete);
	        	   }
	 
	        	   //check the directory again, if empty then delete it
	        	   if(file.list().length==0){
	           	     file.delete();
	        	     System.out.println("Directory is deleted : " 
	                                                  + file.getAbsolutePath());
	        	   }
	    		}
	 
	    	}else{
	    		//if file, then delete it
	    		file.delete();
	    		System.out.println("File is deleted : " + file.getAbsolutePath());
	    	}
	    }
	
	
	public static boolean checkIfLoadStatement(String statement){
		statement = statement.replace("\n", "");
		if (statement.toUpperCase().matches("(.*)STORE(.*)") ) {
			System.out.println("True:" + statement.toUpperCase());
		 return true;
	   } else {
		   System.out.println("False:" + statement.toUpperCase());
		   return false;
	   }
	}
	
	public static List getFileNames(String statement){
		statement = statement.replace("\n", "");
		String[] statementSplits = statement.split(";");
		List<String> systempig1FileNames = new ArrayList<String>();
		for (int i = 0; i<= statementSplits.length-1;i++){
			if (statementSplits[i].toString().toUpperCase().matches("(.*)STORE(.*)")){
				systempig1FileNames.add(statementSplits[i]);
			}
		}
		return systempig1FileNames;
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
	public static boolean checkIfFetchStatement(String statement){
		statement = statement.replace("\n", "");
		if (statement.toUpperCase().matches("(.*)FETCH(.*)") ) {
			 return true;
		   } else {
			   return false;
		   }
	}

	public static String readDumpFile(String statement,String username){
	  log.info("Entering readDumpFile routine");	
	  log.info("The inputs here are ...." + statement + "-----" + username);
		int fileXtns = 0;
		BufferedReader br = null;
	   	String strLineFull=null;
	   	List fnames = getFileNames(statement);  //You got the file name from input statement
	   	/* This is an array of files after splitting semi colon */
	   	
	   	/* now get all files in the systempig1 director */
	   	boolean isItFile = false;

		for (int i = 0;i<=fnames.size()-1;i++ ){
			fileXtns = i+1;
	   	/**************************************************************************/
	   // isItFile = new File(fnames.get(i).toString()).isFile();
		File syspig = new File(username+"/systempig"+fileXtns); 
		log.info("Created syspig object..");
	   	try{
	   		/* Check first if it is a data file converted by STORE command. If NOT then
	   		 * see if it is a .log file. If .log file then stream it */
	   		//String extensionFileName = fnames.get(i).toString().substring(fnames.get(i).toString().lastIndexOf(".")+1,fnames.get(i).toString().length());
	   	
	   	      /* Now this is a file stored by pig Store command.*/
	   		
	   		File f = new File(syspig +"/part-m-00000");
	   	 
	  	  if(f.exists()){
	  		br = new BufferedReader(new FileReader(syspig +"/part-m-00000"));
	  		log.info("It is a M object...");
	  	  }else{
	  		br = new BufferedReader(new FileReader(syspig +"/part-r-00000"));
	  		log.info("It is a R object...");
	  	  }
	   	      
	   	    String strLine ;
	   	    //Read File Line By Line
	   	    while ((strLine = br.readLine()) != null)   {
	   	    	log.info("Inside the while loop...");
	   	      // Print the content on the console
	   	     if (strLineFull != null){
	   	      strLineFull = strLineFull + strLine + System.getProperty("line.separator");;
	   	     }else{
	   	    	 strLineFull =  strLine + System.getProperty("line.separator");
	   	     }
	   	    }
	   	    log.info("Closing the  file object.");
	   	    //Close the input stream
	   	    br.close();
	   	    log.info("Closed file succesfully...");
	   	    }catch (FileNotFoundException e){//Catch exception if any
	   	      System.err.println("Error: " + e.getMessage());
	   	    }catch (IOException ioe){
	   	    	ioe.printStackTrace();
	   	    }
	   	    
	   	     finally {
	   	      try {
	   	          if (br != null) {
	   	              br.close();
	   	          }
	   	      } catch (IOException e) {
	   	      }
	   	     
	   	    }
		}
		if ( strLineFull !=null && strLineFull.equals(null) && strLineFull.isEmpty()){
			log.info("The result set is empty");
			return "Empty ResultSet";
		}else{
		  log.debug("The content of string of file is..."+ strLineFull);
		  // Removed Dump
	   	  return  strLineFull  ;
		}
	}
	
	public static String fetchFile(String statement,String username){
		BufferedReader br = null;
	   	String strLineFull=null;
	   	boolean isItFile = false;
	    statement = statement.trim();
	  // 	statement = statement.trim().substring(6); //Fetch + 1 space
	   	log.info("Inside the fetch method..." + statement);		
	   	if (statement.trim().toUpperCase().contains("FETCH")){
 			  statement = statement.trim().substring(6);
 			}	
	   	/**************************************************************************/
	    isItFile = new File(username + "/" + statement).isFile();
	   	try{
	   		/* Check first if it is a data file converted by STORE command. If NOT then
	   		 * see if it is a .log file. If .log file then stream it */
	   		if (isItFile){
	   		  log.info("Type Chosen is FILE confirmed..");
	   		  br = new BufferedReader(new FileReader(username + "/" + statement));	
	   		}else {	
	   	      /* Now this is a file stored by pig Store command.*/
	   			/* and so take out the 'FETCH" word from the whole string. */
	   			if (statement.trim().toUpperCase().contains("FETCH")){
	   			  statement = statement.trim().substring(6);
	   			}
	   		
	   			File f = new File(username+"/"+statement +"/part-m-00000");
	   	   	 
	  	  	  if(f.exists()){
	  	  		br = new BufferedReader(new FileReader(username+"/"+statement +"/part-m-00000"));
	  	  		log.info("It is a M object inside the fetch...");
	  	  	  }else{
	  	  		br = new BufferedReader(new FileReader(username+"/"+statement +"/part-r-00000"));
	  	  		log.info("It is a R object inside the fetch....");
	  	  	  }	
	   	     // br = new BufferedReader(new FileReader(username+"/"+statement+"/part-m-00000"));
	   		}
	   	    String strLine ;
	   	    //Read File Line By Line
	   	    while ((strLine = br.readLine()) != null)   {
	   	      // Print the content on the console
	   	     if (strLineFull != null){
	   	      strLineFull = strLineFull + strLine + System.getProperty("line.separator");;
	   	     }else{
	   	    	 strLineFull = strLine + strLine + System.getProperty("line.separator");
	   	     }
	   	    }
	   	    
	   	    //Close the input stream
	   	    br.close();
	   	    }catch (FileNotFoundException e){//Catch exception if any
	   	      System.err.println("Error: " + e.getMessage());
	   	    }catch (IOException ioe){
	   	    	ioe.printStackTrace();
	   	    }
	   	    
	   	     finally {
	   	      try {
	   	          if (br != null) {
	   	              br.close();
	   	          }
	   	      } catch (IOException e) {
	   	    	  e.printStackTrace();
	   	      }
	   	     
	   	   
		}
	   	return strLineFull;
	}
	
	public static ArrayList checkIfFilesExist(String username) {
		 File theDir = new File(username);
		 ArrayList<String> fileList = new ArrayList<String>();
		  // if the directory does not exist uplift all files in an array
		  if (theDir.exists())
		  {
			  File[] allPigFiles = theDir.listFiles();

			  for (File file : allPigFiles) {
			      
			         fileList.add(file.getName().trim());
			      
			  } 
		  }
		 
		return fileList;
	}
	
	public static boolean  savePigScriptFile(String username, String fileName, StringBuilder theScript){
	
		File directory = new File("/home/ubuntu/jetty-distribution-8.1.17.v20150415/webapps/war/" + username  + "/data/" ) ;
	    directory.canWrite(); 
		directory.mkdirs();
		directory.canWrite();
	    File f = new File(directory, fileName.toString().trim() );
	//	File f = new File("c:/"+ username+ "/" + "data/" + fileName.toString().trim() );
	    directory.mkdirs();
	    directory.canWrite();
	    f.canWrite();
	   // StreamResult result = new StreamResult(f);
		  
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(f);
			// TODO Auto-generated catch block
			
			fos.write(theScript.toString().getBytes("UTF-8"));
			fos.close();
		
		}catch (Exception ets){   
			ets.printStackTrace();
		}
		
		return true;
	}
	
	public static boolean  barPloterPDF(String username, String fileName, StringBuilder theData){
		/* This gets called by the servlet.
		 * Once this gets called, from here make a call to the RComponent file which
		 * pulls in actual plotter which loads the GD library and creates a PDF.
		 * The class PlotPDF.java
		 */
	
		PlotPDF plotPdf = new PlotPDF();
		PlotPDF.doGraph(fileName, username, theData.toString());
		return true;
	}
}

