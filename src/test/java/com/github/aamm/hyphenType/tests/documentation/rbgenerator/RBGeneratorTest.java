package com.github.aamm.hyphenType.tests.documentation.rbgenerator;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.junit.Test;

import com.github.aamm.hyphenType.datastructure.annotations.ArgumentsObject;
import com.github.aamm.hyphenType.datastructure.annotations.Option;
import com.github.aamm.hyphenType.datastructure.annotations.OptionValue;
import com.github.aamm.hyphenType.datastructure.annotations.SimpleArgument;
import com.github.aamm.hyphenType.documentation.lib.HTMLFormatterEngine.HTMLFormatter;
import com.github.aamm.hyphenType.documentation.lib.StandardFormatterEngine.StandardFormatter;
import com.github.aamm.hyphenType.documentation.rbgenerator.RBGenerator;
import com.github.aamm.hyphenType.tests.documentation.rbgenerator.RBGeneratorTestValidatorEngine.DummyValidator;
import com.github.aamm.hyphenType.util.DefaultAnnotation;
import com.github.aamm.hyphenType.util.resourcebundles.AliasResourceBundle;

public class RBGeneratorTest {
    
    @Test
    public void test1() throws Throwable {
        // Saving the out to restore it later
        PrintStream out = System.out;
        
        // Executing the RBGenerator
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream pw = new PrintStream(baos);
        System.setOut(pw);
        // Safer main, without guessing which is the main class.
        RBGenerator.main(RBGenerator.class, RBGeneratorTestOptionsInterface.class.getName());
        baos.flush();
        baos.close();
        
        // Restoring previously saved out
        System.setOut(out);
        
        String contents = new String(baos.toByteArray());
        contents = contents.replaceAll(RBGeneratorTestOptionsInterface.class.getName(), CloneOfRBGeneratorTestOptionsInterface.class.getName());
        
        ResourceBundle resourceBundle = new PropertyResourceBundle(new ByteArrayInputStream(contents.getBytes()));
        resourceBundle = AliasResourceBundle.convert(resourceBundle);
        
        ArgumentsObject ao = DefaultAnnotation.getAnnotation(CloneOfRBGeneratorTestOptionsInterface.class, ArgumentsObject.class);
        DefaultAnnotation.fillWithResourceBundle(ao, resourceBundle);
        assertEquals("xyz", ao.description());
        
        Option opA = DefaultAnnotation.getAnnotation(CloneOfRBGeneratorTestOptionsInterface.class.getMethod("a"), Option.class);
        DefaultAnnotation.fillWithResourceBundle(opA, resourceBundle);
        assertEquals("243", opA.description());
        assertArrayEquals(new String[] {"i", "ii", "iii"}, opA.names());
        
        Option opB = DefaultAnnotation.getAnnotation(CloneOfRBGeneratorTestOptionsInterface.class.getMethod("b"), Option.class);
        DefaultAnnotation.fillWithResourceBundle(opB, resourceBundle);
        assertEquals("oiy", opB.description());
        
        OptionValue opBValue = DefaultAnnotation.getAnnotation(CloneOfRBGeneratorTestOptionsInterface.class.getMethod("bValue"), OptionValue.class);
        DefaultAnnotation.fillWithResourceBundle(opBValue, resourceBundle);
        assertEquals(true, opBValue.mandatory());
        
        SimpleArgument arg1Argument = DefaultAnnotation.getAnnotation(CloneOfRBGeneratorTestOptionsInterface.class.getMethod("arg1"), SimpleArgument.class);
        DefaultAnnotation.fillWithResourceBundle(arg1Argument, resourceBundle);
        assertEquals("lalala", arg1Argument.description());
        
        System.out.println(resourceBundle.keySet());
        
        DummyValidator dummyValidator = DefaultAnnotation.getAnnotation(CloneOfRBGeneratorTestOptionsInterface.class, DummyValidator.class);
        DefaultAnnotation.fillWithResourceBundle(dummyValidator, resourceBundle);
        assertEquals(true, dummyValidator.p1());
        assertEquals("pinapple", dummyValidator.p2());
        assertEquals(3, dummyValidator.tree().length);
        //
        assertEquals("red", dummyValidator.tree()[0].subp1());
        assertEquals("green", dummyValidator.tree()[0].subp2());
        assertEquals(0, dummyValidator.tree()[0].subtree().length);
        //
        assertEquals("black", dummyValidator.tree()[1].subp1());
        assertEquals("grey", dummyValidator.tree()[1].subp2());
        assertEquals(2, dummyValidator.tree()[1].subtree().length);
        //
        assertEquals("blue", dummyValidator.tree()[1].subtree()[0].subp1());
        assertEquals("yellow", dummyValidator.tree()[1].subtree()[0].subp2());
        //
        assertEquals("purple", dummyValidator.tree()[1].subtree()[1].subp1());
        assertEquals("pink", dummyValidator.tree()[1].subtree()[1].subp2());
        //
        assertEquals("brown", dummyValidator.tree()[2].subp1());
        assertEquals("orange", dummyValidator.tree()[2].subp2());
        assertEquals(0, dummyValidator.tree()[2].subtree().length);
        
        StandardFormatter standardFormatter = DefaultAnnotation.getAnnotation(CloneOfRBGeneratorTestOptionsInterface.class, StandardFormatter.class);
        DefaultAnnotation.fillWithResourceBundle(standardFormatter, resourceBundle);
        
        assertEquals(20, standardFormatter.descriptionIndent());
        assertEquals(80, standardFormatter.maxColumn());
        
        HTMLFormatter htmlFormatter = DefaultAnnotation.getAnnotation(CloneOfRBGeneratorTestOptionsInterface.class, HTMLFormatter.class);
        DefaultAnnotation.fillWithResourceBundle(htmlFormatter, resourceBundle);
        
        assertEquals("lime", htmlFormatter.usage());
        assertEquals("white", htmlFormatter.meaning());
        assertEquals("status code", htmlFormatter.statusCode());
        
        //TODO continue testing this...
    }
}
