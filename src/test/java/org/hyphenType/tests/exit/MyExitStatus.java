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
//START SNIPPET: 1
package org.hyphenType.tests.exit;

import org.hyphenType.exit.ExitStatusConstant;
import org.hyphenType.exit.ExitStatusHelper;
import org.hyphenType.exit.StatusCode;

public enum MyExitStatus implements StatusCode {

    @ExitStatusConstant(catches=IncompatibleClassChangeError.class, message = "", userDescription = "")
    A,
    @ExitStatusConstant(catches=IllegalArgumentException.class, message = "", userDescription = "")
    B;

    public int calls = 0;

    @Override
    public void beforeExit(ExitStatusHelper helper) {
        calls++;
        System.out.println("~~~>" + helper.getThrowable());
    }
}
