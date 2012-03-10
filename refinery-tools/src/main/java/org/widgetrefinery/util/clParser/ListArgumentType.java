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

import java.util.ArrayList;
import java.util.List;

/**
 * Since: 3/4/12 7:31 PM
 */
public class ListArgumentType extends AbstractArgumentType {
    private final ArgumentType listContentType;

    public ListArgumentType(final ArgumentType listContentType) {
        super(listContentType.isConsumesValue());
        this.listContentType = listContentType;
    }

    @Override
    public String getGenericDescription() {
        return this.listContentType.getGenericDescription();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object parse(final String value, final Object oldValue) throws BadUserInputException {
        Object parsedValue = this.listContentType.parse(value, null);

        List values;
        if (null != oldValue && (oldValue instanceof List)) {
            values = (List) oldValue;
        } else {
            values = new ArrayList<Object>();
        }
        values.add(parsedValue);

        return values;
    }
}
