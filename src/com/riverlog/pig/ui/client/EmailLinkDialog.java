package com.riverlog.pig.ui.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

class EmailLinkDialog extends DialogBox implements RequestCallback {
String username = null;
TextBox EmailIdText = new TextBox();
String dataFileName = null;
VerticalPanel panel = new VerticalPanel();
	public EmailLinkDialog() {
       // Set the dialog box's caption.
       setText("Email The Link To Collaborator");
       // Enable animation.
       setAnimationEnabled(true);
       setAutoHideEnabled(true);
       // Enable glass background.
       setGlassEnabled(true);

       // DialogBox is a SimplePanel, so you have to set its widget 
       // property to whatever you want its contents to be.
       Button ok = new Button("Send Email");
       ok.addClickHandler(new ClickHandler() {
          public void onClick(ClickEvent event) {
        	  
         sendEmail(EmailIdText.getText().toString());
     		
        	  
        	  // Window.Location.assign("http://127.0.0.1:8888/EmailPigScript?username=smenon");
             EmailLinkDialog.this.hide();
          }
       });
    

       Label label = new Label("Enter EmailID");
       //TextBox EmailIdText = new TextBox();

 
       panel.setHeight("500");
       panel.setWidth("500");
       panel.setSpacing(10);
       panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
       panel.add(label);
       panel.add(EmailIdText);
       panel.add(ok);

       setWidget(panel);
    }
	
	public boolean sendEmail(String toemail) {
		  
  	  RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, GWT.getHostPageBaseURL()+"EmailLink");
		
  	  StringBuffer postData = new StringBuffer();
		 
		 postData.append(URL.encode("username")).append("=").append(URL.encode("FromLoginScreen"));
		 postData.append(URL.encode("&"));
		 postData.append(URL.encode("toEmail")).append("=").append(URL.encode(toemail));
		 //postData.append(URL.encode("&"));
		 //postData.append(URL.encode("dataFileName")).append("=").append(URL.encode(dataFileName));
		 
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
