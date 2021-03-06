package org.poianitibaldizhou.sagrada.graphics.utils;

import com.jfoenix.validation.base.ValidatorBase;
import javafx.scene.control.TextInputControl;

/**
 * Validator class for IP addresses
 */
public class IPAddressValidator extends ValidatorBase{

    /**
     * Evaluate if the string is correct and match with the ip structure
     */
    @Override
    protected void eval() {
        if (this.srcControl.get() instanceof TextInputControl) {
            this.evalTextInputField();
        }
    }

    /**
     * Evaluate the input of a text field and match it with the ip structure
     */
    private void evalTextInputField() {
        TextInputControl textField = (TextInputControl)this.srcControl.get();
        if(textField.getText().isEmpty())
            this.hasErrors.set(false);
        else if(textField.getText().matches("^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}" +
                "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$"))
            this.hasErrors.set(false);
        else
            this.hasErrors.set(true);
    }
}
