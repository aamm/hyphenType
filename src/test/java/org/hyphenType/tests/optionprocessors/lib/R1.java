/**
 * 
 */
package org.hyphenType.tests.optionprocessors.lib;

import org.hyphenType.optionprocessors.lib.CustomizableValidatorEngine.Rule;

public class R1 implements Rule<CustomizableValidatorTestInterface> {
    @Override
    public void validate(CustomizableValidatorTestInterface option) {
        if(option.a() && option.b())
            option.exit(7);
    }
}