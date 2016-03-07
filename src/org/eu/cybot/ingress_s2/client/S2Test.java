package org.eu.cybot.ingress_s2.client;

import com.google.common.geometry.S2Cell;
import com.google.common.geometry.S2CellId;
import com.google.common.geometry.S2LatLng;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class S2Test implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		final TextBox latField1 = new TextBox();
		final TextBox lngField1 = new TextBox();
		latField1.setText("lat");
		lngField1.setText("lng");
		final Button calcButton1 = new Button("Calc...");
		calcButton1.addStyleName("sendButton");
		final Label resultLabel1 = new Label();

		RootPanel.get("inputFieldContainer1").add(latField1);
		RootPanel.get("inputFieldContainer1").add(lngField1);
		RootPanel.get("calculateButtonContainer1").add(calcButton1);
		RootPanel.get("resultContainer1").add(resultLabel1);

		latField1.setFocus(true);
		latField1.selectAll();

		ClickHandler handler1 = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					S2LatLng ll = S2LatLng.fromDegrees(new Double(latField1.getText()), new Double(lngField1.getText()));
					S2Cell c = new S2Cell(ll);
					CellNamer cn = new CellNamer(c.id());

					resultLabel1.setText(cn.getName());
				} catch (Exception e) {
					resultLabel1.setText(e.getMessage());
				}
			}
		};
		calcButton1.addClickHandler(handler1);

		final TextBox idField2 = new TextBox();
		idField2.setText("cell ID");
		final Button calcButton2 = new Button("Calc...");
		calcButton1.addStyleName("sendButton");
		final Label resultLabel2 = new Label();

		RootPanel.get("inputFieldContainer2").add(idField2);
		RootPanel.get("calculateButtonContainer2").add(calcButton2);
		RootPanel.get("resultContainer2").add(resultLabel2);

		ClickHandler handler2 = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					S2CellId c = CellNamer.parseName(idField2.getText());
					S2LatLng ll = c.toLatLng();
					resultLabel2.setText(ll.latDegrees() + "," + ll.lngDegrees() + " " + c.toString());
				} catch (Exception e) {
					resultLabel1.setText(e.getMessage());
				}
			}
		};
		calcButton2.addClickHandler(handler2);

//		// Create the popup dialog box
//		final DialogBox dialogBox = new DialogBox();
//		dialogBox.setText("Remote Procedure Call");
//		dialogBox.setAnimationEnabled(true);
//		final Button closeButton = new Button("Close");
//		// We can set the id of a widget by accessing its Element
//		closeButton.getElement().setId("closeButton");
//		final Label textToServerLabel = new Label();
//		final HTML serverResponseLabel = new HTML();
//		VerticalPanel dialogVPanel = new VerticalPanel();
//		dialogVPanel.addStyleName("dialogVPanel");
//		dialogVPanel.add(new HTML("<b>Sending name to the server:</b>"));
//		dialogVPanel.add(textToServerLabel);
//		dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
//		dialogVPanel.add(serverResponseLabel);
//		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
//		dialogVPanel.add(closeButton);
//		dialogBox.setWidget(dialogVPanel);
//
//		// Add a handler to close the DialogBox
//		closeButton.addClickHandler(new ClickHandler() {
//			public void onClick(ClickEvent event) {
//				dialogBox.hide();
//			}
//		});
	}
}
