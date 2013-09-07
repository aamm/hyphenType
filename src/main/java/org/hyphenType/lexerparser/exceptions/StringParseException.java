package org.hyphenType.lexerparser.exceptions;

import org.hyphenType.datastructure.Options;

public class StringParseException extends OptionValuesException {
    
    private static final long serialVersionUID = 8177222378273441786L;
    
    public static final String DEFAULT_PATTERN = "Invalid value: {0}.";
    
    public StringParseException(final Throwable cause, final Class<? extends Options<?>> optionsInterface, final String value) {
        super(cause, optionsInterface, DEFAULT_PATTERN, value);
    }
}
