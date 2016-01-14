package com.riverlog.pig.ui.client;

public class Utilities {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public  String filterFnameWithoutType(String remainingstringlocal ) {
		int x = 0;
		String fieldsname = "";
			String fieldname[] = remainingstringlocal.split(",");
			for (int i=0;i<= fieldname.length-1;i++){
				x = fieldname[i].trim().length();
				String yy = fieldname[i];
				if (fieldname[i].trim().substring(0,1).equals("(") ){
					  fieldsname = fieldname[i].trim().substring(1);
								
				} else if ( fieldname[i].trim().substring(x-1).equals(")")){

					fieldsname = fieldsname +"\t" + fieldname[i].trim().substring(0,fieldname[i].trim().length()-1); 
				 } else { 
			    	fieldsname = fieldsname +"\t" + fieldname[i].trim();
			   }
				
			}
			
			//System.out.println(fieldsname);
			return fieldsname;
		}

    ////
	
	public  String mainRetriever(String pigline) {

		// TODO Auto-generated method stub
	//  String pigline = "A = LOAD 'student' USING PigStorage() AS (name:chararray, age:int,  gpa:float);";
		 // String  pigline = "A = LOAD 'student' USING PigStorage() AS (name:chararry, age:int,  gpa:float,  tpa:date);";
	  String fieldsname = "";
	  if (pigline.toUpperCase().indexOf("AS") != -1){
		  int x = pigline.toUpperCase().indexOf("AS") ;
		 String remainingstring1 = pigline.substring(x+2, pigline.length()-1);
		  if (remainingstring1.indexOf(":") == -1){
  			fieldsname =  filterFnameWithoutType(remainingstring1);
  			return fieldsname;
  		  }
		  int stringcount = 0;
		   while(stringcount <= pigline.length()-1) {
			String remainingstring =  pigline.substring(x+2, pigline.length()-1);
		      stringcount = pigline.length()-1;
		      int stringcount2 = 0;
		      while (stringcount2 <= remainingstring.length()-1) {
		    	  if (remainingstring.trim().substring(0,1).equals("(") ) {
		    		   fieldsname = fieldsname + "\t" +  remainingstring.substring(2,remainingstring.indexOf(":"));
		    		   if (remainingstring.indexOf(",") != -1){
		    		   remainingstring = remainingstring.substring(remainingstring.indexOf(",")+1);
		    		 //  System.out.println(remainingstring);
		    	    // System.exit(0);
		    		   stringcount++;
		    		 stringcount2++;
		    		}else{  //that means that this may be the last field to be extracted
		    			fieldsname = fieldsname + "\t" +  remainingstring.substring(0,remainingstring.indexOf(":"));
		    			stringcount++;
		    			stringcount2++;
		    		}
		    		  
		    	  }else{   // majority of code gets executed here.
		    		  
		    		   if (remainingstring.indexOf(",") != -1){
		    			fieldsname = fieldsname + "\t" +  remainingstring.substring(0,remainingstring.indexOf(":"));
		    		    remainingstring = remainingstring.substring(remainingstring.indexOf(",")+1);
		    		   // System.out.println(remainingstring);
		    		    
		    		    stringcount2++;
		    		    stringcount++;
		    		   } else{  //that means that this may be the last field to be extracted
		    			   stringcount2++;
		    			   if (remainingstring.indexOf(":") != -1){
			    			fieldsname = fieldsname + "\t" +  remainingstring.substring(0,remainingstring.indexOf(":"));
			    			remainingstring = remainingstring.substring(0,remainingstring.indexOf(":"));
			    			
		    			   }else{
		    				  // System.out.println(fieldsname);	
		    				   return fieldsname;
		    			   }
			    			
			     	   }
		    		   
		    		//   System.out.println(fieldsname);
		    	
		    	  }
		      }
		  }
			
		  String hh = pigline.substring(x+1);
		  //System.out.println("Yes");
	  } else{
		 //System.out.println("no"); 
	  }
	return fieldsname;

	}	  
	     
	}


