package com.riverlog.pig.ui.server;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

import javax.swing.JTextArea;

import com.google.gwt.core.client.GWT;


public class PlotPDF {

	
	
public static void main(String[] args) {
	//doGraph();
	/* System.exit(0);
		Rengine re;
		String[] dummyArgs = new String[1];
		dummyArgs[0] = "--vanilla";
		re = new Rengine(dummyArgs, false, null);
		re.eval("library(JavaGD)");
		// This is the critical line: Here, we tell R that the JavaGD() device that
		// it is supposed to draw to is implemented in the class MyJavaGD. If it were
		// in a package (say, my.package), this should be set to
		// my/package/MyJavaGD1.
		re.eval("Sys.putenv('JAVAGD_CLASS_NAME'='MyJavaGD1')");
		re.eval("JavaGD()");
		re.eval("plot(c(1,5,3,8,5), type='l', col=2)");
	//	re.end();
	 * */
	 
 }

  public static void doGraph(String datFileName, String username, String theData) {
	  //Rengine re;
		String[] dummyArgs = new String[1];
		
		//jtex.setText("cars	suvs	trucks	\n13	9	7\n18	79	47\n2	6	8");
		
		// FileWriter pw = null;
		 File file = null;
		try {
			 file = new File("/home/ubuntu/jetty-distribution-8.1.17.v20150415/webapps/war/" + username+"/yourgraphdata.dat");
			//pw = new FileWriter ("C:/R-3.0.2/PersonalExamples/cars2.dat");
 			// if file doesnt exists, then create it
 			if (!file.exists()) {
 				file.createNewFile();
 			}
  
 			FileWriter fw = new FileWriter(file.getAbsoluteFile());
 			BufferedWriter bw = new BufferedWriter(fw);
 			bw.write(theData);
 			bw.write("\n");
 			bw.close();
  
 			System.out.println("Done");
  
 		} catch (IOException e) {
 			e.printStackTrace();
 		}

	try{
			System.out.println("Doing graph");
			//String[] cmdArray = (new String[] {});
		//	Runtime.getRuntime().exec("/bin/bash" + " -c" + " Rscript " + username + "/plotPdf.R " +  username + "/yourgraphdata.dat");
			//Process p = new ProcessBuilder("/usr/lib64/R/bin/Rscript " , "/home/ec2-user" + "/plotPdf.R" , username + "/yourgraphdata.dat" ).start();
		//Local Specific
		//	Process p = new ProcessBuilder("/usr/lib/R/bin/Rscript" , "/home/sunnym/WebGrunt/war/smenon" + "/plotPdf.R" , "/home/sunnym/WebGrunt/war/smenon" + "/yourgraphdata.dat" ).start();
	    //"/Users/US-SMenon/Documents/wingrunt/WebGrunt/war/"
			
			
		Process p = new ProcessBuilder("Rscript" , "/home/ubuntu/jetty-distribution-8.1.17.v20150415/webapps/war/" + datFileName.trim() ,   "/home/ubuntu/jetty-distribution-8.1.17.v20150415/webapps/war/"+ username + "/yourgraphdata.dat","/home/ubuntu/jetty-distribution-8.1.17.v20150415/webapps/war/" + username ).start();

	  
			InputStream is = p.getInputStream();
		       InputStreamReader isr = new InputStreamReader(is);
		       BufferedReader br = new BufferedReader(isr);
		       String line;
		          

		       while ((line = br.readLine()) != null) {
		        System.out.println(line);
		       }
			
			System.out.println("Done..");
			
		}catch(Exception ets){
			ets.printStackTrace();
		}

  }
 
}