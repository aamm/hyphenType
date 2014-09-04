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
package com.github.aamm.hyphenType.datastructure.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Aurelio Akira M. Matsui
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SimpleArgument {

    String name() default "";

    /**
     * The index of this simple argument. Each simple argument
     * should have a <strong>unique</strong> index. Indexes should
     * be a sequence in the form 0, 1, ...<br>
     * <br>
     * This property has 0 as the default value to keep the code
     * clean when there is only one argument.<br>
     * <br>
     * But it is not advisable to omit the index when using more than
     * one option argument since it is harder to read. This is a
     * counter example:<br>
     * 
     * <code><pre>
     * &#64;SimpleArgument
     * String arg1();
     * 
     * &#64;SimpleArgument(index=1)
     * String arg2();
     * </pre></code>
     * The following is more readable:<br>
     * <code><pre>
     * &#64;SimpleArgument(index=0)
     * String arg1();
     * 
     * &#64;SimpleArgument(index=1)
     * String arg2();
     * </pre></code>
     * 
     * @return
     */
    int index() default 0;

    String description() default "";

    String regex() default "[^\\Q\\h\\E&^\\Q\\H\\E].*";

    boolean mandatory() default false;

    InputChannel[] channels() default InputChannel.ARGUMENT;
}
