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
package com.github.aamm.hyphenType.tests.exit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
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
import com.github.aamm.hyphenType.unittesting.NonExceptionalExit;
import com.github.aamm.hyphenType.unittesting.UnitTestingAppEngine;
import com.github.aamm.hyphenType.unittesting.UnitTestingOptionExtractor;

/**
 * @author Aurelio Akira M. Matsui
 */
public class ExitStatusTest {

    @Before
    public void setUp() {
        MyExitStatus.A.calls = 0;
        MyExitStatus.B.calls = 0;
    }
    
    @Test
    public void basic_functions_of_UnitTestingOptionExtractor()
	    throws InvalidOptionsInterfaceException,
	    FileNotFoundException, IllegalArgumentException, SecurityException,
	    IOException, InstantiationException, IllegalAccessException,
	    NoSuchMethodException, InvocationTargetException, StringParsingErrorException, MandatoryValueNotFoundException, RegexMismatchException, StringParseException, MandatoryMapValueNotFoundException, MandatorySimpleArgumentNotFoundException, OptionsExtractorException {
	UnitTestingOptionExtractor<MyOptions> oe = new UnitTestingOptionExtractor<MyOptions>(
		MyOptions.class);
	MyOptions myOptions = oe.options();
	assertFalse(oe.exitEnumCalled(myOptions));
	try {
	    myOptions.exit(MyExitStatus.B);
	    fail("Should have thrown a " + NonExceptionalExit.class.getName());
	} catch (NonExceptionalExit e) {
	}
	assertTrue(oe.exitEnumCalled(myOptions));
	assertEquals(MyExitStatus.B, oe.getStatusCodeEnum(myOptions));
	try {
	    myOptions.exit(MyExitStatus.A);
	    fail("Should have thrown a " + RuntimeException.class.getName());
	} catch (RuntimeException e) {
	}
    }

    @Test
    public void calling_exit_status_when_exit_int_called()
	    throws InvalidOptionsInterfaceException,
	    FileNotFoundException, IllegalArgumentException, SecurityException,
	    IOException, InstantiationException, IllegalAccessException,
	    NoSuchMethodException, InvocationTargetException, StringParsingErrorException, MandatoryValueNotFoundException, RegexMismatchException, StringParseException, MandatoryMapValueNotFoundException, MandatorySimpleArgumentNotFoundException, OptionsExtractorException {
	UnitTestingOptionExtractor<MyOptions> oe = new UnitTestingOptionExtractor<MyOptions>(MyOptions.class);

	assertEquals(0, MyExitStatus.A.calls);
	assertEquals(0, MyExitStatus.B.calls);

	MyOptions myOptions = oe.options();
	try {
	    myOptions.exit(3);
	    fail("Should have thrown a " + NonExceptionalExit.class.getName());
	} catch (NonExceptionalExit e) {
	}

	assertEquals(0, MyExitStatus.A.calls);
	assertEquals(0, MyExitStatus.B.calls);

	myOptions = oe.options();
	try {
	    myOptions.exit(1);
	    fail("Should have thrown a " + NonExceptionalExit.class.getName());
	} catch (NonExceptionalExit e) {
	}

	assertEquals(0, MyExitStatus.A.calls);
	assertEquals(1, MyExitStatus.B.calls);

	myOptions = oe.options();
	try {
	    myOptions.exit(-1);
	    fail("Should have thrown a " + NonExceptionalExit.class.getName());
	} catch (NonExceptionalExit e) {
	}

	assertEquals(0, MyExitStatus.A.calls);
	assertEquals(1, MyExitStatus.B.calls);

	myOptions = oe.options();
	try {
	    myOptions.exit(0);
	    fail("Should have thrown a " + NonExceptionalExit.class.getName());
	} catch (NonExceptionalExit e) {
	}

	assertEquals(1, MyExitStatus.A.calls);
	assertEquals(1, MyExitStatus.B.calls);

	myOptions = oe.options();
	try {
	    myOptions.exit(1);
	    fail("Should have thrown a " + NonExceptionalExit.class.getName());
	} catch (NonExceptionalExit e) {
	}

	assertEquals(1, MyExitStatus.A.calls);
	assertEquals(2, MyExitStatus.B.calls);

	myOptions = oe.options();
	try {
	    myOptions.exit(9);
	    fail("Should have thrown a " + NonExceptionalExit.class.getName());
	} catch (NonExceptionalExit e) {
	}

	assertEquals(1, MyExitStatus.A.calls);
	assertEquals(2, MyExitStatus.B.calls);
    }
    
    @Test
    public void test_exception_to_select_exit_status() throws InvalidOptionsInterfaceException {
        
        UnitTestingAppEngine engine = new UnitTestingAppEngine(MyApplication.class, "a");
        assertNull(engine.uncaughtThrowable());
        assertEquals("~~~>java.lang.IllegalArgumentException: xa", engine.out());
        assertEquals(0, MyExitStatus.A.calls);
        assertEquals(1, MyExitStatus.B.calls);
    }

    /**
     * In this test we force throwing a {@link NoSuchMethodError} and
     * expect {@link MyExitStatus#A} to catch it, because
     * {@link NoSuchMethodError} is a subclass of
     * {@link IncompatibleClassChangeError}.
     * 
     * @throws InvalidOptionsInterfaceException
     * @throws InvalidOptionException
     */
    @Test
    public void test_exception_to_select_exit_status_get_parent() throws InvalidOptionsInterfaceException {
        UnitTestingAppEngine engine = new UnitTestingAppEngine(MyApplication.class, "b");
        assertNull(engine.uncaughtThrowable());
        assertEquals(1, MyExitStatus.A.calls);
        assertEquals(0, MyExitStatus.B.calls);
    }

    /**
     * In this test we force throwing a {@link LinkageError} and
     * expect none of the enums of {@link MyExitStatus} to catch
     * it, because {@link LinkageError} is a subclass of {@link Error}
     * 
     * {@link NoSuchMethodError} is a subclass of
     * {@link IncompatibleClassChangeError}.
     * 
     * @throws InvalidOptionsInterfaceException
     * @throws InvalidOptionException
     */
    @Test
    public void test_exception_to_select_exit_status_miss_parent() throws InvalidOptionsInterfaceException {
        UnitTestingAppEngine engine = new UnitTestingAppEngine(MyApplication.class, "c");
        /*
         * For some strange reason, LinkageError does not recognize an identical exception as being equals.
         * So we opted for comparing the string representations of each instance.
         */
        assertEquals(new LinkageError("xc").toString(), engine.uncaughtThrowable().toString());
        assertEquals(0, MyExitStatus.A.calls);
        assertEquals(0, MyExitStatus.B.calls);
    }
}
