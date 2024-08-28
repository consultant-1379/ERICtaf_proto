package com.ericsson.cifwk.taf.demo.calculator.test.pages;

import com.ericsson.cifwk.taf.demo.calculator.test.CalculatorOperation;
import com.ericsson.cifwk.taf.ui.UI;
import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.Button;
import com.ericsson.cifwk.taf.ui.sdk.GenericViewModel;
import com.ericsson.cifwk.taf.ui.sdk.Label;
import com.ericsson.cifwk.taf.ui.sdk.Select;
import com.ericsson.cifwk.taf.ui.sdk.TextBox;

public class CalculatorViewModel extends GenericViewModel {
	@UiComponentMapping(id="variable1")
	private TextBox variable1;

	@UiComponentMapping(id="variable2")
	private TextBox variable2;

	@UiComponentMapping(id="operation")
	private Select operations;

	@UiComponentMapping(id="result")
	private TextBox result;

	@UiComponentMapping(id="okButton")
	private Button okButton;

	@UiComponentMapping(id="errorMessage")
	private Label errorMessage;

	public String calculate(String value1, String value2, CalculatorOperation operation) {
		variable1.setText(value1);
		variable2.setText(value2);
		
		operations.selectByValue(operation.value());
		
		okButton.click();
		
		UI.pause(2000);
		
		return result.getText();
	}

	@Override
	public boolean isCurrentView() {
		return hasComponent("demoCalcHeading");
	}
	
	public Label getErrorMessage() {
		return errorMessage;
	}
}
