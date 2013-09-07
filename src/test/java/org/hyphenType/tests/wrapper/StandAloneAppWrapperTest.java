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
package org.hyphenType.tests.wrapper;

import static junit.framework.Assert.assertEquals;

import org.hyphenType.wrapper.StandAloneAppWrapper;
import org.junit.Test;

/**
 * @author Aurelio Akira M. Matsui
 */
public class StandAloneAppWrapperTest {

    @Test
    public void test1() throws Throwable {
	MyApp.x = false;
	MyApp.xopt = "";
	StandAloneAppWrapper.main(new String[] {StandAloneAppWrapper.ARGUMENT + "org.hyphenType.tests.wrapper.MyApp", "-x=BLA!" });
	assertEquals(true, MyApp.x);
	assertEquals("BLA!", MyApp.xopt);
    }

    @Test
    public void test2() throws Throwable {
	MyApp.x = false;
	MyApp.xopt = "";
	System.setProperty(StandAloneAppWrapper.KEY, MyApp.class.getName());
	StandAloneAppWrapper.main("-x=BLA!");
	System.clearProperty(StandAloneAppWrapper.KEY);
	assertEquals(true, MyApp.x);
	assertEquals("BLA!", MyApp.xopt);
    }

    @Test
    public void test3() throws Throwable {
	MyApp.x = false;
	MyApp.xopt = "";
	StandAloneAppWrapper.main(MyApp.class, "-x=BLA!");
	assertEquals(true, MyApp.x);
	assertEquals("BLA!", MyApp.xopt);
    }

    /*
     * WARNING! Could not create a fourth test to check if can read the class
     * name from a system environment variable for two reasons:
     * 
     * (1) because system environment variables cannot be changed once the JVM is running (for the
     * best of our knowledge) and
     * 
     * (2) because system environment variables are a lot dependent on platform. Nevertheless, the
     * routine to read from system environment variables is pretty similar to the one to read from
     * a system property, which we are checking. Then, we have strong reasons to believe that
     * reading from system environment variables will work fine. Likewise, we could not test the
     * JAR option. In theory, it could be possible to test this option, but the test would be too
     * complex to be meaningful.
     */

    @Test
    public void testNonStaticMainMethod() throws Throwable {
	AnotherApp.x = false;
	AnotherApp.xopt = "";
	StandAloneAppWrapper.main(AnotherApp.class, "-x=xyzxyzxyz");
	assertEquals(true, AnotherApp.x);
	assertEquals("xyzxyzxyz", AnotherApp.xopt);
    }
}
