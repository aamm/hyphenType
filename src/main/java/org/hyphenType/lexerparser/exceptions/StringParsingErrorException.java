package org.hyphenType.lexerparser.exceptions;

import org.hyphenType.datastructure.Options;

/**
 * TODO Change the name of this class. The current name does not say anything
 * about what this exception means.
 * 
 * @author akira
 */
public class StringParsingErrorException extends OptionValuesException {
    
    private static final long serialVersionUID = -8487820729585487956L;
    
    public static final String DEFAULT_PATTERN = "Could not parse input \"%s\" to the type %s.";
    
    public StringParsingErrorException(Throwable cause, Class<? extends Options<?>> optionsInterface, String input, Class<?> type) {
        super(cause, optionsInterface, DEFAULT_PATTERN, input, type.getName());
    }
}
