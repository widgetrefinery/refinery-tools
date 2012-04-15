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

import java.util.ArrayList;
import java.util.List;

/**
 * Extends an existing argument type to allow it to occur multiple times on
 * the command line.
 *
 * @see org.widgetrefinery.util.cl.CLParser
 * @since 3/4/12 7:31 PM
 */
public class ListArgumentType extends AbstractArgumentType {
    private final ArgumentType argumentType;
    private final List         values;

    /**
     * @param argumentType argument type to wrap
     */
    public ListArgumentType(final ArgumentType argumentType) {
        super(argumentType.isConsumesValue());
        this.argumentType = argumentType;
        this.values = new ArrayList();
    }

    @Override
    public String getGenericDescription() {
        return this.argumentType.getGenericDescription();
    }

    /**
     * Uses the wrapped argument type to parse the value and then saves the
     * result to a list.
     *
     * @return list of all values that have been parsed
     * @throws BadUserInputException if the wrapped argument type failed to parse the value
     */
    @SuppressWarnings("unchecked")
    @Override
    public List parse(final String value) throws BadUserInputException {
        Object parsedValue = this.argumentType.parse(value);
        this.values.add(parsedValue);
        return this.values;
    }
}
