/**
 * 
 */
package com.github.aamm.hyphenType.tests.optionprocessors.lib;

import com.github.aamm.hyphenType.optionprocessors.lib.CustomizableValidatorEngine.Rule;

public class R3 implements Rule<CustomizableValidatorTestInterface> {
    @Override
    public void validate(CustomizableValidatorTestInterface option) {
        if(option.c() && option.a())
            option.exit(13);
    }
}