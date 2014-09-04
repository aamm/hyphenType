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
package com.github.aamm.hyphenType.tests.lexer;

import static junit.framework.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Locale;

import org.junit.Test;

import com.github.aamm.hyphenType.datastructure.lexer.LexToken;
import com.github.aamm.hyphenType.datastructure.lexer.option.LexOption;
import com.github.aamm.hyphenType.datastructure.lexer.option.LexOptionMapValue;
import com.github.aamm.hyphenType.datastructure.lexer.option.LexOptionValue;
import com.github.aamm.hyphenType.datastructure.lexer.simple.LexArgument;
import com.github.aamm.hyphenType.documentation.DocumentationFormatterEngine;
import com.github.aamm.hyphenType.documentation.lib.StandardFormatterEngine;
import com.github.aamm.hyphenType.documentation.lib.StandardFormatterEngine.StandardFormatter;
import com.github.aamm.hyphenType.exceptions.InvalidOptionsInterfaceException;
import com.github.aamm.hyphenType.lexerparser.LexerParser;
import com.github.aamm.hyphenType.tests.lexer.c.C;

/**
 * @author Aurelio Akira M. Matsui
 */
public class LexerTest {

    @Test
    public void test1() throws FileNotFoundException, IOException, IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, InvalidOptionsInterfaceException {

        // Locale.setDefault(new Locale("pt", "BR"));
        Locale.setDefault(new Locale("ja"));

        // continue tests from here. Reading from the correct resource bundle is
        // not working fine.

        StandardFormatterEngine<C> formatter = (StandardFormatterEngine<C>) DocumentationFormatterEngine.buildFormatter(C.class, StandardFormatter.class);
        formatter.printDocumentation();

        // LexerParser<RegexInterface1> parser = new
        // LexerParser<RegexInterface1>(RegexInterface1.class);
        //		
        // // parse this => , new String[]{"-Dyy=lll", "-t=testing_this",
        // "-zcvf=asdf", "-w", "aaa", "bbbb", "1234", "999", "666",
        // "--bizarre-long-option", "--testing-a-very-long-option", "A-",
        // "1024", "RegexInterface1-", "D-"}
        //		
        // RegexInterface1 c = parser.options();
        // assertFalse(c.x());
        // assertTrue(c.y());
        // assertTrue(c.z());
        // assertEquals("testing_this", c.zBLA().value);
        // //int[] x = c.e();
        // System.out.println(c.value());
        // System.out.println(c.value2());
        // System.out.println(Arrays.toString(c.value3()));
        //		
        // System.out.println("------------------------");
        //		
        // System.out.println(c.f());
        // System.out.println(c.h());
        // System.out.println(Arrays.toString(c.e()));
        //		
        // System.out.println("------------------------");
        //		
        // System.out.println(c.unparsedArguments());
        //		
        // System.out.println("------------------------");
        //		
        // System.out.println(c.Dvalue());
        //		
        // parser.print();
    }

    @Test
    public void test2() throws Exception {
//         try {
//             new LexerParser<RegexInterface1>(RegexInterface1.class);
//             fail("Arguments with more than 2 hyphens should raise an exception.");
//         }
//         catch (InvalidOptionsInterfaceException e) {
//         }
    }

    @Test
    public void test3() throws Exception {

        Locale.setDefault(new Locale("ja"));

        LexerParser<C> lexPar = new LexerParser<C>(C.class);

        ArrayList<LexToken> expected = new ArrayList<LexToken>();
        expected.add(new LexOption("D"));
        expected.add(new LexOptionMapValue("bc=xyz"));
        expected.add(new LexArgument("test"));
        expected.add(new LexOption("elephant"));
        expected.add(new LexOption("aaa"));
        expected.add(new LexOptionValue("bbb"));
        expected.add(new LexOption("x"));
        expected.add(new LexOption("c"));
        expected.add(new LexOption("v"));
        expected.add(new LexOptionValue("aa=12"));

        assertEquals(expected, lexPar.lexArguments("/Dbc=xyz", "test", "##elephant", "##aaa=bbb", "/xcv=aa=12"));
    }

    @Test
    public void test4() throws Exception {

        LexerParser<C> lexPar = new LexerParser<C>(C.class);

        ArrayList<LexToken> expected = new ArrayList<LexToken>();
        expected.add(new LexArgument("/"));
        expected.add(new LexArgument("user"));

        assertEquals(expected, lexPar.lexArguments("/", "user"));
    }

    @Test
    public void test5() throws Exception {

        LexerParser<C> lexPar = new LexerParser<C>(C.class);

        ArrayList<LexToken> expected = new ArrayList<LexToken>();
        expected.add(new LexArgument("--"));
        expected.add(new LexArgument("user"));

        assertEquals(expected, lexPar.lexArguments("--", "user"));
    }

    @Test
    public void test6() throws Exception {

        LexerParser<SingleHyphenInterface> lexPar = new LexerParser<SingleHyphenInterface>(SingleHyphenInterface.class);

        ArrayList<LexToken> expected = new ArrayList<LexToken>();
        expected.add(new LexOption("pineapple"));
        expected.add(new LexArgument("value"));

        assertEquals(expected, lexPar.lexArguments("-pineapple", "value"));
    }

    @Test
    public void test7() throws Exception {

        LexerParser<SingleHyphenInterface> lexPar = new LexerParser<SingleHyphenInterface>(SingleHyphenInterface.class);

        ArrayList<LexToken> expected = new ArrayList<LexToken>();
        expected.add(new LexOption("pineapple"));
        expected.add(new LexOptionValue("bla"));
        expected.add(new LexArgument("value"));

        assertEquals(expected, lexPar.lexArguments("-pineapple=bla", "value"));
    }

    @Test
    public void test8() throws Exception {

        LexerParser<SingleHyphenInterface> lexPar = new LexerParser<SingleHyphenInterface>(SingleHyphenInterface.class);

        ArrayList<LexToken> expected = new ArrayList<LexToken>();
        expected.add(new LexOption("pineapple"));
        expected.add(new LexOptionMapValue("Key=value"));
        expected.add(new LexArgument("anothervalue"));

        assertEquals(expected, lexPar.lexArguments("-pineappleKey=value", "anothervalue"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void test9() throws FileNotFoundException, IOException, IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, InvalidOptionsInterfaceException {

        Locale.setDefault(new Locale("ja", "JP"));
        DocumentationFormatterEngine<C, ?> formatter = DocumentationFormatterEngine.preferredFormatter(C.class);
        formatter.printDocumentation();
    }

}
