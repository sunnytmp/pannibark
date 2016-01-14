package com.riverlog.pig.ui.client;


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

public class ScriptDialog extends DialogBox implements RequestCallback {
	VerticalPanel panel = new VerticalPanel();
	DialogBox dialogBox_1 = new DialogBox();
	Label lblEnterTheFile = new Label("Enter the file name to be saved");
	TextBox txtbxFilenametxt = new TextBox();
	
	String username = null;
	String fileName=null;
	String theScript = null;
	
	public ScriptDialog(String usernameParam, String theScriptParam){
	
		username = usernameParam;
		
		theScript = theScriptParam;
		
	 setAnimationEnabled(true);
     setAutoHideEnabled(true);
     // Enable glass background.
     setGlassEnabled(true);
     // DialogBox is a SimplePanel, so you have to set its widget 
     // property to whatever you want its contents to be.
     Button ok = new Button("Save Script File");
     ok.addClickHandler(new ClickHandler() {
        public void onClick(ClickEvent event) {
      	  // Window.Location.assign("http://127.0.0.1:8888/EmailPigScript?username=smenon");
        	String temp_string = txtbxFilenametxt.getText().replaceAll("[^\\w|\\s]", "");
    		String final_string = temp_string.replaceAll("\\s$", "");
        	saveScriptFile(username, txtbxFilenametxt.getText().trim() , theScript);
        	ScriptDialog.this.hide();
        }
     });
     Label lblEnterTheFile = new Label("Enter File Name");
     //TextBox EmailIdText = new TextBox();
     panel.setHeight("500");
     panel.setWidth("500");
     panel.setSpacing(10);
     panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
     panel.add(lblEnterTheFile);
     panel.add(txtbxFilenametxt);
     panel.add(ok);
     setWidget(panel);
	
  }
	public boolean saveScriptFile(String username, String fileName, String thePigScript) {
		  
	  	  RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, "SaveScriptServlet");
			 StringBuffer postData = new StringBuffer();
			 Window.alert("SaveScriptServlet");
			 postData.append(URL.encode("username")).append("=").append(URL.encode(username));
			 postData.append(URL.encode("&"));
			 postData.append(URL.encode("scriptFileName")).append("=").append(URL.encode(fileName));
			 postData.append(URL.encode("&"));
			 postData.append(URL.encode("theScript")).append("=").append(URL.encode(theScript));
			 
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


