package org.hyphenType.tests.optionprocessors.lib;

import org.hyphenType.datastructure.Options;
import org.hyphenType.datastructure.annotations.Option;
import org.hyphenType.exit.CanonicalExitCode;
import org.hyphenType.optionprocessors.lib.CustomizableValidatorEngine.CustomizableValidator;

@CustomizableValidator(rules = { R1.class, R2.class, R3.class })
public interface CustomizableValidatorTestInterface extends Options<CanonicalExitCode> {
    
    @Option
    boolean a();

    @Option
    boolean b();

    @Option
    boolean c();
}
