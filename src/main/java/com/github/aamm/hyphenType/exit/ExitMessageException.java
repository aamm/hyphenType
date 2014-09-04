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
// TODO FINISH THIS!!
package com.github.aamm.hyphenType.exit;

/**
 * The base exception that is trapped when a main class is executed and that can
 * be used to find exit codes. When an exception of this class or of a subclass
 * of this class is thrown during the execution of a main class, hyphenType will
 * try to find an exit status constant associated with the exception and use the
 * arguments of this exception to format an error message. Association between
 * exit status constants and exceptions is created using the
 * {@link ExitStatusConstant#equals(Object)} property, in the annotation that
 * decorates an exit status constant.
 * 
 * @author akira
 */
public class ExitMessageException extends Exception {

    /**
     * Serialization UID.
     */
    private static final long serialVersionUID = 4994379172754714249L;

    private String[] arguments;

    /**
     * This constructor requires the caller to provide textual data that
     * will be used to format error messages in exit status constants.
     * 
     * @param arguments Arguments to format error messages.
     */
    public ExitMessageException(String... arguments) {
        this.arguments = arguments;
    }

    /**
     * Arguments to format error messages.
     * 
     * @return Arguments to format error messages
     */
    public String[] getArguments() {
        return arguments;
    }
}
