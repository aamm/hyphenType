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
//START SNIPPET: 1
package example.case3;

import com.github.aamm.hyphenType.datastructure.Options;
import com.github.aamm.hyphenType.datastructure.annotations.ArgumentsObject;
import com.github.aamm.hyphenType.datastructure.annotations.Option;
import com.github.aamm.hyphenType.datastructure.annotations.SimpleArgument;

@SuppressWarnings("unchecked")
@ArgumentsObject(description = "Prints argA and argB to stdout.")
public interface XOpt extends Options {

    @Option(names = { "h", "help" }, description = "Shows this help message")
    boolean h();

    @SimpleArgument(index = 0)
    public String argA();

    @SimpleArgument(index = 1)
    public String argB();
}
