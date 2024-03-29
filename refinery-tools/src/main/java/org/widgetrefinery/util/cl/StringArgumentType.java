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

package org.widgetrefinery.util.cl;

import org.widgetrefinery.util.BadUserInputException;
import org.widgetrefinery.util.StringUtil;
import org.widgetrefinery.util.lang.UtilTranslationKey;

import java.util.regex.Pattern;

/**
 * Deals with arguments that are plain strings. It can optionally enforce that
 * the value match a certain pattern.
 *
 * @see org.widgetrefinery.util.cl.CLParser
 * @since 3/3/12 11:36 PM
 */
public class StringArgumentType extends AbstractArgumentType {
    private final Pattern expectedPattern;

    /**
     * Creates a new instance that will allow any value.
     */
    public StringArgumentType() {
        super(true);
        this.expectedPattern = null;
    }

    /**
     * Creates a new instance that will enforce the value to match a certain
     * pattern.
     *
     * @param expectedPattern the regexp pattern to check against
     */
    public StringArgumentType(final String expectedPattern) {
        super(true);
        this.expectedPattern = Pattern.compile(expectedPattern);
    }

    @Override
    public String getGenericDescription() {
        return "a string value";
    }

    /**
     * Trims the value before returning it. Will optionally validate the value
     * against a regexp pattern.
     *
     * @param value string value from the command line
     * @return validated string
     * @throws BadUserInputException if the string does not match the expected pattern
     */
    @Override
    public String parse(String value) throws BadUserInputException {
        value = StringUtil.trimToEmpty(value);
        if (null != this.expectedPattern && !this.expectedPattern.matcher(value).matches()) {
            throw new BadUserInputException(UtilTranslationKey.CL_ERROR_BAD_SWITCH_VALUE, value);
        }
        return value;
    }
}
