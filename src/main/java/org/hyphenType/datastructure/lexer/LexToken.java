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
package org.hyphenType.datastructure.lexer;

/**
 * @author Aurelio Akira M. Matsui
 */
public abstract class LexToken {

    public final String value;

    public LexToken(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " : \"" + value + "\"";
    }

    @Override
    public boolean equals(Object obj) {
        LexToken other = (LexToken) obj;
        return this.value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
