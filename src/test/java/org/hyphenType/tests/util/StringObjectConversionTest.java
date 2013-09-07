/*
 * This file is part of hyphenType. hyphenType is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. hyphenType is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with hyphenType. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.hyphenType.tests.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.hyphenType.tests.case3_complex_interface.X;
import org.hyphenType.util.soc.StringObjectConversion;
import org.hyphenType.util.soc.StringParsingError;
import org.junit.Test;

/**
 * @author Aurelio Akira M. Matsui
 */
public class StringObjectConversionTest {

    @Test
    public void testEnum() throws StringParsingError {
	X x1 = StringObjectConversion.fromString(X.class, "A");
	assertEquals(X.A, x1);
	X x2 = StringObjectConversion.fromString(X.class, "C");
	assertEquals(X.C, x2);
	X[] x = StringObjectConversion.fromString(X[].class, "[C, A, C, C, B]");
	assertArrayEquals(new X[]{X.C, X.A, X.C, X.C, X.B}, x);
    }
}
