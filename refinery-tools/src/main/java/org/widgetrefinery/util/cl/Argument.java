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
import org.widgetrefinery.util.lang.Translator;
import org.widgetrefinery.util.lang.UtilTranslationKey;

/**
 * Represents a single argument on the command line. An argument can contain
 * multiple names (i.e. "-h" and "--help"), a description, an
 * {@link org.widgetrefinery.util.cl.ArgumentType} for parsing values,
 * and the value itself.
 *
 * @see org.widgetrefinery.util.cl.CLParser
 * @since 3/3/12 8:53 PM
 */
public class Argument implements Comparable<Argument> {
    private static final String DELIMITER = "|";

    private final String[]     names;
    private final ArgumentType type;
    private final String       description;
    private       Object       value;

    /**
     * @param rawName     list of names delimited by a pipe (|) such as "h|help"
     * @param type        argument type for parsing values
     * @param description user-friendly description for this argument
     * @throws IllegalArgumentException if rawName is malformed or type is missing
     */
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

    /**
     * Returns the list of names for this argument.
     *
     * @return list of names
     */
    public String[] getNames() {
        return this.names;
    }

    /**
     * Returns a hopefully user-friendly description for this argument.
     *
     * @return description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Returns true if this argument takes a value, based on the argument type
     * provided to the constructor.
     *
     * @return true if this argument takes a value
     */
    public boolean isConsumesValue() {
        return this.type.isConsumesValue();
    }

    /**
     * Validates and parses the value associated with this argument.
     *
     * @param argument name that appeared with the value on the command line
     * @param rawValue string value from the command line
     * @throws BadUserInputException if the value is invalid
     */
    public void parse(final String argument, final String rawValue) throws BadUserInputException {
        try {
            this.value = this.type.parse(rawValue);
        } catch (BadUserInputException e) {
            throw new BadUserInputException(Translator.get(UtilTranslationKey.CL_ERROR_BAD_SWITCH_VALUE, argument, rawValue), e);
        }
    }

    /**
     * Returns the parsed value that came out of {@link #parse(String, String)}.
     *
     * @return parsed value
     */
    public Object getValue() {
        return this.value;
    }

    /**
     * Compare arguments by their first name.
     *
     * @param other other argument to compare against
     * @return result of comparing first names
     */
    @Override
    public int compareTo(final Argument other) {
        return this.names[0].compareTo(other.names[0]);
    }
}
