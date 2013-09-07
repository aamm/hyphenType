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
package org.hyphenType.tests.unittesting.complex;

/**
 * @author Aurelio Akira M. Matsui
 */
public class AnotherApplication {

    public static void main(AnotherOptions option) {
        System.out.println("a = " + option.a());
        System.out.println("b = " + option.b());
        throw new NullPointerException("Ouch!");
    }
}