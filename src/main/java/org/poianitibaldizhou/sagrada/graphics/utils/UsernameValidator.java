package org.poianitibaldizhou.sagrada.graphics.utils;

import com.jfoenix.validation.base.ValidatorBase;
import javafx.scene.control.TextInputControl;

/**
 * Validator for username
 */
public class UsernameValidator extends ValidatorBase {

    @Override
    protected void eval() {
        if (this.srcControl.get() instanceof TextInputControl) {
            this.evalTextInputField();
        }
    }

    private void evalTextInputField() {
        TextInputControl textField = (TextInputControl)this.srcControl.get();
        if(textField.getText().matches("^[a-zA-Z0-9]*$"))
            this.hasErrors.set(false);
        else
            this.hasErrors.set(true);
    }
}
