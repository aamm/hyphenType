package com.github.aamm.hyphenType.tests.optionprocessors.lib;

import com.github.aamm.hyphenType.datastructure.Options;
import com.github.aamm.hyphenType.datastructure.annotations.Option;
import com.github.aamm.hyphenType.exit.CanonicalExitCode;
import com.github.aamm.hyphenType.optionprocessors.lib.CustomizableValidatorEngine.CustomizableValidator;

@CustomizableValidator(rules = { R1.class, R2.class, R3.class })
public interface CustomizableValidatorTestInterface extends Options<CanonicalExitCode> {
    
    @Option
    boolean a();

    @Option
    boolean b();

    @Option
    boolean c();
}
