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
package com.github.aamm.hyphenType.exit;

import com.github.aamm.hyphenType.documentation.Description;


/**
 * The simplest status code one could possibly conceive. Status codes in this
 * enumeration will not do anything before JVM termination (in other words,
 * {@link StatusCode#beforeExit()} is empty). This enumeration does not specify
 * anything more meaningful than success versus failure. If you want your
 * program to generate more specific codes, create your own enumeration that
 * implements the {@link StatusCode} interface.
 * 
 * @author Aurelio Akira M. Matsui
 */
public enum CanonicalExitCode implements StatusCode {
    /**
     * Status code to report a successful execution of a program.
     */
    @Description("Status code to report a successful execution of a program.\nStatus value is 0 (zero).")
    @ExitStatusConstant(message = "", userDescription = "Successful termination")
    SUCCESS,
    /**
     * Status code to report any problem during the execution of a program.
     */
    @Description("Status code to report any problem during the execution of a program.\nStatus value is 1.")
    @ExitStatusConstant(message = "", userDescription = "Termination with error")
    ERROR;

    @Override
    public void beforeExit(ExitStatusHelper helper) {
        /* 
         * This exit code enum does not execute anything
         * when exiting.
         */
    }
}
