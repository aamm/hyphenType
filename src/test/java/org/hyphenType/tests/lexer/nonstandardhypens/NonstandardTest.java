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
package org.hyphenType.tests.lexer.nonstandardhypens;

import static junit.framework.Assert.assertEquals;

import java.util.ArrayList;

import org.hyphenType.datastructure.lexer.LexToken;
import org.hyphenType.datastructure.lexer.option.LexOption;
import org.hyphenType.datastructure.lexer.option.LexOptionValue;
import org.hyphenType.exceptions.InvalidOptionsInterfaceException;
import org.hyphenType.lexerparser.LexerParser;
import org.junit.Test;

/**
 * @author Aurelio Akira M. Matsui
 */
public class NonstandardTest {

    @Test
    public void test() throws InvalidOptionsInterfaceException {

	LexerParser<NonStandard> lexPar = new LexerParser<NonStandard>(
		NonStandard.class);

	ArrayList<LexToken> expected = new ArrayList<LexToken>();
	expected.add(new LexOption("x"));

	assertEquals(expected, lexPar.lexArguments("@@@@x"));
    }

    @Test
    public void test2() throws InvalidOptionsInterfaceException {

	LexerParser<NonStandard> lexPar = new LexerParser<NonStandard>(
		NonStandard.class);

	ArrayList<LexToken> expected = new ArrayList<LexToken>();
	expected.add(new LexOption("x"));
	expected.add(new LexOption("a"));

	assertEquals(expected, lexPar.lexArguments("@@@@x", "@@@@a"));
    }

    @Test
    public void test3() throws InvalidOptionsInterfaceException {

	LexerParser<NonStandard> lexPar = new LexerParser<NonStandard>(
		NonStandard.class);

	ArrayList<LexToken> expected = new ArrayList<LexToken>();
	expected.add(new LexOption("shazan"));
	expected.add(new LexOptionValue("something"));
	expected.add(new LexOption("a"));

	assertEquals(expected, lexPar.lexArguments("###shazan:=something", "@@@@a"));
    }
}
