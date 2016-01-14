package com.riverlog.pig.ui.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;



public class LoadPdfFromURL {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public boolean LoadPDFFromURL(String urls) {
	
		Window.open(urls, "", "");
	//	Window.open("data:application/pdf;base64," + result, cRFTitle.replace(" ", "_") + ".pdf", "enabled");
		return true;
    }
}