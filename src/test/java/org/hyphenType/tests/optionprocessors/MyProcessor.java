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
package org.hyphenType.tests.optionprocessors;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.hyphenType.datastructure.Options;
import org.hyphenType.optionprocessors.ArgumentsProcessorEngine;

/**
 * @author Aurelio Akira M. Matsui
 */
public class MyProcessor implements ArgumentsProcessorEngine<MyProcessor.MyValidatorE> {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface MyValidatorE {
	int a();

	int b();
    }

    @Override
    public <T extends Options<?>> void process(Class<T> interfaceClass, T options, MyValidatorE config) {
        if (config.a() != 11 || config.b() != 43)
            throw new RuntimeException("Error");
	options.exit(((MyOptions)options).x());
    }
}
