/**
 * 
 */
package com.github.aamm.hyphenType.tests.documentation.lib;

import com.github.aamm.hyphenType.datastructure.Options;
import com.github.aamm.hyphenType.datastructure.annotations.ArgumentsObject;
import com.github.aamm.hyphenType.datastructure.annotations.Option;
import com.github.aamm.hyphenType.datastructure.annotations.OptionValue;
import com.github.aamm.hyphenType.datastructure.annotations.SimpleArgument;

@SuppressWarnings("unchecked")
@ArgumentsObject(description="Testing description")
interface MyOptInt extends Options {
    @Option(description="Descripton a")
    boolean a();
    
    @Option(description="Description b")
    boolean b();
    
    @OptionValue(option="b")
    String bValue();
    
    @SimpleArgument(description="Description argument1")
    String argument1();
    
    @SimpleArgument(description="Description argument2")
    String argument2();
}