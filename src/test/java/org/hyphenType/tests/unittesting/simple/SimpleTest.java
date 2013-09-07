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
package org.hyphenType.tests.unittesting.simple;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.hyphenType.exceptions.InvalidOptionsInterfaceException;
import org.hyphenType.unittesting.UnitTestingAppEngine;
import org.junit.Test;

/**
 * @author Aurelio Akira M. Matsui
 */
public class SimpleTest {

    @Test
    public void test() throws InvalidOptionsInterfaceException {
        UnitTestingAppEngine engine = new UnitTestingAppEngine(UserMadeApplication.class, "-x");
        assertTrue("Something odd just happened.", engine.exitIntCalled());
        assertEquals(13, engine.getStatusCodeInt());
        assertEquals(UserMadeApplication.class.getName(), engine.exitCallPoint().getClassName());
        // Line number detection won't work unless classes were compiled using
        // debug mode.
        assertEquals(23, engine.exitCallPoint().getLineNumber());
        assertEquals("main", engine.exitCallPoint().getMethodName());
        assertEquals(11, UserMadeApplication.number);
        assertEquals("abc123412341234", engine.out());
    }
}
