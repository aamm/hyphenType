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
package org.hyphenType.optionsextractor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;

import org.hyphenType.datastructure.Options;
import org.hyphenType.dynamicproxy.ConcreteArgumentsInvocationHandler;
import org.hyphenType.exceptions.InvalidOptionsInterfaceException;
import org.hyphenType.exit.StatusCode;
import org.hyphenType.input.StandardUserInput;
import org.hyphenType.input.UserInput;
import org.hyphenType.lexerparser.LexerParser;
import org.hyphenType.lexerparser.OptionValues;
import org.hyphenType.lexerparser.exceptions.MandatoryMapValueNotFoundException;
import org.hyphenType.lexerparser.exceptions.MandatorySimpleArgumentNotFoundException;
import org.hyphenType.lexerparser.exceptions.MandatoryValueNotFoundException;
import org.hyphenType.lexerparser.exceptions.RegexMismatchException;
import org.hyphenType.lexerparser.exceptions.StringParseException;
import org.hyphenType.lexerparser.exceptions.StringParsingErrorException;
import org.hyphenType.optionprocessors.ArgumentsProcessorEngine;
import org.hyphenType.util.DefaultAnnotation;
import org.hyphenType.util.I18NResourceBundle;
import org.hyphenType.wrapper.StandAloneAppWrapper;

/**
 * A factory for option objects, and the easiest way to get started with this
 * tool. This class provides the easiest (although not the most flexible, nor
 * the most automated) way to obtain an instance of an option object
 * programmatically. For a non-programmatical and automated method to obtain an
 * option object (i.e. if you do not want to call any API), please utilize the
 * {@link StandAloneAppWrapper} class instead.<br>
 * Here is (possibly the simplest and least invasive) example on how to use this
 * class: <code>
 * <pre>
 * public static void main(String[] arguments) {
 *     OptionsExtractor<MyOptionInterface> soe = new OptionsExtractor<MyOptionInterface>(MyOptionInterface.class);
 *     MyOptionInterface options = soe.options(arguments);
 * }
 * </pre>
 * </code> In the source code above, the OptionsExtractor constructor will
 * perform a deep analysis on the options interface
 * <code>MyOptionInterface</code> All arguments received by the
 * <code>main</code> method will be parsed according to the rules declared in
 * the options interface <code>MyOptionInterface</code> .
 * 
 * @author Aurelio Akira M. Matsui
 * @param <T>
 *            The options interface type.
 */
public class OptionsExtractor<T extends Options<?>> {

    private final Class<T> optionInterfaceClass;
    private final LexerParser<T> lexPar;

    /**
     * Creates an {@link OptionsExtractor} object based on an options interface
     * class. Exceptions thrown by this constructor means that there is a
     * problem with the structure of the options interface. If you are sure your
     * options interface does not have any inconsistency (which can be tested
     * simply by calling this method before wrapping your application), you do
     * not have any reason to expect any exception being thrown from this
     * method.<br/>
     * <br/>
     * The user input option allows the caller to chose which interactive user
     * input to utilize. If you do not want to bother about interactive user
     * inputs (either because your application does not use interactive user
     * input or because you are satisfied with the standard user input), simply
     * call the {@link OptionsExtractor#OptionsExtractor(Class)} constructor
     * instead.
     * 
     * @param optionInterfaceClass
     * @param userInput
     *            The user input to utilize.
     * @throws InvalidOptionsInterfaceException
     * @throws InvalidOptionException
     */
    public OptionsExtractor(final Class<T> optionInterfaceClass) throws InvalidOptionsInterfaceException {

        this.optionInterfaceClass = optionInterfaceClass;
        this.lexPar = new LexerParser<T>(optionInterfaceClass);
    }

    public T options(final String... arguments) throws StringParsingErrorException, MandatoryValueNotFoundException, RegexMismatchException, StringParseException, MandatoryMapValueNotFoundException, MandatorySimpleArgumentNotFoundException, OptionsExtractorException {
        return options(new StandardUserInput(), arguments);
    }

    /**
     * Factory method that creates an instance of the options interface specified in the
     * constructor {@link OptionsExtractor#OptionsExtractor(Class)}.
     * 
     * @param userInput
     *            The input from the user.
     * @param arguments
     *            The arguments to be parsed.
     * @return A new instance of the option interface, containing all arguments
     *         parsed.
     * @throws OptionsExtractorException 
     * @throws MandatorySimpleArgumentNotFoundException 
     * @throws MandatoryMapValueNotFoundException 
     * @throws StringParseException 
     * @throws RegexMismatchException 
     * @throws MandatoryValueNotFoundException 
     * @throws StringParsingErrorException 
     * @throws InvalidOptionException
     */
    @SuppressWarnings("unchecked")
    public T options(final UserInput userInput, final String... arguments) throws OptionsExtractorException, StringParsingErrorException, MandatoryValueNotFoundException, RegexMismatchException, StringParseException, MandatoryMapValueNotFoundException, MandatorySimpleArgumentNotFoundException {
        OptionValues<T> values = new OptionValues<T>(lexPar.lexArguments(arguments), lexPar, userInput);
        InvocationHandler handler = buildInvocationHandler(values, lexPar.getOptionsInterface(), lexPar.getArgsObject().statusCodeEnum(), arguments);
        Class proxyClass = Proxy.getProxyClass(OptionsExtractor.class.getClassLoader(), new Class[] {optionInterfaceClass});
        T f;
        try {
            f = (T) proxyClass.getConstructor(new Class[] {InvocationHandler.class}).newInstance(new Object[] {handler});

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        } catch (SecurityException e) {
            e.printStackTrace();
            return null;
        } catch (InstantiationException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }

        /* Fills all annotations with data from the resource bundles and
         * applies all argument processors.
         */

        for (Annotation annotation : DefaultAnnotation.getAnnotations(optionInterfaceClass)) {

            DefaultAnnotation.fillWithResourceBundle(annotation, new I18NResourceBundle(optionInterfaceClass));

            if (annotation.annotationType().getEnclosingClass() != null && ArgumentsProcessorEngine.class.isAssignableFrom(annotation.annotationType().getEnclosingClass())) {

                Class<? extends ArgumentsProcessorEngine<?>> processorClass = (Class<? extends ArgumentsProcessorEngine<?>>) annotation.annotationType().getEnclosingClass();
                Constructor<?> processorConstructor;
                try {
                    processorConstructor = processorClass.getConstructor();
                } catch (SecurityException e) {
                    throw new OptionsExtractorException("Could not access default constructor in argument processor.");
                } catch (NoSuchMethodException e) {
                    throw new OptionsExtractorException("Argument processor constructor not found.");
                }
                ArgumentsProcessorEngine processor;
                try {
                    processor = (ArgumentsProcessorEngine) processorConstructor.newInstance();
                } catch (IllegalArgumentException e) {
                    throw new OptionsExtractorException("Illegal argument while trying to create a new processor.", e);
                } catch (InstantiationException e) {
                    throw new OptionsExtractorException("Could not instantiate processor.", e);
                } catch (IllegalAccessException e) {
                    throw new OptionsExtractorException("Illegal access to constructor of argument processor.", e);
                } catch (InvocationTargetException e) {
                    throw new OptionsExtractorException("Exception thrown by something inside the constructor of argument processor.", e);
                }
                processor.process(optionInterfaceClass, f, annotation);
            }
        }

        return f;
    }

    /**
     * Prepared to be replaced by a subclass. Extending {@link OptionsExtractor}
     * is useful to replace the composition of objects that will process the
     * arguments.
     * 
     * @param values
     * @param formatter
     * @param exitCodeEnumClass
     * @param rawArguments
     * @return
     */
    protected InvocationHandler buildInvocationHandler(final OptionValues<T> values, final Class<? extends Options<?>> optionsInterface, final Class<? extends StatusCode> exitCodeEnumClass, final String[] rawArguments) {

        return new ConcreteArgumentsInvocationHandler<T>(values, optionsInterface, exitCodeEnumClass, rawArguments);
    }
}
