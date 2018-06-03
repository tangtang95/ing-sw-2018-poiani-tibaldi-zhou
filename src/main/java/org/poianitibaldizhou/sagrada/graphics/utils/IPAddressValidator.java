package org.poianitibaldizhou.sagrada.graphics.utils;

import com.jfoenix.validation.base.ValidatorBase;
import javafx.scene.control.TextInputControl;

public class IPAddressValidator extends ValidatorBase{

    @Override
    protected void eval() {
        if (this.srcControl.get() instanceof TextInputControl) {
            this.evalTextInputField();
        }
    }

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
