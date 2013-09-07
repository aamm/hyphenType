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
package org.hyphenType.tests.optionprocessors;

import org.hyphenType.datastructure.Options;
import org.hyphenType.datastructure.annotations.ArgumentsObject;
import org.hyphenType.datastructure.annotations.Option;
import org.hyphenType.exit.CanonicalExitCode;
import org.hyphenType.tests.optionprocessors.MyProcessor.MyValidatorE;

/**
 * @author Aurelio Akira M. Matsui
 */
@ArgumentsObject
@MyValidatorE(a = 11, b = 43)
public interface MyOptions extends Options<CanonicalExitCode> {

    @Option
    int x();

    @Option
    int y();
}
