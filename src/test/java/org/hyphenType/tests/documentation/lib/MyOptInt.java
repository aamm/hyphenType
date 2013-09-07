/**
 * 
 */
package org.hyphenType.tests.documentation.lib;

import org.hyphenType.datastructure.Options;
import org.hyphenType.datastructure.annotations.ArgumentsObject;
import org.hyphenType.datastructure.annotations.Option;
import org.hyphenType.datastructure.annotations.OptionValue;
import org.hyphenType.datastructure.annotations.SimpleArgument;

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