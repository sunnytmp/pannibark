package com.riverlog.pig.ui.client;

import java.util.Date;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.SimpleCheckBox;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dev.shell.remoteui.MessageTransport.RequestException;
import com.google.gwt.dev.shell.remoteui.RemoteMessageProto.Message.Request;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItemSeparator;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.AbsolutePanel;

public class Login  implements EntryPoint , RequestCallback {
	
	
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";
	private Configuration constants;
	private TextBox textBox;
	private PasswordTextBox textBox_1;
	final Label textToServerLabel = new Label();
	final Label errorLabel = new Label();
	final HTML serverResponseLabel = new HTML();
	final Button closeButton = new Button("Close");
	final DialogBox dialogBox = new DialogBox();
	String resultFromServer=null;
	VerticalPanel verticalPanel1=null;
	boolean doneSendServer = false; //for multithreading aspects.
	private final LogJobExecutionAsync pigExecutionService = GWT
			.create(LogJobExecution.class);
	final FormPanel form1 = new FormPanel();
	public void onModuleLoad() {
		RootPanel rootPanel = RootPanel.get();
		
		AbsolutePanel absolutePanel = new AbsolutePanel();
		absolutePanel.setStyleName("gwt-RichTextArea");
		rootPanel.add(absolutePanel, 520, 300);
		absolutePanel.setSize("479px", "278px");
		
		Label lblMainLoginForm = new Label("Main Login Form");
		absolutePanel.add(lblMainLoginForm, 45, 39);
		lblMainLoginForm.setStyleName("gridpanel");
		lblMainLoginForm.setSize("130px", "25px");
		//initWidget(verticalPanel);
		
		FlexTable flexTable = new FlexTable();
		absolutePanel.add(flexTable, 45, 65);
		flexTable.setBorderWidth(3);
		flexTable.setStyleName("gridpanel");
		flexTable.setSize("398px", "195px");
		
		Label lblSignInTo = new Label("Sign In To Your Account");
		flexTable.setWidget(0, 1, lblSignInTo);
		
		
		
		Label lblUsername = new Label("UserName");
		lblUsername.setStylePrimaryName("body");
		flexTable.setWidget(1, 0, lblUsername);
		
		textBox = new TextBox();
		flexTable.setWidget(1, 1, textBox);
		textBox.setSize("181px", "27px");
		
		Label lblPassword = new Label("Password");
		flexTable.setWidget(2, 0, lblPassword);
		
		textBox_1 = new PasswordTextBox();
		textBox_1.addKeyDownHandler(new KeyDownHandler() {

		    @Override
		    public void onKeyDown(KeyDownEvent event) {
		     if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
		    	 if (textBox.getText().length() == 0
							|| textBox_1.getText().length() == 0) {
							Window.alert("Username or password is empty."); 
						//	SendToServer();
							//Window.Location.assign("../piggyrun/PigLatinEditor.html");					}
							return;
				}
					SendToServer();
		           }
		    }

			
		});
		flexTable.setWidget(2, 1, textBox_1);
		textBox_1.setSize("181px", "27px");
		//initWidget(verticalPanel1);
		flexTable.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_MIDDLE);
		flexTable.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_LEFT);
		
		Button btnSignIn = new Button("Sign In");
		//flexTable.getRowFormatter().addStyleName(0,"verticalPanel1");
		
		btnSignIn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (textBox.getText().length() == 0
						|| textBox_1.getText().length() == 0) {
						Window.alert("Username or password is empty."); 
					//	SendToServer();
						//Window.Location.assign("../piggyrun/PigLatinEditor.html");					}
						return;
			}
				SendToServer();
			
			 
			
		}
			});
		
		CheckBox chckbxRememberInThis = new CheckBox("");
		flexTable.setWidget(3, 0, chckbxRememberInThis);
		
		Label lblRememberInThis = new Label("Remember in this computer");
		flexTable.setWidget(3, 1, lblRememberInThis);
		flexTable.setWidget(4, 0, btnSignIn);
		btnSignIn.setSize("114px", "31px");
		
		Button btnSignOut = new Button("Sign Out");
		btnSignOut.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				/* Sign out of the system */
				textBox.setText("");
				Window.Location.reload();
			}
		});
		flexTable.setWidget(4, 1, btnSignOut);
		btnSignOut.setSize("101px", "30px");
		flexTable.getCellFormatter().setHorizontalAlignment(3, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		flexTable.getCellFormatter().setVerticalAlignment(3, 0, HasVerticalAlignment.ALIGN_MIDDLE);
		flexTable.getCellFormatter().setVerticalAlignment(3, 1, HasVerticalAlignment.ALIGN_TOP);
		RootPanel.get("errorLabelContainer").add(errorLabel);
		
		verticalPanel1 = new VerticalPanel();
		 verticalPanel1.setSpacing(10);
	   
	     
		 
		 
		VerticalPanel verticalPanel = new VerticalPanel();
		constants = GWT.create(Configuration.class);

		dialogBox.setText("Remote Procedure Call");
		dialogBox.setAnimationEnabled(true);
		
		// We can set the id of a widget by accessing its Element
		closeButton.getElement().setId("closeButton");
		
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.add(new HTML("<b>Exeecuting command:</b>"));
		dialogVPanel.add(textToServerLabel);
		dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
		dialogVPanel.add(serverResponseLabel);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(closeButton);
		dialogBox.setWidget(dialogVPanel);
		
		
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				
			}
		});
		
	      
		Label lblSignIntoYour1 = new Label("Sign Into Your Account");
		lblSignIntoYour1.setStyleName("label");
		verticalPanel1.add(lblSignIntoYour1);
		
		FlexTable flexTable1 = new FlexTable();
		verticalPanel1.add(flexTable1);
		
		Label lblUsername1 = new Label("UserName");
		flexTable1.setWidget(0, 0, lblUsername1);
			
		TextBox textBoxUsername1 = new TextBox();
		flexTable1.setWidget(1, 0, textBoxUsername1);
		
	
	
		verticalPanel1.add(flexTable1);
		flexTable1.setWidth("100px");
		
		VerticalPanel verticalPanel11 = new VerticalPanel();
		//initWidget(verticalPanel11);
		verticalPanel11.setSize("612px", "418px");
		
		FlexTable flexTable11 = new FlexTable();
		verticalPanel11.add(flexTable11);
		flexTable11.setSize("611px", "531px");
		
	
		rootPanel.add(verticalPanel);
		  form1.setWidget(verticalPanel1);
		
		MenuBar menuBar = new MenuBar(false);
		rootPanel.add(menuBar, -1, 1);
		menuBar.setSize("1408px", "20px");
		
		MenuItem mntmHome = new MenuItem("Home", false, new Command() {
			public void execute() {
				Window.Location.assign("http://wingrunt.com");
			
			}
		});
	
		
		menuBar.addItem(mntmHome);
		
		MenuItemSeparator separator = new MenuItemSeparator();
		menuBar.addSeparator(separator);
		
		MenuItem mntmQueryWindow = new MenuItem("Query Window", false, (Command) null);
		menuBar.addItem(mntmQueryWindow);
		
		MenuItemSeparator separator_1 = new MenuItemSeparator();
		menuBar.addSeparator(separator_1);
		
		MenuItem mntmCollaboration = new MenuItem("Registration", false, new Command() {
			public void execute() {
				Window.Location.assign(GWT.getHostPageBaseURL()+"index.html");
			
			}
		});
		mntmCollaboration.setHTML("Registration");
		menuBar.addItem(mntmCollaboration);
		
		MenuItemSeparator separator_2 = new MenuItemSeparator();
		menuBar.addSeparator(separator_2);
		MenuBar menuBar_1 = new MenuBar(true);
		
		//MenuItem mntmNewMenu = new MenuItem("New menu", false, menuBar_1);
		MenuBar menuBar_2 = new MenuBar(true);
		
		MenuItem mntmNewMenu_1 = new MenuItem("About WinPig", false, new Command() {
			public void execute() {
				//Apache pig documentation
				Window.Location.assign("http://www/riverlog.com/pig/pigfaq.html");
			}
		});
		
		MenuItem mntmApachePig = new MenuItem("Apache Pig", false, new Command() {
			public void execute() {
				//Apache pig documentation
				Window.open("https://pig.apache.org/docs/r0.12.0/basic.html","_blank", "");
			     
			}
		});
		menuBar_2.addItem(mntmApachePig);
		
		MenuItem mntmPqtFaq = new MenuItem("PQT FAQ", false, new Command() {
			public void execute() {
				//FAQ
				Window.open("https://pig.apache.org/docs/r0.12.0/basic.html","_blank", "");
			     
			}
		});
		menuBar_2.addItem(mntmPqtFaq);
		menuBar.addItem(mntmNewMenu_1);
		
	
		
		MenuItemSeparator separator_3 = new MenuItemSeparator();
		menuBar.addSeparator(separator_3);
		MenuBar menuBar_3 = new MenuBar(true);
		
		MenuItem mntmCollaboration_1 = new MenuItem("Collaboration", false, menuBar_3);
		
	//	MenuItem mntmInvitePartner = new MenuItem("Invite Partner", false, (Command) null);
	//	menuBar_3.addItem(mntmInvitePartner);
		
		MenuItem mntmEmailLink = new MenuItem("Email This Link", false,new Command() {
			public void execute() {
				     		 EmailLinkDialog myDialog = new EmailLinkDialog();

					            int left = Window.getClientWidth()/ 2;
					            int top = Window.getClientHeight()/ 2;
					            myDialog.setPopupPosition(left, top);
					            myDialog.show();	
				     	}
		});
		menuBar_3.addItem(mntmEmailLink);
		menuBar.addItem(mntmCollaboration_1);
		
		MenuItemSeparator separator_4 = new MenuItemSeparator();
		menuBar.addSeparator(separator_4);
		
		MenuItem mntmAbout = new MenuItem("Large File Analytics R", false, new Command() {
	     	public void execute() {
	     		/* Make a call to the properties constant */

	     		
	     		GenericDialog genericDialogLargeFiles = new GenericDialog("Functionality Available For Enterprise Version", constants.enterpriseMessage());
	     		int left = Window.getClientWidth()/ 2;
	            int top = Window.getClientHeight()/ 2;
	            genericDialogLargeFiles.setPopupPosition(left, top);
	            genericDialogLargeFiles.show();	
	     		
	     	}
	     });
		mntmAbout.setHTML("Upload Logs");
		menuBar.addItem(mntmAbout);
		
		MenuItemSeparator separator_5 = new MenuItemSeparator();
		menuBar.addSeparator(separator_5);
		
		MenuItem mntmAbout_1 = new MenuItem("About", false, new Command() {
	     	public void execute() {
	     		/* Make a call to the properties constant */

	     		
	     		GenericDialog genericDialog = new GenericDialog("About Pig Latin Engine SaaS", constants.aboutText());
	     		int left = Window.getClientWidth()/ 2;
	            int top = Window.getClientHeight()/ 2;
	            genericDialog.setPopupPosition(left, top);
	            genericDialog.show();	
	     	}
	     });
	/**	
		TextBox textBox_2 = new TextBox();
		textBox_2.setStyleName("gwt-TextBoxBorder");
		textBox_2.setEnabled(false);
		textBox_2.setReadOnly(true);
		rootPanel.add(textBox_2, 0, 27);
		textBox_2.setSize("1383px", "4px");
		
		Label lblNewLabel = new Label("Please register by clicking the \"Registration\" menu from top or please Enter your Username & Password. ");
		lblNewLabel.setStyleName("gwt-LabelLogin");
		rootPanel.add(lblNewLabel, 414, 158);
		lblNewLabel.setSize("506px", "59px");
		
		Hyperlink hprlnkregisterNow = new Hyperlink("", false, "newHistoryToken");
		rootPanel.add(hprlnkregisterNow, 285, 98);
		String htmlString = new String();
		htmlString = GWT.getHostPageBaseURL();
		HTML htmlNewHtml = new HTML("<a href=\""+ htmlString + "index.html\">Register Now</a>", true);
								//	"<a href=\"http://192.168.1.7:8080/piggyrun/index.html\"
		rootPanel.add(htmlNewHtml, 418, 194);
		htmlNewHtml.setSize("119px", "17px");
		**/
	}
	
	public  void SendToServer() {
		String username =textBox.getText().trim();
		pigExecutionService.greetServer(username+":"+textBox_1.getText().trim(),
				new AsyncCallback<String>() {
					public void onFailure(Throwable caught) {
						// Show the RPC error message to the user
						dialogBox
								.setText("Server Request Failed! - Failure");
						serverResponseLabel
								.addStyleName("serverResponseLabelError");
						serverResponseLabel.setHTML(SERVER_ERROR);
						dialogBox.center();
						closeButton.setFocus(true);
					}

					public void onSuccess(String result) {
					//	dialogBox.setText("Remote Procedure Call");
					//	serverResponseLabel
					//			.removeStyleName("serverResponseLabelError");
						//serverResponseLabel.setHTML(result);
					//	dialogBox.center();
					//	closeButton.setFocus(true);
						
						
						/** Check if statement contained file name and store the file name  */
						//System.out.println("This is a " + result);
						resultFromServer = result;
						doneSendServer = true;
						
							moveForward();
						
					}
				});
		
		
	}
	public void moveForward() { 
/*********************************************************************************************************************/
/**********************************************************************************************************************/
		
		 while (doneSendServer){
			  System.out.println("The done server variable is " + doneSendServer);
			  System.out.println("The value of resultFromServer is " + resultFromServer );
			  doneSendServer = true;
			  
			if (resultFromServer.trim().equals("SuccessFul")){
				
				String sessionID =  textBox.getText().trim() +"-34098095839987fkhsdf8729323khs";/*(Get sessionID from server's response to your login request.)*/;
			    final long DURATION = 1000 * 60 * 60 * 24 * 14; //duration remembering login. 2 weeks in this example.
			    Date expires = new Date(System.currentTimeMillis() + DURATION);
			    Cookies.removeCookie("windsd9n","/");
			    
			    Cookies.setCookie("windsd9n", sessionID, expires, null, "/", false);
			    
				Window.Location.assign(GWT.getHostPageBaseURL()+"PigLatinEditor.html");
				 doneSendServer = false;
				 
			 } else{
				 Window.alert("Wrong Userid Or Password");
				 doneSendServer = false;
			 }
		}

	}
	 
	 native  public static void close()/*-{  $wnd.close();

	 }-*/;

	@Override
	public void onResponseReceived(com.google.gwt.http.client.Request request,
			Response response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(com.google.gwt.http.client.Request request,
			Throwable exception) {
		// TODO Auto-generated method stub
		
	}
}
