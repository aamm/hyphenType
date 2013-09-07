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
import java.util.Collections;
import java.util.List;

import org.hyphenType.datastructure.parser.StructureElement;

/**
 * @author Aurelio Akira M. Matsui
 */
public class StructureOption extends StructureElement implements Comparable<StructureOption> {

    public final String description;
    public final List<String> alternatives;
    public final StructureOptionValue value;
    public final StructureOptionMapValue map;
    public final List<StructureOptionArgument> arguments;

    public StructureOption(Method method, String description, List<String> alternatives, StructureOptionValue value, StructureOptionMapValue map, List<StructureOptionArgument> arguments) {
        super(method);
        this.description = description;
        this.alternatives = Collections.unmodifiableList(alternatives);
        this.value = value;
        this.map = map;
        this.arguments = Collections.unmodifiableList(arguments);
    }

    @Override
    public String toString() {
        return "[alternatives = " + alternatives + ",  method = " + method + ", value = " + value + " ]";
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof StructureOption))
            return false;
        StructureOption other = (StructureOption) obj;
        return other.method.equals(this.method);
    }

    @Override
    public int hashCode() {
        return method.hashCode();
    }

    @Override
    public int compareTo(StructureOption o) {
        return this.toString().compareTo(o.toString());
    }
}
