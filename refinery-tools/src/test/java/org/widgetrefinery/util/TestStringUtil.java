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

import junit.framework.TestCase;

/**
 * @since 3/8/12 12:39 AM
 */
public class TestStringUtil extends TestCase {
    public void testWordWrap() {
        //no wrapping
        assertEquals(" 12 45 7", StringUtil.wordWrap("12 45 7", 8, " ", "  "));

        //end of word hits wrap boundary
        assertEquals(" 123 567\n 901 34", StringUtil.wordWrap("123 567 901 34", 8, " ", "  "));
        //word sits on wrap boundary
        assertEquals(" 1234\n 678 01", StringUtil.wordWrap("1234 678 01", 8, " ", "  "));
        //word starts on wrap boundary
        assertEquals(" 123 56\n 890 234", StringUtil.wordWrap("123 56 890 234", 8, " ", "  "));

        //newline in the middle of a sentence
        assertEquals(" 123\n 567 90", StringUtil.wordWrap("123\n567 90", 8, " ", "  "));
        assertEquals(" 12\n 567 90", StringUtil.wordWrap("12 \n567 90", 8, " ", "  "));
        assertEquals(" 123\n  67 90", StringUtil.wordWrap("123\n 67 90", 8, " ", "  "));
        assertEquals(" 12\n  67 90", StringUtil.wordWrap("12 \n 67 90", 8, " ", "  "));
        //newline right before wrap boundary
        assertEquals(" 123 56\n 890 23", StringUtil.wordWrap("123 56\n890 23", 8, " ", "  "));
        //newline right after wrap boundary
        assertEquals(" 123 567\n 90 23", StringUtil.wordWrap("123 567\n90 23", 8, " ", "  "));
        //two sequential newlines
        assertEquals(" 12 45\n \n 89 12", StringUtil.wordWrap("12 45\n\n89 12", 8, " ", "  "));

        //tab in the middle of a sentence
        assertEquals(" 12  4 6", StringUtil.wordWrap("12\t4 6", 8, " ", "  "));
        assertEquals(" 1   4 6", StringUtil.wordWrap("1 \t4 6", 8, " ", "  "));
        assertEquals(" 1   4 6", StringUtil.wordWrap("1\t 4 6", 8, " ", "  "));
        assertEquals(" 1    56", StringUtil.wordWrap("1 \t 56", 8, " ", "  "));
        //tab right before wrap boundary
        assertEquals(" 123 5\n 789 12", StringUtil.wordWrap("123 5\t789 12", 8, " ", "  "));
        //tab sits on wrap boundary
        assertEquals(" 123 56\n 89 12", StringUtil.wordWrap("123 56\t89 12", 8, " ", "  "));
        //tab right after wrap boundary
        assertEquals(" 123 567\n 9 12", StringUtil.wordWrap("123 567\t9 12", 8, " ", "  "));
        //two sequential tabs
        assertEquals(" 1 3    6 8", StringUtil.wordWrap("1 3\t\t6 8", 12, " ", "  "));

        //newline followed by tab
        assertEquals(" 123\n   67 90", StringUtil.wordWrap("123\n\t67 90", 8, " ", "  "));
        //tab followed by newline
        assertEquals(" 123\n 67 90", StringUtil.wordWrap("123\t\n67 90", 8, " ", "  "));
    }
}
