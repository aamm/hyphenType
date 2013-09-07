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
package org.hyphenType.documentation.lib;

import java.io.PrintStream;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.hyphenType.datastructure.Options;
import org.hyphenType.datastructure.parser.option.StructureOption;
import org.hyphenType.datastructure.parser.option.StructureOptionArgument;
import org.hyphenType.datastructure.parser.simple.StructureSimpleArgument;
import org.hyphenType.documentation.DocumentationFormatterEngine;
import org.hyphenType.exit.StatusCode;
import org.hyphenType.lexerparser.LexerParser;
import org.hyphenType.util.soc.StringParsingError;

/**
 * A documentation formatter that generates HTML code as output. Although there
 * is no specific method in this formatter to redirect the output to a file,
 * saving the output of this formatter
 * 
 * @author Aurelio Akira M. Matsui
 * @param <T>
 */
public class HTMLFormatterEngine<T extends Options<?>> extends DocumentationFormatterEngine<T, HTMLFormatterEngine.HTMLFormatter> {

    /**
     * @author Aurelio Akira M. Matsui
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface HTMLFormatter {
        /**
         * 
         */
        boolean bodyOnly() default false;

        /**
         *
         */
        boolean addW3CLogo() default false;

        /**
         *
         */
        boolean addHyphenTypeLogo() default false;

        /**
         *
         */
        String colorA() default "#DDDDDD";

        /**
         * 
         */
        String colorB() default "#FFFFDD";

        /**
         *
         */
        String usage() default "usage";

        /**
         * 
         */
        String options() default "options";

        /**
         * 
         */
        String option() default "option";

        /**
         * 
         */
        String statusCode() default "status code";

        /**
         * 
         */
        String meaning() default "Meaning";
    }

    @SuppressWarnings("unchecked")
    @Override
    public final void printDocumentation(final PrintStream pw, final LexerParser<T> lexPar, final HTMLFormatter annotation) {

        try {

            if (!annotation.bodyOnly()) {
                pw.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
                pw.println("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">");
                pw.println("\t<head>");
                pw.println("\t\t<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\" />");
                pw.println("\t\t<title>Command line documentation</title>");
                pw.println("\t\t<style type=\"text/css\">");
                pw.println("\t\t\tbody {font-family:Verdana;}");
                pw.println("\t\t\ttr.r0 {");
                pw.println("\t\t\t\tbackground-color: " + annotation.colorA() + ";");
                pw.println("\t\t\t}");
                pw.println("\t\t\ttr.r1 {");
                pw.println("\t\t\t\tbackground-color: " + annotation.colorB() + ";");
                pw.println("\t\t\t}");
                pw.println("\t\t\timg {border: none}");
                pw.println("\t\t</style>");
                pw.println("\t</head>");
                pw.println("\t<body>");
            }

            pw.println("\t\t<p>" + annotation.usage() + ": program [" + annotation.options() + "] ");

            pw.print("\t\t");

            for (StructureSimpleArgument simpleArgument : lexPar.getSimpleArguments()) {
                if (!simpleArgument.isMandatory()) {
                    pw.print("\t\t[");
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

            pw.println("<br/>");
            pw.println("\t\t\t<br/>");
            pw.println("\t\t\t" + getOptionsInterfaceValue("", lexPar.getArgsObject().description()));
            pw.println("\t\t\t<br/>");
            pw.println("\t\t\t<br/>");
            pw.println("\t\t</p>");
            pw.println("\t\t<table cellspacing=\"0\" rules=\"rows\" frame=\"hsides\">");
            pw.println("\t\t\t<tr>");
            pw.println("\t\t\t\t<td>");
            pw.println("\t\t\t\t\t<strong>" + annotation.option() + "</strong>");
            pw.println("\t\t\t\t</td>");
            pw.println("\t\t\t\t<td>");
            pw.println("\t\t\t\t\t&nbsp;&nbsp;&nbsp;&nbsp;");
            pw.println("\t\t\t\t</td>");
            pw.println("\t\t\t\t<td>");
            pw.println("\t\t\t\t\t<strong>" + annotation.meaning() + "</strong>");
            pw.println("\t\t\t\t</td>");
            pw.println("\t\t\t</tr>");

            int x = 0;

            for (StructureOption parsedOption : lexPar.getParsedOptions()) {

                x = (x + 1) % 2;

                pw.println("\t\t\t<tr class=\"r" + x + "\">");
                pw.println("\t\t\t\t<td>");

                String entry = "  ";
                for (String alternative : parsedOption.alternatives) {
                    if (entry.contains(lexPar.getArgsObject().singleHyphen())) {
                        entry += ", ";
                    }
                    if (lexPar.getArgsObject().doubleHyphenInLongOptions() && alternative.length() > 1) {
                        entry += lexPar.getArgsObject().doubleHyphen();
                    } else {
                        entry += lexPar.getArgsObject().singleHyphen();
                    }
                    entry += alternative;
                    if (parsedOption.value != null) {
                        if (!parsedOption.value.mandatory) {
                            entry += "[";
                        }
                        entry += lexPar.getArgsObject().equals() + parsedOption.value.name;
                        if (!parsedOption.value.mandatory) {
                            entry += "]";
                        }
                    }
                }

                for (StructureOptionArgument argument : parsedOption.arguments) {
                    entry += " ";
                    if (!argument.isMandatory()) {
                        entry += "[";
                    } else {
                        entry += "&lt;";
                    }
                    entry += argument.getName();
                    if (argument.method.getReturnType().isArray()) {
                        entry += "...";
                    }
                    if (!argument.isMandatory()) {
                        entry += "]";
                    } else {
                        entry += "&gt;";
                    }
                }

                pw.println("\t\t\t\t\t<code>" + entry + "</code>");
                pw.println("\t\t\t\t</td>");
                pw.println("\t\t\t\t<td>");
                pw.println("\t\t\t\t\t&nbsp;&nbsp;");
                pw.println("\t\t\t\t</td>");
                pw.println("\t\t\t\t<td>");

                String optionDocumentation = getOptionsInterfaceValue(parsedOption.method.getName(), parsedOption.description);
                pw.println("\t\t\t\t\t" + optionDocumentation);
                pw.println("\t\t\t\t</td>");
                pw.println("\t\t\t</tr>");
            }

            pw.println("\t\t</table>");

            if (lexPar.getArgsObject().documentStatusCodes()) {

                pw.println("\t\t<p><br/><br/></p>");
                pw.println("\t\t<table cellspacing=\"0\" rules=\"rows\" frame=\"hsides\">");
                pw.println("\t\t\t<tr>");
                pw.println("\t\t\t\t<td>");
                pw.println("\t\t\t\t\t<strong>" + annotation.statusCode() + "</strong>");
                pw.println("\t\t\t\t</td>");
                pw.println("\t\t\t\t<td>");
                pw.println("\t\t\t\t\t&nbsp;&nbsp;&nbsp;&nbsp;");
                pw.println("\t\t\t\t</td>");
                pw.println("\t\t\t\t<td>");
                pw.println("\t\t\t\t\t<strong>" + annotation.meaning() + "</strong>");
                pw.println("\t\t\t\t</td>");
                pw.println("\t\t\t</tr>");
                pw.println();

                for (StatusCode statusCode : lexPar.getArgsObject().statusCodeEnum().getEnumConstants()) {
                    Enum<?> enumConstant = Enum.valueOf((Class<? extends Enum>) lexPar.getArgsObject().statusCodeEnum(), statusCode.toString());

                    x = (x + 1) % 2;

                    pw.println("\t\t\t<tr class=\"r" + x + "\">");
                    pw.println("\t\t\t\t<td align=\"right\">");
                    pw.println("\t\t\t\t\t" + enumConstant.ordinal());
                    pw.println("\t\t\t\t</td>");
                    pw.println("\t\t\t\t<td>");
                    pw.println("\t\t\t\t\t&nbsp;&nbsp;");
                    pw.println("\t\t\t\t</td>");
                    pw.println("\t\t\t\t<td>");
                    pw.println("\t\t\t\t\t" + getStatusCodeUserDescription(enumConstant));
                    pw.println("\t\t\t\t</td>");
                    pw.println("\t\t\t</tr>");
                }
                pw.println("\t\t</table>");
            }

            pw.println("\t\t<p>");

            if (annotation.addW3CLogo()) {
                pw.println("\t\t\t<a href=\"http://validator.w3.org/check?uri=referer\"><img src=\"data:image/png;base64," + "iVBORw0KGgoAAAANSUhEUgAAAFgAAAAfCAMAAABUFvrSAAADAFBMVEUBAAAIBgMMCQQLCwsSDgYXEgkaFQoUFBQcHB" + "wlHQ4rIhEwJhM2KxU7Lxc9MBgjIyMoKCg0MzI7OztBNBpOPh5OSz9TQiFeSyVkUChqVCpuWCx1Rzl0XC5+ZDFOTk5S" + "T0tWUk1VVVVcXFxqXUplZWVra2twcHB7e3sAWpwIX58JYJ8OY6ERZaIVaKQaa6Yebaghb6klcqopdawxea82fbE4fr" + "I+grRFhrZMi7pVkb1alL9dlsBkm8NoncRtocZ0pcl8qsyWBAOZAACcCASIIBmdNiegDge3PB6mPiqBZzODaDSKbjaO" + "cTiUdjuXeDyaez26QiG/TCaAeWihclPATifBUCjIXy/QbzfWej3Yfj+KgWWjgkGui0WxjUa0gVi3kkm6lEq+mEyjkm" + "uzn2e7pnLFnU7eikXOpFHRp1PVqlXarlfesVjjlUrmm03poFDtqVTitFrmuFzqu13ywWD7xWL+y2WDg4OXl5eYmJip" + "pZ61rZisrKy0tLS/v7+Crs6FsNCLtNKQt9SWu9aYvNebvtifwdqjw9uox92ryd6yzeG20OPCwsLMzMzU1NTA1ufG2u" + "jN3+zT4+7d6fL/7Mbn5+fs7Ozl7vXp8fbu9Pj09PTy9/r1+Pv+/v7MzMwAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" + "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
                        + "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" + "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" + "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADn" + "4rtgAAAAnnRSTlP///////////////////////////////////////////////////////////////////////////" + "//////////////////////////////////////////////////////////////////////////////////////////" + "////////////////////////////////////////////AD6H4/gAAAABYktHRP+lB/LFAAAACXBIWXMAAABIAAAASA" + "BGyWs+AAADSUlEQVRIx+2WaU8TURSGzyCLQEUxiFYtCEo8Da3sYglQbDUaluISV6pYFdsyop1Or8ElSgg2RSjgArSZ" + "63/13JkOXRBZgn7yTXpmyzwz857lFhb3XW9/CsEi317f2c71/OnZ/+C9gSdDKmNySI2GorQNxiaDpFA4FgrS+WgoxF" + "hwck/gMTjBwmCJ+WCYMQso5SBUoQCcZKwGgClg2QSOt7jp7pnWGQFJ2e0tpFYX5+k3TUn+QTOsqAV/hRRmJngiYAVr" + "YILAkipT+C34GzqJ+AW7BDiBhga4VgdwIFmdNMBKMcAQI3Ctz1cOCmPD4hEKVBX5TpdYfg/m7bhC0YHrFHvQtbY2h8"
                        + "40fw9Hzh8tO7CaSZ6V3k2AdWXBNdaKosGtwG78SLED4xSd+I1rbWK37ii5cO58pirCUglYBXhIjVlywWGQoluBl7CT" + "83VE8nUZ28lu3ZUycUlLZ8CVUrgGnpgeR7JgZrUxHVzh9/tjAuzxbFRFM7nwGbGZ8wF083UHLlPqDuZWxSAZrBSXqm" + "byGBuBEQNM0sFCUQJf8QiyAXbRp7uwA5fI7iXei9ifotzlgiMy3R+NqKpMRiv6gUwVzfTAlAiLyUJkxVWPUAYcR5fW" + "fCGB7jV00GOciJ0an95T5133GDLAKXTMY1/K3j6HPeJ42YEJvrAX8M0M1+y8LvJhnnejURmc92HvH2aFGtuWa4I/Id" + "o1qgbElH48j235YNkiwbhxc6QKpJN56NiN18bOnQ2uCV5BvCRaEC8aqAS2rBp76bSRPFutCT4No8chkMu95vG+ynIv" + "e3KHUAvO6i04w2d7dCu6Fwxy00LGCpsJrixlQbAVfL/35Qb3VN5068M1vQW/UrG5U3EHxrXGH4IrJTfAxluqUMWicC" + "jLva/zvC8Mf72388fmfIeIS61UIU60o2jFxbKGxsMwzQvfuKScyTREcxz2ZOV9XDCPtY/6ZoB+X1/udzl6Rw2RDWfV" + "7ngceonFxXIqMgj83eXey3Fe7HfRh0bAlondZQJKgXM2rsrsmd2rXK4j6ZHx8PMAmhM/y2CO1oH7vmdx9X0wfZLj7v" + "0q/8Orcv7D8T039s/8VH7bVu2c718NbJrhuBzq2leo3nTlTX62DfwGZkKiGotSUXQAAAABJRU5ErkJggg==\" " + "alt=\"Valid XHTML 1.0 Strict\" height=\"31\" width=\"88\" /></a>");
            }

            if (annotation.addHyphenTypeLogo()) {
                pw.println("\t\t\t<a href=\"http://hyphenType.org\"><img alt=\"hyphenType\" src=\"data:image/png;base64," + "iVBORw0KGgoAAAANSUhEUgAAAFQAAAAkCAYAAAAXSR0AAAAABHNCSVQICAgIfAhkiAAAAAlwSFlzAAAEnQAABJ0BfD" + "RroQAAABl0RVh0U29mdHdhcmUAd3d3Lmlua3NjYXBlLm9yZ5vuPBoAAA15SURBVGiB7Zp5UBdXtsc/v/6xaNgk4rDK" + "T0UT/ImIigsOyUDUjHF5SHADgxj35cUk46QkMS9xcFIPLevlaWb8GTNJZUajMWJiKUoIgkkUpBRHVBR3ERUkrggosp" + "33R0NDsyeaMpN536pbt/ts9/bt0+ee2/caVqxYIf7+/gAYaAQRPb32vk1ac7ot0dqgN6QZGrXXGr1FWuM+t9Nee2TT" + "tm3DkJSUJGazmYyMjCbCv1Z0ys/H8cqVR2av64QJdH32WaKHD8cKICMjg8jIyEfWwC8d8cCSR2hv/717dH32WRRFQX" + "mEdn+RiIqKIjU1tWUBNzfIy1Prh4TRaPz1D6i9vT1eXl4tCxiNYDKBldVDt9XEQ21sbDAajRgMBnr27Ennzp2bVXR2" + "dsZsNmNjY6Ojd+jQAYOhfhqxtrZGUeqbsLKywtraWqfj6OiIn58fdnZ2Orq1tTVWDR6yoV0Ak8lEz549dfYbtmM2m/" + "Hw8Gi2/y3CxgbMZrC1bdgRtTQnazTqSIrBAElJSbJ582YBJCsrS7Zu3Sp5eXlSVVUlIiLLly8XQADp2LGjbNiwQURE" + "7t69K6WlpRIdHS2AKIoiN2/elAkTJmjyJ0+elLi4OO3+888/l5UrVwogHTp0kI8++kiqq6slLy9PKioqZMmSJTrZDz"
                        + "/8UFatWiXXr1+Xc+fOCSD9+/eXU6dOye3bt+X27dty9uxZ6devn6YXEhIiRUVFUlFRIaWlpVJYWCinT5/W+IDEq/Oy" + "Wjw9RURE/vIXkXv3RKqqRIqLRZ57TuUvWCBy6ZKI0Viv4+YmUlEhMnCgCMi+mTNFRGTuuHHS5PV6eHgQFhaGvb09r7" + "76Km+//TYDBgwAYMmSJQwbNowhQ4bg4uLC4sWL+eSTTzCbzdTU1LB3716Cg4MB8PX1xWQyMXHiRM12UFAQaWlpAMTG" + "xtK/f398fHzo1q0bISEhvPfee9SlcAAxMTFUVlYSERHBSy+9hK2tLdu2bWPTpk24u7vj7u5OSkoKFosFUL19y5YtfP" + "XVV5hMJhwcHFi/fn37vFMEevVSP//kZPj4YzUMbNgAzs4wZky97OzZcPiwWhp6qKLA1408dOHChbq3ee3aNVm6dKkA" + "cv78eYmPj9fx8/PzNS+eP3++HDp0SAB56623JD4+XgoLC8XPz088PDykoqJC7OzsBJArV67I0qVLJSAgQCu5ubmyaN" + "EizUNXr16ta2v06NFSXFwsgwcP1nSioqKkpqZGOnbsKFOnTpXKykpxcnLSdObMmdM+D/XyqqcNGqTS/PzU+7VrRZKS" + "1GsrK5ErV0SiojT5Og/9z/BwaTMS5+Tk4OrqCoC3tzfp6ek6fnp6Ot7e3gCkpqayZs0a7OzsePHFF5k9ezb29vZMmD" + "CBY8eOcfDgQcrKyujQoQOenp6Eh4czcuRIzVZRURFlZWXafU1Nja6tnj17YjAYWLlypY7+/fff4+DggK+vL9nZ2RQX" + "F7f1WK3jxAm1dnWFnBxYuxaOHoUePaB/f9VzExKaqClGI60OqKIoBAQEkJiYCMD169cxm83s3LlTk+nTpw/ffPMNAG"
                        + "fOnKGwsJBJkybRqVMnjhw5whdffIHFYsHOzk773MvLyykuLmbZsmWa7fagqKiI0tJSQkJCmuXfvXuXrl27tttei+jX" + "T60vXlTrnBxIT4e5cyEwENavh4qKJmrG5vLQulnYYDAQHR2No6Mju3btAiAlJYXw8HDs7e0BCAgIoG/fvqSkpGj6aW" + "lpLF++nITaN7h//36cnZ2JjIzUBhRg9+7dxMbG6mZ3b29vzGZzi89Z54nz58/X0UeNGgWoCxRXV1dGjBihPcvgwYNb" + "tKdDXRZhZQVTp8LJk/UDCvDXv8K8efDMM7BuXQsmDE1jaHl5uWRmZsq5c+eksrJSXnnlFS32eHp6yokTJ+TWrVuSnJ" + "ws1dXVYrFYdPEpOjpaREQCAwM12gcffCD3798XW1tbjebm5iaZmZly7do1SUxM1NoODw/XYuj777+vsw3IpEmT5M6d" + "O3L48GFJTEyUvLw8KSoq0vh1WUh2drYUFhZKdnZ2+2JoYaHIzp0iBQUit2+LBAfXy4CItbVIUZHIli16eoMY+kZUlB" + "i+TkqS23fuEBkZSVZWFhs3biQ3N5fOnTtz4MABLjZ8S6i5ZmhoKO7u7hw9epTDjWY6e3t7Bg4cyHfffafRXF1d8fb2" + "5tChQzpZRVEYOnQoPj4+lJWVkZWVRX5+PgBms5kHDx5w/vz5Jp7g4uLC4MGD6dKlC1euXCE9PZ3y8nLNS4YPH46Xlx" + "cZGRmUlJTQtWtXDh48qOnrlp42NjBsGJw/DyEhUFMDqalw7Zq+UWdnuHwZXngB9u3TsfbPnEnw3/5GbHR027P8r7Ho" + "PLS9ZckSkezsZnl1HvrmtGn6PLSkpIQHDx408Yh/exiNEB0Nq1e3LqYo6ixvMpmYPHky62qD7eTJk3/+Tj5GPJGfz4"
                        + "GrV3+c0ujRal2bIjaEQ+1iRFEUrBAhKCiIoKCgh+3nvz2UOg8tLIYjlx93dx4OfW7lYHoS8PN7bH3QEvsjl+GNBOjW" + "WU3HLt74eRp8wgb6eMDhS1Ajj9b2p3kJmLrxeAe0zkPrEBMEtlbw1vafp0HvJ+HT6TDgz/Cg6tHb37NnD3/c3nLnZ8" + "yYwaJFix59w7UwGo1YNdkY+xeGr68vsbWrpEuXLhEbG8u6detwcnICaHUV9ihgMBiaX8v36AIje8Pd+7DzGJQ+AKMC" + "EQPguzNQdLdettdv1LI7B3zdwN0Jzv6g6tt3gG9OwulGObKNFYz1B49OkHwCzhTp+d1d4LmnodMTcPwqpOSibUK+4A" + "enrqntDOoGOVfh2zMqz8vLiylTpgCQnZ1NbGwsYWFhuLm5UVBQwM6dO+ndu7fuJ3dqaiqKohAaGkpycjKdOnVCURR2" + "796Nra0t06ZN0/2oLi8v57PPPiM3NxdPT0+mTZum/Yhvdi0/tAdYosDUGcIC4OtXwcoI1TUw2g9eHqaX/+Pz4OepXg" + "f3hPfGw9+ng9kdxvjBppnqgDfEltnwvBm8OsEXc2B033re756CzbPUWCsCsaPgDyPq+QtD4MOX4I3nwcVe5a9uR5bn" + "7OzMm2++yZdffqnRKioqiIqK4sYNddKwWCxEREQQGRnJ2bNnWblyJUOGDNFy8+LiYgIDA7FYLDg4OLB9+3b8/f2prK" + "wEmomhAPm3YN5nUF6peuXeP8AgExy4AJsPwZ/Gwf+mqnwvZwjqAX/eXa9/oxSmfgwl5ar+x9Mg5GnVa+uwOAFyC9Xr" + "q3fg+d6w+7gq/84YWJUCCbUr2uST8MEU+J899V6alAPv71Gve/1GfSnH29gd69ixIy+//DIWi0XLs7du3Yq1tTXh4e"
                        + "GaXHBwMBs3bsTKyorS0lK8vb3JyMggNDSU+Ph4XF1dtU2/d999l759+/Ltt98ycuTI5jfpCu6ogwWqV+bfAnOtx6ee" + "Unlja3+qTwqE9PNw+Va9/q0ydTDr9A/mwdDu+jYuXK+/vnij3r73k+DmpHp37Ci1jPNXPdHsrtfRbN1QQ0jHZrZ9Gm" + "PevHns27eP3NxcANauXcvcuXN1e1fdunXT7u3t7RkyZIg2gHv37kVEeO2117QCsGPHDqBBYt8aqmrqT1xUVUPCPyFq" + "EOw4Ci/2h9gvW1WntFwdpJZQ3cC+8xNqfaJApdfhnR2q57ek31706tWLESNGYLFYmDFjBllZWboQ0BycnJy4fFlN0m" + "/cuEFoaCgBAQEaPyAggO7dVY8xNPfJt4Wth2HOM7Dk9+qkld70Z5AOQT56j2wNeTfV+nSROtn8HFiwYAExMTHcvHmT" + "iRMnarsRzUFESE1NZfHixQA89dRT2NraMn369Gblm42hbaHoLuw9DVMGwYqvaeLgro4Q0BWK70NYPxjSHWb9o322b5" + "Wpnv/f4+FPiWrc9X4S/qMf/CNTH1p+KsaOHYujoyObNm3iwIEDTfjHjh3j+PHjlJeXs2bNGqqrq5k1axYAixcvZtSo" + "UfTu3ZuIiAhKSkpIS0ujqqqKhQsXqnloQ2N37oONfquZG6Vq2tQQKbnwWx/4Krtph22t4L/GqJPF5dtqSPin+ouTym" + "o1Rjd8B/cq9WlY3C5Y8DtYNQG6OKgvJvOC+jUA/FAC9xrtPhTcafpibWxsMJlMGBvtnRuNRsLCwsjMzGTo0KFN+l9Q" + "UMC4ceO4evUqgYGBpKam4uLiAsDw4cNJSEjgnXfe4fXXX0dRFPz9/YmLiwNUDzUk79olVd6jeaPpnlOLWD0ZbpZBXK"
                        + "PtoFnBauo0/VN1CdtGeG4T1kb1JbQHn+YtY1A3YNmyVuWqqqrw8fEhLi6OmJgYHW/8+PH4+voSHx+PiDQ5XNHYjqIo" + "uoMWn8TF/fijOF7OEPo0bDrYutzDDia0fzB/DLZt28a9e/e0BUBLaG0wQT2d0vjUyk+KoV7OsCYNzv3QlJdT0PRz/K" + "WhtLSUNWvWYNvwuE0txo0b1+ok1Ra0T77vb0f//++7R4CNK1aoP0fcndTyr43HN5B1UBQFQ8zkyTXHTp40tPdodENa" + "c0esm6MrBoPaWG2tGAwYDAaMjWgNa4PBoLtuTlZHry2t2vuJbbdop3ZSqqPduXCB/wPneX1TjWdPrAAAAABJRU5Erk" + "Jggg==\" /></a>");
            }
            pw.println("\t\t</p>");

            if (!annotation.bodyOnly()) {
                pw.println("\t</body>");
                pw.println("</html>");
            }

        } catch (StringParsingError e) {
            e.printStackTrace();
        }
    }
}
