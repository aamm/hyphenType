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
package com.github.aamm.hyphenType.documentation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.ListResourceBundle;

import com.github.aamm.hyphenType.datastructure.annotations.Option;
import com.github.aamm.hyphenType.datastructure.annotations.SimpleArgument;

/**
 * @author Aurelio Akira M. Matsui
 */
public class OptionListResourceBundle extends ListResourceBundle {

    public static final String PROPERTIES_PREFIX = OptionListResourceBundle.class.getPackage().getName();

    private Object[][] map;

    @SuppressWarnings("unchecked")
    public OptionListResourceBundle(Class optionsInterface) {

        ArrayList<String> keys = new ArrayList<String>();
        ArrayList<String> values = new ArrayList<String>();

        for (Method method : optionsInterface.getMethods()) {
            if (method.isAnnotationPresent(Option.class)) {
                keys.add(PROPERTIES_PREFIX + method.getName());
                values.add(((Option) method.getAnnotation(Option.class)).description());
            } else if (method.isAnnotationPresent(SimpleArgument.class)) {
                keys.add(PROPERTIES_PREFIX + method.getName());
                values.add(((SimpleArgument) method.getAnnotation(SimpleArgument.class)).description());
            }
        }

        map = new Object[keys.size()][2];
        for (int i = 0; i < keys.size(); i++) {
            map[i][0] = keys.get(i);
            map[i][1] = values.get(i);
        }
    }

    @Override
    protected Object[][] getContents() {
        return map;
    }
}
