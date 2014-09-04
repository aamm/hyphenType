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
package com.github.aamm.hyphenType.lexerparser;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.aamm.hyphenType.datastructure.Options;
import com.github.aamm.hyphenType.datastructure.annotations.InputChannel;
import com.github.aamm.hyphenType.datastructure.lexer.LexToken;
import com.github.aamm.hyphenType.datastructure.lexer.option.LexOption;
import com.github.aamm.hyphenType.datastructure.lexer.option.LexOptionMapValue;
import com.github.aamm.hyphenType.datastructure.lexer.option.LexOptionValue;
import com.github.aamm.hyphenType.datastructure.lexer.simple.LexArgument;
import com.github.aamm.hyphenType.datastructure.parser.StructureArgument;
import com.github.aamm.hyphenType.datastructure.parser.option.StructureOption;
import com.github.aamm.hyphenType.datastructure.parser.option.StructureOptionArgument;
import com.github.aamm.hyphenType.datastructure.parser.option.StructureOptionMapValue;
import com.github.aamm.hyphenType.datastructure.parser.option.StructureOptionValue;
import com.github.aamm.hyphenType.datastructure.parser.simple.StructureSimpleArgument;
import com.github.aamm.hyphenType.input.StandardUserInput;
import com.github.aamm.hyphenType.input.UserInput;
import com.github.aamm.hyphenType.input.UserInputException;
import com.github.aamm.hyphenType.lexerparser.exceptions.MandatoryMapValueNotFoundException;
import com.github.aamm.hyphenType.lexerparser.exceptions.MandatorySimpleArgumentNotFoundException;
import com.github.aamm.hyphenType.lexerparser.exceptions.MandatoryValueNotFoundException;
import com.github.aamm.hyphenType.lexerparser.exceptions.OptionValuesException;
import com.github.aamm.hyphenType.lexerparser.exceptions.RegexMismatchException;
import com.github.aamm.hyphenType.lexerparser.exceptions.StringParseException;
import com.github.aamm.hyphenType.lexerparser.exceptions.StringParsingErrorException;
import com.github.aamm.hyphenType.util.soc.StringObjectConversion;
import com.github.aamm.hyphenType.util.soc.StringParsingError;

/**
 * Stores the values related to a data structure holding the structure of an
 * options interface.
 * 
 * @author Aurelio Akira M. Matsui
 * @param <T>
 *            TODO
 */
public class OptionValues<T extends Options<?>> {

    /**
     * A map that relates methods of the options interface with
     * its values. Whenever a value is an array, we store it as
     * a {@link List}.
     */
    private HashMap<Method, Object> map = new HashMap<Method, Object>();

    /**
     * 
     */
    private List<LexToken> unusedTokens = new ArrayList<LexToken>();

    /**
     * 
     */
    private LexerParser<T> lexPar;

    /**
     * Although this constructor throws many exceptions, all exceptions extend
     * {@link OptionValuesException}. So you can simply catch
     * {@link OptionValuesException} if you do not want to tackle each
     * exception in a special way.
     * 
     * @param tokens
     *            TODO
     * @param parser
     *            TODO
     * @throws MandatorySimpleArgumentNotFoundException 
     * @throws MandatoryMapValueNotFoundException 
     * @throws StringParseException 
     * @throws RegexMismatchException 
     * @throws MandatoryValueNotFoundException 
     * @throws StringParsingErrorException 
     * @throws InvalidOptionException
     *             TODO
     */
    public OptionValues(final List<LexToken> tokens, final LexerParser<T> parser) throws StringParsingErrorException, MandatoryValueNotFoundException, RegexMismatchException, StringParseException, MandatoryMapValueNotFoundException, MandatorySimpleArgumentNotFoundException {
        this(tokens, parser, new StandardUserInput());
    }

    /**
     * Although this constructor throws many exceptions, all exceptions extend
     * {@link OptionValuesException}. So you can simply catch
     * {@link OptionValuesException} if you do not want to tackle each
     * exception in a special way. 
     * 
     * @param tokens
     *            TODO
     * @param parser
     *            TODO
     * @param userInput
     *            TODO
     * @throws StringParsingErrorException 
     * @throws MandatoryValueNotFoundException 
     * @throws StringParseException 
     * @throws RegexMismatchException 
     * @throws MandatoryMapValueNotFoundException 
     * @throws MandatorySimpleArgumentNotFoundException 
     * @throws InvalidOptionException
     *             TODO
     */
    @SuppressWarnings("unchecked")
    public OptionValues(final List<LexToken> tokens, final LexerParser<T> parser, final UserInput userInput) throws StringParsingErrorException, MandatoryValueNotFoundException, RegexMismatchException, StringParseException, MandatoryMapValueNotFoundException, MandatorySimpleArgumentNotFoundException {

        int simpleArgumentIndex = 0;

        this.lexPar = parser;

        LexTokenStream lts = new LexTokenStream(tokens);

        a: while (lts.hasFutureToken()) {

            LexToken token = lts.consume();

            if (token instanceof LexOption) {
                LexOption lexOption = (LexOption) token;
                StructureOption option = parser.searchOption(lexOption.value);

                if (option == null) {
                    unusedTokens.add(lexOption);
                    continue a;
                }
                if (map.containsKey(option.method)) {
                    int x = (Integer) map.get(option.method);
                    x++;
                    map.put(option.method, x);
                } else {
                    map.put(option.method, new Integer(1));
                }

                if (option.value != null) {
                    if (lts.hasFutureToken() && lts.futureToken() != null && lts.futureToken() instanceof LexOptionValue) {
                        String value = lts.consume().value;
                        try {
                            Object convertedValue;
                            if(option.value.arrayUseFileSeparator) {
                                convertedValue = StringObjectConversion.fromString(option.value.method.getReturnType(), value, true, File.pathSeparator);
                            } else {
                                convertedValue = StringObjectConversion.fromString(option.value.method.getReturnType(), value, true, option.value.arraySeparator);
                            }
                            /* If the return type is an array, we need to get the return type
                             * and convert it into a list.
                             */
                            if(option.value.method.getReturnType().isArray()) {
                                List list = new ArrayList();
                                for(int i=0; i<Array.getLength(convertedValue); i++) {
                                    list.add(Array.get(convertedValue, i));
                                }
                                convertedValue = list;
                            }
                            map.put(option.value.method, convertedValue);
                        } catch (StringParsingError e) {
                            throw new StringParsingErrorException(e, lexPar.getOptionsInterface(), value, option.value.method.getReturnType());
                        }
                    } else if (option.value.mandatory) {
                        throw new MandatoryValueNotFoundException(lexPar.getOptionsInterface(), option.value.name, token.value);
                    }
                }

                if (option.map != null) {
                    if (lts.futureToken() != null && lts.futureToken() instanceof LexOptionMapValue) {

                        String value = lts.consume().value;
                        String k = value.substring(0, value.indexOf(lexPar.getArgsObject().equals()));
                        Object v;
                        try {
                            v = StringObjectConversion.fromString(option.map.valueType, value.substring(value.indexOf(parser.getArgsObject().equals()) + 1, value.length()));
                        } catch (StringParsingError e) {
                            throw new StringParsingErrorException(e, lexPar.getOptionsInterface(), value, option.map.valueType);
                        }

                        if (map.containsKey(option.map.method)) {
                            Map m = (Map) map.get(option.map.method);
                            m.put(k, v);
                        } else {
                            Map m = new HashMap();
                            m.put(k, v);
                            map.put(option.map.method, m);
                        }
                    } else if (option.map.mandatory) {
                        throw new MandatoryMapValueNotFoundException(lexPar.getOptionsInterface(), option.map.keyName, parser.getArgsObject().equals(), option.map.valueName, token.value);
                    }
                }

                for (StructureOptionArgument optionArgument : option.arguments) {
                    if (lts.hasFutureToken() && lts.futureToken() instanceof LexArgument) {

                        if (optionArgument.method.getReturnType().isArray()) {
                            Object v = checkConvert(optionArgument.method.getReturnType().getComponentType(), optionArgument.getName(), lts.consume().value, optionArgument.getRegex(), lexPar.getArgsObject().singleHyphen(), lexPar.getArgsObject().doubleHyphen());
                            registerOptionArgument(optionArgument, v);
                            while (lts.hasFutureToken() && lts.futureToken() instanceof LexArgument && check(lts.futureToken().value, optionArgument.getRegex(), lexPar.getArgsObject().singleHyphen(), lexPar.getArgsObject().doubleHyphen())) {
                                v = checkConvert(optionArgument.method.getReturnType().getComponentType(), optionArgument.getName(), lts.consume().value, optionArgument.getRegex(), lexPar.getArgsObject().singleHyphen(), lexPar.getArgsObject().doubleHyphen());
                                registerOptionArgument(optionArgument, v);
                            }
                        } else {
                            Object v = checkConvert(optionArgument.method.getReturnType(), optionArgument.getName(), lts.consume().value, optionArgument.getRegex(), lexPar.getArgsObject().singleHyphen(), lexPar.getArgsObject().doubleHyphen());
                            registerOptionArgument(optionArgument, v);
                        }
                    } else if (optionArgument.isMandatory()) {

                        try {
                            if (optionArgument.method.getReturnType().isArray()) {
                                Object[] inputs = userInput.readArray(optionArgument, lexPar.getArgsObject().singleHyphen(), lexPar.getArgsObject().doubleHyphen());
                                for (Object input : inputs) {
                                    registerOptionArgument(optionArgument, input);
                                }
                            } else {
                                Object input = userInput.readString(optionArgument, lexPar.getArgsObject().singleHyphen(), lexPar.getArgsObject().doubleHyphen());
                                registerOptionArgument(optionArgument, input);
                            }
                        } catch (UserInputException e) {
                            throw new MandatoryValueNotFoundException(lexPar.getOptionsInterface(), optionArgument.getName(), option.alternatives.toString());
                        }
                    }
                }

            } else if (token instanceof LexArgument) {

                LexArgument lexArgument = (LexArgument) token;

                if (simpleArgumentIndex == parser.getSimpleArguments().size()) {
                    unusedTokens.add(token);
                } else {

                    StructureSimpleArgument simpleArgument = parser.getSimpleArguments().get(simpleArgumentIndex);

                    if (!simpleArgument.getChannels().contains(InputChannel.ARGUMENT)) {
                        unusedTokens.add(token);
                        continue a;
                    }

                    if (simpleArgument.method.getReturnType().isArray()) {
                        List list;

                        if (!map.containsKey(simpleArgument.method)) {
                            list = new ArrayList();
                        } else {
                            list = (List) map.get(simpleArgument.method);
                        }

                        Object v = checkConvert(simpleArgument.method.getReturnType().getComponentType(), simpleArgument.getName(), lexArgument.value, simpleArgument.getRegex(), lexPar.getArgsObject().singleHyphen(), lexPar.getArgsObject().doubleHyphen());
                        list.add(v);
                        while (lts.hasFutureToken() && lts.futureToken() instanceof LexArgument && check(lts.futureToken().value, simpleArgument.getRegex(), lexPar.getArgsObject().singleHyphen(), lexPar.getArgsObject().doubleHyphen())) {
                            v = checkConvert(simpleArgument.method.getReturnType().getComponentType(), simpleArgument.getName(), lts.consume().value, simpleArgument.getRegex(), lexPar.getArgsObject().singleHyphen(), lexPar.getArgsObject().doubleHyphen());
                            list.add(v);
                        }

                        if (!map.containsKey(simpleArgument.method)) {
                            map.put(simpleArgument.method, list);
                        }
                    } else {
                        Object v = checkConvert(simpleArgument.method.getReturnType(), simpleArgument.getName(), lexArgument.value, simpleArgument.getRegex(), lexPar.getArgsObject().singleHyphen(), lexPar.getArgsObject().doubleHyphen());
                        map.put(simpleArgument.method, v);
                    }
                    simpleArgumentIndex++;
                }
            } else {
                unusedTokens.add(token);
            }
        }
        
        /*
         * Checking whether we collected all the mandatory simple arguments or
         * not.
         */
        
        if (simpleArgumentIndex != parser.getSimpleArguments().size()) {
            for (; simpleArgumentIndex < parser.getSimpleArguments().size(); simpleArgumentIndex++) {
                StructureSimpleArgument simpleArgument = parser.getSimpleArguments().get(simpleArgumentIndex);
                if (simpleArgument.isMandatory()) {
                    
                    /*
                     * If we did not collect this argument, let's give a second
                     * chance and try to get it from GUI or text, if available
                     * for this simple argument.
                     */
                    
                    try {
                        if (simpleArgument.method.getReturnType().isArray()) {
                            Object[] inputs = userInput.readArray(simpleArgument, lexPar.getArgsObject().singleHyphen(), lexPar.getArgsObject().doubleHyphen());
                            for (Object input : inputs) {
                                registerOptionArgument(simpleArgument, input);
                            }
                        } else {
                            Object input = userInput.readString(simpleArgument, lexPar.getArgsObject().singleHyphen(), lexPar.getArgsObject().doubleHyphen());
                            registerOptionArgument(simpleArgument, input);
                        }
                        continue;
                    } catch (UserInputException e) {
                        throw new MandatorySimpleArgumentNotFoundException(e, lexPar.getOptionsInterface(), simpleArgument.getName());
                    }
                }
            }
        }

        /*
         * At this point, all array arguments are stored as List objects.
         * Meaning, we need to convert them to actual arrays.
         */

        for (Method method : map.keySet()) {
            if (method.getReturnType().isArray()) {
                List list = (List) map.get(method);
                
                Object array = Array.newInstance(method.getReturnType().getComponentType(), list.size());
                for (int i = 0; i < list.size(); i++) {
                    if (method.getReturnType().getComponentType().isPrimitive()) {
                        Array.set(array, i, StringObjectConversion.toPrimitive(method.getReturnType().getComponentType(), list.get(i)));
                    } else {
                        Array.set(array, i, method.getReturnType().getComponentType().cast(list.get(i)));
                    }
                }
                
                map.put(method, array);
            }
        }
    }
    
    /**
     * @param optionArgument
     *            TODO
     * @param value
     *            TODO
     * @throws InvalidOptionException
     *             TODO
     */
    @SuppressWarnings("unchecked")
    private void registerOptionArgument(final StructureArgument optionArgument, final Object value) {
        
        if (optionArgument.method.getReturnType().isArray()) {
            if (map.containsKey(optionArgument.method)) {
                List list = (List) map.get(optionArgument.method);
                list.add(value);
            } else {
                List list = new ArrayList();
                list.add(value);
                map.put(optionArgument.method, list);
            }
        } else {
            map.put(optionArgument.method, value);
        }
    }

    /**
     * Checks if the value matches the regex. In the regular expression, \h
     * is replaced by the single hyphen, and \H is replaced by the double
     * hyphen.
     * 
     * @param value
     *            The value to be checked.
     * @param regex
     *            The regular expression that the value should match with.
     * @param singleHyphen
     *            The string that represents the single hyphen.
     * @param doubleHyphen
     *            The string that represents the double hyphen.
     * @return TODO
     */
    private boolean check(final String value, final String regex, final String singleHyphen, final String doubleHyphen) {
        String transformedRegex = regex.replace("\\h", singleHyphen);
        transformedRegex = transformedRegex.replace("\\H", doubleHyphen);
        return value.matches(transformedRegex);
    }

    /**
     * @param <Z>
     *            TODO
     * @param clazz
     *            TODO
     * @param name
     *            TODO
     * @param value
     *            TODO
     * @param regex
     *            TODO
     * @param singleHyphen
     *            TODO
     * @param doubleHyphen
     *            TODO
     * @return TODO
     * @throws RegexMismatchException 
     * @throws StringParseException 
     * @throws InvalidOptionException
     *             TODO
     */
    private <Z> Z checkConvert(final Class<Z> clazz, final String name, final String value, final String regex, final String singleHyphen, final String doubleHyphen) throws RegexMismatchException, StringParseException {
        if (!check(value, regex, singleHyphen, doubleHyphen)) {
            throw new RegexMismatchException(lexPar.getOptionsInterface(), value, name, regex);
        }
        try {
            return StringObjectConversion.fromString(clazz, value);
        } catch (StringParsingError e) {
            throw new StringParseException(e, lexPar.getOptionsInterface(), value);
        }
    }

    /**
     * @param option
     *            TODO
     * @return TODO
     */
    public final int getParsedOptionValue(final StructureOption option) {
        if (option == null) {
            System.out.println("Ugh!");
        }
        if (map.containsKey(option.method)) {
            return (Integer) map.get(option.method);
        } else {
            return 0;
        }
    }

    /**
     * @param optionMapValue
     *            TODO
     * @return TODO
     */
    @SuppressWarnings("unchecked")
    public final Map getOptionMapValue(final StructureOptionMapValue optionMapValue) {
        return (Map) map.get(optionMapValue.method);
    }

    /**
     * @param optionValue
     *            TODO
     * @return TODO
     */
    public final Object getOptionValue(final StructureOptionValue optionValue) {
        return map.get(optionValue.method);
    }

    /**
     * @param argument
     *            TODO
     * @return TODO
     */
    public final Object getSimpleArgumentValue(final StructureSimpleArgument argument) {
        if (argument == null) {
            System.out.println("Ugh!");
        }
        if (map.containsKey(argument.method)) {
            return map.get(argument.method);
        } else {
            if(argument.method.getReturnType().isArray()) {
                return Array.newInstance(argument.method.getReturnType().getComponentType(), 0);
            }
            if(argument.method.getReturnType().equals(boolean.class) && argument.method.getReturnType().equals(Boolean.class)) {
                return false;
            }
            if(argument.method.getReturnType().equals(int.class) && argument.method.getReturnType().equals(Integer.class)) {
                return 0;
            }
            // TODO IMPORTANT: We should return something better than null for other types.
            return null;
        }
    }

    /**
     * @return TODO
     */
    public final List<LexToken> unusedArguments() {
        return Collections.unmodifiableList(unusedTokens);
    }

    /**
     * @param method
     *            TODO
     * @return TODO
     */
    public final Object getValue(final Method method) {
        if (lexPar.searchElement(method) instanceof StructureOption) {
            Object obj = map.get(method);
            int n;
            if (obj == null) {
                n = 0;
            } else {
                n = (Integer) map.get(method);
            }
            if (method.getReturnType().equals(boolean.class) || method.getReturnType().equals(Boolean.class)) {
                return n > 0;
            } else if (method.getReturnType().equals(int.class) || method.getReturnType().equals(Integer.class)) {
                return n;
            }
        }

        return map.get(method);
    }

    @Override
    public final int hashCode() {
        return toString().hashCode();
    }

    @Override
    public final String toString() {
        String result = "[";
        for (Method method : map.keySet()) {
            if (method.getReturnType().isArray()) {
                result += String.format("%s=[", method.getName());
                Object array = map.get(method);
                for (int i = 0; i < Array.getLength(array); i++) {
                    result += String.format("%s, ", method.getName(), Array.get(array, i));
                }
                if (Array.getLength(array) > 0) {
                    result = result.substring(0, result.length() - 2);
                }
                result += "], ";
            } else {
                result += String.format("%s=%s, ", method.getName(), map.get(method));
            }
        }
        result += "unusedTokens=" + unusedTokens.toString() + "]";
        return result;
    }
}
