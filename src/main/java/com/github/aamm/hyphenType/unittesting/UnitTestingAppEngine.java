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
package com.github.aamm.hyphenType.unittesting;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import com.github.aamm.hyphenType.datastructure.Options;
import com.github.aamm.hyphenType.exceptions.InvalidOptionsInterfaceException;
import com.github.aamm.hyphenType.lexerparser.exceptions.MandatoryMapValueNotFoundException;
import com.github.aamm.hyphenType.lexerparser.exceptions.MandatorySimpleArgumentNotFoundException;
import com.github.aamm.hyphenType.lexerparser.exceptions.MandatoryValueNotFoundException;
import com.github.aamm.hyphenType.lexerparser.exceptions.RegexMismatchException;
import com.github.aamm.hyphenType.lexerparser.exceptions.StringParseException;
import com.github.aamm.hyphenType.lexerparser.exceptions.StringParsingErrorException;
import com.github.aamm.hyphenType.optionsextractor.OptionsExtractorException;
import com.github.aamm.hyphenType.wrapper.StandAloneAppWrapper;

/**
 * @author Aurelio Akira M. Matsui
 */
public final class UnitTestingAppEngine {

    /**
     * TODO write.
     */
    private Class<?> clazz;
    /**
     * TODO write.
     */
    private String[] args;

    /**
     * TODO write.
     */
    private final String[] errs;
    /**
     * TODO write.
     */
    private final String[] outs;

    /**
     * TODO write.
     */
    private int errsIndex = -1;
    /**
     * TODO write.
     */
    private int outsIndex = -1;

    /**
     * TODO write.
     */
    private NonExceptionalExit nonExceptionalExit = null;

    /**
     * TODO write.
     */
    private Throwable throwable = null;

    /**
     * @param appClass
     * @param arguments
     * @throws InvalidOptionsInterfaceException
     * @throws InvalidOptionException
     */
    public UnitTestingAppEngine(final Class<?> appClass, final String... arguments) throws InvalidOptionsInterfaceException {
        this(appClass, new UnitTestingUserInput(), arguments);
    }

    /**
     * @param appClass
     * @param input
     * @param arguments
     * @throws InvalidOptionsInterfaceException
     * @throws InvalidOptionException
     */
    public UnitTestingAppEngine(final Class<?> appClass, final UnitTestingUserInput input, final String... arguments) throws InvalidOptionsInterfaceException {

        clazz = appClass;
        args = arguments;

        PrintStream errBkp = System.err;
        ByteArrayOutputStream errBaos = new ByteArrayOutputStream();
        PrintStream err = new PrintStream(errBaos);
        System.setErr(err);

        PrintStream outBkp = System.out;
        ByteArrayOutputStream outBaos = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outBaos);
        System.setOut(out);

        UnitTestingAppWrapper appWrapper = new UnitTestingAppWrapper(input);
        try {
            appWrapper.invokeMain(appClass, arguments, false);
        } catch (NonExceptionalExit e) {
            System.out.println("!!!!");
            this.nonExceptionalExit = e;
        } catch (Throwable e) {
            throwable = e;
        }

        err.flush();
        out.flush();
        errs = errBaos.toString().split("\n");
        outs = outBaos.toString().split("\n");

        System.setErr(errBkp);
        System.setOut(outBkp);
    }

    /**
     * @return
     */
    public String err() {
        if (errsIndex + 1 < errs.length) {
            errsIndex++;
            return errs[errsIndex];
        }
        return null;
    }

    /**
     * @return
     */
    public String out() {
        if (outsIndex + 1 < outs.length) {
            outsIndex++;
            return outs[outsIndex];
        }
        return null;
    }

    /**
     * @return
     */
    public Enum<?> getStatusCodeEnum() {
        return nonExceptionalExit.getStatusCodeEnum();
    }

    /**
     * @return True means something called {@link Options#exit(Enum)}.
     */
    public boolean exitEnumCalled() {
        return nonExceptionalExit != null && nonExceptionalExit.exitEnumCalled();
    }

    /**
     * @return
     */
    public int getStatusCodeInt() {
        return nonExceptionalExit.getStatusCodeInt();
    }

    /**
     * @return True means something called {@link Options#exit(int)}.
     */
    public boolean exitIntCalled() {
        return nonExceptionalExit != null && nonExceptionalExit.exitIntCalled();
    }

    /**
     * Retrieves at which point the application under test called
     * {@link Options#exit(Enum)} or {@link Options#exit(int)}.
     * 
     * @return The stack trace element that called {@link Options#exit(Enum)} or
     *         {@link Options#exit(int)}.
     */
    public StackTraceElement exitCallPoint() {
        return nonExceptionalExit.exitCallPoint();
    }

    /**
     * Writes the output written to stdout to a file.
     * 
     * @param fileName
     *            The file name to save output to.
     */
    public void saveStdoutInteraction(final String fileName) {
        try {
            FileOutputStream fos = new FileOutputStream(new File(fileName));
            fos.write(("_> java " + clazz.getName()).getBytes());
            for (String arg : args) {
                fos.write((" " + arg).getBytes());
            }
            fos.write("\n".getBytes());
            for (String out : outs) {
                fos.write((out + "\n").getBytes());
            }
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Writes the output written to stdout to a file using HTML snippet format.
     * 
     * @param fileName
     *            The file name to save output to.
     */
    public void saveStdoutInteractionHTML(final String fileName) {
        saveStdoutInteractionHTML(fileName, "", "");
    }
    
    /**
     * Writes the output written to stdout to a file using HTML snippet format.
     * 
     * @param fileName
     *            The file name to save output to.
     * @param prefix
     *            The string to add at the beginning of the generated HTML
     * @param sufix
     *            The string to add at the end of the generated HTML
     */
    public void saveStdoutInteractionHTML(final String fileName, final String prefix, final String sufix) {
        try {
            FileOutputStream fos = new FileOutputStream(new File(fileName));
            fos.write(prefix.getBytes());
            fos.write(("<strong>_> java " + clazz.getName()).getBytes());
            for (String arg : args) {
                fos.write((" " + arg).getBytes());
            }
            fos.write("</strong>\n".getBytes());
            for (String out : outs) {
                fos.write(out.getBytes());
                fos.write("\n".getBytes());
            }
            fos.write(sufix.getBytes());
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * If the execution of the application under test threw any
     * {@link Throwable} that was not caught by any exit status constant,
     * this method will return this {@link Throwable}.
     * 
     * @return The {@link Throwable} that was possibly thrown during the
     *         execution of the application. Returns null if no throwable
     *         was thrown and uncaught.
     */
    public Throwable uncaughtThrowable() {
        return throwable;
    }

    /**
     * An application wrapper especially designed for unit testing. This
     * application wrapper prevents calls to {@link Options#exit(Enum)} or
     * {@link Options#exit(int)} to actually exit the JVM. Instead, the
     * replacement will simply log these calls.
     * 
     * @author akira
     * @see UnitTestingOptionExtractor
     */
    private class UnitTestingAppWrapper extends StandAloneAppWrapper {

        /**
         * The emulated user input.
         */
        private UnitTestingUserInput input;

        /**
         * @param input
         *            The mock user input to emulate user inputs when some
         *            argument was not passed in command line.
         */
        UnitTestingAppWrapper(final UnitTestingUserInput input) {
            this.input = input;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected Options<?> buildOptionsObject(final Class<?> clazz, final String[] arguments) throws InvalidOptionsInterfaceException, StringParsingErrorException, MandatoryValueNotFoundException, RegexMismatchException, StringParseException, MandatoryMapValueNotFoundException, MandatorySimpleArgumentNotFoundException, OptionsExtractorException {
            UnitTestingOptionExtractor<?> optionsExtractor = new UnitTestingOptionExtractor(clazz);
            return optionsExtractor.options(input, arguments);
        }
    }
}
