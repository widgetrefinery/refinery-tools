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

    public static String wordWrap(final String value, final int width) throws IllegalArgumentException {
        return wordWrap(value, width, "", "");
    }

    public static String wordWrap(final String value, final int width, final String prefix, final String tabPrefix) throws IllegalArgumentException {
        if (prefix.length() >= width) {
            throw new IllegalArgumentException("newline prefix is longer than the requested wrapping width");
        }
        if (prefix.length() + tabPrefix.length() >= width) {
            throw new IllegalArgumentException("newline prefix + tab prefix is longer than the requested wrapping width");
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
                whitespace.append(tabPrefix);
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
