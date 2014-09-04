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
package com.github.aamm.hyphenType.tests.optionprocessors.lib;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

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
public class BooleanValidatorTest {

    @Test
    public void test() throws InvalidOptionsInterfaceException, FileNotFoundException, IllegalArgumentException, SecurityException, IOException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, StringParsingErrorException, MandatoryValueNotFoundException, RegexMismatchException, StringParseException, MandatoryMapValueNotFoundException, MandatorySimpleArgumentNotFoundException, OptionsExtractorException {
	UnitTestingOptionExtractor<BooleanValidatorTestInterface> oe = new UnitTestingOptionExtractor<BooleanValidatorTestInterface>(BooleanValidatorTestInterface.class, false);

	BooleanValidatorTestInterface options;

	options = oe.options("-a", "-a", "-b", "-b");
	assertFalse(oe.exitIntCalled(options));

	options = oe.options("-a", "-a");
	assertTrue(oe.exitIntCalled(options));
	assertEquals(1, oe.getStatusCodeInt(options));

	options = oe.options("-a", "-b");
	assertTrue(oe.exitIntCalled(options));
	assertEquals(88, oe.getStatusCodeInt(options));

	options = oe.options("-a", "-b", "-b");
	assertTrue(oe.exitIntCalled(options));
	assertEquals(1, oe.getStatusCodeInt(options));
    }
}
