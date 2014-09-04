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
package com.github.aamm.hyphenType.util;

import java.util.Enumeration;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import com.github.aamm.hyphenType.datastructure.Options;
import com.github.aamm.hyphenType.datastructure.annotations.ArgumentsObject;
import com.github.aamm.hyphenType.debug.HTLogger;
import com.github.aamm.hyphenType.documentation.OptionListResourceBundle;
import com.github.aamm.hyphenType.util.resourcebundles.AliasResourceBundle;
import com.github.aamm.hyphenType.util.resourcebundles.GenericEncodingControl;

/**
 * @author Aurelio Akira M. Matsui
 */
public class I18NResourceBundle extends ResourceBundle {

    private ResourceBundle rb;

    public I18NResourceBundle(Class<? extends Options<?>> clazz) {
        String bundlePath = "";

        ArgumentsObject ao = (ArgumentsObject) clazz.getAnnotation(ArgumentsObject.class);

        if (ao == null || ao.resourceBundlesLocation().equals(""))
            bundlePath = clazz.getName();
        else
            bundlePath = ao.resourceBundlesLocation();

        try {
            HTLogger.log("Trying to read " + bundlePath + "...");
            ResourceBundle result;
            if (ao == null)
                result = PropertyResourceBundle.getBundle(bundlePath, Locale.getDefault(), I18NResourceBundle.class.getClassLoader(), new GenericEncodingControl(ArgumentsObject.DEFAULT_RB_ENCODING));
            else
                result = PropertyResourceBundle.getBundle(bundlePath, Locale.getDefault(), I18NResourceBundle.class.getClassLoader(), new GenericEncodingControl(ao.resourceBundlesEncoding()));
            rb = AliasResourceBundle.convert(result);
        } catch (MissingResourceException e) {
            HTLogger.log(e);
            rb = new OptionListResourceBundle(clazz);
        }
        if(HTLogger.debugMode()) {
            for(String key :rb.keySet()) {
                HTLogger.log(String.format("%s = %s", key, rb.getString(key)));
            }
        }
    }

    @Override
    public Enumeration<String> getKeys() {
        return rb.getKeys();
    }

    @Override
    protected Object handleGetObject(String key) {
        return rb.getObject(key);
    }
}
