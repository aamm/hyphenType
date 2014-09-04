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
package com.github.aamm.hyphenType.tests.optionprocessors.lib;

import com.github.aamm.hyphenType.datastructure.Options;
import com.github.aamm.hyphenType.datastructure.annotations.Option;
import com.github.aamm.hyphenType.exit.CanonicalExitCode;
import com.github.aamm.hyphenType.optionprocessors.lib.BooleanValidatorEngine.BooleanValidator;
import com.github.aamm.hyphenType.optionprocessors.lib.BooleanValidatorEngine.Rule;

/**
 * @author Aurelio Akira M. Matsui
 */
@BooleanValidator(rules = { @Rule(rule = "!(a & b)", failureCode = 1),
	@Rule(rule = "a < 2", failureCode = 88) }, verbose = true, allBooleans = false)
public interface BooleanValidatorTestInterface extends
	Options<CanonicalExitCode> {

    @Option
    int a();

    @Option
    int b();
}
