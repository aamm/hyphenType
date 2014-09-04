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

import java.lang.reflect.Array;
import java.util.ArrayList;

import com.github.aamm.hyphenType.datastructure.annotations.InputChannel;
import com.github.aamm.hyphenType.datastructure.parser.StructureArgument;

/**
 * @author Aurelio Akira M. Matsui
 */
public abstract class UserInput {

    @SuppressWarnings("unchecked")
    public <T> T[] readArray(final StructureArgument argument, final String singleHyphen, final String doubleHyphen) throws UserInputException {

        if (!argument.getChannels().contains(InputChannel.TEXT) && !argument.getChannels().contains(InputChannel.GUI)) {
            throw new IllegalArgumentException(String.format("Input channels should have at least one of %s or %s.", InputChannel.TEXT, InputChannel.GUI));
        }
        
        if (!isGraphicalUIAvailable() && !isTextUIAvailable()) {
            throw new UserInputException("No input method available. Before invoking a user input, make sure there is a textual or graphical input.");
        }
        
        if (!(argument.getChannels().contains(InputChannel.GUI) && isGraphicalUIAvailable() || argument.getChannels().contains(InputChannel.TEXT) && isTextUIAvailable())) {
            throw new UserInputException("No argument input channel combination available.");
        }

        String regex = argument.getRegex();
        regex = regex.replace("\\h", singleHyphen);
        regex = regex.replace("\\H", doubleHyphen);

        T input = null;
        ArrayList<T> list = new ArrayList<T>();
        int i = 0;
        do {
            if (argument.getChannels().contains(InputChannel.GUI) && isGraphicalUIAvailable()) {
                input = (T) readFromGraphicalUI(argument.method.getReturnType().getComponentType(), String.format("%s[%d]:", argument.getName(), i), regex);
            } else if (isTextUIAvailable()) {
                input = (T) readFromTextUI(argument.method.getReturnType().getComponentType(), String.format("%s[%d]:", argument.getName(), i), regex);
            }
            i++;
            if (input != null) {
                list.add(input);
            }
        } while (input != null);
        
        return list.toArray((T[]) Array.newInstance(argument.method.getReturnType().getComponentType(), list.size()));
    }

    @SuppressWarnings("unchecked")
    public <T> T readString(final StructureArgument argument, final String singleHyphen, final String doubleHyphen) throws UserInputException {
        
        if (!argument.getChannels().contains(InputChannel.TEXT) && !argument.getChannels().contains(InputChannel.GUI)) {
            throw new UserInputException(String.format("Input channels should have at least one of %s or %s.", InputChannel.TEXT, InputChannel.GUI));
        }

        String regex = argument.getRegex();
        regex = regex.replace("\\h", singleHyphen);
        regex = regex.replace("\\H", doubleHyphen);

        if (argument.getChannels().contains(InputChannel.GUI) && isGraphicalUIAvailable()) {
            return (T) readFromGraphicalUI(argument.method.getReturnType(), String.format("%s:", argument.getName()), regex);
        }
        else if (isTextUIAvailable()) {
            return (T) readFromTextUI(argument.method.getReturnType(), String.format("%s:", argument.getName()), regex);
        }
        else {
            throw new UserInputException("No input method available. Before invoking a user input, make sure there is a textual or graphical input.");
        }
    }

    protected abstract boolean isTextUIAvailable();

    protected abstract <T> T readFromTextUI(Class<T> type, String message, String regex) throws UserInputException;

    protected abstract boolean isGraphicalUIAvailable();

    protected abstract <T> T readFromGraphicalUI(Class<T> type, String message, String regex) throws UserInputException;
}
