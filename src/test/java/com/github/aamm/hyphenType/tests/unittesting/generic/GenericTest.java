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
package com.github.aamm.hyphenType.tests.unittesting.generic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.github.aamm.hyphenType.exceptions.InvalidOptionsInterfaceException;
import com.github.aamm.hyphenType.lexerparser.exceptions.MandatoryMapValueNotFoundException;
import com.github.aamm.hyphenType.lexerparser.exceptions.MandatorySimpleArgumentNotFoundException;
import com.github.aamm.hyphenType.lexerparser.exceptions.MandatoryValueNotFoundException;
import com.github.aamm.hyphenType.lexerparser.exceptions.RegexMismatchException;
import com.github.aamm.hyphenType.lexerparser.exceptions.StringParseException;
import com.github.aamm.hyphenType.lexerparser.exceptions.StringParsingErrorException;
import com.github.aamm.hyphenType.optionsextractor.OptionsExtractorException;
import com.github.aamm.hyphenType.tests.case1.SimplestTest;
import com.github.aamm.hyphenType.tests.unittesting.simple.UserMadeApplication;
import com.github.aamm.hyphenType.tests.unittesting.simple.UserMadeOptions;
import com.github.aamm.hyphenType.unittesting.NonExceptionalExit;
import com.github.aamm.hyphenType.unittesting.UnitTestingAppEngine;
import com.github.aamm.hyphenType.unittesting.UnitTestingOptionExtractor;

/**
 * The only difference between this test and {@link SimplestTest} is that this
 * one illustrates a unit test that utilizes {@link UnitTestingOptionExtractor}
 * instead of {@link UnitTestingAppEngine}. Users should know how to utilize
 * {@link UnitTestingOptionExtractor} when they are not testing applications.
 * This is the case when the class that receives options object is not the main
 * class in a stand alone execution. An example is classes under test receive
 * interactive inputs from a console.
 * 
 * @author Aurelio Akira M. Matsui
 */
public class GenericTest {

    @Test
    public void test() throws InvalidOptionsInterfaceException, StringParsingErrorException, MandatoryValueNotFoundException, RegexMismatchException, StringParseException, MandatoryMapValueNotFoundException, MandatorySimpleArgumentNotFoundException, OptionsExtractorException {

        UnitTestingOptionExtractor<UserMadeOptions> extractor = new UnitTestingOptionExtractor<UserMadeOptions>(UserMadeOptions.class);
        try {
            UserMadeApplication.main(extractor.options("-x"));
            fail("Should never reach this line since the line above should have thrown an exception.");
        } catch (NonExceptionalExit e) {
            assertTrue(e.exitIntCalled());
            assertEquals(13, e.getStatusCodeInt());
            assertEquals(UserMadeApplication.class.getName(), e.exitCallPoint().getClassName());
            // Line number detection won't work unless classes were compiled
            // using debug mode.
            assertEquals(23, e.exitCallPoint().getLineNumber());
            assertEquals("main", e.exitCallPoint().getMethodName());
            assertEquals(11, UserMadeApplication.number);
        }
    }
}
