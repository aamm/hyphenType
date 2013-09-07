package org.hyphenType.lexerparser.exceptions;

import org.hyphenType.datastructure.Options;

public class MandatorySimpleArgumentNotFoundException extends OptionValuesException {
    
    private static final long serialVersionUID = 6088913337292159636L;
    
    public static final String DEFAULT_PATTERN = "Mandatory simple argument \"{0}\" not available.";
    
    public MandatorySimpleArgumentNotFoundException(Throwable cause, Class<? extends Options<?>> optionsInterface, String simpleArgumentName) {
        super(cause, optionsInterface, DEFAULT_PATTERN, simpleArgumentName);
    }
}
