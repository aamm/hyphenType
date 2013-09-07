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
package org.hyphenType.exit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Formatter;

import org.hyphenType.datastructure.Options;
import org.hyphenType.unittesting.NonExceptionalExit;

/**
 * A message to decorate the exit status constants for the <strong>final
 * users</strong>.
 * 
 * @author akira
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExitStatusConstant {

    /**
     * The description that will appear in the documentation for the end user.
     */
    String userDescription();

    /**
     * Message to the end user of the application when a problem happens. The
     * value of this variable may refer to replacements using the standard of
     * {@link Formatter#format(String, Object...)}. The values of replacement
     * objects ({0}, {1}, etc.) depends on how the JVM or environment is going
     * to exit:<br/>
     * <ol>
     * <li>Explicit call to {@link Options#exit(Enum, Object...)} - The values
     * of {0}, {1}, etc will be set to be the values of the array (second
     * argument).</li>
     * <li>Explicit call to {@link Options#exit(int, Object...))} - Idem to the
     * above.</li>
     * <li>Explicit call to {@link Options#exit(Throwable)} - {0} will be set to
     * be {@link Throwable#getLocalizedMessage()} and {1} will be the string
     * printed by calling {@link Throwable#printStackTrace()}.</li>
     * <li>When this exit status constant catches an exception - Exit status
     * constants may catch exceptions listed in the property
     * {@link ExitStatusConstant#catches()}. When an exception is caught by an
     * exit status constant, {0} and {1} will be treated as in the above: {0}
     * will be set to be {@link Throwable#getLocalizedMessage()} and {1} will be
     * the string printed by calling {@link Throwable#printStackTrace()}</li>
     * </ol>
     * In the two last cases, if {@link Throwable#getLocalizedMessage()} is
     * null, it will be replaced by the empty string. This is to avoid the end
     * user receiving a message such as: "Program terminated. Error message:
     * null".
     */
    String message();

    /**
     * The list of throwable classes that will be caught by the exit status
     * constant decorated by this annotation. This list is used to implement a
     * visitor design pattern that traps exceptions or errors from a main
     * method. When an uncaught exception comes from a main class, hyphenType
     * will search for one exit status constant that catches it. If an exit
     * status constant catches the throwable, the method
     * {@link StatusCode#beforeExit(ExitStatusHelper)} is called on this exit
     * status constant and the throwable is accessible to the exit status
     * constant via the method {@link ExitStatusHelper#getThrowable()}.<br/>
     * <br/>
     * It is important to notice that the {@link NonExceptionalExit} cannot
     * be trapped by any exit status constant, since the
     * {@link NonExceptionalExit} exception has a special meaning in
     * hyphenType. For more details about the {@link NonExceptionalExit}
     * exception, refer to the documentation of this class.
     * 
     * @see NonExceptionalExit
     */
    Class<? extends Throwable>[] catches() default {};
}
