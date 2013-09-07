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
package org.hyphenType.tests.case1;

import static junit.framework.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.hyphenType.datastructure.lexer.LexToken;
import org.hyphenType.datastructure.lexer.option.LexOption;
import org.hyphenType.datastructure.lexer.option.LexOptionValue;
import org.hyphenType.datastructure.lexer.simple.LexArgument;
import org.hyphenType.lexerparser.exceptions.StringParseException;
import org.hyphenType.optionsextractor.OptionsExtractor;
import org.junit.Test;

/**
 * @author Aurelio Akira M. Matsui
 */
public class SimplestTest {

    @Test
    public void simplest() throws Exception {
	OptionsExtractor<MyOptions> soe = new OptionsExtractor<MyOptions>(
		MyOptions.class);
	MyOptions options = soe.options("aaa", "1234", "true");
	assertEquals("aaa", options.v1());
	assertEquals(1234, options.v2());
	assertEquals(true, options.v3());
    }

    @Test
    public void unparsedArguments() throws Exception {
	OptionsExtractor<MyOptions> soe = new OptionsExtractor<MyOptions>(
		MyOptions.class);
	MyOptions options = soe.options("-t=testing_this", "-zcvf=asdf", "-w",
		"xyz", "9999", "false", "666");
	assertEquals("xyz", options.v1());
	assertEquals(9999, options.v2());
	assertEquals(false, options.v3());

	List<LexToken> expected = new ArrayList<LexToken>();
	expected.add(new LexOption("t"));
	expected.add(new LexOptionValue("testing_this"));
	expected.add(new LexOption("z"));
	expected.add(new LexOption("c"));
	expected.add(new LexOption("v"));
	expected.add(new LexOption("f"));
	expected.add(new LexOptionValue("asdf"));
	expected.add(new LexOption("w"));
	expected.add(new LexArgument("666"));

	assertEquals(expected, options.unparsedArguments());
    }

    @Test(expected = StringParseException.class)
    public void parsingError() throws Exception {
	OptionsExtractor<MyOptions> soe = new OptionsExtractor<MyOptions>(
		MyOptions.class);
	soe.options("aaa", "error", "false");
    }
}
