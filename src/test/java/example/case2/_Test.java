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
package example.case2;

import java.io.IOException;

import org.junit.Test;

import com.github.aamm.hyphenType.exceptions.InvalidOptionsInterfaceException;
import com.github.aamm.hyphenType.unittesting.UnitTestingAppEngine;

/**
 * @author Aurelio Akira M. Matsui
 */
public class _Test {

    @Test
    public void case1() throws InvalidOptionsInterfaceException, IOException {
        UnitTestingAppEngine engine = new UnitTestingAppEngine(XApp.class);
        engine.saveStdoutInteraction("target/output3.txt");
    }
}
