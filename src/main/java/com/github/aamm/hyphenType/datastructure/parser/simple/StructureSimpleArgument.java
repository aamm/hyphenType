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
package com.github.aamm.hyphenType.datastructure.parser.simple;

import java.lang.reflect.Method;
import java.util.List;

import com.github.aamm.hyphenType.datastructure.annotations.InputChannel;
import com.github.aamm.hyphenType.datastructure.parser.StructureArgument;

/**
 * @author Aurelio Akira M. Matsui
 */
public class StructureSimpleArgument extends StructureArgument {

    public StructureSimpleArgument(final Method method, final String name, final boolean mandatory, final int index, final String regex, final List<InputChannel> channels, final String description) {
        super(name, method, mandatory, index, regex, channels, description);
    }

    @Override
    public String toString() {
        return "[ method = " + method + " ]";
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof StructureSimpleArgument))
            return false;
        StructureSimpleArgument other = (StructureSimpleArgument) obj;
        return other.method.equals(this.method);
    }

    @Override
    public int hashCode() {
        return method.hashCode();
    }
}
