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

package org.widgetrefinery.util.lang;

import junit.framework.TestCase;

import java.util.Locale;

/**
 * @since 4/16/12 10:39 PM
 */
public class TestTranslator extends TestCase {
    public void testUTF8() {
        Translator.configure(new Locale("test_utf8"));
        assertEquals("日本語もできるよ！", Translator.get(UtilTranslationKey.CL_HELP_DESCRIPTION));
    }
}
