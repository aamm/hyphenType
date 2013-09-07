/**
 * 
 */
package org.hyphenType.tests.optionprocessors.lib;

import org.hyphenType.optionprocessors.lib.CustomizableValidatorEngine.Rule;

public class R2 implements Rule<CustomizableValidatorTestInterface> {
    @Override
    public void validate(CustomizableValidatorTestInterface option) {
        if(option.b() && option.c())
            option.exit(11);
    }
}