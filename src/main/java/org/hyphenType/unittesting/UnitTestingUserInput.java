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
package org.hyphenType.unittesting;

import java.util.ArrayList;

import org.hyphenType.input.UserInput;
import org.hyphenType.input.UserInputException;

/**
 * An user input to replace a standard user input during unit testing.
 * 
 * @author Aurelio Akira M. Matsui
 */
public final class UnitTestingUserInput extends UserInput {

    // //////////////////////////////////////////
    // TEXT
    // //////////////////////////////////////////

    /**
     * 
     */
    private ArrayList<Object> textUIInputs = new ArrayList<Object>();

    /**
     * 
     */
    private boolean textUIAvailable = true;

    /**
     * @param input The input to add.
     */
    public void addTextUIInput(final Object input) {
        textUIInputs.add(input);
    }

    /**
     * Sets whether the textual user interface is available
     * or not.
     * 
     * @param available The value to set.
     */
    public void setTextUIAvailable(final boolean available) {
        this.textUIAvailable = available;
    }

    @Override
    protected boolean isTextUIAvailable() {
        return textUIAvailable;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <T> T readFromTextUI(final Class<T> type, final String message, final String regex) throws UserInputException {
        if (textUIInputs.size() > 0) {
            return (T) textUIInputs.remove(0);
        } else {
            throw new UserInputException("No input text available");
        }
    }

    // //////////////////////////////////////////
    // GRAPHICAL
    // //////////////////////////////////////////

    /**
     * 
     */
    private ArrayList<Object> graphicalUIInputs = new ArrayList<Object>();

    /**
     * 
     */
    private boolean graphicalUIAvailable = true;

    /**
     * @param input The input to add.
     */
    public void addGraphicalUIInput(final Object input) {
        graphicalUIInputs.add(input);
    }

    /**
     * Sets whether the graphical user interface is available
     * or not.
     * 
     * @param available The value to set.
     */
    public void setGraphicalUIAvailable(final boolean available) {
        this.graphicalUIAvailable = available;
    }

    @Override
    protected boolean isGraphicalUIAvailable() {
        return graphicalUIAvailable;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <T> T readFromGraphicalUI(final Class<T> type, final String message, final String regex) throws UserInputException {
        if (graphicalUIInputs.size() > 0) {
            return (T) graphicalUIInputs.remove(0);
        } else {
            throw new UserInputException("No input text available");
        }
    }

    // //////////////////////////////////////////
    // BOTH
    // //////////////////////////////////////////

    /**
     * Adds an object to both text and graphical UI inputs.
     * 
     * @param input The input to add.
     */
    public void addBothUIInput(final Object input) {
        addTextUIInput(input);
        addGraphicalUIInput(input);
    }
}
