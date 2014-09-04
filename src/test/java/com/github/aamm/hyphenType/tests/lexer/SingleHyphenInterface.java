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
package com.github.aamm.hyphenType.tests.lexer;

import java.util.Map;

import com.github.aamm.hyphenType.datastructure.Options;
import com.github.aamm.hyphenType.datastructure.annotations.ArgumentsObject;
import com.github.aamm.hyphenType.datastructure.annotations.Option;
import com.github.aamm.hyphenType.datastructure.annotations.OptionMapValue;
import com.github.aamm.hyphenType.datastructure.annotations.SimpleArgument;
import com.github.aamm.hyphenType.exit.CanonicalExitCode;

/**
 * @author Aurelio Akira M. Matsui
 */
@ArgumentsObject(doubleHyphenInLongOptions = false, resourceBundlesLocation = "", description = "A very long description to test my framework.\nAnd here is another line.")
public interface SingleHyphenInterface extends Options<CanonicalExitCode> {

    @SimpleArgument(index = 0, mandatory = true)
    String value();

    @SimpleArgument(index = 1, mandatory = true)
    int value2();

    @SimpleArgument(index = 2)
    String[] value3();

    @Option(description = "Option x", names = "pineapple")
    boolean x();

    @Option(description = "X1")
    boolean D();

    @OptionMapValue(option = "D", keyName = "key", valueName = "value")
    Map<String, String> Dvalue();
}
