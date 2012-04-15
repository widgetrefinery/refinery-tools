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

/**
 * Deals with arguments that are boolean flags, such as the commonly used help
 * (--help) or verbose (-v) flags.
 *
 * @see org.widgetrefinery.util.clParser.CLParser
 * @since 3/3/12 8:47 PM
 */
public class BooleanArgumentType extends AbstractArgumentType {
    public BooleanArgumentType() {
        super(false);
    }

    @Override
    public String getGenericDescription() {
        return "a boolean flag";
    }

    /**
     * Boolean arguments do not take a value so this just returns true.
     *
     * @param value string value from the command line
     * @return true
     */
    @Override
    public Boolean parse(final String value) {
        return Boolean.TRUE;
    }
}
