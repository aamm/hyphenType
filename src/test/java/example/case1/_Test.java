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
package example.case1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.hyphenType.exceptions.InvalidOptionsInterfaceException;
import org.hyphenType.unittesting.UnitTestingAppEngine;
import org.junit.Test;

/**
 * @author Aurelio Akira M. Matsui
 */
public class _Test {

    @Test
    public void case1() throws InvalidOptionsInterfaceException, IOException {
        UnitTestingAppEngine engine = new UnitTestingAppEngine(XApp.class);
        assertEquals("null", engine.out());
        assertEquals("null", engine.out());
        assertNull(engine.out());
    }

    @Test
    public void case2() throws InvalidOptionsInterfaceException, IOException {
        UnitTestingAppEngine engine = new UnitTestingAppEngine(XApp.class, "aaa", "bbb");
        assertEquals("aaa", engine.out());
        assertEquals("bbb", engine.out());
        assertNull(engine.out());
        engine.saveStdoutInteraction("target/output1.txt");
        //engine.saveStdoutInteractionHTML("target/examples/output1.html", "<div class=\"source\"><pre>", "</pre></div>");
    }

    @Test
    public void case3() throws InvalidOptionsInterfaceException, IOException {
        UnitTestingAppEngine engine = new UnitTestingAppEngine(XApp.class, "aaa", "bbb", "ccc");
        assertEquals("aaa", engine.out());
        assertEquals("bbb", engine.out());
        assertNull(engine.out());
    }

    @Test
    public void case4() throws InvalidOptionsInterfaceException, IOException {
        UnitTestingAppEngine engine = new UnitTestingAppEngine(XApp.class, "aaa", "bbb", "ccc", "ddd");
        assertEquals("aaa", engine.out());
        assertEquals("bbb", engine.out());
        assertNull(engine.out());
        engine.saveStdoutInteraction("target/output2.txt");
    }
}
