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

package org.widgetrefinery.util.clParser;

import java.util.regex.Pattern;

/**
 * Since: 3/3/12 11:36 PM
 */
public class StringArgumentType extends AbstractArgumentType {
    private final Pattern expectedPattern;

    public StringArgumentType() {
        super(true);
        this.expectedPattern = null;
    }

    public StringArgumentType(final String expectedPattern) {
        super(true);
        this.expectedPattern = Pattern.compile(expectedPattern);
    }

    @Override
    public String getGenericDescription() {
        return "a string value";
    }

    @Override
    public Object parse(final String value, final Object oldValue) {
        if (null != this.expectedPattern && !this.expectedPattern.matcher(value).matches()) {
            throw new RuntimeException("invalid value (" + value + ')');
        }
        return value.trim();
    }
}
