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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.github.aamm.hyphenType.datastructure.Options;
import com.github.aamm.hyphenType.datastructure.annotations.ArgumentsObject;
import com.github.aamm.hyphenType.datastructure.annotations.InputChannel;
import com.github.aamm.hyphenType.datastructure.annotations.Option;
import com.github.aamm.hyphenType.datastructure.annotations.OptionArgument;
import com.github.aamm.hyphenType.datastructure.annotations.OptionMapValue;
import com.github.aamm.hyphenType.datastructure.annotations.OptionValue;
import com.github.aamm.hyphenType.datastructure.annotations.SimpleArgument;
import com.github.aamm.hyphenType.datastructure.lexer.LexToken;
import com.github.aamm.hyphenType.datastructure.lexer.option.LexOption;
import com.github.aamm.hyphenType.datastructure.lexer.option.LexOptionMapValue;
import com.github.aamm.hyphenType.datastructure.lexer.option.LexOptionValue;
import com.github.aamm.hyphenType.datastructure.lexer.simple.LexArgument;
import com.github.aamm.hyphenType.datastructure.parser.StructureElement;
import com.github.aamm.hyphenType.datastructure.parser.option.StructureOption;
import com.github.aamm.hyphenType.datastructure.parser.option.StructureOptionArgument;
import com.github.aamm.hyphenType.datastructure.parser.option.StructureOptionMapValue;
import com.github.aamm.hyphenType.datastructure.parser.option.StructureOptionValue;
import com.github.aamm.hyphenType.datastructure.parser.simple.StructureSimpleArgument;
import com.github.aamm.hyphenType.exceptions.InvalidOptionsInterfaceException;
import com.github.aamm.hyphenType.util.DefaultAnnotation;
import com.github.aamm.hyphenType.util.I18NResourceBundle;

/**
 * A LexerParser is an object that can parse an array of strings and create a
 * list of tokens from it. To do so, the LexerParser's constructor performs a
 * deep analysis on the options interface.<br>
 * <br>
 * Therefore, invoking the LexerParser
 * constructor can also be used to validate the options interface.
 * 
 * @author Aurelio Akira M. Matsui
 * @param <T>
 */
public class LexerParser<T extends com.github.aamm.hyphenType.datastructure.Options<?>> {

    /**
     * 
     */
    private final ArgumentsObject argumentsObject;
    /**
     * 
     */
    private final Class<T> optionsInterface;
    /**
     * 
     */
    private final List<StructureOption> parsedOptions;
    /**
     * 
     */
    private final List<StructureSimpleArgument> simpleArguments;

    /**
     * @param optionsInterfaceClass
     * @throws InvalidOptionsInterfaceException
     */
    public LexerParser(final Class<T> optionsInterfaceClass) throws InvalidOptionsInterfaceException {
        this(optionsInterfaceClass, true);
    }
    
    /**
     * @param optionsInterfaceClass
     * @param loadFromResourceBundle
     * @throws InvalidOptionsInterfaceException
     */
    @SuppressWarnings("unchecked")
    public LexerParser(final Class<T> optionsInterfaceClass, boolean loadFromResourceBundle) throws InvalidOptionsInterfaceException {

        // CHECKING AND BUILDING DATA STRUCTURE

        optionsInterface = optionsInterfaceClass;
        
        if (!optionsInterfaceClass.isInterface()) {
            throw new InvalidOptionsInterfaceException(optionsInterfaceClass.getName() + " is not an interface.");
        }

        argumentsObject = DefaultAnnotation.getAnnotation(optionsInterfaceClass, ArgumentsObject.class);

        I18NResourceBundle i18nrb = null;
        if(loadFromResourceBundle) {
            i18nrb = new I18NResourceBundle(optionsInterfaceClass);
            DefaultAnnotation.fillWithResourceBundle(argumentsObject, i18nrb);
        }

        boolean foundOptionsInterface = false;
        for (Class c : optionsInterfaceClass.getInterfaces()) {
            if (c.equals(Options.class)) {
                foundOptionsInterface = true;
            }
        }
        if (!foundOptionsInterface) {
            throw new InvalidOptionsInterfaceException("Interface " + optionsInterfaceClass.getName() + " should extend the interface " + Options.class.getName() + ".");
        }

        if (!argumentsObject.statusCodeEnum().isEnum()) {
            throw new InvalidOptionsInterfaceException("The status code class " + argumentsObject.statusCodeEnum().getName() + " is not an enumeration.");
        }

        // Verification over. SUCCESS!

        ArrayList<StructureOption> tempOptions = new ArrayList<StructureOption>();
        ArrayList<StructureSimpleArgument> tempSimpleArguments = new ArrayList<StructureSimpleArgument>();

        Method[] methods = optionsInterfaceClass.getMethods();

        // Checking if all the methods have one and only one of our annotations.
        a: for (Method m : methods) {
            for (Method m2 : Options.class.getMethods()) {
                if (m.equals(m2)) {
                    continue a;
                }
            }
            int annotations = 0;
            if (m.isAnnotationPresent(Option.class)) {
                annotations++;
            }
            if (m.isAnnotationPresent(OptionArgument.class)) {
                annotations++;
            }
            if (m.isAnnotationPresent(OptionMapValue.class)) {
                annotations++;
            }
            if (m.isAnnotationPresent(OptionValue.class)) {
                annotations++;
            }
            if (m.isAnnotationPresent(SimpleArgument.class)) {
                annotations++;
            }
            if (annotations == 0) {
                throw new InvalidOptionsInterfaceException("Method " + m + " has no annotation.");
            }
            if (annotations > 1) {
                throw new InvalidOptionsInterfaceException("Method " + m + " has more than one annotation.");
            }
        }

        // Extracting StructureOption objects
        for (Method m : methods) {

            if (m.isAnnotationPresent((Class<? extends Annotation>) Option.class)) {

                if (m.getParameterTypes().length > 0) {
                    throw new InvalidOptionsInterfaceException("Method " + m + " should have no parameters.");
                }

                if (!m.getReturnType().equals(boolean.class) && !m.getReturnType().equals(Boolean.class) && !m.getReturnType().equals(int.class) && !m.getReturnType().equals(Integer.class)) {
                    throw new InvalidOptionsInterfaceException("Method " + m + " should return boolean, " + Boolean.class.getName() + ", int, or " + Integer.class.getName() + ".");
                }

                ArrayList<String> alternatives = new ArrayList<String>();
                Option opt = DefaultAnnotation.getAnnotation(m, Option.class);
                if(loadFromResourceBundle) {
                    DefaultAnnotation.fillWithResourceBundle(opt, i18nrb);
                }
                if (opt.names().length == 1 && opt.names()[0].equals("")) {
                    alternatives.add(m.getName());
                } else {
                    for (String alternative : opt.names()) {
                        alternatives.add(alternative);
                    }
                }

                StructureOptionValue value = null;
                for (Method m2 : methods) {
                    if (m2.isAnnotationPresent((Class<? extends Annotation>) OptionValue.class)) {
                        OptionValue optionValue = DefaultAnnotation.getAnnotation(m2, OptionValue.class);
                        if(loadFromResourceBundle) {
                            DefaultAnnotation.fillWithResourceBundle(optionValue, i18nrb);
                        }
                        if (optionValue.option().equals(m.getName())) {
                            if (value == null) {
                                if (optionValue.name().equals("")) {
                                    value = new StructureOptionValue(m2, m2.getName(), optionValue.mandatory(), optionValue.arraySeparator(), optionValue.arrayUseFileSeparator());
                                } else {
                                    value = new StructureOptionValue(m2, optionValue.name(), optionValue.mandatory(), optionValue.arraySeparator(), optionValue.arrayUseFileSeparator());
                                }
                            } else {
                                throw new InvalidOptionsInterfaceException("The option " + optionValue.option() + " has more than one option value. Options should have only one option value.");
                            }
                        }
                    }
                }

                StructureOptionMapValue map = null;
                for (Method m2 : methods) {
                    if (m2.isAnnotationPresent((Class<? extends Annotation>) OptionMapValue.class)) {
                        OptionMapValue optionMapValue = DefaultAnnotation.getAnnotation(m2, OptionMapValue.class);
                        if(loadFromResourceBundle) {
                            DefaultAnnotation.fillWithResourceBundle(optionMapValue, i18nrb);
                        }
                        if (!m2.getReturnType().equals(Map.class)) {
                            throw new InvalidOptionsInterfaceException("The method " + m2 + " is an option map, therefore its return type should be " + Map.class.getName() + ".");
                        }
                        if (optionMapValue.option().equals(m.getName())) {
                            if (map == null) {
                                map = new StructureOptionMapValue(m2, optionMapValue.keyName(), optionMapValue.valueName(), optionMapValue.valueType(), optionMapValue.mandatory());
                            } else {
                                throw new InvalidOptionsInterfaceException("The option " + optionMapValue.option() + " has more than one option map. Options should have only one option map.");
                            }
                        }
                    }
                }

                List<StructureOptionArgument> optionArguments = new ArrayList<StructureOptionArgument>();
                boolean alreadyHasArrayArgument = false;
                boolean somethingFound = false;
                boolean alreadyHasOptional = false;
                boolean alreadyHasOptionWithoutArgumentInputChannel = false;
                do {
                    somethingFound = false;
                    for (Method m2 : methods) {
                        if (m2.isAnnotationPresent((Class<? extends Annotation>) OptionArgument.class)) {
                            OptionArgument optionArgument = DefaultAnnotation.getAnnotation(m2, OptionArgument.class);
                            if(loadFromResourceBundle) {
                                DefaultAnnotation.fillWithResourceBundle(optionArgument, i18nrb);
                            }
                            if (optionArgument.option().equals(m.getName()) && optionArgument.index() == optionArguments.size()) {

                                somethingFound = true;

                                if (optionArgument.mandatory()) {
                                    if (alreadyHasOptional) {
                                        throw new InvalidOptionsInterfaceException("LexUnknown " + m2 + " is mandatory after an optional argument. No mandatory argument can follow an optional argument.");
                                    }
                                } else {
                                    alreadyHasOptional = true;
                                }

                                if (alreadyHasArrayArgument) {
                                    throw new InvalidOptionsInterfaceException("Arguments referring to option " + m + " can only have one array and the array argument should be the last one. To solve this problem you can remove agument " + m2);
                                }
                                if (m2.getReturnType().isArray()) {
                                    alreadyHasArrayArgument = true;
                                }
                                String name = optionArgument.name();
                                if (name.equals("")) {
                                    name = m2.getName();
                                }
                                if (optionArgument.channels().length == 0) {
                                    throw new InvalidOptionsInterfaceException("Option argument " + m2 + " should have at least one input channel.");
                                }
                                if (Arrays.binarySearch(optionArgument.channels(), InputChannel.ARGUMENT, null) >= 0) {
                                    if (alreadyHasOptionWithoutArgumentInputChannel) {
                                        throw new InvalidOptionsInterfaceException("Option argument " + m2 + " should not have the " + InputChannel.ARGUMENT + " input channel. Arguments containing the " + InputChannel.ARGUMENT + " input channel should not succeed arguments without this input channel.");
                                    }
                                } else {
                                    alreadyHasOptionWithoutArgumentInputChannel = true;
                                }

                                if (!optionArgument.mandatory() && Arrays.binarySearch(optionArgument.channels(), InputChannel.GUI, null) >= 0) {
                                    throw new InvalidOptionsInterfaceException("Option argument " + m2 + " is optional. So it should not have the " + InputChannel.GUI + " input channel.");
                                }
                                if (!optionArgument.mandatory() && Arrays.binarySearch(optionArgument.channels(), InputChannel.TEXT, null) >= 0) {
                                    throw new InvalidOptionsInterfaceException("Option argument " + m2 + " is optional. So it should not have the " + InputChannel.TEXT + " input channel.");
                                }

                                optionArguments.add(new StructureOptionArgument(name, m2, optionArgument.mandatory(), optionArgument.index(), optionArgument.regex(), Arrays.asList(optionArgument.channels()), optionArgument.description()));
                            }
                        }
                    }
                } while (somethingFound);

                Collections.sort(optionArguments);
                tempOptions.add(new StructureOption(m, opt.description(), alternatives, value, map, Collections.unmodifiableList(optionArguments)));
            }
        }

        // Extracting ParsedParsedSimple objects
        for (Method m : methods) {
            if (m.isAnnotationPresent((Class<? extends Annotation>) SimpleArgument.class)) {
                
                if (m.getParameterTypes().length > 0) {
                    throw new InvalidOptionsInterfaceException("Method " + m + " should have no parameters.");
                }
                
                SimpleArgument annotation = DefaultAnnotation.getAnnotation(m, SimpleArgument.class);
                if(loadFromResourceBundle) {
                    DefaultAnnotation.fillWithResourceBundle(annotation, i18nrb);
                }
                
                String name;
                if (annotation.name().equals("")) {
                    name = m.getName();
                } else {
                    name = annotation.name();
                }
                tempSimpleArguments.add(new StructureSimpleArgument(m, name, annotation.mandatory(), annotation.index(), annotation.regex(), Arrays.asList(annotation.channels()), annotation.description()));
            }
        }

        Collections.sort(tempSimpleArguments);

        boolean alreadyHasOptional = false;
        boolean alreadyHasArray = false;

        for (StructureSimpleArgument a : tempSimpleArguments) {
            if (alreadyHasArray) {
                throw new InvalidOptionsInterfaceException("Cannot have any argument after an array argument. Array arguments will consume all remaining arguments.");
            }
            if (a.isMandatory()) {
                if (alreadyHasOptional) {
                    throw new InvalidOptionsInterfaceException("The parsedOptions interface should not have a mandatory agument after an optional one.");
                }
            } else {
                alreadyHasOptional = true;
            }
            if (a.method.getReturnType().isArray()) {
                if (alreadyHasArray) {
                    throw new InvalidOptionsInterfaceException("Only the last argument can be an array argument.");
                }
                alreadyHasArray = true;
            }
        }

        /*
         * Sorting the options and simple arguments. We need to sort those lists
         * because we will want to print documentations of them in a sorted
         * order.
         */

        Collections.sort(tempOptions);
        parsedOptions = Collections.unmodifiableList(tempOptions);
        Collections.sort(tempSimpleArguments);
        simpleArguments = Collections.unmodifiableList(tempSimpleArguments);

        if (parsedOptions.isEmpty() && simpleArguments.isEmpty()) {
            throw new InvalidOptionsInterfaceException("Empty parsedOptions interface: " + optionsInterfaceClass.getName());
        }
    }

    /**
     * @return TODO
     */
    public final ArgumentsObject getArgsObject() {
        return argumentsObject;
    }

    /**
     * @return TODO
     */
    public final Class<T> getOptionsInterface() {
        return optionsInterface;
    }

    /**
     * Classifies arguments and creates tokens based on it.
     * <ol>
     * <li>{@link LexOption} (as "version" in "-version")</li>
     * <li>{@link LexOptionValue} (as "12" in "-x=12")</li>
     * <li>{@link LexOptionMapValue} (as "a=b" in "-xa=b")</li>
     * <li>{@link LexUnknown} (as "aa" and "bb" in "-x aa bb")</li>
     * </ol>
     * 
     * @param arguments
     *            The arguments to be converted into lexer tokens.
     * @return A list of tokens in the order they appear in the arguments.
     */
    public final List<LexToken> lexArguments(final String... arguments) {

        ArrayList<LexToken> expandedArgs = new ArrayList<LexToken>();

        // Solely to make the source code more readable

        final String hyphen = argumentsObject.singleHyphen();
        final String hyphenHyphen = argumentsObject.doubleHyphen();
        final String equals = argumentsObject.equals();

        // index to iterate over the simple arguments
        int simpleArgumentIndex = 0;

        a: for (String argument : arguments) {

            if (argument.equals(hyphen)) {
                expandedArgs.add(new LexArgument(hyphen));
                continue a;
            }

            if (argument.equals(hyphenHyphen)) {
                expandedArgs.add(new LexArgument(hyphenHyphen));
                continue a;
            }

            if (simpleArguments.size() > 0 && simpleArgumentIndex < simpleArguments.size()) {
                StructureSimpleArgument simpleArgument = simpleArguments.get(simpleArgumentIndex);
                String regex = simpleArgument.getRegex();
                regex = regex.replace("\\h", argumentsObject.singleHyphen());
                regex = regex.replace("\\H", argumentsObject.doubleHyphen());
                if (Pattern.matches(regex, argument)) {
                    expandedArgs.add(new LexArgument(argument));
                    if (!simpleArgument.method.getReturnType().isArray()) {
                        simpleArgumentIndex++;
                    }
                    continue a;
                }
            }

            if (argumentsObject.doubleHyphenInLongOptions()) {
                if (argument.startsWith(hyphen) && !argument.startsWith(hyphenHyphen)) {
                    StructureOption option = searchOption(argument.substring(hyphen.length(), hyphen.length() + 1));
                    if (option == null || option.map == null) {
                        int i = hyphen.length();
                        while (i < argument.length() && argument.indexOf(equals) != i) {
                            expandedArgs.add(new LexOption(String.valueOf(argument.charAt(i))));
                            if (i + 1 < argument.length() && argument.indexOf(equals) == i + 1) {
                                expandedArgs.add(new LexOptionValue(argument.substring(i + 1 + equals.length())));
                            }
                            i++;
                        }
                    } else {
                        if (argument.contains(equals)) {
                            expandedArgs.add(new LexOption(String.valueOf(argument.charAt(hyphen.length()))));
                            expandedArgs.add(new LexOptionMapValue(argument.substring(hyphen.length() + equals.length())));
                        } else {
                            for (int i = 1; i < argument.length(); i++) {
                                expandedArgs.add(new LexOption(String.valueOf(argument.charAt(i))));
                            }
                        }
                    }
                } else if (argument.startsWith(hyphenHyphen)) {
                    if (argument.contains(equals)) {
                        expandedArgs.add(new LexOption(argument.substring(hyphenHyphen.length(), argument.indexOf(equals))));
                        expandedArgs.add(new LexOptionValue(argument.substring(argument.indexOf(equals) + equals.length(), argument.length())));
                    } else {
                        expandedArgs.add(new LexOption(argument.substring(hyphenHyphen.length())));
                    }
                } else {
                    expandedArgs.add(new LexArgument(argument));
                }
            } else {
                if (argument.startsWith(hyphen)) {
                    if (argument.contains(equals)) {
                        String prefix = searchOptionPrefixing(argument);
                        if (prefix != null) {
                            expandedArgs.add(new LexOption(prefix));
                            if (argument.indexOf(equals) == prefix.length() + 1) {
                                expandedArgs.add(new LexOptionValue(argument.substring(prefix.length() + 1 + equals.length())));
                            } else {
                                expandedArgs.add(new LexOptionMapValue(argument.substring(prefix.length() + 1)));
                            }
                        } else {
                            expandedArgs.add(new LexOption(argument.substring(1, argument.indexOf(equals))));
                            expandedArgs.add(new LexOptionValue(argument.substring(argument.indexOf(equals) + 1, argument.length())));
                        }
                    } else {
                        expandedArgs.add(new LexOption(String.valueOf(argument.substring(hyphen.length()))));
                    }
                } else {
                    expandedArgs.add(new LexArgument(argument));
                }
            }
        }

        return expandedArgs;
    }

    /**
     * @return TODO
     */
    public final List<StructureOption> getParsedOptions() {
        return Collections.unmodifiableList(parsedOptions);
    }

    /**
     * @return TODO
     */
    public final List<StructureSimpleArgument> getSimpleArguments() {
        return Collections.unmodifiableList(simpleArguments);
    }

    /**
     * @param name
     *            TODO
     * @return TODO
     */
    public final StructureOption searchOption(final String name) {
        for (StructureOption parsedOption : parsedOptions) {
            if (parsedOption.alternatives.contains(name)) {
                return parsedOption;
            }
        }
        return null;
    }

    /**
     * @param name
     *            TODO
     * @return TODO
     */
    public final String searchOptionPrefixing(final String name) {
        String filteredName = name.substring(1);
        for (StructureOption option : parsedOptions) {
            for (String alternative : option.alternatives) {
                if (filteredName.startsWith(alternative)) {
                    return alternative;
                }
            }
        }
        return null;
    }

    /**
     * @param name
     *            TODO
     * @return TODO
     */
    public final StructureSimpleArgument searchArgument(final String name) {
        for (StructureSimpleArgument simpleArgument : simpleArguments) {
            if (simpleArgument.getName().equals(name)) {
                return simpleArgument;
            }
        }
        return null;
    }

    /**
     * @param method
     *            TODO
     * @return TODO
     */
    public final StructureElement searchElement(final Method method) {
        for (StructureOption option : parsedOptions) {
            if (option.method.equals(method)) {
                return option;
            }
            if (option.value != null && option.value.method.equals(method)) {
                return option.value;
            }
            if (option.map != null && option.map.method.equals(method)) {
                return option.map;
            }
            for (StructureOptionArgument optionArgument : option.arguments) {
                if (optionArgument.method.equals(method)) {
                    return optionArgument;
                }
            }
        }
        for (StructureSimpleArgument simpleArgument : simpleArguments) {
            if (simpleArgument.method.equals(method)) {
                return simpleArgument;
            }
        }

        return null;
    }
}
