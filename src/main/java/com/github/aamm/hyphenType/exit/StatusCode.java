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

import com.github.aamm.hyphenType.datastructure.Options;

/**
 * Status code enumerations. Enumerations that implement this interface can be
 * used as status codes, which are arguments for the method
 * {@link Options#exit(Enum)}.<br>
 * <br>
 * <strong>!ATTENTION!</strong> {@link Enum#ordinal()} is used as the status
 * code, when the method {@link System#exit(int)} is called. Meaning,
 * <strong>the first enumeration constant should necessarily mean successful
 * program execution</strong>, since its ordinal will be zero.
 * 
 * @author Aurelio Akira M. Matsui
 */
public interface StatusCode {
    
    /**
     * A call back method that is invoked by the method
     * {@link Options#exit(Enum)} right before attempting to terminate the VM.
     * This method can be used to output custom message to the console, for
     * instance.<br/>
     * This method is similar to a {@link Runtime#addShutdownHook(Thread)}. The
     * main difference is that this method will be called on the enumeration
     * constant related to the exit status.
     * 
     * @param helper A helper object that gives access to some utility methods.
     */
    public void beforeExit(ExitStatusHelper helper);
}
