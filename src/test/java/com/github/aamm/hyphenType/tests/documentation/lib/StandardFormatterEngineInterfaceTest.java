package com.github.aamm.hyphenType.tests.documentation.lib;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.aamm.hyphenType.documentation.DocumentationFormatterEngine;
import com.github.aamm.hyphenType.documentation.lib.StandardFormatterEngine;
import com.github.aamm.hyphenType.documentation.lib.StandardFormatterEngine.StandardFormatter;

/**
 * Tests the {@link StandardFormatterEngine} using an interface to
 * check if the output is fine.
 * 
 * @author akira
 */
public class StandardFormatterEngineInterfaceTest {

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @SuppressWarnings("unchecked")
    @Test
    public void test1() {
        
        DocumentationFormatterEngine e = StandardFormatterEngine.buildFormatter(MyOptInt.class, StandardFormatter.class);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream p = new PrintStream(baos);
        e.printDocumentation(p);
        
        String expected = "usage: program [options] [argument1] [argument2] \n"+
        "\n"+
        "Testing description\n"+
        "\n"+
        "Arguments\n" +
        "\n"+
        "  argument1         Description argument1\n" +
        "  argument2         Description argument2\n" +
        "\n" +
        "Options\n"+
        "\n"+
        "  -a                Descripton a\n"+
        "  -b[=bValue]       Description b\n";
        
        Assert.assertEquals(expected, new String(baos.toByteArray()));
    }
}
