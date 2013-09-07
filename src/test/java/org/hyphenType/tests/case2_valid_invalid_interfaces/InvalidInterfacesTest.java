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
package org.hyphenType.tests.case2_valid_invalid_interfaces;

import org.hyphenType.exceptions.InvalidOptionsInterfaceException;
import org.hyphenType.lexerparser.LexerParser;
import org.junit.Test;

/**
 * @author Aurelio Akira M. Matsui
 */
public class InvalidInterfacesTest {

    @Test(expected = InvalidOptionsInterfaceException.class)
    public void emptyInteface() throws Exception {
	new LexerParser<Interface01_Invalid>(Interface01_Invalid.class);
    }

    @Test(expected = InvalidOptionsInterfaceException.class)
    public void mandatoryAfterOptional() throws Exception {
	new LexerParser<Interface02_Invalid>(Interface02_Invalid.class);
    }

    @Test(expected = InvalidOptionsInterfaceException.class)
    public void scalarAfterArray() throws Exception {
	new LexerParser<Interface03_Invalid>(Interface03_Invalid.class);
    }

    @Test(expected = InvalidOptionsInterfaceException.class)
    public void methodWithoutAnnotation() throws Exception {
	new LexerParser<Interface04_Invalid>(Interface04_Invalid.class);
    }

    @Test(expected = InvalidOptionsInterfaceException.class)
    public void methodWithTwoAnnotations() throws Exception {
	new LexerParser<Interface05_Invalid>(Interface05_Invalid.class);
    }

    @Test(expected = InvalidOptionsInterfaceException.class)
    public void methodWithArgument() throws Exception {
	new LexerParser<Interface06_Invalid>(Interface06_Invalid.class);
    }

    @Test(expected = InvalidOptionsInterfaceException.class)
    public void optionArgumentAfterArrayArgument() throws Exception {
	new LexerParser<Interface07_Invalid>(Interface07_Invalid.class);
    }

    @Test(expected = InvalidOptionsInterfaceException.class)
    public void mandatoryArgumentAfterOptional() throws Exception {
	new LexerParser<Interface08_Invalid>(Interface08_Invalid.class);
    }

    @Test(expected = InvalidOptionsInterfaceException.class)
    public void mandatorySimpleArgumentAfterOptional() throws Exception {
	new LexerParser<Interface09_Invalid>(Interface09_Invalid.class);
    }

    @Test(expected = InvalidOptionsInterfaceException.class)
    public void arrayArgumentNotLastOne() throws Exception {
	new LexerParser<Interface10_Invalid>(Interface10_Invalid.class);
    }

    @Test(expected = InvalidOptionsInterfaceException.class)
    public void argumentInputNotLastOne() throws Exception {
	new LexerParser<Interface11_Invalid>(Interface11_Invalid.class);
    }

    @Test
    public void correctArgumentInputSequence() throws Exception {
	new LexerParser<Interface12_Valid>(Interface12_Valid.class);
    }

    @Test(expected = InvalidOptionsInterfaceException.class)
    public void optionalArgumentWithNonArgumentChannel() throws Exception {
	new LexerParser<Interface13_Invalid>(Interface13_Invalid.class);
    }
}
