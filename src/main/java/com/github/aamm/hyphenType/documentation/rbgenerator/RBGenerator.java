package com.github.aamm.hyphenType.documentation.rbgenerator;

import static com.github.aamm.hyphenType.exit.CanonicalExitCode.ERROR;
import static com.github.aamm.hyphenType.exit.CanonicalExitCode.SUCCESS;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.github.aamm.hyphenType.datastructure.annotations.ArgumentsObject;
import com.github.aamm.hyphenType.datastructure.annotations.Option;
import com.github.aamm.hyphenType.datastructure.annotations.OptionValue;
import com.github.aamm.hyphenType.datastructure.annotations.SimpleArgument;
import com.github.aamm.hyphenType.datastructure.parser.option.StructureOption;
import com.github.aamm.hyphenType.datastructure.parser.option.StructureOptionArgument;
import com.github.aamm.hyphenType.datastructure.parser.simple.StructureSimpleArgument;
import com.github.aamm.hyphenType.documentation.Description;
import com.github.aamm.hyphenType.documentation.DocumentationFormatterEngine;
import com.github.aamm.hyphenType.exceptions.InvalidOptionsInterfaceException;
import com.github.aamm.hyphenType.exit.ExitStatusConstant;
import com.github.aamm.hyphenType.lexerparser.LexerParser;
import com.github.aamm.hyphenType.lexerparser.exceptions.MandatoryMapValueNotFoundException;
import com.github.aamm.hyphenType.lexerparser.exceptions.MandatorySimpleArgumentNotFoundException;
import com.github.aamm.hyphenType.lexerparser.exceptions.MandatoryValueNotFoundException;
import com.github.aamm.hyphenType.lexerparser.exceptions.RegexMismatchException;
import com.github.aamm.hyphenType.lexerparser.exceptions.StringParseException;
import com.github.aamm.hyphenType.lexerparser.exceptions.StringParsingErrorException;
import com.github.aamm.hyphenType.optionprocessors.ArgumentsProcessorEngine;
import com.github.aamm.hyphenType.util.DefaultAnnotation;
import com.github.aamm.hyphenType.wrapper.StandAloneAppWrapper;

public class RBGenerator extends StandAloneAppWrapper {

    @SuppressWarnings("unchecked")
    public void main(RBGeneratorOptions args) {
        
        if (args.h()) {
            args.printDocumentation();
            args.exit(SUCCESS);
        }

        if (args.optionsClass()==null) {
            System.err.println("No options interface. Giving up. Use --help for more details.");
            args.exit(ERROR);
        }
        
        if (args.f() && args.fileName().equals("")) {
            System.err.println("You should inform a file name. Giving up. Use --help for more details.");
            args.exit(ERROR);
        }
        
        try {
            
            LexerParser lexParser = new LexerParser(args.optionsClass(), false);
            ArgumentsObject argumentsObject = lexParser.getArgsObject();
            
            /*
             * ALIASES
             */
            ByteArrayOutputStream aliasesBao = new ByteArrayOutputStream();
            PrintWriter aliasesPw = new PrintWriter(aliasesBao);
            aliasesPw.println("#");
            aliasesPw.println("# Aliases");
            aliasesPw.println("#");
            aliasesPw.println();
            
            /*
             * ARGUMENTS OBJECT
             */
            aliasesPw.println(String.format("alias.ao = %s", ArgumentsObject.class.getName()));
            ByteArrayOutputStream aoBao = new ByteArrayOutputStream();
            PrintWriter aoPw = new PrintWriter(aoBao);
            aoPw.println("#");
            aoPw.println("# Arguments object");
            aoPw.println("#");
            aoPw.println();
            aoPw.println(String.format("${ao}.description = %s", argumentsObject.description()));
            aoPw.println(String.format("${ao}.doubleHyphenInLongOptions = %s", argumentsObject.doubleHyphenInLongOptions()));
            aoPw.println(String.format("${ao}.singleHyphen = %s", argumentsObject.singleHyphen()));
            aoPw.println(String.format("${ao}.doubleHyphen = %s", argumentsObject.doubleHyphen()));
            aoPw.println(String.format("${ao}.equals = %s", argumentsObject.equals()));
            aoPw.println(String.format("${ao}.documentStatusCodes = %s", argumentsObject.documentStatusCodes()));
            aoPw.println(String.format("${ao}.preferredDocumentationFormatter = %s", argumentsObject.preferredDocumentationFormatter().getName()));

            
            /*
             * ARGUMENT PROCESSORS
             */
            ByteArrayOutputStream processorsBao = new ByteArrayOutputStream();
            PrintWriter processorsPw = new PrintWriter(processorsBao);
            processorsPw.println("#");
            processorsPw.println("# Argument processors");
            processorsPw.println("#");
            processorsPw.println();
            boolean found = false;
            for (Annotation annotation : args.optionsClass().getAnnotations()) {
                if (annotation.annotationType().getEnclosingClass() != null && ArgumentsProcessorEngine.class.isAssignableFrom(annotation.annotationType().getEnclosingClass())) {
                    found = true;
                    Class<?> processorAnnotationClass = annotation.annotationType();
                    /* Not compatible with two processors with the same simple name, such as x.y.A and w.z.A.
                     * But this is not a big problem, since the user can always his own set of aliases to
                     * manually eliminate this collision.
                     */
                    String aliasName = processorAnnotationClass.getSimpleName();
                    aliasesPw.println(String.format("alias.p.%s = %s", aliasName, processorAnnotationClass.getName()));
                    processorsPw.println("# " + annotation.annotationType().getSimpleName());
                    processorsPw.println();
                    for (Method m : processorAnnotationClass.getDeclaredMethods()) {
                        if (m.isAnnotationPresent(Description.class)) {
                            processorsPw.println("# " + m.getAnnotation(Description.class).value());
                        }
                        try {
                            if (m.getReturnType().isArray()) {
                                if (m.getReturnType().getComponentType().isAnnotation()) {
                                    processorsPw.print(expandAnnotationArray(String.format("${p.%s}.%s", aliasName, m.getName()), (Annotation[])m.invoke(annotation), m.getReturnType().getComponentType()));
                                } else {
                                    processorsPw.println(String.format("${p.%s}.%s = %s", aliasName, m.getName(), Arrays.toString((Object[])m.invoke(annotation))));
                                }
                            } else {
                                processorsPw.println(String.format("${p.%s}.%s = %s", aliasName, m.getName(), m.invoke(annotation)));
                            }
                            /* We do not expect any of these following exceptions to ever happen.
                             */
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            if(!found) {
                processorsPw.println("# No argument processor found");
            }
            
            /*
             * OPTIONS
             */
            aliasesPw.println("alias.Interface = " + args.optionsClass().getName());
            aliasesPw.println("alias.Option = " + Option.class.getName());
            aliasesPw.println("alias.OptionValue = " + OptionValue.class.getName());
            aliasesPw.println("alias.OptionArgument = " + OptionValue.class.getName());
            ByteArrayOutputStream optionsBao = new ByteArrayOutputStream();
            PrintWriter optionsPw = new PrintWriter(optionsBao);
            optionsPw.println("#");
            optionsPw.println("# Options");
            optionsPw.println("#");
            optionsPw.println();
            List<StructureOption> options = lexParser.getParsedOptions();
            for(StructureOption option : options) {
                optionsPw.println(String.format("${Interface}.%s@${Option}.names = %s", option.method.getName(), option.alternatives));
                optionsPw.println(String.format("${Interface}.%s@${Option}.description = %s", option.method.getName(), option.description));
                if(option.value!=null) {
                    optionsPw.println(String.format("${Interface}.%s@${OptionValue}.name = %s", option.value.method.getName(), option.value.name));
                    optionsPw.println(String.format("${Interface}.%s@${OptionValue}.option = %s", option.value.method.getName(), option.method.getName()));
                    optionsPw.println(String.format("${Interface}.%s@${OptionValue}.mandatory = %s", option.value.method.getName(), option.value.mandatory));
                    optionsPw.println(String.format("${Interface}.%s@${OptionValue}.arraySeparator = %s", option.value.method.getName(), option.value.arraySeparator));
                    optionsPw.println(String.format("${Interface}.%s@${OptionValue}.arrayUseFileSeparator = %s", option.value.method.getName(), option.value.arrayUseFileSeparator));
                }
                for(StructureOptionArgument argument : option.arguments) {
                    optionsPw.println(String.format("${Interface}.%s@${OptionArgument}.name = %s", argument.method.getName(), argument.getName()));
                    optionsPw.println(String.format("${Interface}.%s@${OptionArgument}.option = %s", argument.method.getName(), option.method.getName()));
                    optionsPw.println(String.format("${Interface}.%s@${OptionArgument}.index = %s", argument.method.getName(), argument.getIndex()));
                    optionsPw.println(String.format("${Interface}.%s@${OptionArgument}.regex = %s", argument.method.getName(), argument.getRegex()));
                    optionsPw.println(String.format("${Interface}.%s@${OptionArgument}.channels = %s", argument.method.getName(), argument.getChannels()));
                    optionsPw.println(String.format("${Interface}.%s@${OptionArgument}.mandatory = %s", argument.method.getName(), argument.isMandatory()));
                    optionsPw.println(String.format("${Interface}.%s@${OptionArgument}.description = %s", argument.method.getName(), argument.getDescription()));
                }
            }
            
            /*
             * SIMPLE ARGUMENTS
             */
            aliasesPw.println("alias.SimpleArgument = " + SimpleArgument.class.getName());
            ByteArrayOutputStream simpleArgumentsBao = new ByteArrayOutputStream();
            PrintWriter simpleArgumentsPw = new PrintWriter(simpleArgumentsBao);
            simpleArgumentsPw.println("#");
            simpleArgumentsPw.println("# Simple Arguments");
            simpleArgumentsPw.println("#");
            simpleArgumentsPw.println();
            
            List<StructureSimpleArgument> simpleArguments = lexParser.getSimpleArguments();
            for(StructureSimpleArgument simpleArgument : simpleArguments) {
                simpleArgumentsPw.println(String.format("${Interface}.%s@${SimpleArgument}.name = %s", simpleArgument.method.getName(), simpleArgument.getName()));
                simpleArgumentsPw.println(String.format("${Interface}.%s@${SimpleArgument}.index = %s", simpleArgument.method.getName(), simpleArgument.getIndex()));
                simpleArgumentsPw.println(String.format("${Interface}.%s@${SimpleArgument}.description = %s", simpleArgument.method.getName(), simpleArgument.getDescription()));
                simpleArgumentsPw.println(String.format("${Interface}.%s@${SimpleArgument}.regex = %s", simpleArgument.method.getName(), simpleArgument.getRegex()));
                simpleArgumentsPw.println(String.format("${Interface}.%s@${SimpleArgument}.mandatory = %s", simpleArgument.method.getName(), simpleArgument.isMandatory()));
                simpleArgumentsPw.println(String.format("${Interface}.%s@${SimpleArgument}.channels = %s", simpleArgument.method.getName(), simpleArgument.getChannels()));
            }
            
            /*
             * EXIT STATUS
             */
            aliasesPw.println("alias.status = " + argumentsObject.statusCodeEnum().getName());
            aliasesPw.println("alias.stdoc = " + ExitStatusConstant.class.getName());
            ByteArrayOutputStream statusBao = new ByteArrayOutputStream();
            PrintWriter statusPw = new PrintWriter(statusBao);
            statusPw.println("#");
            statusPw.println("# Exit status");
            statusPw.println("#");
            Class<? extends Enum> statusCodeEnumClass = (Class<? extends Enum>) argumentsObject.statusCodeEnum();
            for(Enum enumConstant : statusCodeEnumClass.getEnumConstants()) {
                statusPw.println();
                try {
                    if(argumentsObject.statusCodeEnum().getField(enumConstant.toString()).isAnnotationPresent(Description.class)) {
                        String description = argumentsObject.statusCodeEnum().getField(enumConstant.toString()).getAnnotation(Description.class).value();
                        description = description.replaceAll("\n", "\n# ");
                        statusPw.println(String.format("# %s", description));
                    }
                    /* I do not really care about these exceptions, since o came from the class I am checking.
                     * So unless there is some bug in the reflection API (which is frankly unlikely), the above
                     * code should work fine.
                     */
                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
                ExitStatusConstant constant = DefaultAnnotation.getAnnotation((Enum)enumConstant, ExitStatusConstant.class);
                if(constant.userDescription()==null) {
                    statusPw.println(String.format("${status}.%s@${stdoc}.userDescription = ", enumConstant.toString()));
                } else {
                    statusPw.println(String.format("${status}.%s@${stdoc}.userDescription = %s", enumConstant.toString(), constant.userDescription()));
                }
                if(constant.message()==null) {
                    statusPw.println(String.format("${status}.%s@${stdoc}.message = ", enumConstant.toString()));
                } else {
                    statusPw.println(String.format("${status}.%s@${stdoc}.message = %s", enumConstant.toString(), constant.message()));
                }
                
                // TODO Test
                for (int i = 0; i< constant.catches().length; i++) {
                    statusPw.println(String.format("${status}.%s@${stdoc}.exceptions[%d] = %s", enumConstant.toString(), i, constant.catches()[i].getName()));
                }
            }
            
            /*
             * DOCUMENTATION FORMATTER
             */
            ByteArrayOutputStream formatterBao = new ByteArrayOutputStream();
            PrintWriter formatterPw = new PrintWriter(formatterBao);
            
            ArrayList<Class<? extends Annotation>> documentationFormattersList = new ArrayList<Class<? extends Annotation>>();
            // Adding the preferred formatter
            documentationFormattersList.add(argumentsObject.preferredDocumentationFormatter());
            // Adding each of the formatters configured by annotations present in the options interface
            for (Annotation annotation : args.optionsClass().getAnnotations()) {
                if (
                    annotation.annotationType().getEnclosingClass()!=null &&
                    DocumentationFormatterEngine.class.isAssignableFrom(annotation.annotationType().getEnclosingClass()) &&
                    !documentationFormattersList.contains(annotation.annotationType())
                ) {
                    documentationFormattersList.add(annotation.annotationType());
                }
            }
            
            formatterPw.println("#");
            formatterPw.println("# Documentation formatters");
            formatterPw.println("#");
            for (Class<? extends Annotation> formatterClass : documentationFormattersList) {
                // Not compatible with documentation formatter annotations with identical simple names
                aliasesPw.println(String.format("alias.f.%s = %s", formatterClass.getSimpleName(), formatterClass.getName()));
                formatterPw.println();
                formatterPw.println("# " + formatterClass.getSimpleName());
                formatterPw.println();
                
                Annotation annotation = args.optionsClass().getAnnotation(formatterClass);
                
                for(Method m : formatterClass.getDeclaredMethods()) {
                    if(m.isAnnotationPresent(Description.class)) {
                        String description = m.getAnnotation(Description.class).value();
                        description = description.replaceAll("\n", "\n# ");
                        formatterPw.println(String.format("# %s", description));
                    }
                    
                    if (annotation == null) {
                        if(m.getDefaultValue() == null) {
                            formatterPw.println(String.format("${f.%s}.%s = ", formatterClass.getSimpleName(), m.getName()));
                        } else {
                            formatterPw.println(String.format("${f.%s}.%s = %s", formatterClass.getSimpleName(), m.getName(), m.getDefaultValue()));
                        }
                    } else {
                        try {
                            formatterPw.println(String.format("${f.%s}.%s = %s", formatterClass.getSimpleName(), m.getName(), m.invoke(annotation)));
                            // We do not expect the following exceptions be ever thrown.
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            
            
            /*
             * ERROR MESSAGES
             */
            ByteArrayOutputStream errorBao = new ByteArrayOutputStream();
            PrintWriter errorPw = new PrintWriter(errorBao);
            errorPw.println("#");
            errorPw.println("# Internal error messages");
            errorPw.println("#");
            errorPw.println();
            errorPw.println(MandatoryMapValueNotFoundException.class.getName() + ".DEFAULT_PATTERN = " + MandatoryMapValueNotFoundException.DEFAULT_PATTERN);
            errorPw.println(MandatorySimpleArgumentNotFoundException.class.getName() + ".DEFAULT_PATTERN = " + MandatorySimpleArgumentNotFoundException.DEFAULT_PATTERN);
            errorPw.println(MandatoryValueNotFoundException.class.getName() + ".DEFAULT_PATTERN = " + MandatoryValueNotFoundException.DEFAULT_PATTERN);
            errorPw.println(RegexMismatchException.class.getName() + ".DEFAULT_PATTERN = " + RegexMismatchException.DEFAULT_PATTERN);
            errorPw.println(StringParseException.class.getName() + ".DEFAULT_PATTERN = " + StringParseException.DEFAULT_PATTERN);
            errorPw.println(StringParsingErrorException.class.getName() + ".DEFAULT_PATTERN = " + StringParsingErrorException.DEFAULT_PATTERN);
            
            /*
             * FLUSHING IT ALL
             */
            aliasesPw.flush();
            aliasesBao.flush();
            aoPw.flush();
            aoBao.flush();
            processorsPw.flush();
            processorsBao.flush();
            optionsPw.flush();
            optionsBao.flush();
            simpleArgumentsPw.flush();
            simpleArgumentsBao.flush();
            statusPw.flush();
            statusBao.flush();
            formatterPw.flush();
            formatterBao.flush();
            errorPw.flush();
            errorBao.flush();

            /*
             * WRITING TO OUTPUT
             */
            ArrayList<OutputStream> outputStreams = new ArrayList<OutputStream>();
            if(args.f()) {
                String fileNameStem;
                String fileNameExtension;
                if(args.fileName().contains(".")) {
                    fileNameStem = args.fileName().substring(0, args.fileName().lastIndexOf('.'));
                    if(args.fileName().endsWith(".")) {
                        fileNameExtension = "properties";
                    }
                    else {
                        fileNameExtension = args.fileName().substring(args.fileName().lastIndexOf('.') + 1);
                    }
                } else {
                    fileNameStem = args.fileName();
                    fileNameExtension = "properties";
                    outputStreams.add(new FileOutputStream(new File(String.format("%s.%s", fileNameStem, fileNameExtension))));
                }
                
                for(String variant : args.supportedLanguages()) {
                    outputStreams.add(new FileOutputStream(new File(String.format("%s_%s.%s", fileNameStem, variant, fileNameExtension))));
                }
            } else {
                outputStreams.add(System.out);
            }
            
            for(OutputStream os : outputStreams) {
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
                PrintWriter pw = new PrintWriter(bos);
                Date now = new Date();
                pw.println("# ");
                pw.println(String.format("# Configuration file for the %s options interface.", args.optionsClass().getName()));
                pw.println(String.format("# Automatically generated using %s on %s, %s.", RBGenerator.class.getName(), DateFormat.getDateInstance(DateFormat.FULL).format(now), DateFormat.getTimeInstance(DateFormat.FULL).format(now)));
                pw.println("# ");
                pw.println();
                pw.println(aliasesBao.toString());
                pw.println();
                pw.println(aoBao.toString());
                pw.println();
                pw.println(processorsBao.toString());
                pw.println();
                pw.println(optionsBao.toString());
                pw.println();
                pw.println(simpleArgumentsBao.toString());
                pw.println();
                pw.println(statusBao.toString());
                pw.println();
                pw.println(formatterBao.toString());
                pw.println();
                pw.println(errorBao.toString());
                pw.flush();
                bos.flush();
                os.flush();
                os.close();
            }
            
        } catch (InvalidOptionsInterfaceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public static String expandAnnotationArray(String prefix, Annotation[] array, Class<?> arrayClass) {
        String result = "";
        try {
            int i = 0;
            for (Annotation annotation : array) {

                for (Method m : arrayClass.getDeclaredMethods()) {
                    if (m.getReturnType().isArray() && m.getReturnType().getComponentType().isAnnotation()) {
                        result += expandAnnotationArray(String.format("%s[%d].%s", prefix, i, m.getName()), (Annotation[]) m.invoke(annotation), m.getReturnType().getComponentType());
                    } else {
                        result += String.format("%s[%d].%s = %s\n", prefix, i, m.getName(), m.invoke(annotation));
                    }
                }
                i++;
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return result;
    }
}
