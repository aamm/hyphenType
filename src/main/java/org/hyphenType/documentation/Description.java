package org.hyphenType.documentation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.hyphenType.documentation.rbgenerator.RBGenerator;

/**
 * Documents a property of a formatter annotation for
 * <strong>developers</strong>. This
 * annotation is used by the {@link RBGenerator} to create
 * comments before each formatter option property in the
 * generated resource bundle. Also, this annotation can
 * be used to document a documentation formatter in a
 * machine readable way.
 * 
 * @author akira
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface Description {
    String value();
}
