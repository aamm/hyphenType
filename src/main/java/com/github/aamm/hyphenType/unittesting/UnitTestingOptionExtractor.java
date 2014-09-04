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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import com.github.aamm.hyphenType.datastructure.Options;
import com.github.aamm.hyphenType.exceptions.InvalidOptionsInterfaceException;
import com.github.aamm.hyphenType.exit.StatusCode;
import com.github.aamm.hyphenType.lexerparser.OptionValues;
import com.github.aamm.hyphenType.optionprocessors.ArgumentsProcessorEngine;
import com.github.aamm.hyphenType.optionsextractor.OptionsExtractor;

/**
 * An {@link OptionsExtractor} that is ready for unit tests. This kind of
 * {@link OptionsExtractor} will replace the actual routines to terminate the
 * JVM ({@link Options#exit(Enum)} and {@link Options#exit(int)}) with mock
 * implementations. Calls for exit methods will be logged and can be read by
 * unit tests after the code under test was executed. To analyze the usage of
 * exit methods, the following methods are available:<br>
 * <ul>
 * <li> {@link UnitTestingOptionExtractor#getStatusCodeEnum(Options)}</li>
 * <li> {@link UnitTestingOptionExtractor#exitEnumCalled(Options)}</li>
 * <li> {@link UnitTestingOptionExtractor#getStatusCodeInt(Options)}</li>
 * <li> {@link UnitTestingOptionExtractor#exitIntCalled(Options)}</li>
 * </ul>
 * This class can also be utilized to test option interface processors (see
 * {@link ArgumentsProcessorEngine}).
 * 
 * @author Aurelio Akira M. Matsui
 * @param <T>
 * @see ArgumentsProcessorEngine
 * @see OptionsExtractor
 */
public class UnitTestingOptionExtractor<T extends Options<?>> extends OptionsExtractor<T> {

    private final boolean throwsNotExceptionalExit;

    /**
     * Creates a new {@link UnitTestingOptionExtractor} object that will work as
     * a factory for objects whose options interface's class is the class given
     * as argument. This constructor will configure
     * {@link UnitTestingOptionExtractor} to factor option objects that throw
     * {@link NonExceptionalExit} exceptions. If you want to chose whether or
     * not the options objects will throw {@link NonExceptionalExit}, use the
     * constructor
     * {@link UnitTestingOptionExtractor#UnitTestingOptionExtractor(Class, boolean)}
     * instead.
     * 
     * @param clazz
     * @throws InvalidOptionsInterfaceException
     * @throws InvalidOptionException
     */
    public UnitTestingOptionExtractor(Class<T> clazz) throws InvalidOptionsInterfaceException {
        this(clazz, true);
    }

    /**
     * Creates a new {@link UnitTestingOptionExtractor} object in a way that
     * allows the caller to chose whether or not calls to
     * {@link Options#exit(Enum)} or {@link Options#exit(int)} will result into
     * throwing a {@link NonExceptionalExit} exception.
     * 
     * @param clazz
     *            The option interface class.
     * @param throwsNotExceptionalExit
     *            A flag to configure whether option objects will throw
     *            {@link NonExceptionalExit} when one calls
     *            {@link Options#exit(Enum)} or {@link Options#exit(int)}.
     * @throws InvalidOptionsInterfaceException
     * @throws InvalidOptionException
     * @see NonExceptionalExit
     */
    public UnitTestingOptionExtractor(Class<T> clazz, boolean throwsNotExceptionalExit) throws InvalidOptionsInterfaceException {
        super(clazz);
        this.throwsNotExceptionalExit = throwsNotExceptionalExit;
    }

    @Override
    protected InvocationHandler buildInvocationHandler(OptionValues<T> values, Class<? extends Options<?>> optionsInterface, Class<? extends StatusCode> exitCodeEnumClass, String[] rawArguments) {
        return new MockArgumentsInvocationHandler<T>(values, optionsInterface, exitCodeEnumClass, rawArguments, throwsNotExceptionalExit);
    }

    /**
     * Returns whether or not the method {@link Options#exit(Enum)} was called
     * on the given options.
     * 
     * @param options
     * @return
     */
    public boolean exitEnumCalled(T options) {
        return retrieveInvocationHandler(options).exitEnumCalled();
    }

    /**
     * Retrieves the status code of a certain options object. The status code is
     * set when the method {@link Options#exit(Enum)} is called. If
     * {@link Options#exit(Enum)} was never called on the given options object,
     * this method will throw a {@link RuntimeException}. You can call
     * {@link UnitTestingOptionExtractor#exitEnumCalled(Options)} to avoid
     * having to catch the exception.
     * 
     * @param options
     *            The options object that we will extract the status code
     *            enumeration from.
     * @return The status code, if the method {@link Options#exit(Enum)} was
     *         already called.
     * @throws RuntimeException
     *             If the method {@link Options#exit(Enum)} was never called.
     */
    public Enum<?> getStatusCodeEnum(T options) {
        return retrieveInvocationHandler(options).getStatusCodeEnum();
    }

    /**
     * Returns whether or not the method {@link Options#exit(int)} was called on
     * the given options.
     * 
     * @param options
     * @return
     */
    public boolean exitIntCalled(T options) {
        return retrieveInvocationHandler(options).exitIntCalled();
    }

    /**
     * Retrieves the status code (int) of a certain options object. The status
     * code (int) is set when the method {@link Options#exit(int)} is called. If
     * {@link Options#exit(int)} was never called on the given options object,
     * this method will throw a {@link RuntimeException}. You can call
     * {@link UnitTestingOptionExtractor#exitEnumCalled(int)} to avoid having to
     * catch the exception.
     * 
     * @param options
     *            The options object that we will extract the status code
     *            integer from.
     * @return The status code, if the method {@link Options#exit(int)} was
     *         already called.
     * @throws RuntimeException
     *             If the method {@link Options#exit(int)} was never called.
     */
    public int getStatusCodeInt(T options) {
        return retrieveInvocationHandler(options).getStatusCodeInt();
    }

    /**
     * Retrieves a {@link ValidatorsInvocationHandler} object from the argument
     * object.
     * 
     * @param options
     *            The {@link Options} object based on which the
     *            {@link ValidatorsInvocationHandler} object will be retrieved.
     * @return The {@link ValidatorsInvocationHandler} object.
     * @throws RuntimeException
     *             If the argument cannot be cast to
     *             {@link ValidatorsInvocationHandler}.
     */
    @SuppressWarnings("unchecked")
    private MockArgumentsInvocationHandler<T> retrieveInvocationHandler(T options) {
        Object obj = Proxy.getInvocationHandler(options);
        if (obj instanceof MockArgumentsInvocationHandler) {
            MockArgumentsInvocationHandler<T> mih = (MockArgumentsInvocationHandler<T>) obj;
            return mih;
        } else {
            throw new RuntimeException("Argument is not a " + MockArgumentsInvocationHandler.class.getName() + " object, which probably means it was not created by " + UnitTestingOptionExtractor.class.getName() + ".");
        }
    }
}
