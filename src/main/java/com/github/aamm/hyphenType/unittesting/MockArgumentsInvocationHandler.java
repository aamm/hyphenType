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
package com.github.aamm.hyphenType.unittesting;

import com.github.aamm.hyphenType.datastructure.Options;
import com.github.aamm.hyphenType.dynamicproxy.AbstractArgumentsInvocationHandler;
import com.github.aamm.hyphenType.dynamicproxy.ConcreteArgumentsInvocationHandler;
import com.github.aamm.hyphenType.exit.StatusCode;
import com.github.aamm.hyphenType.lexerparser.OptionValues;

/**
 * A mock to replace {@link ConcreteArgumentsInvocationHandler}. When this class is used
 * instead of {@link ConcreteArgumentsInvocationHandler}, this class avoids calls to
 * {@link ArgumentsInvocationHandler#exit(Enum))} and
 * {@link ArgumentsInvocationHandler#exit(int)))} to actually terminate the JVM.
 * Instead, this class will log those calls. This way, a unit test can read the
 * logs and determine if user source code is trying to terminate the JVM
 * correctly.<br/>
 * <br/>
 * This class was designed to be utilized by {@link UnitTestingOptionExtractor}.
 * 
 * @author Aurelio Akira M. Matsui
 * @see UnitTestingOptionExtractor
 * @see ConcreteArgumentsInvocationHandler
 * @param <T>
 *            The option interface type.
 */
public class MockArgumentsInvocationHandler<T extends Options<?>> extends AbstractArgumentsInvocationHandler<T> {

    /**
     * Whether one called the {@link MockArgumentsInvocationHandler#exit(Enum)} method or not.
     */
    private boolean exitEnumCalled = false;
    /**
     * The exit status code (enumeration), if called.
     */
    private Enum<?> statusCodeEnum = null;

    /**
     * Whether one called the {@link MockArgumentsInvocationHandler#exit(int)} method or not.
     */
    private boolean exitIntCalled = false;
    /**
     * The exit status code (int), if called.
     */
    private int statusCodeInt = 0;

    /**
     * Whether this object will throw the {@link NonExceptionalExit} exception or not.
     * 
     * @see NonExceptionalExit
     */
    private final boolean throwsNonExceptionalExit;

    /**
     * Sole constructor.
     * 
     * @param values
     *            The values parsed.
     * @param formatter
     *            The documentation formatter.
     * @param exitCodeEnumClass
     *            The enumeration class that will be used to exit.
     * @param rawArguments
     *            The raw arguments "received" from the command line.
     * @param throwsNonExceptionalExit
     *            Whether this mock implementation will throw the
     *            {@link NonExceptionalExit} exception or not.
     */
    public MockArgumentsInvocationHandler(final OptionValues<T> values, final Class<? extends Options<?>> optionsInterface, final Class<? extends StatusCode> exitCodeEnumClass, final String[] rawArguments, final boolean throwsNonExceptionalExit) {
        super(values, optionsInterface, exitCodeEnumClass, rawArguments);
        this.throwsNonExceptionalExit = throwsNonExceptionalExit;
    }

    @Override
    protected final void exit(final Enum<?> e) {
        if (exitEnumCalled) {
            throw new RuntimeException("Invalid state. Cannot call exit(Enum) more than once.");
        }
        exitEnumCalled = true;
        System.out.println("Pretending to exit the VM using code: " + e);
        statusCodeEnum = e;
        if (throwsNonExceptionalExit) {
            throw new NonExceptionalExit(e);
        }
    }

    @Override
    protected final void exit(final int code) {
        if (exitIntCalled) {
            throw new RuntimeException("Invalid state. Cannot call exit(int) more than once.");
        }
        exitIntCalled = true;
        System.out.println("Pretending to exit the VM using code: " + code);
        statusCodeInt = code;
        if (throwsNonExceptionalExit) {
            throw new NonExceptionalExit(code);
        }
    }

    /**
     * Useful to analyze how the
     * {@link MockArgumentsInvocationHandler#exit(Enum)} method was called.
     * 
     * @return The status code returned by the last call to the
     *         {@link MockArgumentsInvocationHandler#exit(Enum)} method.
     */
    public final Enum<?> getStatusCodeEnum() {
        if (!exitEnumCalled) {
            throw new RuntimeException("Invalid state. exit(Enum) was not called yet.");
        }
        return statusCodeEnum;
    }

    /**
     * Useful to check whether the
     * {@link MockArgumentsInvocationHandler#exit(Enum)} method was called or
     * not.
     * 
     * @return Whether the method
     *         {@link MockArgumentsInvocationHandler#exit(Enum)} was called or
     *         not.
     */
    public final boolean exitEnumCalled() {
        return exitEnumCalled;
    }

    /**
     * Useful to analyze how the
     * {@link MockArgumentsInvocationHandler#exit(int)} method was called.
     * 
     * @return The status code returned by the last call to the
     *         {@link MockArgumentsInvocationHandler#exit(int)} method.
     */
    public final int getStatusCodeInt() {
        if (!exitIntCalled) {
            throw new RuntimeException("Invalid state. exit(int) was not called yet.");
        }
        return statusCodeInt;
    }

    /**
     * Useful to check whether the
     * {@link MockArgumentsInvocationHandler#exit(int)} method was called or
     * not.
     * 
     * @return Whether the method
     *         {@link MockArgumentsInvocationHandler#exit(int)} was called or
     *         not.
     */
    public final boolean exitIntCalled() {
        return exitIntCalled;
    }
}
