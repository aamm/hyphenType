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
package org.hyphenType.tests.regex;

import org.hyphenType.datastructure.Options;
import org.hyphenType.datastructure.annotations.ArgumentsObject;
import org.hyphenType.datastructure.annotations.Option;
import org.hyphenType.datastructure.annotations.SimpleArgument;
import org.hyphenType.exit.CanonicalExitCode;

/**
 * @author Aurelio Akira M. Matsui
 */
@ArgumentsObject(singleHyphen = "@", doubleHyphen = "@@")
public interface RegexInterface1 extends Options<CanonicalExitCode> {

    @SimpleArgument(index = 0, mandatory = true, regex = "@@bla")
    String value();

    @SimpleArgument(index = 1, mandatory = true, regex = "\\Hxyz")
    String value2();

    @SimpleArgument(index = 2, mandatory = false, regex = "\\h\\d\\d")
    // Hyphen and two digits.
    String[] value3();

    @Option
    boolean x();

    @Option
    boolean y();
}
