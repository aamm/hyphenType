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

/**
 * @author Aurelio Akira M. Matsui
 */
public enum InputChannel {
    /**
     * An argument passed to the command line or static string.
     */
    ARGUMENT,
    /**
     * Reads input from interactive text-based interface.
     */
    TEXT,
    /**
     * Shows a Swing graphical interface requesting user input.
     */
    GUI
}
