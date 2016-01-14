package com.riverlog.pig.ui.client;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import gwtupload.client.IUploadStatus.Status;

import gwtupload.client.IUploader;

import gwtupload.client.IUploader.UploadedInfo;

import gwtupload.client.MultiUploader;

import gwtupload.client.PreloadedImage;

import gwtupload.client.PreloadedImage.OnLoadPreloadedImageHandler;

import javax.swing.filechooser.FileView;

import org.mortbay.log.Log;

import com.gargoylesoftware.htmlunit.javascript.host.Text;

import com.google.gwt.core.client.EntryPoint;

import com.google.gwt.core.client.GWT;

import com.google.gwt.dev.shell.remoteui.MessageTransport.RequestException;

import com.google.gwt.dev.shell.remoteui.RemoteMessageProto.Message.Request;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;

import com.google.gwt.event.dom.client.ChangeHandler;

import com.google.gwt.event.dom.client.ClickEvent;

import com.google.gwt.event.dom.client.ClickHandler;

import com.google.gwt.http.client.RequestBuilder;

import com.google.gwt.http.client.RequestCallback;

import com.google.gwt.http.client.Response;

import com.google.gwt.http.client.URL;

import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;

import com.google.gwt.user.client.Element;

import com.google.gwt.user.client.Timer;

import com.google.gwt.user.client.Window;

import com.google.gwt.user.client.rpc.AsyncCallback;

import com.google.gwt.user.client.ui.Button;

import com.google.gwt.user.client.ui.DialogBox;

import com.google.gwt.user.client.ui.FileUpload;

import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;

import com.google.gwt.user.client.ui.FlowPanel;

import com.google.gwt.user.client.ui.FormPanel;

import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;

import com.google.gwt.user.client.ui.ClickListener;

import com.google.gwt.user.client.ui.HTML;

import com.google.gwt.user.client.ui.Image;

import com.google.gwt.user.client.ui.Label;

import com.google.gwt.user.client.ui.PopupPanel;

import com.google.gwt.user.client.ui.PushButton;

import com.google.gwt.user.client.ui.RootPanel;

import com.google.gwt.user.client.ui.SimplePanel;

import com.google.gwt.user.client.ui.TextBox;

import com.google.gwt.user.client.ui.VerticalPanel;

import com.google.gwt.user.client.ui.TextArea;

import com.google.gwt.user.client.ui.Widget;

import com.google.gwt.user.client.ui.ListBox;

import com.google.gwt.user.client.ui.FlexTable;

import com.google.gwt.user.client.ui.Hyperlink;

import com.google.gwt.user.client.ui.VerticalSplitPanel;

import com.google.gwt.user.client.ui.MenuBar;

import com.google.gwt.user.client.ui.MenuItem;

import com.google.gwt.user.client.Command;

import com.google.gwt.user.client.ui.MenuItemSeparator;
import com.google.gwt.user.client.ui.InlineHyperlink;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.event.logical.shared.AttachEvent.Handler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.ui.NumberLabel;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Grid;

/**
 * 
 * 
 * 
 * Entry point classes define <code>onModuleLoad()</code>.
 */

public class PigLatinEditor implements EntryPoint, RequestCallback {
	public Grid grid = new Grid(100, 100);
	public ScrollPanel scrollPanel = new ScrollPanel();
	private Configuration constants;

	private MyPopup p = null;

	private Timer timer = null;
	/**
	 * 
	 * 
	 * 
	 * The message displayed to the user when the server cannot be reached or
	 * 
	 * 
	 * 
	 * returns an error.
	 */

	private static final String SERVER_ERROR = "An error occurred while "

	+ "attempting to contact the server. Please check your network "

	+ "connection and try again.";

	final Label textToServerLabel = new Label();

	final Button closeButton = new Button("Close");

	final DialogBox dialogBox = new DialogBox();

	final Label errorLabel = new Label();

	private Button clickMeButton;

	TextArea txtrEnterPigLatin = new TextArea();

	final HTML serverResponseLabel = new HTML();

	ListBox listBox = new ListBox();

	TextArea textAreaHistory = new TextArea();

	String dumpStatement = new String();

	String composedPigLatinStatments = new String("");

	String username = null;

	StringBuilder fullPigScripts = null;

	TextArea dumpTextArea = new TextArea();

	boolean thisIsADumpStatement = false;

	boolean hasNotDoneThis = true;

	boolean thisIsAFetchStatement = true;

	private FormPanel form = new FormPanel();

	final FileUpload fileUpload = new FileUpload();

	VerticalPanel panel = new VerticalPanel();

	String textToServer = new String("");

	RootPanel rootPanel = RootPanel.get();

	/**
	 * 
	 * 
	 * 
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */

	private final PigJobExecutionAsync pigExecutionService = GWT

	.create(PigJobExecution.class);

	public void onModuleLoad() {

		String sessionID = Cookies.getCookie("windsd9n");

		constants = GWT.create(Configuration.class);

		String aboutTxt = constants.aboutText();

		/**************************************************************************************************************/

		if (sessionID == null || sessionID.isEmpty()) {

			Window.alert("Illegitimate Attempt To Access Resources.. Please login..");

			Window.Location.assign(GWT.getHostPageBaseURL() + "Login.html");

			return;

		}

		username = sessionID.substring(0, sessionID.indexOf("-"));

		/*
		 * The subscription validity is not good
		 * 
		 * Do not allow the user access to pig latin browser
		 */

		CheckSubscriptionValidity chkSubValidity = new CheckSubscriptionValidity();

		// The method below will spit out the "OUT" jsp command and check for
		// validity right there.

		int validity = -1;

		validity = chkSubValidity.isSubscriptionActive(username);

		// call servlet and get pig scripts if any

		getPigScriptsFromServlet(username);
		rootPanel.setStyleName("body");

		// Now get the return value if not null then process it using gson.

		RootPanel.get("errorLabelContainer").add(errorLabel);

		clickMeButton = new Button();
		clickMeButton.setHTML("<center><html<b>Execute</b></html></center>");
		clickMeButton.setStyleName("gwt-PushButton-up-hovering");

		rootPanel.add(clickMeButton, 490, 352);
		clickMeButton.setSize("107px", "48px");

		clickMeButton.setText("Execute");
		txtrEnterPigLatin.setStyleName("gwt-RichTextArea");

		txtrEnterPigLatin.setText("Enter Pig Latin Statement Here..");

		rootPanel.add(txtrEnterPigLatin, 30, 103);

		txtrEnterPigLatin.setSize("555px", "231px");

		txtrEnterPigLatin.setFocus(true);

		Label lblFilesSaved = new Label("Stored Data File");

		rootPanel.add(lblFilesSaved, 597, 24);
		listBox.setStyleName("gwt-RichTextArea");

		rootPanel.add(listBox, 597, 103);
		listBox.setSize("148px", "233px");

		listBox.setVisibleItemCount(5);

		Label lblCommandHistory = new Label("Command History");

		rootPanel.add(lblCommandHistory, 32, 593);
		textAreaHistory.setStyleName("gwt-RichTextArea");

		textAreaHistory.setReadOnly(true);

		rootPanel.add(textAreaHistory, 30, 614);
		textAreaHistory.setSize("555px", "121px");

		Button btnClear = new Button("<Center><B>Clear</B></Center>");
		btnClear.setStyleName("gwt-PushButton-up-hovering");

		btnClear.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {

				txtrEnterPigLatin.setText("");

			}

		});

		rootPanel.add(btnClear, 374, 352);
		btnClear.setSize("116px", "48px");

		PushButton pshbtnInvite = new PushButton("Fetch");
		pshbtnInvite.setStyleName("gwt-PushButton-up-hovering");

		pshbtnInvite.setHTML("<center><b>Fetch</B></Center>");

		pshbtnInvite.setEnabled(true);

		rootPanel.add(pshbtnInvite, 758, 102);

		pshbtnInvite.setSize("105px", "16px");

		PushButton pshbtnNewButton = new PushButton("------>Email");
		pshbtnNewButton.setStyleName("gwt-PushButton-up-hovering");

		pshbtnNewButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {

				if (listBox.getSelectedIndex() <= 0) {

					Window.alert("Please choose a file to send as email.");

					return;

				}

				EmailDialog myDialog = new EmailDialog(username, listBox
						.getItemText(listBox.getSelectedIndex()).toString());

				int left = Window.getClientWidth() / 2;

				int top = Window.getClientHeight() / 2;

				myDialog.setPopupPosition(left, top);

				myDialog.show();

			}

		});

		pshbtnNewButton.setHTML("<B><Center>Email</Center></B>");

		rootPanel.add(pshbtnNewButton, 758, 132);

		pshbtnNewButton.setSize("105px", "16px");

		TextArea textArea = new TextArea();
		textArea.setStyleName("gwt-RichTextArea");

		rootPanel.add(textArea, 608, 614);
		textArea.setSize("293px", "121px");

		Label lblScribblePad = new Label("Scribble Pad");

		rootPanel.add(lblScribblePad, 608, 592);

		lblScribblePad.setSize("84px", "16px");
		dumpTextArea.setStyleName("gwt-RichTextArea");

		rootPanel.add(dumpTextArea, 28, 407);
		dumpTextArea.setSize("459px", "167px");

		Label lblDumpHistory = new Label("Dump Result");

		rootPanel.add(lblDumpHistory, 33, 387);

		final PushButton pshbtnLoadYourData = new PushButton("Upload Files");
		pshbtnLoadYourData.setStyleName("gwt-PushButton-up-hovering");

		pshbtnLoadYourData.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {

				pshbtnLoadYourData.setEnabled(false);

				/* FileUpload Button */

				Label selectLabel = new Label("Select a file:");

				Button uploadButton = new Button("Upload File");

				// pass action to the form to point to service handling file

				// receiving operation.

				form.setAction(GWT.getHostPageBaseURL() + "UploadServlet");

				// // set form to use the POST method, and multipart MIME
				// encoding.

				form.setMethod(FormPanel.METHOD_POST);

				form.setEncoding(FormPanel.ENCODING_MULTIPART);

				fileUpload.setName(username); // fileUpload

				form.setWidget(fileUpload);

				// add a label

				panel.add(selectLabel);

				// add fileUpload widget

				panel.add(fileUpload);

				form.setWidget(fileUpload);

				// add a button to upload the file

				panel.add(uploadButton);

				uploadButton.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {

						// get the filename to be uploaded

						String filename = fileUpload.getFilename();

						if (filename.length() == 0) {

							Window.alert("No File Specified!");

						} else {

							// submit the form

							form.submit();
							pshbtnLoadYourData.setEnabled(true);

						}

					}

				});

			}

		});

		pshbtnLoadYourData.setHTML("<B><Center>Upload Files</Center></B>");

		rootPanel.add(pshbtnLoadYourData, 758, 162);

		pshbtnLoadYourData.setSize("105px", "16px");

		TextBox textBox = new TextBox();

		textBox.setStyleName("gwt-TextBoxBorder");

		textBox.setReadOnly(true);

		textBox.setEnabled(false);

		rootPanel.add(textBox, 5, 24);

		textBox.setSize("1383px", "10px");

		PushButton pshbtnGettingStarted = new PushButton("Getting Started");
		pshbtnGettingStarted.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

			}
		});
		pshbtnGettingStarted.setStyleName("gwt-PushButton-up-hovering");

		rootPanel.add(pshbtnGettingStarted, 758, 192);

		pshbtnGettingStarted.setSize("105px", "16px");

		Label validityIndicator_label = new Label("");

		validityIndicator_label.setStyleName("gwt-HTML-new");

		rootPanel.add(validityIndicator_label, 490, 66);

		validityIndicator_label.setSize("223px", "16px");

		Button btnNewButton = new Button("<html><b>Save Script</b></html>");
		btnNewButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				if (txtrEnterPigLatin.getText() != null) {

					ScriptDialog scriptDialog = new ScriptDialog(username,
							txtrEnterPigLatin.getText().toString());

					scriptDialog.center();

					scriptDialog.show();

				}
			}
		});
		btnNewButton.setStyleName("gwt-PushButton-up-hovering");
		rootPanel.add(btnNewButton, 272, 352);
		btnNewButton.setSize("116px", "48px");

		Image image = new Image("visualizer.png");
		image.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
			}
		});
		image.setSize("134px", "165px");
		PushButton pshbtnNewButton_2 = new PushButton(image);
		pshbtnNewButton_2.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				// This click will load the PDF in the browser

				LoadPdfFromURL loadpdfurl = new LoadPdfFromURL();

				String url = null;

				try {

					url = GWT.getHostPageBaseURL() + username
							+ "/yourgraph.pdf";

				} catch (Exception ets) {

					ets.printStackTrace();

				}

				loadpdfurl.LoadPDFFromURL(url);
			}
		});
		pshbtnNewButton_2.setStyleName("gwt-PushButton-up-hovering");
		rootPanel.add(pshbtnNewButton_2, 608, 406);
		pshbtnNewButton_2.setSize("135px", "165px");

		Label lblVisualize = new Label("Visualize");
		rootPanel.add(lblVisualize, 618, 474);

		Image image_1 = new Image("bar-chart.png");
		image_1.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
			}
		});
		image_1.setSize("77px", "27px");
		PushButton pshbtnNewButton_1 = new PushButton(image_1);

		pshbtnNewButton_1.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				/*
				 * This event gets fired when the BarPlot button
				 * 
				 * 
				 * 
				 * in this pigLatinEditor is clicked. From there we must send
				 * the
				 * 
				 * 
				 * 
				 * text in the command result window
				 */

				BarPlotClient barPlotClient = new BarPlotClient();

				String[] textArea = dumpTextArea.getText().split("\\n");
				
				String firstText = "";

				// getting the text from text area and then splitting line by
				// line to remove tabs from each line
				// Becuase there was some space after the first line, the graph
				// was not forming at all. Graph failed. Now works.

				for (int i = 0; i <= textArea.length - 1; i++) {

					firstText = firstText + textArea[i];

					firstText = firstText.trim();

					firstText = firstText + "\n";

				}

				barPlotClient.creatBarPloterPDF(username, "plotPdf.R",
						firstText);

			}
		});
		pshbtnNewButton_1.setStylePrimaryName("gwt-PushButton-up-hovering");
		rootPanel.add(pshbtnNewButton_1, 501, 406);
		pshbtnNewButton_1.setSize("88px", "25px");

		Image image_2 = new Image("linegraph.png");
		image_2.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				// Line Plot
				// GenericDialog gendialog = new
				// GenericDialog("Line Plot","Coming soon. Yes, it will be available to YOU.");
				// gendialog.show();
				/*
				 * This event gets fired when the LinePlot button
				 */
				String[] textArea = dumpTextArea.getText().split("\\n");
				String firstText = "";
				for (int i = 0; i <= textArea.length - 1; i++) {
					firstText = firstText + textArea[i];
					firstText = firstText.trim();
					firstText = firstText + "\n";

				}
				LinePlotClient linePlotClient = new LinePlotClient();
				linePlotClient.createLinePloterPDF(username, "plotPdfLine.R",
						firstText);

			}
		});
		image_2.setSize("88px", "28px");
		PushButton pshbtnNewButton_3 = new PushButton(image_2);
		pshbtnNewButton_3.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// Line Plot
				// GenericDialog gendialog = new
				// GenericDialog("Line Plot","Coming soon. Yes, it will be available to YOU.");
				// gendialog.show();
				/*
				 * This event gets fired when the LinePlot button
				 */
				String[] textArea = dumpTextArea.getText().split("\\n");
				String firstText = "";
				for (int i = 0; i <= textArea.length - 1; i++) {
					firstText = firstText + textArea[i];
					firstText = firstText.trim();
					firstText = firstText + "\n";

				}
				LinePlotClient linePlotClient = new LinePlotClient();
				linePlotClient.createLinePloterPDF(username, "plotPdfLine.R",
						firstText);

			}
		});
		pshbtnNewButton_3.setStylePrimaryName("gwt-PushButton-up-hovering");
		rootPanel.add(pshbtnNewButton_3, 501, 440);
		pshbtnNewButton_3.setSize("88px", "25px");

		Image image_4 = new Image("piechart.png");
		image_4.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				// GenericDialog gendialog = new
				// GenericDialog("Dot Charts","Coming soon.... Yes, YOU can get all these...");

				// gendialog.show();

				// Pie chart

				String[] textArea = dumpTextArea.getText().split("\\n");

				String firstText = "";

				for (int i = 0; i <= textArea.length - 1; i++) {

					firstText = firstText + textArea[i];

					firstText = firstText.trim();

					firstText = firstText + "\n";

				}

				PiePlotClient piePlotClient = new PiePlotClient();
				piePlotClient.createLinePloterPDF(username, "plotPdfPie.R",
						firstText);

			}
		});
		image_4.setSize("89px", "28px");
		PushButton pshbtnNewButton_5 = new PushButton(image_4);
		pshbtnNewButton_5.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// GenericDialog gendialog = new
				// GenericDialog("Dot Charts","Coming soon.... Yes, YOU can get all these...");

				// gendialog.show();

				// Pie chart

				String[] textArea = dumpTextArea.getText().split("\\n");

				String firstText = "";

				for (int i = 0; i <= textArea.length - 1; i++) {

					firstText = firstText + textArea[i];

					firstText = firstText.trim();

					firstText = firstText + "\n";

				}

				PiePlotClient piePlotClient = new PiePlotClient();
				piePlotClient.createLinePloterPDF(username, "plotPdfPie.R",
						firstText);

			}
		});
		pshbtnNewButton_5.setStyleName("gwt-PushButton-up-hovering");
		rootPanel.add(pshbtnNewButton_5, 501, 509);
		pshbtnNewButton_5.setSize("88px", "27px");

		Image image_3 = new Image("histogram.png");
		image_3.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				// Histogram
				// Histo Plot
				// GenericDialog gendialog = new
				// GenericDialog("Line Plot","Coming soon. Yes, it will be available to YOU.");
				// gendialog.show();
				/*
				 * This event gets fired when the HistoPlot button
				 */

				String[] textArea = dumpTextArea.getText().split("\\n");
				String firstText = "";
				for (int i = 0; i <= textArea.length - 1; i++) {
					firstText = firstText + textArea[i];
					firstText = firstText.trim();
					firstText = firstText + "\n";

				}

				HistoPlotClient histoPlotClient = new HistoPlotClient();
				histoPlotClient.createLinePloterPDF(username,
						"plotPdfHisto.R", firstText);
			}
		});
		image_3.setSize("89px", "25px");
		PushButton pshbtnNewButton_4 = new PushButton(image_3);
		pshbtnNewButton_4.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// Histogram
				// Histo Plot
				// GenericDialog gendialog = new
				// GenericDialog("Line Plot","Coming soon. Yes, it will be available to YOU.");
				// gendialog.show();
				/*
				 * This event gets fired when the HistoPlot button
				 */

				String[] textArea = dumpTextArea.getText().split("\\n");
				String firstText = "";
				for (int i = 0; i <= textArea.length - 1; i++) {
					firstText = firstText + textArea[i];
					firstText = firstText.trim();
					firstText = firstText + "\n";

				}

				HistoPlotClient histoPlotClient = new HistoPlotClient();
				histoPlotClient.createLinePloterPDF(username,
						"plotPdfHisto.R", firstText);
			}
		});
		pshbtnNewButton_4.setStyleName("gwt-PushButton-up-hovering");
		rootPanel.add(pshbtnNewButton_4, 501, 474);
		pshbtnNewButton_4.setSize("88px", "25px");

		Image image_5 = new Image("scatter.png");
		image_5.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				// GenericDialog gendialog = new
				// GenericDialog("Dot Charts","Coming soon.... Yes, YOU can get all these...");

				// gendialog.show();

				// Scatter plot

				String[] textArea = dumpTextArea.getText().split("\\n");

				String firstText = "";

				for (int i = 0; i <= textArea.length - 1; i++) {

					firstText = firstText + textArea[i];

					firstText = firstText.trim();

					firstText = firstText + "\n";

				}

				ScatterPlotClient scatterPlotClient = new ScatterPlotClient();

				scatterPlotClient.createLinePloterPDF(username,
						"plotPdfScatter.R", firstText);
			}
		});
		image_5.setSize("87px", "27px");
		PushButton pshbtnNewButton_6 = new PushButton(image_5);
		pshbtnNewButton_6.setStyleName("gwt-PushButton-up-hovering");
		pshbtnNewButton_6.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// GenericDialog gendialog = new
				// GenericDialog("Dot Charts","Coming soon.... Yes, YOU can get all these...");

				// gendialog.show();

				// Scatter plot

				String[] textArea = dumpTextArea.getText().split("\\n");

				String firstText = "";

				for (int i = 0; i <= textArea.length - 1; i++) {

					firstText = firstText + textArea[i];

					firstText = firstText.trim();

					firstText = firstText + "\n";

				}

				ScatterPlotClient scatterPlotClient = new ScatterPlotClient();

				scatterPlotClient.createLinePloterPDF(username,
						"plotPdfScatter.R", firstText);
			}
		});
		rootPanel.add(pshbtnNewButton_6, 501, 546);
		pshbtnNewButton_6.setSize("88px", "27px");

		Image imagert = new Image("realtime.png");
		imagert.setSize("105px", "106px");
		PushButton pushButton = new PushButton(imagert);
		pushButton.setStyleName("gwt-PushButton-up-hovering");
		rootPanel.add(pushButton, 758, 220);
		pushButton.setSize("103px", "109px");
		scrollPanel.setAlwaysShowScrollBars(true);

		rootPanel.add(scrollPanel, 925, 103);
		scrollPanel.setSize("900px", "670px");
				rootPanel.add(grid, 877, 103);
		
				grid.setStyleName("gridpanel-virtual");
				
				Button btnclearGrid = new Button();
				btnclearGrid.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent arg0) {
					
						//grid clear button
						 int numRows = grid.getRowCount();
					      int numColumns = grid.getColumnCount();
					      for (int row = 0; row < numRows; row++) {
					    	  grid.removeRow(row);
					     
					      }
					}
				});
				btnclearGrid.setText("Clear Grid");
				btnclearGrid.setStyleName("gwt-PushButton-up-hovering");
				btnclearGrid.setHTML("<html<b>Clear Grid</b></html>");
				rootPanel.add(btnclearGrid, 638, 352);
				btnclearGrid.setSize("107px", "48px");

		txtrEnterPigLatin.selectAll();

		/*****************************************************************************************/

		// Create the popup dialog box

		dialogBox.setText("Remote Procedure Call");

		dialogBox.setAnimationEnabled(true);

		// We can set the id of a widget by accessing its Element

		closeButton.getElement().setId("closeButton");

		VerticalPanel dialogVPanel = new VerticalPanel();

		dialogVPanel.addStyleName("dialogVPanel");

		dialogVPanel.add(new HTML("<b>Executing command:</b>"));

		dialogVPanel.add(textToServerLabel);

		dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));

		dialogVPanel.add(serverResponseLabel);

		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);

		dialogVPanel.add(closeButton);

		dialogBox.setWidget(dialogVPanel);

		/*****************************************************************************************/

		closeButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {

				dialogBox.hide();

				txtrEnterPigLatin.setEnabled(true);

				txtrEnterPigLatin.setFocus(true);

			}

		});

		/* This is the fetch file button */

		pshbtnInvite.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {

				if (listBox.getSelectedIndex() == -1) {

					Window.alert("Please select a stored file");

					return;

				}

				textToServer = "Fetch:"
						+ listBox.getItemText(listBox.getSelectedIndex());

				p = new MyPopup();

				p.center();

				p.show();

				/**********************************************************************************************/

				sendNameToServer();

			}

		});

		/* This is the execute button */

		clickMeButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {

				p = new MyPopup();

				p.center();

				p.show();

				/**********************************************************************************************/

				dumpTextArea.setText("");

				textToServer = txtrEnterPigLatin.getText();

				composedPigLatinStatments = textToServer;

				sendNameToServer();

			}

		});

	}

	private void sendNameToServer() {

		if (textToServer == null || textToServer.trim().equals("")) {

			textToServer = txtrEnterPigLatin.getText();

		}

		// First, we validate the input.

		errorLabel.setText("");

		if (!FieldVerifier.isValidName(textToServer) && dumpStatement == null
				&& dumpStatement.isEmpty()

				&& dumpStatement.toString().trim().length() == 0) {

			errorLabel.setText("Enter The Correct Pig Latin Statements");

			return;

		}

		if (FieldVerifier.isDumpCommand(textToServer.toString())) {

			thisIsADumpStatement = true;

			/*
			 * Compose the full pig latin deconstructing the text box sentence
			 * 
			 * 
			 * 
			 * and constructing full pig statements. The convertDumpToStore is
			 * prettymuch
			 * 
			 * 
			 * 
			 * composing the whole statement.
			 */

			composedPigLatinStatments = FieldVerifier.convertDumpToStore(
					textToServer, username);

		}

		if (FieldVerifier.checkIfFetchStatement(textToServer.toString())) {

			thisIsAFetchStatement = true;

			composedPigLatinStatments = "Fetch "
					+ listBox.getItemText(listBox.getSelectedIndex());

		}

		/******************************************************************************/

		// Then, we send the input to the server.

		// clickMeButton.setEnabled(false);

		textToServerLabel.setText(textToServer);

		serverResponseLabel.setText("");

		textToServer = composedPigLatinStatments.replaceAll(";\\s+", ";");

		pigExecutionService.greetServer(username + ":" + textToServer,

		new AsyncCallback<String>() {

			public void onFailure(Throwable caught) {

				// Show the RPC error message to the user

				clickMeButton.setEnabled(true);

				dialogBox

				.setText("Pig Statement Call - Failure");

				serverResponseLabel

				.addStyleName("serverResponseLabelError");

				serverResponseLabel.setHTML(SERVER_ERROR);

				dialogBox.center();

				closeButton.setFocus(true);

				timer.cancel();

				p.hide();

			}

			public void onSuccess(String result) {

				// dialogBox.setText("Remote Procedure Call");

				// serverResponseLabel

				// .removeStyleName("serverResponseLabelError");

				// serverResponseLabel.setHTML(result);

				// dialogBox.center();

				// closeButton.setFocus(true);

				// clickMeButton.setEnabled(true);

				/**
				 * Check if statement contained file name and store the file
				 * name
				 */

				if (textToServer.substring(0, 5).trim().toUpperCase()
						.equals("FETCH")
						|| thisIsADumpStatement) {

					/*
					 * With the result we got from pig, the dump return,
					 * populate the grid.
					 */
					// get the field names from here for graph. and add in front of variable "result"
					Utilities utilities = new Utilities();
					String fieldsname = utilities.mainRetriever(txtrEnterPigLatin.getText().trim());
					result = fieldsname.trim() + "\n" + result; 
					dumpTextArea.setText(result.trim());
					textAreaHistory.setText(textAreaHistory.getText() + "\n"
							+ txtrEnterPigLatin.getText());

					String[] dumpResult = result.toString().split("\n");
					String[] dumpResultColumns = dumpResult[0].split("\t");
					grid.resize(dumpResult.length, dumpResultColumns.length);
					/*****************/
					// Put some values in the grid cells.
					;
					for (int row = 0; row <= dumpResult.length - 1; row++) {

						if (dumpResultColumns.length >= 0) {
							for (int col = 0; col <= dumpResultColumns.length - 1; col++) {

								if (row <= 0) {
									// grid = new Grid(dumpResult.length,
									// dumpResultColumns.length);
									grid.getCellFormatter().setStyleName(row,
											col, "tableCell-odd");
								} else {
									grid.getCellFormatter().setStyleName(row,
											col, "tableCell-even");
								}
								if (dumpResultColumns.length - 1 > 0) {
									grid.setText(row, col,
											dumpResultColumns[col]);
								} else {
									grid.setText(row, col, dumpResult[row]);
								}
							}
						}
					}

					// Just for good measure, let's put a button in the center.
					// grid.setWidget(2, 2, new
					// Button("Does nothing, but could"));

					// You can use the CellFormatter to affect the layout of the
					// grid's cells.

					grid.getCellFormatter().setWidth(0, 2, "56px");
					grid.setBorderWidth(5);

					/****************/

					timer.cancel();
					p.hide();

				} else {

					textAreaHistory.setText(textAreaHistory.getText().trim()
							+ "\n" + txtrEnterPigLatin.getText());

					dumpStatement = null;

					boolean isStoreStatement = FieldVerifier
							.checkIfStoreStatement(txtrEnterPigLatin.getText());

					if (isStoreStatement) {

						String theText = FieldVerifier
								.getFileName(txtrEnterPigLatin.getText());

						FieldVerifier.addItemToText(listBox, theText);

					}

				}

			}

		});

	}

	public void getPigScriptsFromServlet(String pigScriptsJson) {

		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST,
				GWT.getHostPageBaseURL() + "getpigfiles");

		StringBuffer postData = new StringBuffer();

		postData.append(URL.encode("username")).append("=")
				.append(URL.encode(pigScriptsJson));

		builder.setHeader("Content-type", "application/x-www-form-urlencoded");

		try {

			builder.setRequestData(postData.toString()); /*
														 * or other
														 * RequestCallback impl
														 */

			builder.setCallback(this);

			com.google.gwt.http.client.Request res = builder.send();

		} catch (Exception e) {

			// handle this

			Window.alert("Exception..." + e.getMessage());

		}

		// File Upload purposes

		form.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {

			public void onSubmitComplete(SubmitCompleteEvent event) {

				// When the form submission is successfully completed, this

				// event is fired. Assuming the service returned a response

				// of type text/html, we can get the result text here

				Window.alert(event.getResults());

				// File Upload - add

				String filename = fileUpload.getFilename();

				filename = filename.substring(filename.lastIndexOf("\\") + 1);

				FieldVerifier.addItemToText(listBox, filename);

			}

		});

		MenuBar menuBar = new MenuBar(false);

		rootPanel.add(menuBar, 5, 5);

		menuBar.setSize("1408px", "35px");

		MenuItem menuItem = new MenuItem("Home", false, new Command() {

			public void execute() {

				// Home command

				Window.Location.assign(GWT.getHostPageBaseURL() + "Login.html");

			}

		});

		menuBar.addItem(menuItem);

		MenuItemSeparator separator = new MenuItemSeparator();

		menuBar.addSeparator(separator);

		MenuItem menuItem_1 = new MenuItem("Query Window", false,
				(Command) null);

		menuItem_1.setEnabled(false);

		menuBar.addItem(menuItem_1);

		MenuItemSeparator separator_1 = new MenuItemSeparator();

		menuBar.addSeparator(separator_1);

		MenuItem menuItem_2 = new MenuItem("Collaboration", false,
				new Command() {
					public void execute() {
					}
				});

		/*
		 * menuItem_2.setHTML("Registration");
		 * 
		 * 
		 * 
		 * menuBar.addItem(menuItem_2);
		 */

		MenuItemSeparator separator_2 = new MenuItemSeparator();

		menuBar.addSeparator(separator_2);

		MenuBar menuBar_1 = new MenuBar(true);

		MenuItem mntmPigDocumentation = new MenuItem("Pig Documentation",false, menuBar_1);

		MenuItem mntmApachePigSite = new MenuItem("Apache Pig Site", false, new Command() {

					public void execute() {

						// sending to apache pig site.

						Window.open("https://pig.apache.org/", "_blank", "");

					}

				});

		menuBar_1.addItem(mntmApachePigSite);

		MenuItem mntmPqtFaq = new MenuItem("PQT FAQ", false, new Command() {

			public void execute() {

				// sending to apache pig site.

				Window.open("https://pig.apache.org/docs/r0.12.0/basic.html",
						"_blank", "");

			}

		});

		menuBar_1.addItem(mntmPqtFaq);

		menuBar.addItem(mntmPigDocumentation);

		MenuItemSeparator separator_3 = new MenuItemSeparator();

		menuBar.addSeparator(separator_3);

		MenuBar menuBar_2 = new MenuBar(true);

		MenuItem mntmCollaboration = new MenuItem("Collaboration", false,
				menuBar_2);

		MenuItem mntmInvitePartner = new MenuItem(
				"Send Pig Script As Attachment", false, new Command() {

					public void execute() {

						EmailDialog myDialog = new EmailDialog(username,
								listBox.getItemText(listBox.getSelectedIndex())
										.toString());

						int left = Window.getClientWidth() / 2;

						int top = Window.getClientHeight() / 2;

						myDialog.setPopupPosition(left, top);

						myDialog.show();

					}

				});

		menuBar_2.addItem(mntmInvitePartner);

		MenuItem mntmSendThisLink = new MenuItem("Send this link", false,
				new Command() {

					public void execute() {

						EmailDialog myDialog = new EmailDialog(username,
								listBox.getItemText(listBox.getSelectedIndex())
										.toString());

						int left = Window.getClientWidth() / 2;

						int top = Window.getClientHeight() / 2;

						myDialog.setPopupPosition(left, top);

						myDialog.show();

					}

				});

		menuBar_2.addItem(mntmSendThisLink);

		menuBar.addItem(mntmCollaboration);

		MenuItemSeparator separator_4 = new MenuItemSeparator();

		menuBar.addSeparator(separator_4);

		MenuItem menuItem_5 = new MenuItem("Large File Analytics R", false,
				new Command() {

					// menuItem_5.setHTML("Large File Processing"){

					public void execute() {

						/* Make a call to the properties constant */

						GenericDialog genericDialogLargeFiles = new GenericDialog(
								"Functionality Available For Enterprise Version",
								constants.enterpriseMessage());

						int left = Window.getClientWidth() / 2;

						int top = Window.getClientHeight() / 2;

						genericDialogLargeFiles.setPopupPosition(left, top);

						genericDialogLargeFiles.show();

					}

				});

		menuBar.addItem(menuItem_5);

		MenuItemSeparator separator_5 = new MenuItemSeparator();

		menuBar.addSeparator(separator_5);

		MenuItem menuItem_6 = new MenuItem("About", false, new Command() {

			public void execute() {

				/* Make a call to the properties constant */

				GenericDialog genericDialog = new GenericDialog(
						"About Pig Latin Engine SaaS", constants.aboutText());

				int left = Window.getClientWidth() / 2;

				int top = Window.getClientHeight() / 2;

				genericDialog.setPopupPosition(left, top);

				genericDialog.show();

			}

		});

		menuBar.addItem(menuItem_6);

		rootPanel.add(panel, 780, 439);

		panel.setSize("139px", "94px");

		panel.setSpacing(10);

		RootPanel.get("gwtContainer").add(form, 780, 418);

	}

	@Override
	public void onResponseReceived(com.google.gwt.http.client.Request request,

	Response response) {

		// TODO Auto-generated method stub

		fullPigScripts = new StringBuilder(response.getText());

		if (fullPigScripts.toString() != null
				&& fullPigScripts.toString().trim().length() != 0) {

			// Populate the list box

			FieldVerifier.populateListBox(fullPigScripts, this.listBox,
					username);

		}

	}

	@Override
	public void onError(com.google.gwt.http.client.Request request,

	Throwable exception) {

		// TODO Auto-generated method stub

		Window.alert(exception.getMessage() + "ERROR");

	}

	class MyPopup extends PopupPanel {

		boolean incremental = false;

		Label contents = null;

		public MyPopup() {

			super(true);

			contents = new Label("Pig processing......");

			contents.setWidth("400px");

			setWidget(contents);

			setStyleName("popups-Popup");

			setAnimationEnabled(true);

			timer = new Timer()

			{

				@Override
				public void run()

				{

					contents.setVisible(!incremental);

					incremental = !incremental;

				}

			};

			timer.scheduleRepeating(500);

		}

	}
}