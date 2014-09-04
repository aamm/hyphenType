package com.github.aamm.hyphenType.lexerparser.exceptions;

import com.github.aamm.hyphenType.datastructure.Options;

public class MandatoryValueNotFoundException extends OptionValuesException {
    
    private static final long serialVersionUID = 7469667198090717957L;
    
    public static final String DEFAULT_PATTERN = "Mandatory value {0} in option {1} not available.";
    
    public MandatoryValueNotFoundException(Class<? extends Options<?>> optionsInterface, String optionName, String valueName) {
        super(optionsInterface, DEFAULT_PATTERN, optionName, valueName);
    }
}
