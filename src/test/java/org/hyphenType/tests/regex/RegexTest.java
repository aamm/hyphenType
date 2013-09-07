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
package org.hyphenType.tests.regex;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.hyphenType.datastructure.lexer.LexToken;
import org.hyphenType.datastructure.lexer.option.LexOption;
import org.hyphenType.datastructure.lexer.simple.LexArgument;
import org.hyphenType.lexerparser.LexerParser;
import org.hyphenType.lexerparser.exceptions.RegexMismatchException;
import org.hyphenType.optionsextractor.OptionsExtractor;
import org.junit.Test;

/**
 * @author Aurelio Akira M. Matsui
 */
public class RegexTest {

    @Test
    public void test1() throws Exception {

	LexerParser<RegexInterface1> lexPar = new LexerParser<RegexInterface1>(
		RegexInterface1.class);

	ArrayList<LexToken> expected = new ArrayList<LexToken>();
	expected.add(new LexOption("a"));
	expected.add(new LexOption("abc"));
	expected.add(new LexArgument("@@bla"));

	assertEquals(expected, lexPar.lexArguments("@a", "@@abc", "@@bla"));
    }

    @Test
    public void test2() throws Exception {

	LexerParser<RegexInterface1> lexPar = new LexerParser<RegexInterface1>(
		RegexInterface1.class);

	ArrayList<LexToken> expected = new ArrayList<LexToken>();
	expected.add(new LexOption("a"));
	expected.add(new LexArgument("something"));
	expected.add(new LexOption("abc"));
	expected.add(new LexArgument("@@bla"));

	assertEquals(expected, lexPar.lexArguments("@a", "something", "@@abc",
		"@@bla"));
    }

    @Test
    public void test3() throws Exception {

	LexerParser<RegexInterface1> lexPar = new LexerParser<RegexInterface1>(
		RegexInterface1.class);

	ArrayList<LexToken> expected = new ArrayList<LexToken>();
	expected.add(new LexOption("a"));
	expected.add(new LexOption("b"));
	expected.add(new LexOption("xyz"));

	assertEquals(expected, lexPar.lexArguments("@ab", "@@xyz"));
    }

    @Test
    public void test4() throws Exception {

	LexerParser<RegexInterface1> lexPar = new LexerParser<RegexInterface1>(
		RegexInterface1.class);

	ArrayList<LexToken> expected = new ArrayList<LexToken>();
	expected.add(new LexArgument("@@bla"));
	expected.add(new LexArgument("@@xyz"));
	expected.add(new LexOption("x"));
	expected.add(new LexArgument("2"));
	expected.add(new LexArgument("4"));

	assertEquals(expected, lexPar.lexArguments("@@bla", "@@xyz", "@x", "2",
		"4"));

	OptionsExtractor<RegexInterface1> oe = new OptionsExtractor<RegexInterface1>(
		RegexInterface1.class);
	RegexInterface1 ri = oe.options("@@bla", "@@xyz", "@x", "@02", "@04");
	assertEquals(true, ri.x());
	assertEquals(false, ri.y());
	assertEquals("@@bla", ri.value());
	assertEquals("@@xyz", ri.value2());
	System.out.println(Arrays.toString(ri.value3()));
	assertArrayEquals(new String[] { "@02", "@04" }, ri.value3());
    }

    @Test
    public void test5() throws Exception {
	OptionsExtractor<RegexInterface1> oe = new OptionsExtractor<RegexInterface1>(
		RegexInterface1.class);
	RegexInterface1 ri1 = oe.options("@@bla", "@@xyz", "@x", "@02", "@77",
		"04"); // 04 will finish the array, since it does not match the
	// regex of value3
	assertEquals("@@bla", ri1.value());
	assertEquals("@@xyz", ri1.value2());
	assertTrue(ri1.x());
	assertArrayEquals(new String[] { "@02", "@77" }, ri1.value3());
	ArrayList<LexToken> expected = new ArrayList<LexToken>();
	expected.add(new LexArgument("04"));
	assertEquals(expected, ri1.unparsedArguments());
    }

    @Test
    public void test6() throws Exception {
	OptionsExtractor<RegexInterface1> oe = new OptionsExtractor<RegexInterface1>(
		RegexInterface1.class);
	RegexInterface1 ri = oe.options("@@bla", "@@xyz", "@x", "@02", "@99",
		"@88");
	assertEquals(true, ri.x());
	assertEquals(false, ri.y());
	assertEquals("@@bla", ri.value());
	assertEquals("@@xyz", ri.value2());

	assertArrayEquals(new String[] { "@02", "@99", "@88" }, ri.value3());
    }

    @Test
    public void test7() throws Exception {
	OptionsExtractor<RegexInterface2> oe = new OptionsExtractor<RegexInterface2>(
		RegexInterface2.class);
	RegexInterface2 ri2 = oe.options("-x", "red", "sky");
	assertEquals(true, ri2.x());
	assertEquals(false, ri2.y());
	assertEquals("red", ri2.xa0());
	assertEquals("sky", ri2.xa1());
    }

    @Test
    public void test8() throws Exception {
	OptionsExtractor<RegexInterface2> oe = new OptionsExtractor<RegexInterface2>(
		RegexInterface2.class);
	RegexInterface2 ri2 = oe.options("-x", "-y");
	assertEquals(true, ri2.x());
	assertEquals(true, ri2.y());
	assertEquals(null, ri2.xa0());
	assertEquals(null, ri2.xa1());
    }

    @Test
    public void test9() throws Exception {
	OptionsExtractor<RegexInterface2> oe = new OptionsExtractor<RegexInterface2>(
		RegexInterface2.class);
	RegexInterface2 ri2 = oe.options("-x", "red", "-y", "sky");
	assertEquals(true, ri2.x());
	assertEquals(true, ri2.y());
	assertEquals("red", ri2.xa0());
	assertEquals(null, ri2.xa1());
    }

    @Test
    public void test10() throws Exception {
	OptionsExtractor<RegexInterface2> oe = new OptionsExtractor<RegexInterface2>(
		RegexInterface2.class);
	RegexInterface2 ri2 = oe.options("-x", "green", "leaf", "-y", "sky");
	assertEquals(true, ri2.x());
	assertEquals(true, ri2.y());
	assertEquals("green", ri2.xa0());
	assertEquals("leaf", ri2.xa1());
    }

    @Test(expected = RegexMismatchException.class)
    public void test11() throws Exception {
	OptionsExtractor<RegexInterface2> oe = new OptionsExtractor<RegexInterface2>(
		RegexInterface2.class);
	oe.options("-x", "green", "error", "-y", "sky");
    }
}
