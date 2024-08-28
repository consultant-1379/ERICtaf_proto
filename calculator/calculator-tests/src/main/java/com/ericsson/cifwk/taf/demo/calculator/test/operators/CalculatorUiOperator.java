package com.ericsson.cifwk.taf.demo.calculator.test.operators;

import java.util.Map;

import javax.inject.Singleton;

import com.ericsson.cifwk.taf.UiOperator;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.data.Ports;
import com.ericsson.cifwk.taf.demo.calculator.test.CalculatorOperation;
import com.ericsson.cifwk.taf.demo.calculator.test.helpers.CalculatorOperatorHelper;
import com.ericsson.cifwk.taf.demo.calculator.test.pages.CalculatorViewModel;
import com.ericsson.cifwk.taf.ui.Browser;
import com.ericsson.cifwk.taf.ui.BrowserTab;
import com.ericsson.cifwk.taf.ui.BrowserType;
import com.ericsson.cifwk.taf.ui.UI;
import com.ericsson.cifwk.taf.ui.sdk.Label;

@Operator(context = Context.UI)
@Singleton
public class CalculatorUiOperator implements CalculatorOperator, UiOperator {
    private final Browser browser;
    private final BrowserTab browserTab;
    private final CalculatorViewModel calcView;

    public CalculatorUiOperator() {
        String url = getCalcUrl();
        this.browser = UI.newBrowser(BrowserType.FIREFOX);
        this.browserTab = browser.open(url);
        this.calcView = browserTab.getView(CalculatorViewModel.class);
    }

    private String getCalcUrl() {
        Host calcHost = DataHandler.getHostByName("calculator");
        String uri = (String) DataHandler.getAttribute("calculator.web.uri");
        Map<Ports, String> portMap = calcHost.getPort();
        String calcPort = portMap.get(Ports.HTTP);
        if (calcPort == null) {
            throw new IllegalArgumentException("HTTP port not defined for host 'calculator'");
        }

        return String.format("http://%s:%s%s", calcHost.getIp(), calcPort, uri);
    }

    @Override
    public String add(String value1, String value2) {
        return performOperation(value1, value2, CalculatorOperation.ADD);
    }

    public boolean isValidationError() {
        Label errorMessage = calcView.getErrorMessage();
        return errorMessage.isDisplayed();
    }

    @Override
    public String subtract(String value1, String value2) {
        return performOperation(value1, value2, CalculatorOperation.SUBTRACT);
    }

    @Override
    public String multiply(String value1, String value2) {
        return performOperation(value1, value2, CalculatorOperation.MULTIPLY);
    }

    @Override
    public String divide(String value1, String value2) {
        return performOperation(value1, value2, CalculatorOperation.DIVIDE);
    }

    private String performOperation(String value1, String value2, CalculatorOperation operation) {
        String result = calcView.calculate(value1, value2, operation);
        if (!CalculatorOperatorHelper.isNumeric(result)) {
            if ("DIV/0".equals(result)) {
                return CalculatorOperator.DIVISION_BY_ZERO_ATTEMPT;
            } else {
                return CalculatorOperator.OPERATION_FAILED;
            }
        }

        return result;
    }
}
