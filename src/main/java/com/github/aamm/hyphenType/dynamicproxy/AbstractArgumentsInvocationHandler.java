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

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.text.MessageFormat;

import com.github.aamm.hyphenType.datastructure.Options;
import com.github.aamm.hyphenType.documentation.DocumentationFormatterEngine;
import com.github.aamm.hyphenType.exit.ExitStatusConstant;
import com.github.aamm.hyphenType.exit.ExitStatusHelper;
import com.github.aamm.hyphenType.exit.StatusCode;
import com.github.aamm.hyphenType.lexerparser.OptionValues;
import com.github.aamm.hyphenType.unittesting.NonExceptionalExit;

/**
 * @author Aurelio Akira M. Matsui
 * @param <T> The option interface type.
 */
public abstract class AbstractArgumentsInvocationHandler<T extends Options<?>> implements InvocationHandler {

    /**
     * TODO Comment.
     */
    private final OptionValues<T> optionValues;
    
    /**
     * TODO Comment.
     */
    private final Class<? extends Options<?>> optionsInterface;
    
    /**
     * TODO Comment.
     */
    private final Class<? extends StatusCode> exitCodeEnumClass;
    
    /**
     * TODO Comment.
     */
    private final String[] rawArguments;

    @SuppressWarnings("unchecked")
    private DocumentationFormatterEngine defaultFormatter = null;
    
    /**
     * TODO Comment.
     * 
     * @param optionValues TODO Comment.
     * @param formatter TODO Comment.
     * @param exitCodeEnumClass TODO Comment.
     * @param rawArguments TODO Comment.
     */
    public AbstractArgumentsInvocationHandler(final OptionValues<T> optionValues, final Class<? extends Options<?>> optionsInterface, final Class<? extends StatusCode> exitCodeEnumClass, final String[] rawArguments) {
        this.optionValues = optionValues;
        this.optionsInterface = optionsInterface;
        this.exitCodeEnumClass = exitCodeEnumClass;
        this.rawArguments = rawArguments;
    }
    
    /**
     * Initializes the formatter. This is a lazy
     * initialization to avoid building a formatter
     * when the user does not need one.
     */
    @SuppressWarnings("unchecked")
    private DocumentationFormatterEngine defaultFormatter() {
        if(defaultFormatter == null) {
            defaultFormatter = DocumentationFormatterEngine.preferredFormatter(optionsInterface);
        }
        return defaultFormatter;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        
        if (method.equals(Object.class.getMethod("equals", Object.class))) {
            return this.equals(args[0]);
        }
        
        if (method.equals(Object.class.getMethod("hashCode"))) {
            return this.hashCode();
        }
        
        if (method.equals(Object.class.getMethod("toString"))) {
            return this.toString();
        }
        
        /*
         * Methods from the {@link Options} interface.
         */
        
        if (method.equals(Options.class.getMethod("printDocumentation"))) {
            printDocumentation();
            return null;
        }
        
        if (method.equals(Options.class.getMethod("printDocumentation", PrintStream.class))) {
            printDocumentation((PrintStream) args[0]);
            return null;
        }
        
        if (method.equals(Options.class.getMethod("printDocumentation", Class.class))) {
            printDocumentation((Class) args[0]);
            return null;
        }
        
        if (method.equals(Options.class.getMethod("printDocumentation", Class.class, PrintStream.class))) {
            printDocumentation((Class) args[0], (PrintStream) args[1]);
            return null;
        }
        
        if (method.equals(Options.class.getMethod("unparsedArguments"))) {
            return optionValues.unusedArguments();
        }
        
        if (method.equals(Options.class.getMethod("exit", Enum.class, Object[].class))) {
            Object obj = args[0];
            if (StatusCode.class.isAssignableFrom(obj.getClass())) {
                StatusCode statusCode = (StatusCode) obj;
                Object[] arguments = (Object[])args[1];
                statusCode.beforeExit(new ExitStatusHelper(optionsInterface, (Enum<? extends StatusCode>) statusCode, null, arguments));
            }
            exit((Enum<?>) obj);
            return null;
        }
        
        if (method.equals(Options.class.getMethod("exit", int.class, Object[].class))) {
            int code = (Integer) args[0];
            
            Enum<?>[] consts = (Enum[]) exitCodeEnumClass.getMethod("values").invoke(null);
            if (code > -1 && code < consts.length) {
                StatusCode statusCode = (StatusCode) consts[code];
                Object[] arguments = (Object[])args[1];
                statusCode.beforeExit(new ExitStatusHelper(optionsInterface, (Enum<? extends StatusCode>) statusCode, null, arguments));
            }
            exit(code);
        }
        
        if (method.equals(Options.class.getMethod("exit", Throwable.class))) {
            
            Throwable t = (Throwable) args[0];
            Class tClass = t.getClass();
            if(tClass.equals(NonExceptionalExit.class)) {
                /*
                 * Adding an exception! We simply ignore the NonExceptionalExit
                 * exceptions since they have a special meaning and are thrown
                 * to interrupt execution flow when something calls exit.
                 * It is part of the contract of the ExitStatusConstant.catches()
                 * that no exit status constant can catch the NonExceptionalExit
                 */
                return false;
            }
            do {
                for(Field f : exitCodeEnumClass.getFields()) {
                    if(f.isAnnotationPresent(ExitStatusConstant.class)) {
                        ExitStatusConstant annotation = f.getAnnotation(ExitStatusConstant.class);
                        for(Class<? extends Throwable> ec: annotation.catches()) {
                            if(ec.equals(tClass)) {
                                
                                // Retrieving the localized message
                                String localizedMessage = t.getLocalizedMessage();
                                if(localizedMessage == null) {
                                    localizedMessage = "";
                                }
                                
                                // Retrieving the stack trace
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                PrintWriter pw = new PrintWriter(baos);
                                t.printStackTrace(pw);
                                pw.flush();
                                String stackTrace = new String(baos.toByteArray());
                                
                                StatusCode s = StatusCode.class.cast(f.get(null));
                                // {0}=localizedMessage, {1}=stackTrace
                                s.beforeExit(new ExitStatusHelper(optionsInterface, (Enum<? extends StatusCode>) s, t, localizedMessage, stackTrace));
                                exit((Enum<? extends StatusCode>) s);
                                return true;
                            }
                        }
                    }
                }
                tClass = tClass.getSuperclass();
            } while(!tClass.equals(Object.class)); // Repeats until the Throwable class.
            return false;
        }
        
        if (method.equals(Options.class.getMethod("rawArguments"))) {
            return rawArguments;
        }
        
        if (method.equals(Options.class.getMethod("localeMessage", String.class))) {
            return defaultFormatter().getMessage((String)args[0], null);
        }
        
        if (method.equals(Options.class.getMethod("localeMessage", String.class, String.class))) {
            return defaultFormatter().getMessage((String)args[0], (String)args[1]);
        }
        
        if (method.equals(Options.class.getMethod("formattedLocaleMessage", String.class, Object[].class))) {
            String pattern = defaultFormatter().getMessage((String)args[0], null);
            return MessageFormat.format(pattern, (Object[])args[1]);
        }
        
        if (method.equals(Options.class.getMethod("formattedLocaleMessageDefault", String.class, String.class, Object[].class))) {
            String pattern = defaultFormatter().getMessage((String)args[0], (String)args[1]);
            return MessageFormat.format(pattern, (Object[])args[2]);
        }
        
        if (method.getReturnType().isArray() && optionValues.getValue(method) == null) {
            return Array.newInstance(method.getReturnType().getComponentType(), 0);
        }
        
        return optionValues.getValue(method);
    }
    
    /**
     * TODO Comment.
     */
    protected final void printDocumentation() {
        defaultFormatter().printDocumentation();
    }

    /**
     * TODO Comment.
     * 
     * @param printStream TODO Comment.
     */
    protected final void printDocumentation(final PrintStream printStream) {
        defaultFormatter().printDocumentation(printStream);
    }

    /**
     * TODO Comment.
     */
    protected final void printDocumentation(Class<? extends Annotation> formatterAnnotationClass) {
        DocumentationFormatterEngine.buildFormatter(optionsInterface, formatterAnnotationClass).printDocumentation();
    }

    /**
     * TODO Comment.
     * 
     * @param printStream TODO Comment.
     */
    protected final void printDocumentation(Class<? extends Annotation> formatterAnnotationClass, final PrintStream printStream) {
        DocumentationFormatterEngine.buildFormatter(optionsInterface, formatterAnnotationClass).printDocumentation(printStream);
    }

    /**
     * TODO Comment.
     * 
     * @param e TODO Comment.
     */
    protected abstract void exit(final Enum<?> e);

    /**
     * TODO Comment.
     * 
     * @param code TODO Comment.
     */
    protected abstract void exit(final int code);

    @Override
    public final boolean equals(final Object obj) {
        if (!(obj instanceof AbstractArgumentsInvocationHandler<?>)) {
            return false;
        }
        AbstractArgumentsInvocationHandler<?> other = (AbstractArgumentsInvocationHandler<?>) obj;
        return optionValues.equals(other.optionValues) && exitCodeEnumClass.equals(other.exitCodeEnumClass);
    }

    @Override
    public final int hashCode() {
        return optionValues.hashCode();
    }

    @Override
    public final String toString() {
        return optionValues.toString();
    }
}
