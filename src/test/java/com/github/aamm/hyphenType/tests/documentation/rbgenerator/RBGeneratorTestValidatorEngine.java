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
package com.github.aamm.hyphenType.tests.documentation.rbgenerator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.github.aamm.hyphenType.datastructure.Options;
import com.github.aamm.hyphenType.documentation.Description;
import com.github.aamm.hyphenType.optionprocessors.ArgumentsProcessorEngine;

/**
 * Dummy validator engine.
 * 
 * @author Aurelio Akira M. Matsui
 */
public class RBGeneratorTestValidatorEngine implements ArgumentsProcessorEngine<RBGeneratorTestValidatorEngine.DummyValidator> {
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface DummyValidator {
        @Description("Tree description")
        Node[] tree();
        
        @Description("p1 description")
        boolean p1() default false;

        @Description("p2 description")
        String p2() default "p2 default";
    }
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface Node {
        @Description("Should not appear")
        SubNode[] subtree() default {};
        
        String subp1() default "subp1 default";
        
        String subp2() default "subp2 default";
    }
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface SubNode {
        String subp1() default "subp1 default";
        
        String subp2() default "subp2 default";
    }
    
    @Override
    public <T extends Options<?>> void process(Class<T> interfaceClass, T options, DummyValidator config) {
    }
}
