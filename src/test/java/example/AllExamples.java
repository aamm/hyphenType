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
package example;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author Aurelio Akira M. Matsui
 */
@RunWith(Suite.class)
@Suite.SuiteClasses( {
//
        example.case1._Test.class,
        //
        example.case2._Test.class,
        //
        example.case3._Test.class
})
public class AllExamples {

//    @BeforeClass
//    public static void clean() {
//        File dir = new File("target/examples");
//        delete(dir);
//        dir.mkdirs();
//    }
//
//    private static void delete(File f) {
//        if (f.isDirectory()) {
//            for (File c : f.listFiles())
//                delete(c);
//        }
//        f.delete();
//    }
}
