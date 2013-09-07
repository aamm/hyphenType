package org.hyphenType.tests.documentation.lib;

import org.hyphenType.documentation.lib.StandardFormatterEngine;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StandardFormatterEngineTest {

    @SuppressWarnings("unchecked")
    StandardFormatterEngine engine;
    String expected;
    String title;
    String message;
    int descriptionIndent;
    int maxColumn;
    boolean lineBreakAtSpace;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        engine = new StandardFormatterEngine();
    }

    @After
    public void tearDown() {
        String actual = engine.print(title, message, 2, descriptionIndent, maxColumn, lineBreakAtSpace);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void test1() {
        title = "XYZ";
        message = "Bla";
        descriptionIndent = 20;
        maxColumn = 40;
        lineBreakAtSpace = true;

        expected = "  XYZ               Bla";
        // -------- 00000000001111111111222222222233333333334444444444
        // -------- 01234567890123456789012345678901234567890123456789
    }

    @Test
    public void test2() {
        title = "ZZZ";
        message = "Bla ble blo";
        descriptionIndent = 11;
        maxColumn = 15;
        lineBreakAtSpace = true;

        expected = "  ZZZ      Bla \n" +
                   "           ble \n" +
                   "           blo";
        // -------- 00000000001111111111222222222233333333334444444444
        // -------- 01234567890123456789012345678901234567890123456789
    }

    @Test
    public void test3() {
        title = "Zaaaaaaaaaaaaz";
        message = "Bla ble blo";
        descriptionIndent = 11;
        maxColumn = 15;
        lineBreakAtSpace = true;

        expected = "  Zaaaaaaaaaaaaz\n" +
                   "           Bla \n" +
                   "           ble \n" +
                   "           blo";
        // -------- 00000000001111111111222222222233333333334444444444
        // -------- 01234567890123456789012345678901234567890123456789
    }

    @Test
    public void test4() {
        title = "A";
        message = "Blab bleb blob";
        descriptionIndent = 11;
        maxColumn = 15;
        lineBreakAtSpace = true;

        expected = "  A        Blab\n" +
                   "           bleb\n" +
                   "           blob";
        // -------- 00000000001111111111222222222233333333334444444444
        // -------- 01234567890123456789012345678901234567890123456789
    }


    @Test
    public void test5() {
        title = "A";
        message = "Blable";
        descriptionIndent = 11;
        maxColumn = 15;
        lineBreakAtSpace = true;

        expected = "  A        Blab\n" +
                   "           le";
        // -------- 00000000001111111111222222222233333333334444444444
        // -------- 01234567890123456789012345678901234567890123456789
    }

    @Test
    public void test6() {
        title = "A";
        message = "Blableblibloblu";
        descriptionIndent = 11;
        maxColumn = 15;
        lineBreakAtSpace = true;

        expected = "  A        Blab\n" +
                   "           lebl\n" +
                   "           iblo\n" +
                   "           blu";
        // -------- 00000000001111111111222222222233333333334444444444
        // -------- 01234567890123456789012345678901234567890123456789
    }

    @Test
    public void test7() {
        title = "A";
        message = "Blablebliblobl x";
        descriptionIndent = 11;
        maxColumn = 15;
        lineBreakAtSpace = true;

        expected = "  A        Blab\n" +
                   "           lebl\n" +
                   "           iblo\n" +
                   "           bl x";
        // -------- 00000000001111111111222222222233333333334444444444
        // -------- 01234567890123456789012345678901234567890123456789
    }

    @Test
    public void test8() {
        title = "A";
        message = "x Blablebliblobl";
        descriptionIndent = 11;
        maxColumn = 15;
        lineBreakAtSpace = true;

        expected = "  A        x Bl\n" +
                   "           able\n" +
                   "           blib\n" +
                   "           lobl";
        // -------- 00000000001111111111222222222233333333334444444444
        // -------- 01234567890123456789012345678901234567890123456789
    }

    @Test
    public void test9() {
        title = "Awewewewewew";
        message = "x Blablebliblobl";
        descriptionIndent = 11;
        maxColumn = 15;
        lineBreakAtSpace = true;

        expected = "  Awewewewewew\n" +
                   "           x Bl\n" +
                   "           able\n" +
                   "           blib\n" +
                   "           lobl";
        // -------- 00000000001111111111222222222233333333334444444444
        // -------- 01234567890123456789012345678901234567890123456789
    }

    @Test
    public void test10() {
        title = "Awewewewe";
        message = "x Blablebliblobl";
        descriptionIndent = 11;
        maxColumn = 15;
        lineBreakAtSpace = true;

        expected = "  Awewewewe\n" +
                   "           x Bl\n" +
                   "           able\n" +
                   "           blib\n" +
                   "           lobl";
        // -------- 00000000001111111111222222222233333333334444444444
        // -------- 01234567890123456789012345678901234567890123456789
    }

    @Test
    public void test11() {
        title = "TEST";
        message = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        descriptionIndent = 11;
        maxColumn = 15;
        lineBreakAtSpace = false;

        expected = "  TEST     ABCD\n" +
                   "           EFGH\n" +
                   "           IJKL\n" +
                   "           MNOP\n" +
                   "           QRST\n" +
                   "           UVWX\n" +
                   "           YZ";
        // -------- 00000000001111111111222222222233333333334444444444
        // -------- 01234567890123456789012345678901234567890123456789
    }

    @Test
    public void test12() {
        title = "TEST";
        message = "ABCDEFGHIJKLMNOPQRSTUVWXYZ12";
        descriptionIndent = 11;
        maxColumn = 15;
        lineBreakAtSpace = false;

        expected = "  TEST     ABCD\n" +
                   "           EFGH\n" +
                   "           IJKL\n" +
                   "           MNOP\n" +
                   "           QRST\n" +
                   "           UVWX\n" +
                   "           YZ12";
        // -------- 00000000001111111111222222222233333333334444444444
        // -------- 01234567890123456789012345678901234567890123456789
    }
}
