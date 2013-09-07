package org.hyphenType.optionprocessors.lib;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.hyphenType.datastructure.Options;
import org.hyphenType.optionprocessors.ArgumentsProcessorEngine;

public class CustomizableValidatorEngine implements ArgumentsProcessorEngine<CustomizableValidatorEngine.CustomizableValidator> {

    public interface Rule<T> {
        void validate(T option);
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface CustomizableValidator {
        Class<? extends Rule<?>>[] rules();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Options<?>> void process(Class<T> interfaceClass, T options, CustomizableValidator config) {
        for (Class<? extends Rule<?>> ruleClass : config.rules()) {
            try {
                Rule<T> rule = (Rule<T>) ruleClass.newInstance();
                rule.validate(options);
            } catch (InstantiationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
