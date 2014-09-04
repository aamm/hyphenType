/*
 * This file is part of hyphenType.
 * 
 * hyphenType is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * hyphenType is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with hyphenType.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.aamm.hyphenType.tests.case3_complex_interface;

import com.github.aamm.hyphenType.datastructure.Options;
import com.github.aamm.hyphenType.datastructure.annotations.ArgumentsObject;
import com.github.aamm.hyphenType.datastructure.annotations.Option;
import com.github.aamm.hyphenType.datastructure.annotations.OptionArgument;
import com.github.aamm.hyphenType.datastructure.annotations.OptionValue;
import com.github.aamm.hyphenType.datastructure.annotations.SimpleArgument;
import com.github.aamm.hyphenType.exit.CanonicalExitCode;
import com.github.aamm.hyphenType.optionprocessors.lib.BooleanValidatorEngine.BooleanValidator;
import com.github.aamm.hyphenType.optionprocessors.lib.BooleanValidatorEngine.Rule;

/**
 * @author Aurelio Akira M. Matsui
 */
@ArgumentsObject(doubleHyphenInLongOptions = true, description = "Description")
@BooleanValidator(
        rules = { @Rule(rule = "abc")}
)
public interface MyComplexOptions extends Options<CanonicalExitCode> {

    @Option
    boolean x();
    
    @OptionValue(option = "x")
    String xOption();
    
    @OptionArgument(option = "x", index = 0, mandatory = true)
    String xarg1();
    
    @OptionArgument(option = "x", index = 1)
    long xarg2();
    
    @OptionArgument(option = "x", index = 2)
    MyType[] xarg3();
    
    @Option
    int y();
    
    @SimpleArgument(index = 0)
    String sa0();
    
    @SimpleArgument(index = 1)
    int sa1();
    
    @SimpleArgument(index = 2)
    boolean sa2();
    
    @SimpleArgument(index = 3)
    X[] sa3();
}
