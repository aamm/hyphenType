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
package org.hyphenType.tests.unittesting.complex;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.hyphenType.exceptions.InvalidOptionsInterfaceException;
import org.hyphenType.unittesting.UnitTestingAppEngine;
import org.hyphenType.unittesting.UnitTestingUserInput;
import org.junit.Test;

/**
 * @author Aurelio Akira M. Matsui
 */
public class ComplexTest {

    @Test
    public void test() throws InvalidOptionsInterfaceException {
	UnitTestingUserInput input = new UnitTestingUserInput();
	input.addGraphicalUIInput("abc");
	input.addGraphicalUIInput("def");
	input.setGraphicalUIAvailable(true);
	UnitTestingAppEngine engine = new UnitTestingAppEngine(AnotherApplication.class, input);
	assertFalse(engine.exitIntCalled());
	assertEquals("a = abc", engine.out());
	assertEquals("b = def", engine.out());
	assertEquals(NullPointerException.class, engine.uncaughtThrowable().getClass());
	assertEquals("Ouch!", engine.uncaughtThrowable().getMessage());
    }
}
