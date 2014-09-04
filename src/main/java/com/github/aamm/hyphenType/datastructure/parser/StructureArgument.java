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
package com.github.aamm.hyphenType.datastructure.parser;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import com.github.aamm.hyphenType.datastructure.annotations.InputChannel;

/**
 * @author Aurelio Akira M. Matsui
 */
public abstract class StructureArgument extends StructureElement implements Comparable<StructureArgument> {

    private final String name;
    private final boolean mandatory;
    private final int index;
    private final String regex;
    private final List<InputChannel> channels;
    private final String description;

    public StructureArgument(String name, Method method, boolean mandatory, int index, String regex, List<InputChannel> channels, String description) {
        super(method);
        this.name = name;
        this.mandatory = mandatory;
        this.index = index;
        this.regex = regex;
        this.channels = Collections.unmodifiableList(channels);
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public int getIndex() {
        return index;
    }

    public String getRegex() {
        return regex;
    }

    public List<InputChannel> getChannels() {
        return channels;
    }
    
    public String getDescription() {
        return description;
    }

    @Override
    public int compareTo(StructureArgument other) {
        return this.index - other.index;
    }
}
