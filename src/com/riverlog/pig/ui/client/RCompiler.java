package com.riverlog.pig.ui.client;
//import org.rosuda.JRI.Rengine;
public class RCompiler {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// new R-engine
	//	System.out.println(System.getProperty("java.library.path"));
	//	Rengine re = null;
		try{
      //  re =new Rengine (new String [] {"--vanilla"}, false, null);
		}catch(Exception et){
			et.printStackTrace(); 
			
		}
       // if (!re.waitForR())
        {
            System.out.println ("Cannot load R");
            return;
        }
       
        // print a random number from uniform distribution
      //  System.out.println (re.eval ("runif(1)").asDouble ());
       
        // done...
       // re.end();

	}

}
