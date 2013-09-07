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
package org.hyphenType.tests.parser;

import java.util.Map;

import org.hyphenType.datastructure.Options;
import org.hyphenType.datastructure.annotations.ArgumentsObject;
import org.hyphenType.datastructure.annotations.Option;
import org.hyphenType.datastructure.annotations.OptionMapValue;
import org.hyphenType.datastructure.annotations.OptionValue;
import org.hyphenType.datastructure.annotations.SimpleArgument;
import org.hyphenType.exit.CanonicalExitCode;

/**
 * @author Aurelio Akira M. Matsui
 */
@ArgumentsObject(doubleHyphenInLongOptions = true, resourceBundlesLocation = "", description = "Test")
public interface MyOptions extends Options<CanonicalExitCode> {

    @Option
    public boolean a();

    @Option
    public boolean b();

    @OptionValue(option="b")
    String[] array();
    
    @Option
    public boolean c();

    @OptionValue(option = "c", mandatory = true)
    public String ca();

    @Option
    public boolean d();

    @OptionMapValue(option = "d", mandatory = true, keyName = "a", valueName = "b", valueType = Integer.class)
    public Map<String, Integer> dm();

    @SimpleArgument(index = 0)
    public String a1();

    @SimpleArgument(index = 1)
    public String a2();
}
