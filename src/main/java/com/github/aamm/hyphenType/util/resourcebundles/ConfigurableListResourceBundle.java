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
package com.github.aamm.hyphenType.util.resourcebundles;

import java.util.ListResourceBundle;
import java.util.Map;

/**
 * A simple list resource bundle whose content can be manipulated externally.
 * This class is useful when testing a class that reads from a resource bundle.
 * 
 * @author Aurelio Akira M. Matsui
 */
public class ConfigurableListResourceBundle extends ListResourceBundle {

    private Object[][] contents = null;

    public ConfigurableListResourceBundle(Object[][] contents) {
        this.contents = contents;
    }

    public ConfigurableListResourceBundle(Map<?, ?> contentsMap) {
        contents = new Object[contentsMap.size()][2];
        for (int i = 0; i < contentsMap.size(); i++) {
            contents[i][0] = contentsMap.keySet().toArray()[i];
            contents[i][1] = contentsMap.get(contents[i][0]);
        }
    }

    @Override
    protected Object[][] getContents() {
        return contents;
    }
}
