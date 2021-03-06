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
package com.github.aamm.hyphenType.tests.regex;

import com.github.aamm.hyphenType.datastructure.Options;
import com.github.aamm.hyphenType.datastructure.annotations.ArgumentsObject;
import com.github.aamm.hyphenType.datastructure.annotations.Option;
import com.github.aamm.hyphenType.datastructure.annotations.OptionArgument;
import com.github.aamm.hyphenType.exit.CanonicalExitCode;

/**
 * @author Aurelio Akira M. Matsui
 */
@ArgumentsObject(singleHyphen = "-", doubleHyphen = "--")
public interface RegexInterface2 extends Options<CanonicalExitCode> {

    @Option
    boolean x();

    @OptionArgument(index = 0, option = "x", regex = "red|green|blue")
    String xa0();

    @OptionArgument(index = 1, option = "x", regex = "apple|leaf|sky")
    String xa1();

    @Option
    boolean y();
}
