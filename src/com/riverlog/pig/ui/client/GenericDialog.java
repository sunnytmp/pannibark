package com.riverlog.pig.ui.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class GenericDialog extends DialogBox {

	public GenericDialog(String title, String textOnWindow){
		setText(title);
		setGlassEnabled(true);
		
		// DialogBox is a SimplePanel, so you have to set its widget 
        // property to whatever you want its contents to be.
        Button ok = new Button("OK");
        ok.addClickHandler(new ClickHandler() {
           public void onClick(ClickEvent event) {
              GenericDialog.this.hide();
           }
        });
        
        
        Label label = new Label(textOnWindow);

        VerticalPanel panel = new VerticalPanel();
        panel.setHeight("100");
        panel.setWidth("300");
        panel.setSpacing(10);
        panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        panel.add(label);
        panel.add(ok);

        setWidget(panel);
        
	}
}
