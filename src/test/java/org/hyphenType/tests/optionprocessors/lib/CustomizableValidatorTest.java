package org.hyphenType.tests.optionprocessors.lib;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.hyphenType.exceptions.InvalidOptionsInterfaceException;
import org.hyphenType.lexerparser.exceptions.MandatoryMapValueNotFoundException;
import org.hyphenType.lexerparser.exceptions.MandatorySimpleArgumentNotFoundException;
import org.hyphenType.lexerparser.exceptions.MandatoryValueNotFoundException;
import org.hyphenType.lexerparser.exceptions.RegexMismatchException;
import org.hyphenType.lexerparser.exceptions.StringParseException;
import org.hyphenType.lexerparser.exceptions.StringParsingErrorException;
import org.hyphenType.optionsextractor.OptionsExtractorException;
import org.hyphenType.unittesting.UnitTestingOptionExtractor;
import org.junit.Test;


public class CustomizableValidatorTest {

    @Test
    public void testNoError() throws InvalidOptionsInterfaceException, StringParsingErrorException, MandatoryValueNotFoundException, RegexMismatchException, StringParseException, MandatoryMapValueNotFoundException, MandatorySimpleArgumentNotFoundException, OptionsExtractorException {
        UnitTestingOptionExtractor<CustomizableValidatorTestInterface> oe = new UnitTestingOptionExtractor<CustomizableValidatorTestInterface>(CustomizableValidatorTestInterface.class, false);
        CustomizableValidatorTestInterface options;
        options = oe.options("-a", "-a", "-d", "-e");
        assertFalse(oe.exitIntCalled(options));
    }

    @Test
    public void testErrorR1() throws InvalidOptionsInterfaceException, StringParsingErrorException, MandatoryValueNotFoundException, RegexMismatchException, StringParseException, MandatoryMapValueNotFoundException, MandatorySimpleArgumentNotFoundException, OptionsExtractorException {
        UnitTestingOptionExtractor<CustomizableValidatorTestInterface> oe = new UnitTestingOptionExtractor<CustomizableValidatorTestInterface>(CustomizableValidatorTestInterface.class, false);
        CustomizableValidatorTestInterface options;
        options = oe.options("-a", "-a", "-b", "-b");
        assertTrue(oe.exitIntCalled(options));
        assertEquals(7, oe.getStatusCodeInt(options));
    }

    @Test
    public void testErrorR2() throws InvalidOptionsInterfaceException, StringParsingErrorException, MandatoryValueNotFoundException, RegexMismatchException, StringParseException, MandatoryMapValueNotFoundException, MandatorySimpleArgumentNotFoundException, OptionsExtractorException {
        UnitTestingOptionExtractor<CustomizableValidatorTestInterface> oe = new UnitTestingOptionExtractor<CustomizableValidatorTestInterface>(CustomizableValidatorTestInterface.class, false);
        CustomizableValidatorTestInterface options;
        options = oe.options("-b", "-d", "-c", "-e");
        assertTrue(oe.exitIntCalled(options));
        assertEquals(11, oe.getStatusCodeInt(options));
    }

    @Test
    public void testErrorR3() throws InvalidOptionsInterfaceException, StringParsingErrorException, MandatoryValueNotFoundException, RegexMismatchException, StringParseException, MandatoryMapValueNotFoundException, MandatorySimpleArgumentNotFoundException, OptionsExtractorException {
        UnitTestingOptionExtractor<CustomizableValidatorTestInterface> oe = new UnitTestingOptionExtractor<CustomizableValidatorTestInterface>(CustomizableValidatorTestInterface.class, false);
        CustomizableValidatorTestInterface options;
        options = oe.options("-a", "-x", "-y", "-c");
        assertTrue(oe.exitIntCalled(options));
        assertEquals(13, oe.getStatusCodeInt(options));
    }
}
