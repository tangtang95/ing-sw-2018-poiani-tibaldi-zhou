package org.poianitibaldizhou.sagrada.graphics.utils;

import com.jfoenix.validation.base.ValidatorBase;
import javafx.scene.control.TextInputControl;

/**
 * Validator for username
 */
public class UsernameValidator extends ValidatorBase {

    /**
     * Check that the evaluated input contains only characters.
     */
    @Override
    protected void eval() {
        if (this.srcControl.get() instanceof TextInputControl) {
            this.evalTextInputField();
        }
    }

    /**
     * Evaluates the text input for a field and it has to contains only characters.
     * If this is not respected, has error is set to true.
     */
    private void evalTextInputField() {
        TextInputControl textField = (TextInputControl)this.srcControl.get();
        if(textField.getText().matches("^[a-zA-Z0-9]*$"))
            this.hasErrors.set(false);
        else
            this.hasErrors.set(true);
    }
}
