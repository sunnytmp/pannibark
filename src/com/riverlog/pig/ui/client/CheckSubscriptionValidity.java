package com.riverlog.pig.ui.client;



import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;

import com.google.gwt.http.client.RequestBuilder;

import com.google.gwt.http.client.RequestCallback;

import com.google.gwt.http.client.Response;

import com.google.gwt.http.client.URL;

import com.google.gwt.user.client.Window;

public class CheckSubscriptionValidity implements RequestCallback {
	public int noOfDaysCalculated = 0;
	
	public int isSubscriptionActive(String username) {
		
	  	  RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, "CheckSubscriptionServlet");

	  	  StringBuffer postData = new StringBuffer();
			 postData.append(URL.encode("username")).append("=").append(URL.encode(username));
			 builder.setHeader("Content-type", "application/x-www-form-urlencoded");

			 try {

			   builder.setRequestData(postData.toString()); /* or other RequestCallback impl*/
			   builder.setCallback(this);
			   com.google.gwt.http.client.Request res = builder.send();
			   
			 } catch (Exception e) {

			   // handle this
				 Window.alert("Exception..." + e.getMessage());
				 return -1;
			 }
			 
			 
			return noOfDaysCalculated;
			
		}



		@Override

		public void onResponseReceived(Request request, Response response) {

			// WE are checking what is the number of days returned.
			String subscriptionStatus = response.getText();
			
			if(!subscriptionStatus.isEmpty() && subscriptionStatus != null){
			 if(subscriptionStatus.trim().equalsIgnoreCase("subscriptionEnded")){
				 //This means his subscription has expired
				Window.alert("Your Subscription Has Expired. Please re-subscribe/renew the service.\nThank you for using WinGrunt.com");
				Window.Location.assign("http://www.wingrunt.com");
			 }
			 
			}

			

		}



		@Override

		public void onError(Request request, Throwable exception) {

			// TODO Auto-generated method stub

			Window.alert(exception.getMessage());

		}

}

