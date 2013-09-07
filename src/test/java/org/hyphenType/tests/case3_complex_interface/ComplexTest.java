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
package org.hyphenType.tests.case3_complex_interface;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;

import java.util.Arrays;

import org.hyphenType.optionsextractor.OptionsExtractor;
import org.junit.Test;

/**
 * @author Aurelio Akira M. Matsui
 */
public class ComplexTest {

    @Test
    public void complexTest() throws Exception {
	String[] arguments = new String[] { "-x=Bla", "aaa", "111", "a", "b", "c", "-y", "testV1", "-y", "-y", "-y", "1212", "true", "B", "A" };

	OptionsExtractor<MyComplexOptions> extractor = new OptionsExtractor<MyComplexOptions>(MyComplexOptions.class);
	
	MyComplexOptions options = extractor.options(arguments);

	assertEquals(true, options.x());

	assertEquals("Bla", options.xOption());

	MyType[] expectedArray = new MyType[] { new MyType("a"),
		new MyType("b"), new MyType("c") };
	assertTrue(Arrays.equals(expectedArray, options.xarg3()));

	assertEquals("testV1", options.sa0());
	assertEquals(1212, options.sa1());
	assertEquals(true, options.sa2());
	X[] expectedXArray = new X[] { X.B, X.A };
	assertArrayEquals(expectedXArray, options.sa3());

	assertEquals(4, options.y());
    }

    @Test
    public void multipleOccurences() throws Exception {
	MyComplexOptions options = new OptionsExtractor<MyComplexOptions>(
		MyComplexOptions.class).options("-y", "-y", "-y", "-y");
	assertEquals(4, options.y());

	options.printDocumentation();
    }
}
