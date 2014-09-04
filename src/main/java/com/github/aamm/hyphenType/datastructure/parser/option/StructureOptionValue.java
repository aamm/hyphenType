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
package com.github.aamm.hyphenType.datastructure.parser.option;

import java.lang.reflect.Method;

import com.github.aamm.hyphenType.datastructure.parser.StructureElement;

/**
 * @author Aurelio Akira M. Matsui
 */
public class StructureOptionValue extends StructureElement {

    public final String name;
    public final boolean mandatory;
    public final String arraySeparator;
    public final boolean arrayUseFileSeparator;

    public StructureOptionValue(final Method method, final String name, final boolean mandatory, final String arraySeparator, final boolean arrayUseFileSeparator) {
        super(method);
        this.name = name;
        this.mandatory = mandatory;
        this.arraySeparator = arraySeparator;
        this.arrayUseFileSeparator = arrayUseFileSeparator;
    }
}
