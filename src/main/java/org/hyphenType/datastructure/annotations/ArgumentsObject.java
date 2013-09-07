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
package org.hyphenType.datastructure.annotations;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
        
import org.hyphenType.documentation.lib.StandardFormatterEngine.StandardFormatter;
import org.hyphenType.exit.CanonicalExitCode;
import org.hyphenType.exit.StatusCode;

/**
 * @author Aurelio Akira M. Matsui
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ArgumentsObject {

    /**
     * Default encoding for the resource bundles.
     */
    String DEFAULT_RB_ENCODING = "UTF-8";
    /**
     * Default single hyphen.
     */
    String DEFAULT_SINGLE_HYPHEN = "-";
    /**
     * Default double hyphen.
     */
    String DEFAULT_DOUBLE_HYPHEN = "--";
    /**
     * Default equals string.
     */
    String DEFAULT_EQUALS = "=";

    /**
     * The path to find the resource bundles containing the documentation. If
     * this property is empty, will search for the resource bundles in the same
     * package as the arguments object interface is. Paths are directly
     * forwarded to {@link ResourceBundle#getBundle(String)}, and therefore,
     * should be fully qualified names pointing to a set of property files. This
     * property is useful when the same set of resource bundles are shared
     * between several classes.<br>
     * <br>
     * If the value of this property is the empty string (the default value),
     * the fully qualified name of the options interface will be used instead.
     * In other words, if the options interface is <code>a.b.C</code>, and if
     * this property is an empty string, then files such as
     * <code>a.b.C.properties</code>, <code>a.b.C_es.properties</code>, etc...
     * (in other words, the files /a/b/C.properties and /a/b/C_es.properties
     * where / is the root of the JAR file) will be used.<br>
     * <br>
     * See {@link ResourceBundle#getBundle(String baseName, Locale locale, ClassLoader loader)}
     * for more information.
     * 
     * @see ResourceBundle#getBundle(String baseName, Locale locale, ClassLoader
     *      loader)
     */
    String resourceBundlesLocation() default "";

    /**
     * Configures which encoding will be used to read resource bundle files.
     */
    String resourceBundlesEncoding() default DEFAULT_RB_ENCODING;

    /**
     * Documentation of the options interface. This documentation is overridden
     * by data from resource bundles, if present.
     */
    String description() default "";

    /**
     * Whether long options (those with more than one character) will
     * automatically receive two hyphens.
     */
    boolean doubleHyphenInLongOptions() default true;

    /**
     * The string that will be used as the single hyphen. A single hyphen is the
     * prefix for single character options. A single hyphen is used not only to
     * mark the start of an option whose name is a single character (e.g.: "-n"
     * for an option named "n"), but also to start a cluster of single character
     * options (e.g.: "-xcvf" which is short for "-x -c -v -f").
     */
    String singleHyphen() default DEFAULT_SINGLE_HYPHEN;

    /**
     * 
     */
    String doubleHyphen() default DEFAULT_DOUBLE_HYPHEN;

    /**
     * The string that will be used as the equals sign. In other words, the
     * symbol that will be used to separate keys from values.
     */
    String equals() default DEFAULT_EQUALS;

    /**
     * The class that will format the documentation. The formatter class will
     * be used when the following methods are called:
     * <ul>
     * <li>{@link Options#printDocumentation()}</li>
     * <li>{@link Options#printDocumentation(java.io.PrintStream)}</li>
     * </ul>
     * Which are mapped by {@link ArgumentsInvocationHandler} to these methods:
     * <ul>
     * <li>{@link DocumentationFormatterEngine#printDocumentation()}</li>
     * <li>
     * {@link DocumentationFormatterEngine#printDocumentation(java.io.PrintStream)}
     * </li>
     * </ul>
     */
    Class<? extends Annotation> preferredDocumentationFormatter() default StandardFormatter.class;

    /**
     * The status code enumeration for this arguments object.
     */
    Class<? extends StatusCode> statusCodeEnum() default CanonicalExitCode.class;

    /**
     * Sets whether or not calls to {@link Options#printDocumentation()}, or
     * {@link Options#printDocumentation(java.io.PrintStream)} should generate
     * documentation for status codes. The standard interpretation for status
     * codes is that a zero means a successful program termination while any
     * non-zero number is a unique error code. A caller program or shell could
     * then read this error code and act accordingly, or show a custom message
     * to a user.<br>
     * <br>
     * Nevertheless, there are cases in which status codes are not important.<br>
     * <br>
     * Status codes may be simply ignored by programmers (perhaps because they
     * simply return zero despite of what happened during program execution).
     * Finally, status codes may be used to return some numerical value
     * (although this is considered a violation of the convention). In all these
     * cases above, the programmer may chose to simply omit information about
     * status codes from his/her documentation. This flags allows the programmer
     * to do so, although the actual behavior of this flag will depend upon the
     * implementation of {@link DocumentationFormatterEngine} in use.
     * 
     * @see ArgumentsObject#statusCodeEnum()
     * @see ArgumentsObject#preferredDocumentationFormatter()
     */
    boolean documentStatusCodes() default false;
}
