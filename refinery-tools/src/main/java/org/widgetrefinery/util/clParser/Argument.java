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

import org.widgetrefinery.util.BadUserInputException;
import org.widgetrefinery.util.StringUtil;

/**
 * Since: 3/3/12 8:53 PM
 */
public class Argument implements Comparable<Argument> {
    private static final String DELIMITER = "|";

    private final String[]     names;
    private final ArgumentType type;
    private final String       description;
    private       Object       value;

    public Argument(final String rawName, final ArgumentType type) throws IllegalArgumentException {
        this(rawName, type, null);
    }

    public Argument(final String rawName, final ArgumentType type, final String description) throws IllegalArgumentException {
        if (StringUtil.isBlank(rawName) || rawName.contains("=")) {
            throw new IllegalArgumentException("invalid argument name (" + rawName + ')');
        }
        if (null == type) {
            throw new IllegalArgumentException("missing argument type for " + rawName);
        }
        this.names = rawName.trim().split("\\Q" + DELIMITER + "\\E");
        this.type = type;
        this.description = StringUtil.isNotBlank(description) ? description.trim() : type.getGenericDescription();
    }

    public String[] getNames() {
        return this.names;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean isConsumesValue() {
        return this.type.isConsumesValue();
    }

    public void parse(final String argument, final String rawValue) throws BadUserInputException {
        try {
            this.value = this.type.parse(rawValue, this.value);
        } catch (BadUserInputException e) {
            throw new BadUserInputException("invalid value for " + argument, e);
        }
    }

    public Object getValue() {
        return this.value;
    }

    @Override
    public int compareTo(final Argument other) {
        return this.names[0].compareTo(other.names[0]);
    }
}
