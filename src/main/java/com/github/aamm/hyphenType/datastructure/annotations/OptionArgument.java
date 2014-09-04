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
public @interface OptionArgument {

    String name() default "";

    String option();

    
    /**
     * The index of this option argument. Each option argument
     * should have a <strong>unique</strong> index. Indexes should
     * be a sequence in the form 0, 1, ...<br>
     * <br>
     * This property has 0 as the default value to keep the code
     * clean when there is only one argument.<br>
     * <br>
     * But it is not advisable to omit the index when using more than
     * one option argument since it is harder to read. This is a
     * counter example:<br>
     * <code><pre>
     * &#64;OptionArgument(option="a")
     * String arg1();
     * 
     * &#64;OptionArgument(option="a", index=1)
     * String arg2();
     * </pre></code>
     * The following is more readable:<br>
     * <code><pre>
     * &#64;OptionArgument(option="a", index=0)
     * String arg1();
     * 
     * &#64;OptionArgument(option="a", index=1)
     * String arg2();
     * </pre></code>
     * 
     * @return The index of this option argument.
     */
    int index() default 0;

    /**
     * Regular expression that a string should match in order to be accepted as
     * this argument. Default value will accept anything that does not start
     * with a single or a double hyphen. When you write a regex, you can refer
     * to the single hyphen as \h and the double hyphen as \H. Therefore, the
     * default regex is "[^\Q\h\E&^\Q\H\E].*".<br/>
     * <br/>
     * Note that single and double hyphens are, respectively, the strings "-"
     * and "--", but these strings can be changed through the properties
     * {@link ArgumentsObject#singleHyphen()} and
     * {@link ArgumentsObject#doubleHyphen()}.
     */
    String regex() default "[^\\Q\\h\\E&^\\Q\\H\\E].*";

    InputChannel[] channels() default InputChannel.ARGUMENT;

    boolean mandatory() default false;
    
    String description() default "";
}
