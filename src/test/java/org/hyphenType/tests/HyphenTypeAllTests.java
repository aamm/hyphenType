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
package org.hyphenType.tests;

import org.hyphenType.tests.bugfound.TestBug;
import org.hyphenType.tests.case1.SimplestTest;
import org.hyphenType.tests.case2_valid_invalid_interfaces.InvalidInterfacesTest;
import org.hyphenType.tests.case3_complex_interface.ComplexTest;
import org.hyphenType.tests.documentation.DummyFormatterTest;
import org.hyphenType.tests.documentation.lib.StandardFormatterEngineInterfaceTest;
import org.hyphenType.tests.documentation.lib.StandardFormatterEngineTest;
import org.hyphenType.tests.documentation.rbgenerator.RBGeneratorTest;
import org.hyphenType.tests.exit.ExitStatusTest;
import org.hyphenType.tests.lexer.LexerTest;
import org.hyphenType.tests.lexer.mandatoryarguments.MandatoryArgumentsTest;
import org.hyphenType.tests.lexer.nonstandardhypens.NonstandardTest;
import org.hyphenType.tests.lexer.stopdependingonregex.StopAtRegexUnmatchTest;
import org.hyphenType.tests.optionprocessors.ValidatorsTest;
import org.hyphenType.tests.optionprocessors.lib.BooleanValidatorTest;
import org.hyphenType.tests.optionprocessors.lib.CustomizableValidatorTest;
import org.hyphenType.tests.parser.ParserTest;
import org.hyphenType.tests.regex.RegexTest;
import org.hyphenType.tests.unittesting.UnitTestingUserInputTest;
import org.hyphenType.tests.unittesting.generic.GenericTest;
import org.hyphenType.tests.unittesting.simple.SimpleTest;
import org.hyphenType.tests.util.DefaultAnnotationTest;
import org.hyphenType.tests.util.StringObjectConversionTest;
import org.hyphenType.tests.util.resourcebundles.AliasResourceBundleTest;
import org.hyphenType.tests.util.resourcebundles.ConfigurableListResourceBundleTest;
import org.hyphenType.tests.wrapper.StandAloneAppWrapperTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import example.AllExamples;

/**
 * @author Aurelio Akira M. Matsui
 */
@RunWith(Suite.class)
@Suite.SuiteClasses( {
//
        AllExamples.class,
        //
        LexerTest.class,
        //
        StandAloneAppWrapperTest.class,
        //
        SimplestTest.class,
        //
        InvalidInterfacesTest.class,
        //
        ParserTest.class,
        //
        ComplexTest.class,
        //
        TestBug.class,
        //
        StringObjectConversionTest.class,
        //
        NonstandardTest.class,
        //
        RegexTest.class,
        //
        ExitStatusTest.class,
        //
        ValidatorsTest.class,
        //
        BooleanValidatorTest.class,
        //
        DummyFormatterTest.class,
        //
        DefaultAnnotationTest.class,
        //
        ConfigurableListResourceBundleTest.class,
        //
        MandatoryArgumentsTest.class,
        //
        SimpleTest.class,
        //
        org.hyphenType.tests.unittesting.complex.ComplexTest.class,
        //
        GenericTest.class,
        //
        StopAtRegexUnmatchTest.class,
        //
        AliasResourceBundleTest.class,
        //
        UnitTestingUserInputTest.class,
        //
        RBGeneratorTest.class,
        //
        CustomizableValidatorTest.class,
        //
        StandardFormatterEngineTest.class,
        //
        StandardFormatterEngineInterfaceTest.class
        })
public class HyphenTypeAllTests {

}
