package org.hyphenType.lexerparser.exceptions;

import org.hyphenType.datastructure.Options;

public class RegexMismatchException extends OptionValuesException {
    
    private static final long serialVersionUID = 7465957351508605749L;
    
    public static final String DEFAULT_PATTERN = "The value {0} is not suitable for the option argument {1}, which uses the regex \"{2}\"";
    
    public RegexMismatchException(Class<? extends Options<?>> optionsInterface, String value, String name, String regex) {
        super(optionsInterface, DEFAULT_PATTERN, value, name, regex);
    }
}
