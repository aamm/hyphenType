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
package com.github.aamm.hyphenType.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.github.aamm.hyphenType.tests.bugfound.TestBug;
import com.github.aamm.hyphenType.tests.case1.SimplestTest;
import com.github.aamm.hyphenType.tests.case2_valid_invalid_interfaces.InvalidInterfacesTest;
import com.github.aamm.hyphenType.tests.case3_complex_interface.ComplexTest;
import com.github.aamm.hyphenType.tests.documentation.DummyFormatterTest;
import com.github.aamm.hyphenType.tests.documentation.lib.StandardFormatterEngineInterfaceTest;
import com.github.aamm.hyphenType.tests.documentation.lib.StandardFormatterEngineTest;
import com.github.aamm.hyphenType.tests.documentation.rbgenerator.RBGeneratorTest;
import com.github.aamm.hyphenType.tests.exit.ExitStatusTest;
import com.github.aamm.hyphenType.tests.lexer.LexerTest;
import com.github.aamm.hyphenType.tests.lexer.mandatoryarguments.MandatoryArgumentsTest;
import com.github.aamm.hyphenType.tests.lexer.nonstandardhypens.NonstandardTest;
import com.github.aamm.hyphenType.tests.lexer.stopdependingonregex.StopAtRegexUnmatchTest;
import com.github.aamm.hyphenType.tests.optionprocessors.ValidatorsTest;
import com.github.aamm.hyphenType.tests.optionprocessors.lib.BooleanValidatorTest;
import com.github.aamm.hyphenType.tests.optionprocessors.lib.CustomizableValidatorTest;
import com.github.aamm.hyphenType.tests.parser.ParserTest;
import com.github.aamm.hyphenType.tests.regex.RegexTest;
import com.github.aamm.hyphenType.tests.unittesting.UnitTestingUserInputTest;
import com.github.aamm.hyphenType.tests.unittesting.generic.GenericTest;
import com.github.aamm.hyphenType.tests.unittesting.simple.SimpleTest;
import com.github.aamm.hyphenType.tests.util.DefaultAnnotationTest;
import com.github.aamm.hyphenType.tests.util.StringObjectConversionTest;
import com.github.aamm.hyphenType.tests.util.resourcebundles.AliasResourceBundleTest;
import com.github.aamm.hyphenType.tests.util.resourcebundles.ConfigurableListResourceBundleTest;
import com.github.aamm.hyphenType.tests.wrapper.StandAloneAppWrapperTest;

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
        com.github.aamm.hyphenType.tests.unittesting.complex.ComplexTest.class,
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
