package com.github.aamm.hyphenType.optionsextractor;

public class OptionsExtractorException extends Exception {
    
    private static final long serialVersionUID = -3472033083135157142L;
    
    OptionsExtractorException(String message) {
        super(message);
    }
    
    OptionsExtractorException(String message, Throwable cause) {
        super(message, cause);
    }
}
