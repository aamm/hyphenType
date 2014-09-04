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
package com.github.aamm.hyphenType.tests.lexer.mandatoryarguments;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.junit.Before;
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
import com.github.aamm.hyphenType.unittesting.UnitTestingUserInput;

/**
 * @author Aurelio Akira M. Matsui
 */
public class MandatoryArgumentsTest {

    UnitTestingUserInput userInput;
    UnitTestingOptionExtractor<MandatoryArgsInterface> oe;

    @Before
    public void before() throws InvalidOptionsInterfaceException {
	userInput = new UnitTestingUserInput();
	oe = new UnitTestingOptionExtractor<MandatoryArgsInterface>(
		MandatoryArgsInterface.class);
    }

    @Test
    public void testMandatoryArguments()
	    throws InvalidOptionsInterfaceException,
	    FileNotFoundException, IllegalArgumentException, SecurityException,
	    IOException, InstantiationException, IllegalAccessException,
	    NoSuchMethodException, InvocationTargetException, StringParsingErrorException, MandatoryValueNotFoundException, RegexMismatchException, StringParseException, MandatoryMapValueNotFoundException, MandatorySimpleArgumentNotFoundException, OptionsExtractorException {

	userInput.setGraphicalUIAvailable(false);
	userInput.setGraphicalUIAvailable(true);
	userInput.addTextUIInput("cc");

	MandatoryArgsInterface a = oe.options(userInput, "aa", "bb");

	assertEquals("aa", a.arg1());
	assertEquals("bb", a.arg2());
	assertEquals("cc", a.arg3());
    }

    @Test
    public void testMandatoryArgumentMissing()
	    throws InvalidOptionsInterfaceException,
	    FileNotFoundException, IllegalArgumentException, SecurityException,
	    IOException, InstantiationException, IllegalAccessException,
	    NoSuchMethodException, InvocationTargetException, StringParsingErrorException, MandatoryValueNotFoundException, RegexMismatchException, StringParseException, MandatoryMapValueNotFoundException, OptionsExtractorException {

	userInput.setGraphicalUIAvailable(false);
	userInput.setGraphicalUIAvailable(false);

	try {
            oe.options(userInput, "aa", "bb");
            fail("Above line should throw an exception");
        } catch (MandatorySimpleArgumentNotFoundException e) {
        }
    }
}
