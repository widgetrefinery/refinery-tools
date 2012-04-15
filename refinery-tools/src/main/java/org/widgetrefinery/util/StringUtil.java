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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Collection of helper methods for working with strings. This class was
 * inspired by <a href="http://commons.apache.org/lang/">apache commons</a>.
 * The motivation for building this class was to keep the application lean.
 *
 * @since 3/4/12 10:17 PM
 */
public class StringUtil {
    /**
     * Checks if the given string is blank. Blank is defined as either null,
     * empty string, or a string with only whitespace.
     *
     * @param value string to check
     * @return true if blank
     * @see #isNotBlank(String)
     */
    public static boolean isBlank(final String value) {
        return null == value || value.trim().isEmpty();
    }

    /**
     * Checks if the given string is not blank. This returns the opposite of
     * {@link #isBlank(String)}. This is provided to help keep the code clear
     * as it can be difficult to distinguish between <code>!isBlank(str)</code>
     * and <code>isBlank(str)</code>.
     *
     * @param value string to check
     * @return true if not blank
     * @see #isBlank(String)
     */
    public static boolean isNotBlank(final String value) {
        return !isBlank(value);
    }

    /**
     * Null-safe trim.
     *
     * @param value string to trim
     * @return trimmed string
     */
    public static String trimToEmpty(final String value) {
        return null != value ? value.trim() : "";
    }

    /**
     * Formats the given byte array as a hexadecimal string.
     *
     * @param input byte array to format
     * @return hexadecimal string
     */
    public static String toHexString(final byte[] input) {
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

    /**
     * Format the given string so that no single line is longer than the
     * given width. Wrapping is done on word boundaries. Note that tab
     * characters are replaced with a constant sequence of spaces.
     *
     * @param value string to format
     * @param width width to restrict each line to
     * @return formatted string
     * @throws IllegalArgumentException if the width is smaller than the tab size
     * @see #wordWrap(String, int, String, String)
     */
    public static String wordWrap(final String value, final int width) throws IllegalArgumentException {
        return wordWrap(value, width, "", "    ");
    }

    /**
     * Format the given string so that no single line is longer than the
     * given width. Wrapping is done on word boundaries. Each new line after
     * the first will be prefixed with the given prefix. Tab characters will
     * be replaced with the given string.
     *
     * @param value  string to format
     * @param width  width to restrict each line to
     * @param prefix prefix for each line (except the first line)
     * @param tab    string to replace tab characters with
     * @return formatted string
     * @throws IllegalArgumentException if the width is smaller than prefix + tab
     */
    public static String wordWrap(final String value, final int width, final String prefix, final String tab) throws IllegalArgumentException {
        if (prefix.length() >= width) {
            throw new IllegalArgumentException("newline prefix is longer than the requested wrapping width");
        }
        if (prefix.length() + tab.length() >= width) {
            throw new IllegalArgumentException("newline prefix + tab is longer than the requested wrapping width");
        }

        StringBuilder sb = new StringBuilder();
        Pattern p = Pattern.compile(" +|[\n\t]");
        Matcher m = p.matcher(value);
        int startNdx = 0;
        int curLineWidth = 0;
        StringBuilder whitespace = new StringBuilder(prefix);

        while (m.find()) {
            int length = m.start() - startNdx;
            if (0 < length) {
                String word = value.substring(startNdx, m.start());
                if (curLineWidth + whitespace.length() + length <= width) {
                    sb.append(whitespace);
                    curLineWidth += whitespace.length();
                } else {
                    sb.append('\n').append(prefix);
                    curLineWidth = prefix.length();
                }
                sb.append(word);
                curLineWidth += length;
                whitespace = new StringBuilder();
            }
            startNdx = m.end();

            String matched = m.group();
            if ("\n".equals(matched)) {
                sb.append('\n').append(prefix);
                curLineWidth = prefix.length();
                whitespace = new StringBuilder();
            } else if ("\t".equals(matched)) {
                whitespace.append(tab);
            } else {
                whitespace.append(matched);
            }
        }
        String remaining = value.substring(startNdx);
        if (curLineWidth + whitespace.length() + remaining.length() <= width) {
            sb.append(whitespace);
        } else {
            sb.append('\n').append(prefix);
        }
        sb.append(remaining);

        return sb.toString();
    }
}
