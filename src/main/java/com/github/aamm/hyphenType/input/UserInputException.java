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
package com.github.aamm.hyphenType.input;

import java.io.IOException;

/**
 * @author Aurelio Akira M. Matsui
 */
public class UserInputException extends IOException {

    private static final long serialVersionUID = 7910907028773489806L;

    public UserInputException() {
        super();
    }

    public UserInputException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserInputException(String message) {
        super(message);
    }

    public UserInputException(Throwable cause) {
        super(cause);
    }
}
