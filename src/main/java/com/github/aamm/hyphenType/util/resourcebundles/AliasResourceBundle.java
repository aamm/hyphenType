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

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Matcher;

/**
 * @author Aurelio Akira M. Matsui
 */
public final class AliasResourceBundle {

    /**
     * Makes is a utility class.
     */
    private AliasResourceBundle() {
    }

    public static final String ALIAS_DECLARATION_PREFIX = "alias.";
    
    /**
     * Takes a resource bundle and creates a new one with the aliases replaced.
     * Will search for properties in the format "alias.<i>name</i>", where
     * <i>name</i> is the name of the alias. Whenever an alias is found,
     * replaces this alias as a prefix, whenever the prefix is found as the
     * start of any property. For example, takes the following
     * 
     * <pre>
     * alias.x = blablablablablablablabla
     * alias.y = yyyy
     * ${x} = A
     * ${x}.b = Z
     * ${x}.${y} = W
     * </pre>
     * 
     * and converts into the following:
     * 
     * <pre>
     * blablablablablablablabla = A
     * blablablablablablablabla.b = Z
     * blablablablablablablabla.yyyy = W
     * </pre>
     * 
     * @param rb
     *            The resource bundle to convert.
     * @return The converted resource bundle.
     */
    public static ResourceBundle convert(final ResourceBundle rb) {

        Map<String, String> aliases = new HashMap<String, String>();
        for (String key : rb.keySet()) {
            if (key.startsWith(ALIAS_DECLARATION_PREFIX)) {
                aliases.put(key.substring(ALIAS_DECLARATION_PREFIX.length()), rb.getString(key));
            }
        }
        
        Map<String, String> newRBMap = new HashMap<String, String>();
        for (String key : rb.keySet()) {
            if (!key.startsWith(ALIAS_DECLARATION_PREFIX)) {
                String newKey = key;
                for (String alias : aliases.keySet()) {
                    if (newKey.contains("${" + alias + "}")) {
                        newKey = newKey.replaceAll("\\$\\{" + alias + "\\}", Matcher.quoteReplacement(aliases.get(alias)));
                    }
                }
                newRBMap.put(newKey, rb.getString(key));
            }
        }

        return new ConfigurableListResourceBundle(newRBMap);
    }
}
