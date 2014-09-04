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
package com.github.aamm.hyphenType.dynamicproxy;

import com.github.aamm.hyphenType.datastructure.Options;
import com.github.aamm.hyphenType.exit.StatusCode;
import com.github.aamm.hyphenType.lexerparser.OptionValues;

/**
 * @author Aurelio Akira M. Matsui
 * @param <T>
 *            The option interface type.
 */
public class ConcreteArgumentsInvocationHandler<T extends Options<?>> extends AbstractArgumentsInvocationHandler<T> {

    /**
     * TODO Comment.
     * 
     * @param optionValues
     *            TODO Comment.
     * @param formatter
     *            TODO Comment.
     * @param exitCodeEnumClass
     *            TODO Comment.
     * @param rawArguments
     *            TODO Comment.
     */
    public ConcreteArgumentsInvocationHandler(final OptionValues<T> optionValues, Class<? extends Options<?>> optionsInterface, final Class<? extends StatusCode> exitCodeEnumClass, final String[] rawArguments) {
        super(optionValues, optionsInterface, exitCodeEnumClass, rawArguments);
    }

    /**
     * TODO Comment.
     * 
     * @param e
     *            TODO Comment.
     */
    @Override
    protected final void exit(final Enum<?> e) {
        System.exit(e.ordinal());
    }

    /**
     * TODO Comment.
     * 
     * @param code
     *            TODO Comment.
     */
    @Override
    protected final void exit(final int code) {
        System.exit(code);
    }
}
