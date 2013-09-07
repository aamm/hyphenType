package org.hyphenType.lexerparser.exceptions;

import org.hyphenType.datastructure.Options;

public class MandatoryMapValueNotFoundException extends OptionValuesException {
    
    private static final long serialVersionUID = -1039441597828674960L;
    
    public static final String DEFAULT_PATTERN = "Mandatory map {0}{1}{2} in option {3} not available.";
    
    public MandatoryMapValueNotFoundException(Class<? extends Options<?>> optionsInterface, String keyName, String equalsSign, String valueName, String optionName) {
        super(optionsInterface, DEFAULT_PATTERN, keyName, equalsSign, valueName, optionName);
    }
}
