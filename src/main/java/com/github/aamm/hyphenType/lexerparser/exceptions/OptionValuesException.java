package com.github.aamm.hyphenType.lexerparser.exceptions;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.github.aamm.hyphenType.datastructure.Options;
import com.github.aamm.hyphenType.util.I18NResourceBundle;

public abstract class OptionValuesException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -4134292017377886083L;
    
    private String localizedMessage;
    
    public OptionValuesException(Class<? extends Options<?>> optionsInterface, String pattern, Object... arguments) {
        super(MessageFormat.format(pattern, arguments));
        ResourceBundle rb = new I18NResourceBundle(optionsInterface);
        String key = this.getClass().getName();
        try {
            if(rb.containsKey(key)) {
                this.localizedMessage = MessageFormat.format(rb.getString(key), arguments);
            } else {
                this.localizedMessage = this.getMessage();
            }
        } catch (MissingResourceException e) {
            System.err.println("Key " + key + " not found.");
            this.localizedMessage = this.getMessage();
        }
    }
    
    public OptionValuesException(Throwable cause, Class<? extends Options<?>> optionsInterface, String pattern, Object... arguments) {
        super(MessageFormat.format(pattern, arguments), cause);
        ResourceBundle rb = new I18NResourceBundle(optionsInterface);
        String key = this.getClass().getName();
        try {
            if(rb.containsKey(key)) {
                this.localizedMessage = MessageFormat.format(rb.getString(key), arguments);
            } else {
                this.localizedMessage = this.getMessage();
            }
        } catch (MissingResourceException e) {
            System.err.println("Key " + key + " not found.");
            this.localizedMessage = this.getMessage();
        }
    }
    
    @Override
    public String getLocalizedMessage() {
        return localizedMessage;
    }
}
