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
package com.github.aamm.hyphenType.documentation.lib;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.StringTokenizer;

import com.github.aamm.hyphenType.datastructure.Options;
import com.github.aamm.hyphenType.datastructure.parser.option.StructureOption;
import com.github.aamm.hyphenType.datastructure.parser.option.StructureOptionArgument;
import com.github.aamm.hyphenType.datastructure.parser.simple.StructureSimpleArgument;
import com.github.aamm.hyphenType.documentation.Description;
import com.github.aamm.hyphenType.documentation.DocumentationFormatterEngine;
import com.github.aamm.hyphenType.exit.StatusCode;
import com.github.aamm.hyphenType.lexerparser.LexerParser;
import com.github.aamm.hyphenType.util.soc.StringParsingError;

/**
 * A documentation formatter inspired on the standard Unix way to show
 * documentation in the command line. This documentation is typically invoked
 * in Unix-like systems using --help or -h.
 * 
 * @author Aurelio Akira M. Matsui
 * @param <T>
 */
public class StandardFormatterEngine<T extends Options<?>> extends DocumentationFormatterEngine<T, StandardFormatterEngine.StandardFormatter> {

    /**
     * Annotation to configure the {@link StandardFormatterEngine}.
     * 
     * @author Aurelio Akira M. Matsui
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface StandardFormatter {

        /**
         * The default value of the property {@link StandardFormatter#descriptionIndent()}.
         */
        final int DEFAULT_DESCRIPTION_INDENT = 20;
        
        /**
         * The default value of the property {@link StandardFormatter#maxColumn()}.
         */
        final int DEFAULT_MAX_COLUMN = 80;
        
        /**
         * The label that represents the executing program.
         */
        @Description("The label that represents the executing program.")
        String program() default "program";

        /**
         * The "usage" label.
         */
        @Description("The \"usage\" label.")
        String usage() default "usage";

        /**
         * The "options" label.
         */
        @Description("The \"options\" label.")
        String options() default "options";

        /**
         * The "Status code" label.
         */
        @Description("The \"Status code\" label.")
        String statusCode() default "Status code";

        /**
         * Whether lines should only break at spaces or if lines can break in the middle of a word.
         */
        @Description("Whether lines should only break at spaces or if lines can break in the middle of a word.")
        boolean lineBreakAtSpace() default true;

        /**
         * The number of characters before the description text.
         */
        @Description("The number of characters before the description text.")
        int descriptionIndent() default DEFAULT_DESCRIPTION_INDENT;

        /**
         * The maximum number of columns before a line break. This value should be larger than descriptionIndent.
         */
        @Description("The maximum number of columns before a line break.\nThis value should be larger than descriptionIndent.")
        int maxColumn() default DEFAULT_MAX_COLUMN;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final void printDocumentation(final PrintStream pw, final LexerParser<T> lexPar, final StandardFormatter annotation) {

        try {

            pw.format("%s: %s ", annotation.usage(), annotation.program());
            if (lexPar.getParsedOptions().size() != 0) {
                pw.format("[%s] ", annotation.options());
            }

            for (StructureSimpleArgument simpleArgument : lexPar.getSimpleArguments()) {
                if (!simpleArgument.isMandatory()) {
                    pw.print("[");
                }
                pw.print(getOptionsInterfaceValue(simpleArgument.getName(), simpleArgument.getName()));
                if (simpleArgument.method.getReturnType().isArray()) {
                    pw.print("...");
                }
                if (!simpleArgument.isMandatory()) {
                    pw.print("]");
                }
                pw.print(" ");
            }

            pw.println();
            pw.println(print("", getOptionsInterfaceValue("", lexPar.getArgsObject().description()), 0, 0, annotation.maxColumn(), annotation.lineBreakAtSpace()));

            String simpleArgumentDescriptions = "";
            for (StructureSimpleArgument simpleArgument : lexPar.getSimpleArguments()) {
                simpleArgumentDescriptions += print(simpleArgument.getName(), simpleArgument.getDescription(), 2, annotation.descriptionIndent(), annotation.maxColumn(), annotation.lineBreakAtSpace()) + "\n";
            }
            if (simpleArgumentDescriptions.length() > 0) {
                pw.println();
                pw.println("Arguments");
                pw.println();
                pw.print(simpleArgumentDescriptions);
            }

            pw.println();
            pw.println("Options");
            pw.println();

            for (StructureOption parsedOption : lexPar.getParsedOptions()) {
                String optionName = "";
                for (String alternative : parsedOption.alternatives) {
                    if (optionName.contains(lexPar.getArgsObject().singleHyphen())) {
                        optionName += ", ";
                    }
                    if (lexPar.getArgsObject().doubleHyphenInLongOptions() && alternative.length() > 1) {
                        optionName += lexPar.getArgsObject().doubleHyphen();
                    } else {
                        optionName += lexPar.getArgsObject().singleHyphen();
                    }
                    optionName += alternative;
                    if (parsedOption.value != null) {
                        if (!parsedOption.value.mandatory) {
                            optionName += "[";
                        }
                        optionName += lexPar.getArgsObject().equals() + parsedOption.value.name;
                        if (!parsedOption.value.mandatory) {
                            optionName += "]";
                        }
                    }
                }

                for (StructureOptionArgument argument : parsedOption.arguments) {
                    optionName += " ";
                    if (!argument.isMandatory()) {
                        optionName += "[";
                    } else {
                        optionName += "<";
                    }
                    optionName += argument.getName();
                    if (argument.method.getReturnType().isArray()) {
                        optionName += "...";
                    }
                    if (!argument.isMandatory()) {
                        optionName += "]";
                    } else {
                        optionName += ">";
                    }
                }

                String optionDocumentation = getOptionsInterfaceValue(parsedOption.method.getName(), parsedOption.description);
                pw.println(print(optionName, optionDocumentation, 2, annotation.descriptionIndent(), annotation.maxColumn(), annotation.lineBreakAtSpace()));
            }

            if (lexPar.getArgsObject().documentStatusCodes()) {

                pw.println();
                pw.println(annotation.statusCode());

                for (StatusCode statusCode : lexPar.getArgsObject().statusCodeEnum().getEnumConstants()) {
                    Enum<?> enumConstant = Enum.valueOf((Class<? extends Enum>) lexPar.getArgsObject().statusCodeEnum(), statusCode.toString());
                    pw.println(print(Integer.toString(enumConstant.ordinal()), getStatusCodeUserDescription(enumConstant), 2, annotation.descriptionIndent(), annotation.maxColumn(), annotation.lineBreakAtSpace()));
                }
            }
        } catch (StringParsingError e) {
            e.printStackTrace();
        }
    }

    /**
     * Generates a string that contains the textual representation of a title
     * and a message, formatting using a certain indent and a maximum column.
     * Format is as follows (hyphens are used to represent white spaces):
     * 
     * <pre>
     * --Title-------Text-text-text
     * --------------text-more-text
     * --------------and-even-more-
     * --------------text.
     * </pre>
     * 
     * @param title The title of the group of lines.
     * @param description The description that accompanies the title.
     * @param titleIndent The indent of the title, in characters, relative to the left margin.
     * @param descriptionIndent The indent of the description, in characters, relative to the left margin.
     * @param maxColumn The maximum column to print characters.
     * @param lineBreakAtSpace Whether lines should break always at a space character (creating a dented effect), or at any character (making the block look like a rectangle).
     * @return A string representation of the title and description, formatted according with descriptionIndent, maxColumn, and lineBreakAtSpace.
     */
    public final String print(final String title, final String description, final int titleIndent, final int descriptionIndent, final int maxColumn, final boolean lineBreakAtSpace) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream pw = new PrintStream(baos);

        String line = space(titleIndent) + title;
        if (2 + title.length() > descriptionIndent - 1) {
            pw.println(line);
            line = space(descriptionIndent);
        } else {
            line += space(descriptionIndent - line.length());
        }

        if (lineBreakAtSpace) {
            StringTokenizer st = new StringTokenizer(description.trim());
            while (st.hasMoreTokens()) {
                String word = st.nextToken();
                if (line.length() + word.length() > maxColumn) {

                    if (word.length() > maxColumn - descriptionIndent) {
                        while (word.length() > maxColumn - descriptionIndent) {
                            // Word is larger than the space available for text.
                            // We have no other choice but to cut the word.
                            int removedRange = maxColumn - line.length();
                            line += word.substring(0, removedRange);
                            pw.println(line);
                            word = word.substring(removedRange, word.length());
                            line = space(descriptionIndent);
                        }
                        if (word.length() > 0) {
                            line += word;
                        }
                        if (st.hasMoreTokens() && line.length() + 1 <= maxColumn) {
                            line += " ";
                        }
                    } else {
                        pw.println(line);
                        line = space(descriptionIndent) + word;
                        if (st.hasMoreTokens() && line.length() + 1 <= maxColumn) {
                            line += " ";
                        }
                    }
                } else {
                    line += word;
                    if (st.hasMoreTokens() && line.length() + 1 <= maxColumn) {
                        line += " ";
                    }
                }
            }
        } else {
            for (char b : description.toCharArray()) {
                if (line.equals("")) {
                    line = space(descriptionIndent);
                    pw.println();
                }
                line += b;
                if (line.length() == maxColumn) {
                    pw.print(line);
                    line = "";
                }
            }
        }
        if (line.length() > descriptionIndent) {
            pw.print(line);
        }
        return new String(baos.toByteArray());
    }

    /**
     * Creates a string with n space characters.
     * 
     * @param n How many space characters in the string.
     * @return A string with n space characters.
     */
    private String space(final int n) {
        String s = "";
        for (int i = 0; i < n; i++) {
            s += " ";
        }
        return s;
    }
}
