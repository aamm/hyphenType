/*
 * This file is part of hyphenType.
 * 
 * hyphenType is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * hyphenType is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with hyphenType.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.aamm.hyphenType.tests.bugfound;

import com.github.aamm.hyphenType.datastructure.Options;
import com.github.aamm.hyphenType.datastructure.annotations.ArgumentsObject;
import com.github.aamm.hyphenType.datastructure.annotations.Option;
import com.github.aamm.hyphenType.exit.CanonicalExitCode;

/**
 * @author Aurelio Akira M. Matsui
 */
@ArgumentsObject(doubleHyphenInLongOptions = true, resourceBundlesLocation = "", description = "Loooong description."
// documentationFormatterOptions = {
// @FormatOption(c = StandardFormatterEngine.class, k = "usage", v = "Usage"),
// @FormatOption(c = StandardFormatterEngine.class, k = "options", v =
// "options"),
// @FormatOption(c = StandardFormatterEngine.class, k = "linebreakatspace", v =
// "true"),
// @FormatOption(c = StandardFormatterEngine.class, k = "descriptionindent", v =
// "20"),
// @FormatOption(c = StandardFormatterEngine.class, k = "maxcolumn", v = "50")
// }
)
public interface MyOpts extends Options<CanonicalExitCode> {
    @Option
    boolean a();

    @Option
    boolean b();
}
