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
package org.hyphenType.tests.documentation;

import java.io.PrintStream;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.hyphenType.datastructure.Options;
import org.hyphenType.documentation.DocumentationFormatterEngine;
import org.hyphenType.exit.StatusCode;
import org.hyphenType.lexerparser.LexerParser;
import org.hyphenType.util.soc.StringParsingError;

/**
 * @author Aurelio Akira M. Matsui
 */
public class DummyFormatterEngine<T extends Options<?>> extends DocumentationFormatterEngine<T, DummyFormatterEngine.DummyFormatter> {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface DummyFormatter {

	String propertyA();

	String propertyB() default "ABC";

	String propertyC();

	String propertyD() default "ze";
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void printDocumentation(PrintStream pw, LexerParser<T> parser, DummyFormatter annotation) {
	pw.format("propertyA = %s, propertyB = %s, propertyC = %s, propertyD = %s;", annotation.propertyA(), annotation.propertyB(), annotation.propertyC(), annotation.propertyD());
	
        if (parser.getArgsObject().documentStatusCodes()) {

            for (StatusCode statusCode : parser.getArgsObject().statusCodeEnum().getEnumConstants()) {
                Enum<?> enumConstant = Enum.valueOf((Class<? extends Enum>) parser.getArgsObject().statusCodeEnum(), statusCode.toString());
                String entry = "";
                try {
                    entry = " " + enumConstant.ordinal() + "=" + getStatusCodeUserDescription((Enum)statusCode);
                } catch (StringParsingError e) {
                    e.printStackTrace();
                }
                pw.print(entry);
            }
        }
    }
}
