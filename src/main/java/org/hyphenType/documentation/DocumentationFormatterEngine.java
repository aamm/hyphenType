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
package org.hyphenType.documentation;

import java.io.PrintStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.hyphenType.datastructure.Options;
import org.hyphenType.datastructure.annotations.ArgumentsObject;
import org.hyphenType.exceptions.InvalidOptionsInterfaceException;
import org.hyphenType.exit.ExitStatusConstant;
import org.hyphenType.lexerparser.LexerParser;
import org.hyphenType.util.DefaultAnnotation;
import org.hyphenType.util.I18NResourceBundle;
import org.hyphenType.util.soc.StringObjectConversion;
import org.hyphenType.util.soc.StringParsingError;

/**
 * @author Aurelio Akira M. Matsui
 * @param <T>
 *            TODO
 * @param <V>
 *            TODO
 */
public abstract class DocumentationFormatterEngine<T extends Options<?>, V extends Annotation> {

    /**
     * 
     */
    private LexerParser<T> lexPar;
    /**
     * 
     */
    private V annotation;
    /**
     * 
     */
    private ResourceBundle rb;

    /**
     * 
     */
    protected DocumentationFormatterEngine() {
    }

    /**
     * @param lexerParser
     *            TODO
     */
    private void setLexerParser(final LexerParser<T> lexerParser) {
        lexPar = lexerParser;
    }

    /**
     * @param formatterAnnotation
     *            TODO
     */
    private void setAnnotation(final V formatterAnnotation) {
        annotation = formatterAnnotation;
        // TODO Check if the annotation was correctly declared.
    }

    /**
     * @param resourceBundle
     *            TODO
     */
    private void setResourceBundle(final ResourceBundle resourceBundle) {
        rb = resourceBundle;
    }

    /**
     * @return TODO
     */
    public final V getAnnotation() {
        return annotation;
    }

    /**
     * @param key
     *            TODO
     * @param defaultValue
     *            TODO
     * @return TODO
     */
    public final String getMessage(final String key, final String defaultValue) {
        try {
            return rb.getString(key);
        } catch (MissingResourceException e) {
            return defaultValue;
        }
    }

    /**
     * Prints the documentation to the default stdout (System.out).
     */
    public final void printDocumentation() {
        printDocumentation(System.out, lexPar, annotation);
    }

    /**
     * Prints the documentation to the given {@link PrintStream}. Tip: A very
     * convenient way to save the output to a file is to call
     * 
     * <pre>
     * <code>
     * printDocumentation(new PrintStream("file.name"));
     * </code>
     * </pre>
     * 
     * @param pw
     *            TODO
     */
    public final void printDocumentation(final PrintStream pw) {
        printDocumentation(pw, lexPar, annotation);
        pw.flush();
    }

    /**
     * @param pw
     *            TODO
     * @param parser
     *            TODO
     * @param formatterAnnotation
     *            TODO
     */
    protected abstract void printDocumentation(PrintStream pw, LexerParser<T> parser, V formatterAnnotation);

    /**
     * Retrieves messages from the resource bundles.
     * 
     * @param key
     *            TODO
     * @param defaultValue
     *            TODO
     * @return TODO
     * @throws StringParsingError
     *             TODO
     */
    protected final String getOptionsInterfaceValue(final String key, final String defaultValue) throws StringParsingError {
        String result = getOptionsInterfaceValue(key);
        if (result == null) {
            return defaultValue;
        }
        return result;
    }

    /**
     * Retrieves messages from the resource bundles.
     * 
     * @param key
     *            TODO
     * @return TODO
     * @throws StringParsingError
     *             TODO
     */
    protected final String getOptionsInterfaceValue(final String key) throws StringParsingError {
        String rbKey;
        if (key.equals("")) {
            rbKey = lexPar.getOptionsInterface().getName();
        } else {
            rbKey = lexPar.getOptionsInterface().getName() + "." + key;
        }

        if (rb.containsKey(rbKey)) {
            return rb.getString(rbKey);
        } else {
            return null;
        }
    }

    /**
     * @param key
     *            TODO
     * @return TODO
     * @throws StringParsingError
     *             TODO
     */
    protected final String getStatusCodeUserDescription(final Enum<?> statusCode) throws StringParsingError {
        
        ExitStatusConstant exitStatusDocumentation = DefaultAnnotation.getAnnotation(statusCode, ExitStatusConstant.class);
        DefaultAnnotation.fillWithResourceBundle(exitStatusDocumentation, rb);
        
        if(exitStatusDocumentation.userDescription()==null || exitStatusDocumentation.userDescription().equals("")) {
            return statusCode.toString();
        } else {
            return exitStatusDocumentation.userDescription();
        }
    }

    /**
     * Builds the preferred formatter. Either the one that is default for the
     * options interface; the one chosen by the options interface user, which
     * can be configured using properties files; or the default formatter, which
     * is the default value of the property
     * {@link ArgumentsObject#preferredDocumentationFormatter()}.
     * 
     * @param optionsInterface
     *            TODO
     * @return TODO
     */
    @SuppressWarnings("unchecked")
    public static DocumentationFormatterEngine preferredFormatter(final Class<? extends Options<?>> optionsInterface) {

        /*
         * Note: Originally, this method's signature was public static <R
         * extends Options> DocumentationFormatterEngine<R>
         * preferredFormatter(Class<R> optionsInterface) But there seems to be a
         * difference between the compiler of JDK6 and the one used by Eclipse.
         * So for now we will utilize the (poorer) alternative: public static
         * DocumentationFormatterEngine preferredFormatter(Class
         * optionsInterface)
         */

        ArgumentsObject ao = DefaultAnnotation.getAnnotation(optionsInterface, ArgumentsObject.class);
        Class<? extends Annotation> formatterAnnotationClass = (Class<? extends Annotation>) ao.preferredDocumentationFormatter();
        return buildFormatter(optionsInterface, formatterAnnotationClass);
    }

    /**
     * Builds a formatter based on the options interface and the formatter
     * annotation. Documentation formatters are referenced by their annotation,
     * instead of its class.
     * 
     * @param <A>
     *            The options interface class.
     * @param <B>
     *            The documentation formatter annotation.
     * @param optionsInterface
     *            The options interface class.
     * @param formatterAnnotationClass
     *            The documentation formatter annotation.
     * @return A new documentation formatter.
     */
    @SuppressWarnings("unchecked")
    public static <A extends Options<?>, B extends Annotation> DocumentationFormatterEngine<A, B> buildFormatter(final Class<A> optionsInterface, final Class<B> formatterAnnotationClass) {

        Class<? extends DocumentationFormatterEngine<A, B>> formatterClass = (Class<? extends DocumentationFormatterEngine<A, B>>) formatterAnnotationClass.getEnclosingClass();

        try {

            B annotationObj = DefaultAnnotation.getAnnotation(optionsInterface, formatterAnnotationClass);
            ResourceBundle rb = new I18NResourceBundle(optionsInterface);

            for (Method m : formatterAnnotationClass.getMethods()) {
                if (m.getDeclaringClass().equals(formatterAnnotationClass)) {

                    if (rb.containsKey(formatterAnnotationClass.getName() + "." + m.getName())) {
                        try {
                            DefaultAnnotation.set(annotationObj, m, StringObjectConversion.fromString(m.getReturnType(), rb.getString(formatterAnnotationClass.getName() + "." + m.getName())));
                        } catch (StringParsingError e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    } else {
                        if (m.invoke(annotationObj) != null) {
                            DefaultAnnotation.set(annotationObj, m, m.invoke(annotationObj));
                        }
                    }
                } else {
                    if (m.getDefaultValue() != null) {
                        DefaultAnnotation.set(annotationObj, m, m.getDefaultValue());
                    }
                }
            }

            Constructor<DocumentationFormatterEngine<A, B>> constructor = (Constructor<DocumentationFormatterEngine<A, B>>) formatterClass.getConstructor();
            DocumentationFormatterEngine<A, B> engine = constructor.newInstance();
            engine.setLexerParser(new LexerParser(optionsInterface));
            engine.setAnnotation(annotationObj);
            engine.setResourceBundle(rb);
            return engine;

        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidOptionsInterfaceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
