/**
 * 
 */
package com.github.aamm.hyphenType.tests.optionprocessors.lib;

import com.github.aamm.hyphenType.optionprocessors.lib.CustomizableValidatorEngine.Rule;

public class R1 implements Rule<CustomizableValidatorTestInterface> {
    @Override
    public void validate(CustomizableValidatorTestInterface option) {
        if(option.a() && option.b())
            option.exit(7);
    }
}