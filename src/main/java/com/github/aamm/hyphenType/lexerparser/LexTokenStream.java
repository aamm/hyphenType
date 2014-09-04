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
package com.github.aamm.hyphenType.lexerparser;

import java.util.List;

import com.github.aamm.hyphenType.datastructure.lexer.LexToken;

/**
 * @author Aurelio Akira M. Matsui
 */
public class LexTokenStream {

    private int index = 0;
    private final List<LexToken> tokens;

    public LexTokenStream(List<LexToken> tokens) {
        this.tokens = tokens;
    }

    public boolean hasFutureToken() {
        return tokens.size() > index;
    }

    public LexToken futureToken() {
        return tokens.get(index);
    }

    public LexToken currentToken() {
        if (index - 1 > -1)
            return tokens.get(index - 1);
        else
            return null;
    }

    public LexToken consume() {
        LexToken token = tokens.get(index);
        index++;
        return token;
    }
}
