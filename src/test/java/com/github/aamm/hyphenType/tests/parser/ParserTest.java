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
package com.github.aamm.hyphenType.tests.parser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.github.aamm.hyphenType.datastructure.annotations.OptionValue;
import com.github.aamm.hyphenType.datastructure.lexer.LexToken;
import com.github.aamm.hyphenType.datastructure.lexer.simple.LexArgument;
import com.github.aamm.hyphenType.exceptions.InvalidOptionsInterfaceException;
import com.github.aamm.hyphenType.lexerparser.LexerParser;
import com.github.aamm.hyphenType.lexerparser.OptionValues;
import com.github.aamm.hyphenType.lexerparser.exceptions.MandatoryMapValueNotFoundException;
import com.github.aamm.hyphenType.lexerparser.exceptions.MandatorySimpleArgumentNotFoundException;
import com.github.aamm.hyphenType.lexerparser.exceptions.MandatoryValueNotFoundException;
import com.github.aamm.hyphenType.lexerparser.exceptions.RegexMismatchException;
import com.github.aamm.hyphenType.lexerparser.exceptions.StringParseException;
import com.github.aamm.hyphenType.lexerparser.exceptions.StringParsingErrorException;

/**
 * @author Aurelio Akira M. Matsui
 */
public class ParserTest {

    @Test
    public void test1() throws FileNotFoundException, IOException, IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, InvalidOptionsInterfaceException, StringParsingErrorException, MandatoryValueNotFoundException, RegexMismatchException, StringParseException, MandatoryMapValueNotFoundException, MandatorySimpleArgumentNotFoundException {
        LexerParser<MyOptions> lexPar = new LexerParser<MyOptions>(MyOptions.class);
        OptionValues<MyOptions> values = new OptionValues<MyOptions>(lexPar.lexArguments("-a=blablabla", "123", "-b", "-a", "-a", "xxx", "yyy", "zzz"), lexPar);
        assertEquals(3, values.getParsedOptionValue(lexPar.searchOption("a")));
        assertEquals(1, values.getParsedOptionValue(lexPar.searchOption("b")));
    }

    @Test
    public void test2() throws FileNotFoundException, IOException, IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, InvalidOptionsInterfaceException, StringParsingErrorException, MandatoryValueNotFoundException, RegexMismatchException, StringParseException, MandatoryMapValueNotFoundException, MandatorySimpleArgumentNotFoundException {
        LexerParser<MyOptions> lexPar = new LexerParser<MyOptions>(MyOptions.class);
        OptionValues<MyOptions> values = new OptionValues<MyOptions>(lexPar.lexArguments("aaaaa", "bbbbb", "ccccc", "ddddd"), lexPar);
        assertEquals("aaaaa", values.getSimpleArgumentValue(lexPar.searchArgument("a1")));
        assertEquals("bbbbb", values.getSimpleArgumentValue(lexPar.searchArgument("a2")));

        List<LexToken> expected = new ArrayList<LexToken>();
        expected.add(new LexArgument("ccccc"));
        expected.add(new LexArgument("ddddd"));
        assertEquals(expected, values.unusedArguments());
    }

    @Test
    public void test3() throws FileNotFoundException, IOException, IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, InvalidOptionsInterfaceException, StringParsingErrorException, MandatoryValueNotFoundException, RegexMismatchException, StringParseException, MandatoryMapValueNotFoundException, MandatorySimpleArgumentNotFoundException {
        LexerParser<MyOptions> lexPar = new LexerParser<MyOptions>(MyOptions.class);
        OptionValues<MyOptions> values = new OptionValues<MyOptions>(lexPar.lexArguments("-c=qqq"), lexPar);
        assertEquals(0, values.getParsedOptionValue(lexPar.searchOption("a")));
        assertEquals(0, values.getParsedOptionValue(lexPar.searchOption("b")));
        assertEquals(1, values.getParsedOptionValue(lexPar.searchOption("c")));
        assertEquals("qqq", values.getOptionValue(lexPar.searchOption("c").value));
    }

    @Test
    public void test4() throws FileNotFoundException, IOException, IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, InvalidOptionsInterfaceException, StringParsingErrorException, MandatoryValueNotFoundException, RegexMismatchException, StringParseException, MandatoryMapValueNotFoundException, MandatorySimpleArgumentNotFoundException {
        LexerParser<MyOptions> lexPar = new LexerParser<MyOptions>(MyOptions.class);
        OptionValues<MyOptions> values = new OptionValues<MyOptions>(lexPar.lexArguments("-c=qqq"), lexPar);
        assertEquals(0, values.getParsedOptionValue(lexPar.searchOption("a")));
        assertEquals(0, values.getParsedOptionValue(lexPar.searchOption("b")));
        assertEquals(1, values.getParsedOptionValue(lexPar.searchOption("c")));
        assertEquals("qqq", values.getOptionValue(lexPar.searchOption("c").value));
    }

    @Test
    public void test5() throws FileNotFoundException, IOException, IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, InvalidOptionsInterfaceException, StringParsingErrorException, MandatoryValueNotFoundException, RegexMismatchException, StringParseException, MandatoryMapValueNotFoundException, MandatorySimpleArgumentNotFoundException {
        LexerParser<MyOptions> lexPar = new LexerParser<MyOptions>(MyOptions.class);
        OptionValues<MyOptions> values = new OptionValues<MyOptions>(lexPar.lexArguments("-da=111", "-db=222"), lexPar);

        assertEquals(0, values.getParsedOptionValue(lexPar.searchOption("a")));
        assertEquals(0, values.getParsedOptionValue(lexPar.searchOption("b")));
        assertEquals(0, values.getParsedOptionValue(lexPar.searchOption("c")));
        assertEquals(2, values.getParsedOptionValue(lexPar.searchOption("d")));

        Map<String, Integer> expected = new HashMap<String, Integer>();
        expected.put("a", 111);
        expected.put("b", 222);

        assertEquals(expected, values.getOptionMapValue(lexPar.searchOption("d").map));
    }

    @Test
    public void test6() throws FileNotFoundException, IOException, IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, InvalidOptionsInterfaceException, StringParsingErrorException, MandatoryValueNotFoundException, RegexMismatchException, StringParseException, MandatoryMapValueNotFoundException, MandatorySimpleArgumentNotFoundException {
        LexerParser<MyOptions> lexPar = new LexerParser<MyOptions>(MyOptions.class);
        OptionValues<MyOptions> values = new OptionValues<MyOptions>(lexPar.lexArguments("-b=[bla,ble,blo]"), lexPar);

        assertArrayEquals(new String[] { "bla", "ble", "blo" }, (String[]) values.getOptionValue(lexPar.searchOption("b").value));
    }

    /**
     * Tests if array values accept single values without square brackets.
     * 
     * @throws FileNotFoundException
     * @throws IOException
     * @throws IllegalArgumentException
     * @throws SecurityException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws InvalidOptionsInterfaceException
     * @throws MandatorySimpleArgumentNotFoundException 
     * @throws MandatoryMapValueNotFoundException 
     * @throws StringParseException 
     * @throws RegexMismatchException 
     * @throws MandatoryValueNotFoundException 
     * @throws StringParsingErrorException 
     * @throws InvalidOptionException
     */
    @Test
    public void test8() throws FileNotFoundException, IOException, IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, InvalidOptionsInterfaceException, StringParsingErrorException, MandatoryValueNotFoundException, RegexMismatchException, StringParseException, MandatoryMapValueNotFoundException, MandatorySimpleArgumentNotFoundException {
        LexerParser<MyOptions> lexPar = new LexerParser<MyOptions>(MyOptions.class);
        OptionValues<MyOptions> values;

        values = new OptionValues<MyOptions>(lexPar.lexArguments("-b=abc"), lexPar);
        assertArrayEquals(new String[] { "abc" }, (String[]) values.getOptionValue(lexPar.searchOption("b").value));

        values = new OptionValues<MyOptions>(lexPar.lexArguments("-b=ee,ff,gg"), lexPar);
        assertArrayEquals(new String[] { "ee", "ff", "gg" }, (String[]) values.getOptionValue(lexPar.searchOption("b").value));
    }

    /**
     * Tests if arrays can be separated by {@link File#pathSeparator} if the
     * {@link OptionValue#arrayUseFileSeparator()} is set to true. In other
     * words, check if {@link LexerParser} is following the contract of
     * {@link OptionValue#arrayUseFileSeparator()}.
     * 
     * @throws FileNotFoundException
     * @throws IOException
     * @throws IllegalArgumentException
     * @throws SecurityException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws InvalidOptionsInterfaceException
     * @throws MandatorySimpleArgumentNotFoundException 
     * @throws MandatoryMapValueNotFoundException 
     * @throws StringParseException 
     * @throws RegexMismatchException 
     * @throws MandatoryValueNotFoundException 
     * @throws StringParsingErrorException 
     * @throws InvalidOptionException
     */
    @Test
    public void test9() throws FileNotFoundException, IOException, IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, InvalidOptionsInterfaceException, StringParsingErrorException, MandatoryValueNotFoundException, RegexMismatchException, StringParseException, MandatoryMapValueNotFoundException, MandatorySimpleArgumentNotFoundException {
        LexerParser<MyOptionsEndingInArray> lexPar = new LexerParser<MyOptionsEndingInArray>(MyOptionsEndingInArray.class);
        OptionValues<MyOptionsEndingInArray> values;

        values = new OptionValues<MyOptionsEndingInArray>(lexPar.lexArguments("-x=www"), lexPar);
        assertArrayEquals(new String[] { "www" }, (String[]) values.getOptionValue(lexPar.searchOption("x").value));

        values = new OptionValues<MyOptionsEndingInArray>(lexPar.lexArguments("-x=www" + File.pathSeparator + "rrr"), lexPar);
        assertArrayEquals(new String[] { "www", "rrr" }, (String[]) values.getOptionValue(lexPar.searchOption("x").value));
    }

    /**
     * Tests if array separators can be replaced by the # character.
     * 
     * @throws FileNotFoundException
     * @throws IOException
     * @throws IllegalArgumentException
     * @throws SecurityException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws InvalidOptionsInterfaceException
     * @throws MandatorySimpleArgumentNotFoundException 
     * @throws MandatoryMapValueNotFoundException 
     * @throws StringParseException 
     * @throws RegexMismatchException 
     * @throws MandatoryValueNotFoundException 
     * @throws StringParsingErrorException 
     * @throws InvalidOptionException
     */
    @Test
    public void test10() throws FileNotFoundException, IOException, IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, InvalidOptionsInterfaceException, StringParsingErrorException, MandatoryValueNotFoundException, RegexMismatchException, StringParseException, MandatoryMapValueNotFoundException, MandatorySimpleArgumentNotFoundException {
        LexerParser<MyOptionsEndingInArray> lexPar = new LexerParser<MyOptionsEndingInArray>(MyOptionsEndingInArray.class);
        OptionValues<MyOptionsEndingInArray> values;

        values = new OptionValues<MyOptionsEndingInArray>(lexPar.lexArguments("-y=lalala"), lexPar);
        assertArrayEquals(new String[] { "lalala" }, (String[]) values.getOptionValue(lexPar.searchOption("y").value));

        values = new OptionValues<MyOptionsEndingInArray>(lexPar.lexArguments("-y=lalala#eee"), lexPar);
        assertArrayEquals(new String[] { "lalala", "eee" }, (String[]) values.getOptionValue(lexPar.searchOption("y").value));
    }

    /**
     * Tests whether the parser will put all trailing arguments in an array. The
     * simple argument {@link MyOptionsEndingInArray#a3()} is an array. So all
     * simple arguments after the second should go to
     * {@link MyOptionsEndingInArray#a3()}. On the other hand, if there are only
     * two arguments, the value of {@link MyOptionsEndingInArray#a3()} should be
     * an empty array.
     * 
     * @throws FileNotFoundException
     * @throws IOException
     * @throws IllegalArgumentException
     * @throws SecurityException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws InvalidOptionsInterfaceException
     * @throws MandatorySimpleArgumentNotFoundException 
     * @throws MandatoryMapValueNotFoundException 
     * @throws StringParseException 
     * @throws RegexMismatchException 
     * @throws MandatoryValueNotFoundException 
     * @throws StringParsingErrorException 
     * @throws InvalidOptionException
     */
    @Test
    public void test11() throws FileNotFoundException, IOException, IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, InvalidOptionsInterfaceException, StringParsingErrorException, MandatoryValueNotFoundException, RegexMismatchException, StringParseException, MandatoryMapValueNotFoundException, MandatorySimpleArgumentNotFoundException {
        LexerParser<MyOptionsEndingInArray> lexPar = new LexerParser<MyOptionsEndingInArray>(MyOptionsEndingInArray.class);
        OptionValues<MyOptionsEndingInArray> values;

        values = new OptionValues<MyOptionsEndingInArray>(lexPar.lexArguments("11", "22", "33"), lexPar);
        assertArrayEquals(new String[] { "33" }, (String[]) values.getSimpleArgumentValue(lexPar.searchArgument("a3")));

        values = new OptionValues<MyOptionsEndingInArray>(lexPar.lexArguments("11", "22"), lexPar);
        assertArrayEquals(new String[] {}, (String[]) values.getSimpleArgumentValue(lexPar.searchArgument("a3")));

        values = new OptionValues<MyOptionsEndingInArray>(lexPar.lexArguments("11"), lexPar);
        assertEquals("11", values.getSimpleArgumentValue(lexPar.searchArgument("a1")));
        assertNull(values.getSimpleArgumentValue(lexPar.searchArgument("a2")));
        assertArrayEquals(new String[] {}, (String[]) values.getSimpleArgumentValue(lexPar.searchArgument("a3")));

        values = new OptionValues<MyOptionsEndingInArray>(lexPar.lexArguments("11", "22", "33", "444", "5"), lexPar);
        assertArrayEquals(new String[] { "33", "444", "5" }, (String[]) values.getSimpleArgumentValue(lexPar.searchArgument("a3")));
    }
}
