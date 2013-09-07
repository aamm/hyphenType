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
package org.hyphenType.tests.util.resourcebundles;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.MissingResourceException;

import org.hyphenType.util.resourcebundles.ConfigurableListResourceBundle;
import org.junit.Test;

/**
 * @author Aurelio Akira M. Matsui
 */
public class ConfigurableListResourceBundleTest {

    @Test
    public void configurableListResourceBundle() {
	HashMap<String, String> map = new HashMap<String, String>();
	map.put("a", "x");
	map.put("b", "y");
	ConfigurableListResourceBundle clrb = new ConfigurableListResourceBundle(
		map);
	assertEquals("x", clrb.getString("a"));
	assertEquals("y", clrb.getString("b"));
	try {
	    clrb.getString("c");
	    fail("Should throw a MissingResourceException");
	} catch (MissingResourceException e) {
	}
    }
}
