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
package com.github.aamm.hyphenType.tests.lexer.stopdependingonregex;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.github.aamm.hyphenType.exceptions.InvalidOptionsInterfaceException;
import com.github.aamm.hyphenType.lexerparser.exceptions.MandatoryMapValueNotFoundException;
import com.github.aamm.hyphenType.lexerparser.exceptions.MandatorySimpleArgumentNotFoundException;
import com.github.aamm.hyphenType.lexerparser.exceptions.MandatoryValueNotFoundException;
import com.github.aamm.hyphenType.lexerparser.exceptions.RegexMismatchException;
import com.github.aamm.hyphenType.lexerparser.exceptions.StringParseException;
import com.github.aamm.hyphenType.lexerparser.exceptions.StringParsingErrorException;
import com.github.aamm.hyphenType.optionsextractor.OptionsExtractorException;
import com.github.aamm.hyphenType.unittesting.UnitTestingOptionExtractor;

/**
 * @author Aurelio Akira M. Matsui
 */
public class StopAtRegexUnmatchTest {

    @Test
    public void test1() throws InvalidOptionsInterfaceException, StringParsingErrorException, MandatoryValueNotFoundException, RegexMismatchException, StringParseException, MandatoryMapValueNotFoundException, MandatorySimpleArgumentNotFoundException, OptionsExtractorException {
	UnitTestingOptionExtractor<ComplexOptions> utoe = new UnitTestingOptionExtractor<ComplexOptions>(
		ComplexOptions.class);
	ComplexOptions complexOptions = utoe.options("-x", "a1", "a2", "a3", "bla");
	assertTrue(complexOptions.x());
	assertArrayEquals(new String[] { "a1", "a2", "a3" }, complexOptions.a());
	assertArrayEquals(new String[] { "bla" }, complexOptions.arg1());
    }

    @Test
    public void test2() throws InvalidOptionsInterfaceException, StringParsingErrorException, MandatoryValueNotFoundException, RegexMismatchException, StringParseException, MandatoryMapValueNotFoundException, MandatorySimpleArgumentNotFoundException, OptionsExtractorException {
	UnitTestingOptionExtractor<ComplexOptions> utoe = new UnitTestingOptionExtractor<ComplexOptions>(
		ComplexOptions.class);
	ComplexOptions complexOptions = utoe.options("bla", "ble", "c1", "-x", "a1", "a2", "a3");
	assertTrue(complexOptions.x());
	assertArrayEquals(new String[] { "a1", "a2", "a3" }, complexOptions.a());
	assertArrayEquals(new String[] { "bla", "ble" }, complexOptions.arg1());
    }

    @Test
    public void test3() throws InvalidOptionsInterfaceException, StringParsingErrorException, MandatoryValueNotFoundException, RegexMismatchException, StringParseException, MandatoryMapValueNotFoundException, MandatorySimpleArgumentNotFoundException, OptionsExtractorException {
	UnitTestingOptionExtractor<ComplexOptions> utoe = new UnitTestingOptionExtractor<ComplexOptions>(
		ComplexOptions.class);
	ComplexOptions complexOptions = utoe.options("bla", "ble", "c1", "-x", "a1", "a2", "a3", "c1", "c2");
	assertTrue(complexOptions.x());
	assertArrayEquals(new String[] { "a1", "a2", "a3" }, complexOptions.a());
	assertArrayEquals(new String[] { "bla", "ble" }, complexOptions.arg1());
    }
}
