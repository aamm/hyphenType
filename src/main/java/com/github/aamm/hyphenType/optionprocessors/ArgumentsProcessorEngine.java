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
package com.github.aamm.hyphenType.optionprocessors;

import java.lang.annotation.Annotation;

import com.github.aamm.hyphenType.datastructure.Options;
import com.github.aamm.hyphenType.optionprocessors.lib.BooleanValidatorEngine;

/**
 * A processor for arguments parsed. A processor is an object that will check an
 * option object to search for inconsistencies. All processor are called before
 * option objects are made available for the main application, representing part
 * of a contract the arguments must met in order to be accepted into the main
 * application. As in design by contract, the main application does not need to
 * have defensive source code. In other words, the main application does not
 * need to protect itself from malformed option objects. <br/>
 * <br/>
 * Argument processor classes should declare an inner annotation that is to be
 * added to the options interface. When such annotation is detected, the
 * extractor will search for the class in which the annotation was declared
 * using {@link Class#getEnclosingClass()}. After the processor class was found,
 * a new instance of the processor class the extractor will create a new
 * instance of this class using the default constructor.<br/>
 * <br/>
 * The method {@link ArgumentsProcessorEngine#process(Class, Options, Enum)},
 * should search for contract violations. When the first violation is found,
 * this method should call either {@link Options#exit(Enum)} or
 * {@link Options#exit(int)} to terminate the JVM. <br/>
 * <br/>
 * Although a good example of implementation of {@link ArgumentsProcessorEngine}
 * is the class {@link BooleanValidatorEngine}, maybe the following pseudo code
 * clarifies our explanation:<br/>
 * 
 * <pre>
 * <code>
 * public class FooValidatorEngine&lt;T extends Options&lt;?&gt;&gt; implements ArgumentValidator&lt;T, FooValidator&gt; {
 * 
 * 	&#64;Retention(RetentionPolicy.RUNTIME)
 * 	&#64;Target(ElementType.TYPE)
 * 	public &#64;interface FooValidator {
 * 
 * 		String propertyA() default "ABC";
 * 
 * 		String propertyB();
 * 	}
 * 
 * 	&#64;Override
 * 	public void run(Class&lt;T&gt; interfaceClass, T options, FooValidator config) {
 * 		
 * 		if(options are <strong>NOT</strong> OK)
 * 			options.exit(returnCode);
 * 	}
 * }
 * </code>
 * </pre>
 * 
 * Properties with default values (such as propertyA above) will come with the
 * default value filled even if the user did not specify any value for this
 * property explicitly.<br/>
 * <br/>
 * When writing an argument processor engine keep in mind that users will
 * interface only with the inner annotation and the processor itself will be
 * invisible. Which means the annotation, not the processor, should have a
 * friendly name.
 * 
 * @author Aurelio Akira M. Matsui
 * @param <T>
 */
//public interface ArgumentsProcessorEngine<T extends Options<?>, U extends Annotation> {
public interface ArgumentsProcessorEngine<U extends Annotation> {

    /**
     * Must call {@link Options#exit(Enum)} or {@link Options#exit(int)} if, and
     * only if, the options violate the contract represented by this argument
     * validator engine.
     * 
     * @param interfaceClass The options interface class
     * @param options The options object
     * @param config The configuration annotation
     */
    <T extends Options<?>> void process(Class<T> interfaceClass, T options, U config);
}
