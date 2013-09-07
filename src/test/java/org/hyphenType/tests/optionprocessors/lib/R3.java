/**
 * 
 */
package org.hyphenType.tests.optionprocessors.lib;

import org.hyphenType.optionprocessors.lib.CustomizableValidatorEngine.Rule;

public class R3 implements Rule<CustomizableValidatorTestInterface> {
    @Override
    public void validate(CustomizableValidatorTestInterface option) {
        if(option.c() && option.a())
            option.exit(13);
    }
}