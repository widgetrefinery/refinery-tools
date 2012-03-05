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

/**
 * Since: 3/4/12 10:17 PM
 */
public class StringUtil {
    public static boolean isBlank(final String value) {
        return null == value || value.trim().isEmpty();
    }

    public static boolean isNotBlank(final String value) {
        return !isBlank(value);
    }

    public static String trimToEmpty(final String value) {
        return null != value ? value.trim() : "";
    }

    public static String toString(final byte[] input) {
        StringBuilder sb = new StringBuilder();
        for (byte value : input) {
            String hex = Integer.toHexString(0xFF & (int) value);
            if (1 == hex.length()) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}
