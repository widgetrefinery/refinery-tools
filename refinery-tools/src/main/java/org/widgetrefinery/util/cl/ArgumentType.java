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

/**
 * Defines the type of an argument, such as an argument that takes a numeric
 * value or a boolean flag that takes no values. It is also responsible for
 * validating and parsing the values that come in.
 *
 * @see org.widgetrefinery.util.cl.Argument
 * @see org.widgetrefinery.util.cl.CLParser
 * @since 3/4/12 6:33 PM
 */
public interface ArgumentType {
    /**
     * Returns true if this argument takes a value. This should generally be
     * true except for boolean flags.
     *
     * @return true if this argument type takes a value
     */
    boolean isConsumesValue();

    /**
     * Provides a generic description for the argument. Generally the
     * application developer should provide the description when creating an
     * {@link org.widgetrefinery.util.cl.Argument}.
     *
     * @return generic description
     */
    String getGenericDescription();

    /**
     * Validates and parses the value associated with this argument.
     *
     * @param value string value from the command line
     * @return parsed value
     * @throws BadUserInputException if the value is invalid
     */
    Object parse(String value) throws BadUserInputException;
}
