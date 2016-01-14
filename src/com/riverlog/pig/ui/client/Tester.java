package com.riverlog.pig.ui.client;

public class Tester {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		
		String x = "car	 suv 	truck		\n"
+ "10	89	11\n"
+ "3	7	3\n"
+ "23	56	89\n" ;
		
		
		String[] textArea = x.split("\n");
		String firstText = "";
		for (int i= 0; i <= textArea.length-1; i++){	
		System.out.println(textArea[i].indexOf("\\n"));
			   firstText = firstText + textArea[i];
		       firstText = firstText.trim();
		       firstText = firstText + "\\n";
		       System.out.println("First text " + firstText);
		}
		
	}

}
