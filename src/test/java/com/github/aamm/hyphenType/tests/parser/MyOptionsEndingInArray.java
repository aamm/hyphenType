package com.github.aamm.hyphenType.tests.parser;

import com.github.aamm.hyphenType.datastructure.Options;
import com.github.aamm.hyphenType.datastructure.annotations.ArgumentsObject;
import com.github.aamm.hyphenType.datastructure.annotations.Option;
import com.github.aamm.hyphenType.datastructure.annotations.OptionValue;
import com.github.aamm.hyphenType.datastructure.annotations.SimpleArgument;
import com.github.aamm.hyphenType.exit.CanonicalExitCode;

@ArgumentsObject(doubleHyphenInLongOptions = true, resourceBundlesLocation = "", description = "Test")
public interface MyOptionsEndingInArray extends Options<CanonicalExitCode> {

    @Option
    public int x();
    
    @OptionValue(option = "x", arrayUseFileSeparator=true)
    public String[] xArray();
    
    @Option
    public int y();
    
    @OptionValue(option = "y", arraySeparator="#")
    public String[] yArray();
    
    @SimpleArgument(index = 0)
    public String a1();

    @SimpleArgument(index = 1)
    public String a2();

    @SimpleArgument(index = 2)
    public String[] a3();
}
