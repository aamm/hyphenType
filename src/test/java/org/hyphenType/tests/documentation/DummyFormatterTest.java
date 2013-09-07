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
package org.hyphenType.tests.documentation;

import static junit.framework.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;

import org.hyphenType.datastructure.Options;
import org.hyphenType.exceptions.InvalidOptionsInterfaceException;
import org.hyphenType.lexerparser.exceptions.MandatoryMapValueNotFoundException;
import org.hyphenType.lexerparser.exceptions.MandatorySimpleArgumentNotFoundException;
import org.hyphenType.lexerparser.exceptions.MandatoryValueNotFoundException;
import org.hyphenType.lexerparser.exceptions.RegexMismatchException;
import org.hyphenType.lexerparser.exceptions.StringParseException;
import org.hyphenType.lexerparser.exceptions.StringParsingErrorException;
import org.hyphenType.optionsextractor.OptionsExtractor;
import org.hyphenType.optionsextractor.OptionsExtractorException;
import org.hyphenType.tests.documentation.DummyFormatterEngine.DummyFormatter;
import org.junit.Test;

/**
 * @author Aurelio Akira M. Matsui
 */
public class DummyFormatterTest {

    @Test
    public void test1() throws InvalidOptionsInterfaceException, FileNotFoundException, IllegalArgumentException, SecurityException, IOException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, StringParsingErrorException, MandatoryValueNotFoundException, RegexMismatchException, StringParseException, MandatoryMapValueNotFoundException, MandatorySimpleArgumentNotFoundException, OptionsExtractorException {
        OptionsExtractor<SomeOptions> oe = new OptionsExtractor<SomeOptions>(SomeOptions.class);
        SomeOptions options = oe.options("a");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream pw = new PrintStream(baos);
        options.printDocumentation(pw);
        assertEquals("propertyA = eee, propertyB = zzz, propertyC = propc, propertyD = ze; 0=Test 1=X1 2=ERROR_2", baos.toString());
    }

    @Test
    public void test2() throws InvalidOptionsInterfaceException, FileNotFoundException, IllegalArgumentException, SecurityException, IOException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, StringParsingErrorException, MandatoryValueNotFoundException, RegexMismatchException, StringParseException, MandatoryMapValueNotFoundException, MandatorySimpleArgumentNotFoundException, OptionsExtractorException {
        // Saving the current default locale to eliminate possible interferences
        // this test can cause to other tests.
        Locale backup = Locale.getDefault();

        Locale.setDefault(new Locale("pt"));
        OptionsExtractor<SomeOptions> oe = new OptionsExtractor<SomeOptions>(SomeOptions.class);
        SomeOptions options = oe.options("a");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream pw = new PrintStream(baos);
        options.printDocumentation(pw);
        assertEquals("propertyA = fff, propertyB = zzz, propertyC = propc, propertyD = ze; 0=Test 1=XPort1 2=ERROR_2", baos.toString());

        // Restoring the default locale.
        Locale.setDefault(backup);
    }

    @Test
    public void test3() throws InvalidOptionsInterfaceException, FileNotFoundException, IllegalArgumentException, SecurityException, IOException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, StringParsingErrorException, MandatoryValueNotFoundException, RegexMismatchException, StringParseException, MandatoryMapValueNotFoundException, MandatorySimpleArgumentNotFoundException, OptionsExtractorException {
        // Saving the current default locale to eliminate possible interferences
        // this test can cause to other tests.
        Locale backup = Locale.getDefault();

        Locale.setDefault(new Locale("pt"));
        OptionsExtractor<SomeOptions> oe = new OptionsExtractor<SomeOptions>(SomeOptions.class);
        SomeOptions options = oe.options("a");
        assertEquals("Message1 Portuguese", options.localeMessage("extraMessage1"));
        assertEquals("Message2 English", options.localeMessage("extraMessage2"));

        // Restoring the default locale.
        Locale.setDefault(backup);
    }

    @Test
    public void test4() throws InvalidOptionsInterfaceException, FileNotFoundException, IllegalArgumentException, SecurityException, IOException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, StringParsingErrorException, MandatoryValueNotFoundException, RegexMismatchException, StringParseException, MandatoryMapValueNotFoundException, MandatorySimpleArgumentNotFoundException, OptionsExtractorException {
        // Saving the current default locale to eliminate possible interferences
        // this test can cause to other tests.
        Locale backup = Locale.getDefault();

        Locale.setDefault(new Locale("pt"));
        OptionsExtractor<SomeOptions> oe = new OptionsExtractor<SomeOptions>(SomeOptions.class);
        SomeOptions options = oe.options("a");
        assertEquals("The book is on the table.", options.formattedLocaleMessage("pattern1", "book", "table"));
        assertEquals("X red y blue.", options.formattedLocaleMessage("pattern2", "blue", "red"));

        // Restoring the default locale.
        Locale.setDefault(backup);
    }
    
    /**
     * Tests {@link Options#localeMessage(String, String)}.
     * 
     * @throws InvalidOptionsInterfaceException
     * @throws InvalidOptionException
     * @throws FileNotFoundException
     * @throws IllegalArgumentException
     * @throws SecurityException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws OptionsExtractorException 
     * @throws MandatorySimpleArgumentNotFoundException 
     * @throws MandatoryMapValueNotFoundException 
     * @throws StringParseException 
     * @throws RegexMismatchException 
     * @throws MandatoryValueNotFoundException 
     * @throws StringParsingErrorException 
     */
    @Test
    public void test5() throws InvalidOptionsInterfaceException, FileNotFoundException, IllegalArgumentException, SecurityException, IOException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, StringParsingErrorException, MandatoryValueNotFoundException, RegexMismatchException, StringParseException, MandatoryMapValueNotFoundException, MandatorySimpleArgumentNotFoundException, OptionsExtractorException {
        // Saving the current default locale to eliminate possible interferences
        // this test can cause to other tests.
        Locale backup = Locale.getDefault();

        Locale.setDefault(new Locale("pt"));
        OptionsExtractor<SomeOptions> oe = new OptionsExtractor<SomeOptions>(SomeOptions.class);
        SomeOptions options = oe.options("a");
        assertEquals("Something", options.localeMessage("key293238239", "Something"));
        
        // Restoring the default locale.
        Locale.setDefault(backup);
    }
    
    
    /**
     * Tests {@link Options#formattedLocaleMessageDefault(String, String, Object...)}.
     * 
     * @throws InvalidOptionsInterfaceException
     * @throws InvalidOptionException
     * @throws FileNotFoundException
     * @throws IllegalArgumentException
     * @throws SecurityException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws OptionsExtractorException 
     * @throws MandatorySimpleArgumentNotFoundException 
     * @throws MandatoryMapValueNotFoundException 
     * @throws StringParseException 
     * @throws RegexMismatchException 
     * @throws MandatoryValueNotFoundException 
     * @throws StringParsingErrorException 
     */
    @Test
    public void test6() throws InvalidOptionsInterfaceException, FileNotFoundException, IllegalArgumentException, SecurityException, IOException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, StringParsingErrorException, MandatoryValueNotFoundException, RegexMismatchException, StringParseException, MandatoryMapValueNotFoundException, MandatorySimpleArgumentNotFoundException, OptionsExtractorException {
        // Saving the current default locale to eliminate possible interferences
        // this test can cause to other tests.
        Locale backup = Locale.getDefault();

        Locale.setDefault(new Locale("pt"));
        OptionsExtractor<SomeOptions> oe = new OptionsExtractor<SomeOptions>(SomeOptions.class);
        SomeOptions options = oe.options("a");
        assertEquals("I can see the book and the table.", options.formattedLocaleMessageDefault("pattern999", "I can see the {0} and the {1}.", "book", "table"));
        
        // Restoring the default locale.
        Locale.setDefault(backup);
    }
    
    /**
     * The same as test1, but making a call for a specific documentation
     * formatter, instead of just using the default one.
     * 
     * @throws InvalidOptionsInterfaceException
     * @throws InvalidOptionException
     * @throws FileNotFoundException
     * @throws IllegalArgumentException
     * @throws SecurityException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws OptionsExtractorException 
     * @throws MandatorySimpleArgumentNotFoundException 
     * @throws MandatoryMapValueNotFoundException 
     * @throws StringParseException 
     * @throws RegexMismatchException 
     * @throws MandatoryValueNotFoundException 
     * @throws StringParsingErrorException 
     */
    @Test
    public void test7() throws InvalidOptionsInterfaceException, FileNotFoundException, IllegalArgumentException, SecurityException, IOException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, StringParsingErrorException, MandatoryValueNotFoundException, RegexMismatchException, StringParseException, MandatoryMapValueNotFoundException, MandatorySimpleArgumentNotFoundException, OptionsExtractorException {
        OptionsExtractor<SomeOptions> oe = new OptionsExtractor<SomeOptions>(SomeOptions.class);
        SomeOptions options = oe.options("a");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream pw = new PrintStream(baos);
        options.printDocumentation(DummyFormatter.class, pw);
        assertEquals("propertyA = eee, propertyB = zzz, propertyC = propc, propertyD = ze; 0=Test 1=X1 2=ERROR_2", baos.toString());
    }
}
