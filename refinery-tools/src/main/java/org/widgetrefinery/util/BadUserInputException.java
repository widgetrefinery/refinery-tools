/*
 * Copyright (C) 2012  Widget Refinery
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.widgetrefinery.util;

import org.widgetrefinery.util.lang.TranslationKey;
import org.widgetrefinery.util.lang.Translator;

/**
 * Represents an error caused by bad user input.
 *
 * @since 3/6/12 8:47 PM
 */
public class BadUserInputException extends RuntimeException {
    private final TranslationKey key;

    public BadUserInputException(final TranslationKey key, final Object... badValues) {
        super(Translator.get(key, badValues));
        this.key = key;
    }

    public TranslationKey getKey() {
        return this.key;
    }
}
