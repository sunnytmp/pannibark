package com.riverlog.pig.ui.client;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;

public class BarPlotClient implements RequestCallback {

	
	public boolean creatBarPloterPDF(String username, String fileName, String theData) {
		  
	  	  RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, "FileSavePlotServlet");
			 StringBuffer postData = new StringBuffer();
			 
			 postData.append(URL.encode("username")).append("=").append(URL.encode(username));
			 postData.append(URL.encode("&"));
			 postData.append(URL.encode("plotFileName")).append("=").append(URL.encode(fileName));
			 postData.append(URL.encode("&"));
			 postData.append(URL.encode("theData")).append("=").append(URL.encode(theData));
			 
			 builder.setHeader("Content-type", "application/x-www-form-urlencoded");
			
			 try {
			   builder.setRequestData(postData.toString()); /* or other RequestCallback impl*/
			    
			   builder.setCallback(this);

			   com.google.gwt.http.client.Request res = builder.send(); 
			   
			   
			 } catch (Exception e) {
			   // handle this
				 Window.alert("Exception..." + e.getMessage());
			 }
			return true;
		}

		@Override
		public void onResponseReceived(Request request, Response response) {
			// TODO Auto-generated method stub
			Window.alert(response.getText());
		}

		@Override
		public void onError(Request request, Throwable exception) {
			// TODO Auto-generated method stub
			Window.alert(exception.getMessage());
		}
}
