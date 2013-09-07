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
package org.hyphenType.datastructure.parser.option;

import java.lang.reflect.Method;

import org.hyphenType.datastructure.parser.StructureElement;

/**
 * @author Aurelio Akira M. Matsui
 */
public class StructureOptionMapValue extends StructureElement {
    public final String keyName;
    public final String valueName;
    @SuppressWarnings("unchecked")
    public final Class valueType;
    public final boolean mandatory;

    @SuppressWarnings("unchecked")
    public StructureOptionMapValue(Method method, String keyName, String valueName, Class valueType, boolean mandatory) {
        super(method);
        this.keyName = keyName;
        this.valueName = valueName;
        this.valueType = valueType;
        this.mandatory = mandatory;
    }
}
