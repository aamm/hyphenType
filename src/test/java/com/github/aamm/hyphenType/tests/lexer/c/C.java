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
package com.github.aamm.hyphenType.tests.lexer.c;

import java.util.Map;

import com.github.aamm.hyphenType.datastructure.Options;
import com.github.aamm.hyphenType.datastructure.annotations.ArgumentsObject;
import com.github.aamm.hyphenType.datastructure.annotations.Option;
import com.github.aamm.hyphenType.datastructure.annotations.OptionArgument;
import com.github.aamm.hyphenType.datastructure.annotations.OptionMapValue;
import com.github.aamm.hyphenType.datastructure.annotations.OptionValue;
import com.github.aamm.hyphenType.datastructure.annotations.SimpleArgument;
import com.github.aamm.hyphenType.documentation.lib.HTMLFormatterEngine.HTMLFormatter;
import com.github.aamm.hyphenType.documentation.lib.StandardFormatterEngine.StandardFormatter;
import com.github.aamm.hyphenType.exit.CanonicalExitCode;
import com.github.aamm.hyphenType.tests.lexer.MyType;

/**
 * @author Aurelio Akira M. Matsui
 */
@ArgumentsObject(
/**/doubleHyphenInLongOptions = true,
/**/resourceBundlesLocation = "",
/**/description = "A very long description to test my framework.\nAnd here is another line.",
/**/preferredDocumentationFormatter = HTMLFormatter.class)
@StandardFormatter(options = "aaaaa")
public interface C extends Options<CanonicalExitCode>, B {

    @SimpleArgument(index = 0, mandatory = true)
    String value();

    @SimpleArgument(index = 1, mandatory = true)
    int value2();

    @SimpleArgument(index = 2)
    String[] value3();

    @Option(description = "Option x")
    boolean x();

    @Option(names = { "b", "c", "testing-a-very-long-option" }, description = "Option y")
    boolean y();

    @Option(names = "t", description = "Option t")
    boolean z();

    @OptionValue(name = "BLA", option = "z", mandatory = true)
    MyType zBLA();

    @Option(description = "Option w")
    boolean w();

    @OptionArgument(index = 0, option = "w", mandatory = true, name = "A1")
    String f();

    @OptionArgument(index = 1, option = "w", mandatory = true, name = "A2")
    String h();

    @OptionArgument(index = 2, option = "w", mandatory = false, name = "A3")
    int[] e();

    @Option(description = "This is a description of the map option. To use this option you have to pass key value pairs.")
    boolean D();

    @OptionMapValue(option = "D", keyName = "key", valueName = "value")
    Map<String, String> Dvalue();
}
