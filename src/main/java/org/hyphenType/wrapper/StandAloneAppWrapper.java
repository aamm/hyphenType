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
package org.hyphenType.wrapper;

import static org.hyphenType.datastructure.annotations.ArgumentsObject.DEFAULT_DOUBLE_HYPHEN;
import static org.hyphenType.datastructure.annotations.ArgumentsObject.DEFAULT_EQUALS;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.hyphenType.datastructure.Options;
import org.hyphenType.debug.HTLogger;
import org.hyphenType.exceptions.InvalidOptionsInterfaceException;
import org.hyphenType.input.UserInput;
import org.hyphenType.lexerparser.exceptions.MandatorySimpleArgumentNotFoundException;
import org.hyphenType.lexerparser.exceptions.OptionValuesException;
import org.hyphenType.optionsextractor.OptionsExtractor;
import org.hyphenType.optionsextractor.OptionsExtractorException;
import org.hyphenType.unittesting.NonExceptionalExit;

/**
 * A wrapper that can be called instead of a standard main class. When called as
 * a main class, this class will try to find which class is the actual main
 * class of the application. After this actual main class was found, this class
 * will parse the arguments to a options interface and give an instance of the
 * options interface to the actual main class.
 * 
 * @author Aurelio Akira M. Matsui
 */
public class StandAloneAppWrapper {

    /**
     * Manifest files can have a key value pair with this key to explicitly set
     * which is the actual main class.
     */
    public static final String KEY = "StandAloneAppWrapper-main-class";

    /**
     * The especial argument that explicitly sets which is the main class.
     */
    public static final String ARGUMENT = DEFAULT_DOUBLE_HYPHEN + KEY + DEFAULT_EQUALS;

    /**
     * Standard main method.
     * 
     * @param arguments
     *            Arguments from the command line.
     * @throws Throwable
     *             Anything that can go wrong.
     */
    @SuppressWarnings("unchecked")
    public static void main(final String... arguments) throws Throwable {

        /*
         * TODO We need to conduct an extensive review of this main method.
         * There are several security concerns to address here. We cannot rely
         * on system properties or variables in order to find which is the main
         * class. That could violate security as it could allow for malicious
         * code to easily change the main class. The same problem does not
         * happen when we read properties from the manifest file, as we assume
         * that the integrity of the JAR cannot be violated. Also, we need a
         * method to find the main class in which we analyze the stack trace to
         * retrieve the current running class. There is a decision to make.
         * Shall we restrict the valid main classes to only those that extend
         * StandAloneAppWrapper? If we do restrict, then we can eliminate
         * unreliable methods to find the main class. On the other hand, Java
         * does not allow for multiple inheritance. So if a programmer needs to
         * create a main class XSub that extends a superclass X, XSub will be
         * not allowed to extend StandAloneAppWrapper. So what is the best
         * design?
         */

        String mainClassName = null;

        /*
         * (1) key in MANIFEST.MF
         */
        /*
         * (1.1) If this class was added to user's jar file. I.e. if this class
         * is in the directly called jar file.
         */
        try {
            JarFile jarFile = new JarFile(new File(StandAloneAppWrapper.class.getProtectionDomain().getCodeSource().getLocation().toURI()));
            Manifest m = jarFile.getManifest();
            mainClassName = m.getMainAttributes().getValue(KEY);
        } catch (FileNotFoundException e) {
            mainClassName = null;
        } catch (IOException e) {
            mainClassName = null;
        } catch (URISyntaxException e) {
            mainClassName = null;
        }
        /*
         * (1.2) If this class is in a jar file referenced by the directly
         * called jar file.
         */
        try {
            StringTokenizer st = new StringTokenizer(System.getProperty("java.class.path"), System.getProperty("path.separator"));
            if (st.countTokens() == 1) { // java -jar calls will have only one
                                         // item in class path
                JarFile jarFile = new JarFile(new File(st.nextToken()));
                Manifest m = jarFile.getManifest();
                mainClassName = m.getMainAttributes().getValue(KEY);
            }
        } catch (FileNotFoundException e) {
            mainClassName = null;
        } catch (IOException e) {
            mainClassName = null;
        }

        String[] correctedArguments = arguments;

        /*
         * (2) Extra argument
         */
        if (mainClassInvalid(mainClassName) && correctedArguments.length > 0 && correctedArguments[0].startsWith(ARGUMENT)) {
            mainClassName = correctedArguments[0].substring(ARGUMENT.length(), correctedArguments[0].length());
            correctedArguments = Arrays.copyOfRange(correctedArguments, 1, correctedArguments.length);
        }

        /*
         * (3) System property variable
         */
        if (mainClassInvalid(mainClassName)) {
            mainClassName = System.getProperty(KEY);
        }

        /*
         * (4) System environment variable
         */
        if (mainClassInvalid(mainClassName)) {
            mainClassName = System.getenv(KEY);
        }

        /*
         * (5) Reflection over jar file. This will detect cases in which this
         * JVM was invoked using "java -jar something.jar" and this class
         * StandAloneAppWrapper was indirectly called either because the main
         * class is a subclass of StandAloneAppWrapper or because explicit call
         * to StandAloneAppWrapper was made somehow. The same process will also
         * detect direct calls to a jar file (such as "./something.jar") in
         * Linux and Solaris environments.
         */
        if (mainClassInvalid(mainClassName)) {
            try {
                StringTokenizer st = new StringTokenizer(System.getProperty("java.class.path"), System.getProperty("path.separator"));
                if (st.countTokens() == 1) { // java -jar calls will have only
                    // one item in class path
                    JarFile jarFile = new JarFile(new File(st.nextToken()));
                    Manifest m = jarFile.getManifest();
                    mainClassName = m.getMainAttributes().getValue("Main-Class");
                }
            } catch (FileNotFoundException e) {
                mainClassName = null;
            } catch (IOException e) {
                mainClassName = null;
            }
        }

        /*
         * (6) Detects the main class via system property "sun.java.command".
         */
        if (mainClassInvalid(mainClassName)) {
            try {
                String candidateClassName = System.getProperty("sun.java.command");
                // If there are arguments, they will come along the command. So
                // we need to
                // filter them out.
                if (candidateClassName.contains(" ")) {
                    candidateClassName = candidateClassName.substring(0, candidateClassName.indexOf(" "));
                }
                if (!StandAloneAppWrapper.class.equals(StandAloneAppWrapper.class.getClassLoader().loadClass(candidateClassName))) {
                    mainClassName = candidateClassName;
                }
            } catch (ClassNotFoundException e) {
                mainClassName = null;
            }
        }

        if (mainClassInvalid(mainClassName)) {
            System.err.println("Error: could not find the main class to execute. Possible methods are (in this order of precedence):\n" + "(1) add the property '" + KEY + "' to the MANIFEST.MF file;\n" + "(2) pass a first argument called " + ARGUMENT + " to the " + StandAloneAppWrapper.class.getName() + " class;\n" + "(3) set the system property " + KEY + " for the JVM (utilizing the parameter -D or programmatically before calling the " + StandAloneAppWrapper.class.getName() + " class);\n" + "(4) set the environment variable " + KEY + " before executing this class;\n" + "(5) make your main class extend or call " + StandAloneAppWrapper.class.getName() + ", put your main class in a jar file, and start the JVM using \"java -jar\"; or\n" + "(6) simply make your main class YourClass extend or call " + StandAloneAppWrapper.class.getName() + " and start the JVM using \"java YourClass\" (works also inside of your favorite IDE).");
            return;
        }
        
        Class clazz;
        try {
            clazz = StandAloneAppWrapper.class.getClassLoader().loadClass(mainClassName);
        } catch (ClassNotFoundException e) {
            System.err.println("Error: could not load the main class " + mainClassName);
            return;
        }

        new StandAloneAppWrapper().invokeMain(clazz, correctedArguments);
    }

    protected static boolean mainClassInvalid(String mainClassName) {
        if (mainClassName == null)
            return true;
        try {
            StandAloneAppWrapper.class.getClassLoader().loadClass(mainClassName);
        } catch (ClassNotFoundException e) {
            return true;
        }
        return false;
    }

    /**
     * A safer main method. It is safer because this method receives which is
     * the actual main class as a parameter.
     * 
     * @param mainClass
     *            The main class to execute.
     * @param arguments
     *            The arguments, from command line.
     * @throws Throwable
     *             Anything that can go wrong.
     */
    public static void main(final Class<?> mainClass, final String... arguments) throws Throwable {

        new StandAloneAppWrapper().invokeMain(mainClass, arguments);
    }

    /**
     * Invokes the main method of the given class. It also parses the received
     * arguments and creates an option object. This method will trap all
     * throwables, preventing them to go uncaught to the JVM. Instead, an error
     * message is printed.
     *  
     * @param mainClass
     *            The main class to be executed.
     * @param arguments
     *            The arguments received from the command line.
     * @param trapThrowable
     *            If true, this method will prevent throwables from going
     *            uncaught to the JVM, which causes the throwable to be
     *            printed in user's console.
     * @throws Throwable
     *             Anything that can go wrong.
     */
    public final void invokeMain(final Class<?> mainClass, final String[] arguments) throws Throwable {
        invokeMain(mainClass, arguments, true);
    }
    
    /**
     * Invokes the main method of the given class. It also parses the received
     * arguments and creates an option object.
     * 
     * @param mainClass
     *            The main class to be executed.
     * @param arguments
     *            The arguments received from the command line.
     * @param trapThrowable
     *            If true, this method will prevent throwables from going uncaught to the JVM, which causes the throwable to be printed in user's console.
     * @throws Throwable
     *             Anything that can go wrong.
     */
    public final void invokeMain(final Class<?> mainClass, final String[] arguments, final boolean trapThrowable) throws Throwable {

        List<String> messages = new ArrayList<String>();

        for (Method method : mainClass.getMethods()) {
            if (method.getName().equals("main")) {
                if (Modifier.isStatic(method.getModifiers())) {
                    if (method.getParameterTypes().length == 2 && method.getParameterTypes()[0].equals(Class.class) && method.getParameterTypes()[1].equals(String[].class)) {
                        // Found myself! I.e. found this method
                        // StandaloneAppWrapper#main(String[])
                        continue;
                    }
                    if (method.getParameterTypes().length == 1 && method.getParameterTypes()[0].equals(String[].class)) {
                        // Found myself! I.e. found this method
                        // StandaloneAppWrapper#main(Class, String[])
                        continue;
                    }
                }
                if (method.getParameterTypes().length != 1) {
                    messages.add("Method main in class " + mainClass.getName() + " should have only one argument.");
                    continue;
                }
                if (!method.getParameterTypes()[0].isInterface()) {
                    messages.add("The type of the argument of the method main of the class " + mainClass.getName() + " should be an interface.");
                    continue;
                }
                if (!Options.class.isAssignableFrom(method.getParameterTypes()[0])) {
                    messages.add(String.format("The parameter of the main method should be of an interface that extends %s.", Options.class.getName()));
                    continue;
                }
                // Main method found.
                try {
                    Options<?> options;

                    try {
                        options = buildOptionsObject(method.getParameterTypes()[0], arguments);
                        systemOptions = options;
                    } catch (MandatorySimpleArgumentNotFoundException e) {
                        System.err.println(e.getLocalizedMessage());
                        HTLogger.log(e);
                        return;
                    } catch (OptionValuesException e) {
                        System.err.println(e.getLocalizedMessage());
                        HTLogger.log(e);
                        return;
                    }

                    Object instance = null;

                    if (!Modifier.isStatic(method.getModifiers())) {
                        Constructor<?> constructor;
                        try {
                            constructor = mainClass.getConstructor();
                        } catch (NoSuchMethodException e) {
                            messages.add(String.format("Class %s should have a default (empty) constructor.", mainClass.getName()));
                            continue;
                        }
                        instance = constructor.newInstance();
                    }

                    try {
                        
                        //
                        // !!! INVOKING THE MAIN METHOD !!!
                        //
                        
                        method.invoke(instance, options);
                        
                    } catch (InvocationTargetException e) {
                        if(e.getCause() instanceof NonExceptionalExit)
                            throw e.getCause();
                        if(!options.exit(e.getCause())) {
                            /*
                             * No exit status constant caught the throwable, so
                             * we print the throwable's localized message.
                             */
                            messages.add(e.getCause().getLocalizedMessage());
                            printExceptionMessage(messages, e);
                            if(!trapThrowable) {
                                throw e.getCause();
                            }
                        }
                    }

                } catch (InvalidOptionsInterfaceException e) {
                    messages.add(String.format("Invalid options interface %s. Check the error trace for details:", method.getParameterTypes()[0].getName()));
                    printExceptionMessage(messages, e);
                } catch (IllegalArgumentException e) {
                    printExceptionMessage(messages, e);
                } catch (SecurityException e) {
                    printExceptionMessage(messages, e);
                } catch (InstantiationException e) {
                    printExceptionMessage(messages, e);
                } catch (IllegalAccessException e) {
                    printExceptionMessage(messages, e);
                } catch (InvocationTargetException e) {
                    printExceptionMessage(messages, e);
                }
            }
        }

        if (messages.size() > 0) {
            System.err.println("There was an error executing the main class. This sort of error refers to the structure of the main class, instead of it's execution. Therefore, this error message should not appear when you distribute this application to the final user. Maybe the following can provide some hints on how to solve the problem:");
            for (String problem : messages) {
                System.err.println(problem);
            }
        }
    }

    private void printExceptionMessage(List<String> messages, Exception e) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter pw = new PrintWriter(baos);
        e.printStackTrace(pw);
        pw.flush();
        try {
            baos.flush();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        messages.add(baos.toString());
    }

    /**
     * Ready to be overridden in a subclass.
     * 
     * @param mainClass
     *            The options interface type.
     * @param arguments
     *            The arguments, as received from the command line.
     * @return The options object.
     * @throws InvalidOptionsInterfaceException
     *             If the options interface is invalid. There are many reasons
     *             why an options interface may be invalid. For a detailed
     *             description on the conditions for an options interface to be
     *             invalid, please refer to
     *             {@link InvalidOptionsInterfaceException}
     * @throws OptionValuesException
     *             If there is anything wrong with the arguments passed. For an
     *             in depth description, please refer to
     *             {@link OptionsExtractor#options(UserInput, String...)} .
     * @throws OptionsExtractorException 
     */
    @SuppressWarnings("unchecked")
    protected Options buildOptionsObject(final Class<?> mainClass, final String[] arguments) throws InvalidOptionsInterfaceException, OptionValuesException, OptionsExtractorException {
        OptionsExtractor<?> optionsExtractor = new OptionsExtractor(mainClass);
        Options result = optionsExtractor.options(arguments);
        return result;
    }

    private static Options<?> systemOptions;

    /**
     * Gives system-wide access to the options object. If you did not use the
     * {@link StandAloneAppWrapper} to start your application, this method will
     * return null. Avoid using this method if you use the {@link StandAloneAppWrapper}
     * for any purpose else than running the main class.
     * 
     * @param <E>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <E extends Options<?>> E options() {
        return (E) systemOptions;
    }
}
