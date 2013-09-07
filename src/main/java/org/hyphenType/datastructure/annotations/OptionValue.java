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
package org.hyphenType.datastructure.annotations;

import java.io.File;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Aurelio Akira M. Matsui
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OptionValue {
    String name() default "";

    String option();

    boolean mandatory() default false;

    /**
     * The separator that a user can use to separate elements of arrays, if the
     * type of this option value is an array. This parameter will be ignored if
     * the type of this option value is not an array.
     * 
     * @return The separator for array values.
     */
    String arraySeparator() default ",";

    /**
     * If true and if the type of this option value is an array, hyphenType will
     * use {@link File#pathSeparator} despite of the value of
     * {@link OptionValue#arraySeparator()}. Setting this parameter to true
     * makes it easy to build arrays that behave accordingly to the platform.
     * 
     * @return
     */
    boolean arrayUseFileSeparator() default false;
}
