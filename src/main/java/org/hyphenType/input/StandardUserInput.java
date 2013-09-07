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
package org.hyphenType.input;

import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.io.Console;

import javax.swing.JOptionPane;

import org.hyphenType.util.soc.StringObjectConversion;
import org.hyphenType.util.soc.StringParsingError;

/**
 * @author Aurelio Akira M. Matsui
 */
public class StandardUserInput extends UserInput {

    @Override
    protected boolean isGraphicalUIAvailable() {
        return !GraphicsEnvironment.isHeadless();
    }

    @Override
    protected <T> T readFromGraphicalUI(Class<T> type, String message, String regex) throws UserInputException {
        if (!isGraphicalUIAvailable())
            throw new UserInputException("Graphical environment not available.");
        try {
            String input = JOptionPane.showInputDialog(message);
            if (input == null)
                return null;
            if (!input.matches(regex))
                throw new UserInputException("Input does not match regex.");
            return StringObjectConversion.fromString(type, input);
        } catch (HeadlessException e) {
            throw new UserInputException(e);
        } catch (StringParsingError e) {
            throw new UserInputException(e);
        }
    }

    @Override
    protected boolean isTextUIAvailable() {
        return System.console() != null;
    }

    @Override
    protected <T> T readFromTextUI(Class<T> type, String message, String regex) throws UserInputException {
        if (!isTextUIAvailable())
            throw new UserInputException("System console not available while trying to use text input.");
        try {
            Console console = System.console();
            String input = console.readLine(message + " ");
            if (input.equals(""))
                return null;
            if (!input.matches(regex))
                throw new UserInputException("Input does not match regex.");
            return StringObjectConversion.fromString(type, input);
        } catch (StringParsingError e) {
            throw new UserInputException(e);
        }
    }
}
