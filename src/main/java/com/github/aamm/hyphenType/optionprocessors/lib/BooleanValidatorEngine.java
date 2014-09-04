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
package com.github.aamm.hyphenType.optionprocessors.lib;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.github.aamm.hyphenType.datastructure.Options;
import com.github.aamm.hyphenType.datastructure.annotations.ArgumentsObject;
import com.github.aamm.hyphenType.datastructure.annotations.Option;
import com.github.aamm.hyphenType.datastructure.annotations.OptionArgument;
import com.github.aamm.hyphenType.datastructure.annotations.SimpleArgument;
import com.github.aamm.hyphenType.documentation.Description;
import com.github.aamm.hyphenType.optionprocessors.ArgumentsProcessorEngine;
import com.github.aamm.hyphenType.util.DefaultAnnotation;

/**
 * Rules must be written in JavaScript and should refer to the names of the
 * option methods. Return types of methods apply. I.e., if the return type is
 * int, variables referring to this method will be considered int, similarly for
 * boolean. One can turn all variables into booleans (converting non zero values
 * to false) by choosing {@link BooleanValidator#allBooleans()} to be true.<br/>
 * <br/>
 * <strong>WARNING! SECURITY RISK.</strong> There is a risk of script injection
 * if you allow the annotations to be loaded from properties files. The attacker
 * needs to add a properties file with the same name as the options interface to
 * the classpath. This malicious properties file may override the rules of a
 * {@link BooleanValidator} annotation. This trick will cause this engine to
 * execute the malicious scripts. Although this risk may look scary at first,
 * the sort of access the attacker would need to add a file to the classpath is
 * equivalent to the sort of access the attacker would need to have to replace
 * files in another user's space. In other words, the security risk described
 * here is a minor risk compared to the breach one needs in order to exploit the
 * risk itself.
 * 
 * @author Aurelio Akira M. Matsui
 */
public class BooleanValidatorEngine implements ArgumentsProcessorEngine<BooleanValidatorEngine.BooleanValidator> {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface BooleanValidator {
        @Description("The set of rules to be checked")
        Rule[] rules();

        @Description("Whether the processor will output messages detailing the execution. Good for debugging.")
        boolean verbose() default false;

        @Description("Whether the processor will output failure messages.")
        boolean showFailureMessages() default false;

        @Description("Whether the processor will threat integers as booleans. I.e. if zero will be considered FALSE and all other values will be TRUE.")
        boolean allBooleans() default false;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface Rule {
        String rule();

        String failureCodeName() default "";

        int failureCode() default 0;

        String failureMessage() default "";
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Options<?>> void process(Class<T> interfaceClass, T options, BooleanValidator config) {

        ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
        
        for (Method method : interfaceClass.getMethods()) {
            if (method.isAnnotationPresent(Option.class)) {
                try {
                    Object val;
                    if (method.getReturnType().equals(boolean.class) || method.getReturnType().equals(Boolean.class))
                        val = (Boolean) method.invoke(options);
                    else {
                        if (config.allBooleans())
                            val = ((Integer) method.invoke(options)) != 0;
                        else
                            val = (Integer) method.invoke(options);
                    }
                    engine.put(method.getName(), val);
                    continue;
                } catch (IllegalArgumentException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (method.isAnnotationPresent(OptionArgument.class) || method.isAnnotationPresent(SimpleArgument.class)) {
                try {
                    engine.put(method.getName(), method.invoke(options));
                    continue;
                } catch (IllegalArgumentException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        for (Rule rule : config.rules()) {
            try {
                int returnCode;
                if (!rule.failureCodeName().equals("")) {
                    ArgumentsObject ao = DefaultAnnotation.getAnnotation(interfaceClass, ArgumentsObject.class);
                    returnCode = Enum.valueOf((Class<? extends Enum>) ao.statusCodeEnum(), rule.failureCodeName()).ordinal();
                } else {
                    returnCode = rule.failureCode();
                }

                boolean val;
                Object obj = engine.eval(rule.rule());
                if (obj instanceof Double)
                    val = ((Double) obj).doubleValue() > 0;
                else if (obj instanceof Boolean)
                    val = (Boolean) obj;
                else {
                    if (config.verbose()) {
                        if (obj == null)
                            System.err.println("Rule \"" + rule + "\" did not return anything. Should return a boolean or a number.");
                        else
                            System.err.println("Rule \"" + rule + "\" returned a strange object type: " + obj.getClass().getName());
                    }
                    options.exit(returnCode);
                    return;
                }
                if (val) {
                    if (config.verbose())
                        System.err.println(String.format("Rule checking failure: \"%s\"", rule.rule()));
                    if (config.showFailureMessages())
                        System.err.print(rule.failureMessage());
                    options.exit(returnCode);
                    return;
                }

                if (config.verbose())
                    System.out.println(String.format("Rule checking success: \"%s\"", rule.rule()));
            } catch (ScriptException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
