package org.hyphenType.tests.documentation.rbgenerator;

import org.hyphenType.datastructure.Options;
import org.hyphenType.datastructure.annotations.ArgumentsObject;
import org.hyphenType.datastructure.annotations.Option;
import org.hyphenType.datastructure.annotations.OptionValue;
import org.hyphenType.datastructure.annotations.SimpleArgument;
import org.hyphenType.exit.CanonicalExitCode;

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
