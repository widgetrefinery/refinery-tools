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

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Provides translation service for an application. Translations are defined in
 * property files.
 *
 * @see java.util.ResourceBundle
 * @since 4/14/12 12:43 AM
 */
public class Translator {
    private static final String DEFAULT_NAME = "translate";
    private static ResourceBundle resourceBundle;

    protected Translator() {
    }

    /**
     * Switch the translator to the default locale as defined by
     * {@link java.util.Locale#getDefault()}. The properties file will be
     * derived from the name in {@link #DEFAULT_NAME}.
     *
     * @see #configure(String, java.util.Locale)
     */
    public static void configure() {
        configure(DEFAULT_NAME, Locale.getDefault());
    }

    /**
     * Switch the translator to the given locale. The properties file will be
     * derived from the name in {@link #DEFAULT_NAME}.
     *
     * @param locale the new locale
     * @see #configure(String, java.util.Locale)
     */
    public static void configure(final Locale locale) {
        configure(DEFAULT_NAME, locale);
    }

    /**
     * Switch the translator to the given local. The properties file will be
     * derived from the given name.
     *
     * @param name   the name of the properties file to load
     * @param locale the new locale
     */
    public static void configure(final String name, final Locale locale) {
        resourceBundle = ResourceBundle.getBundle(name, locale);
    }

    /**
     * Returns the underlying {@link java.util.ResourceBundle}. If none was
     * configured then it will load the default ResourceBundle.
     *
     * @return ResourceBundle containing the translations
     */
    protected static ResourceBundle getResourceBundle() {
        if (null == resourceBundle) {
            configure();
        }
        return resourceBundle;
    }

    /**
     * Returns the localized text for the given key. If format objects are
     * provided, the localized text is run through
     * {@link java.text.MessageFormat#format(String, Object...)}.
     *
     * @param key    lookup key
     * @param params optional format objects
     * @return localized text
     */
    public static String get(final TranslatorKey key, Object... params) {
        String result = getResourceBundle().getString(key.getKey());
        if (null == result) {
            result = "{" + key.getKey() + "}";
        } else if (null != params) {
            result = MessageFormat.format(result, params);
        }
        return result;
    }
}
