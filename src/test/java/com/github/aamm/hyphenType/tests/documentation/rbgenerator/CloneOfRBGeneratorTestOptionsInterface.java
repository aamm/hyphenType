package com.github.aamm.hyphenType.tests.documentation.rbgenerator;

import com.github.aamm.hyphenType.datastructure.Options;
import com.github.aamm.hyphenType.datastructure.annotations.ArgumentsObject;
import com.github.aamm.hyphenType.datastructure.annotations.Option;
import com.github.aamm.hyphenType.datastructure.annotations.OptionValue;
import com.github.aamm.hyphenType.datastructure.annotations.SimpleArgument;
import com.github.aamm.hyphenType.exit.CanonicalExitCode;

@ArgumentsObject(description="59867")
public interface CloneOfRBGeneratorTestOptionsInterface extends Options<CanonicalExitCode> {

    @Option(description="9687", names = {"x", "xx", "xxx"})
    boolean a();

    @Option(description="werre")
    boolean b();
    
    @OptionValue(option="b", mandatory=false)
    String bValue();
    
    @SimpleArgument(description="xyz")
    String arg1();
}
