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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.hyphenType.util.resourcebundles.AliasResourceBundle;
import org.hyphenType.util.resourcebundles.ConfigurableListResourceBundle;
import org.junit.Test;

/**
 * @author Aurelio Akira M. Matsui
 */
public class AliasResourceBundleTest {

    @Test
    public void test1() {
	Map<String, String> map = new HashMap<String, String>();
	map.put("alias.x", "blablablablablabla");
	map.put("alias.y", "some.quite.long.and.troublesome.to.write.string");
	map.put("${x}", "123");
	map.put("${x}.text", "456");
	map.put("${y}", "789");
	map.put("${y}.x", "876");
	ConfigurableListResourceBundle clrb = new ConfigurableListResourceBundle(map);
	ResourceBundle rb = AliasResourceBundle.convert(clrb);
	System.out.println(rb.keySet());
	assertTrue(rb.containsKey("blablablablablabla"));
	assertTrue(rb.containsKey("blablablablablabla.text"));
	assertTrue(rb.containsKey("some.quite.long.and.troublesome.to.write.string"));
	assertTrue(rb.containsKey("some.quite.long.and.troublesome.to.write.string.x"));
	assertFalse(rb.containsKey("alias.x"));
	assertFalse(rb.containsKey("alias.y"));
	assertEquals("123", rb.getString("blablablablablabla"));
	assertEquals("456", rb.getString("blablablablablabla.text"));
	assertEquals("789", rb.getString("some.quite.long.and.troublesome.to.write.string"));
	assertEquals("876", rb.getString("some.quite.long.and.troublesome.to.write.string.x"));
    }
}
