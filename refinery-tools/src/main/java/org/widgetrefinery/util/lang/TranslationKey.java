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

/**
 * Represents a single key in the translation properties file. The purpose for
 * this class is to strongly type the key rather than use raw strings which
 * should help with maintenance, refactoring, etc.
 *
 * @see org.widgetrefinery.util.lang.Translator
 * @since 4/14/12 12:44 AM
 */
public interface TranslationKey {
    /**
     * The key to look up in the translation properties file.
     *
     * @return key
     */
    String getKey();
}
