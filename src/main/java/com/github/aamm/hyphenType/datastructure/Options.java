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
package com.github.aamm.hyphenType.datastructure;

import java.io.PrintStream;
import java.lang.annotation.Annotation;
import java.text.MessageFormat;
import java.util.List;

import com.github.aamm.hyphenType.datastructure.annotations.ArgumentsObject;
import com.github.aamm.hyphenType.datastructure.lexer.LexToken;
import com.github.aamm.hyphenType.exit.ExitStatusHelper;
import com.github.aamm.hyphenType.exit.StatusCode;

/**
 * Interface that should be extended by
 * all options interfaces.
 * 
 * @author Aurelio Akira M. Matsui
 * @param <T> The enumeration for status codes
 */
public interface Options<T extends Enum<?> & StatusCode> {
    
    /**
     * Prints the documentation using the preferred documentation formatter
     * to the standard {@link System#out}. Note: if not explicitly set,
     * the preferred formatter is the one defined as the default value of
     * the {@link ArgumentsObject#preferredDocumentationFormatter()}
     * property.
     */
    void printDocumentation();
    
    /**
     * Prints the documentation using the preferred documentation formatter.
     * Note: if not explicitly set, the preferred formatter is the one
     * defined as the default value of the
     * {@link ArgumentsObject#preferredDocumentationFormatter()} property.
     * 
     * @param pw
     */
    void printDocumentation(PrintStream pw);
    
    /**
     * Prints the documentation using the given documentation formatter
     * to the standard {@link System#out}. Uses the given documentation
     * formatter.
     * 
     * TODO What happens if we try to load the annotation from the resource bundles but the annotation is not complete?
     * 
     * @param formatterClass
     */
    void printDocumentation(Class<? extends Annotation> formatterClass);
    
    /**
     * Prints the documentation using the given documentation formatter.
     * Uses the given {@link PrintStream} instead of {@link System#out}.
     * 
     * TODO What happens if we try to load the annotation from the resource bundles but the annotation is not complete?
     * 
     * @param formatterClass
     * @param pw
     */
    void printDocumentation(Class<? extends Annotation> formatterClass, PrintStream pw);
    
    /**
     * All the arguments that were ignored by the parsing process.
     * 
     * @return
     */
    List<LexToken> unparsedArguments();
    
    /**
     * Terminates the JVM or other current environment
     * 
     * @param status The status code to terminate the JVM
     * @param arguments The arguments that will be passed to {@link StatusCode#beforeExit()}.
     */
    void exit(T status, Object... arguments);
    
    /**
     * Terminates the JVM or other current environment
     * 
     * @param code The status code to terminate the JVM 
     * @param arguments The arguments that will be passed to {@link StatusCode#beforeExit()}.
     */
    void exit(int code, Object... args);
    
    /**
     * Terminates the JVM or other current environment. This method searches for one exit
     * status (enumeration constant) that receives the given throwable. 
     * 
     * @param throwable The throwable object that will be carried to the {@link ExitStatusHelper}. 
     * @return True if the throwable could be visited by any enumeration constant.
     */
    boolean exit(Throwable throwable);
    
    /**
     * Retrieves the array of arguments as they were received
     * from the user.
     * 
     * @return The raw array of arguments.
     */
    String[] rawArguments();
    
    /**
     * Gets a message from the resource bundle of the current locale.
     * This method is merely a convenience to allow for users to
     * easily access extra messages inside of the resource bundles
     * of hyphenType.
     * 
     * @param key The key to search for.
     * @return The message related to the key.
     */
    String localeMessage(String key);
    
    /**
     * Gets a message from the resource bundle of the current locale.
     * This method is merely a convenience to allow for users to
     * easily access extra messages inside of the resource bundles
     * of hyphenType.
     * 
     * @param key The key to search for.
     * @param defaultValue The default value, in case the key was not found.
     * @return The message related to the key.
     */
    String localeMessage(String key, String defaultValue);

    /**
     * Equivalent to {@link Options#localeMessage(String)}, but
     * wraps a procedure that allows for replacement of variables
     * within the messages. This procedure uses a {@link MessageFormat}
     * to replace variables.
     * 
     * @see MessageFormat
     * @param key The key to search for.
     * @param values The values to replace each variable.
     * @return The message related to the key and formatted according to the values.
     */
    String formattedLocaleMessage(String key, Object... values);

    /**
     * Equivalent to {@link Options#localeMessage(String)}, but
     * wraps a procedure that allows for replacement of variables
     * within the messages. This procedure uses a {@link MessageFormat}
     * to replace variables.
     * 
     * @see MessageFormat
     * @param key The key to search for.
     * @param defaultValue The default value, in case the key was not found.
     * @param values The values to replace each variable.
     * @return The message related to the key and formatted according to the values.
     */
    String formattedLocaleMessageDefault(String key, String defaultValue, Object... values);
}
