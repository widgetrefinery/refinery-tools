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
 * List of translation keys for the utility classes.
 *
 * @see org.widgetrefinery.util.lang.Translator
 * @since 4/14/12 11:22 AM
 */
public enum UtilTranslationKey implements TranslationKey {
    CL_HELP_USAGE("cl.help.usage"),
    CL_HELP_DESCRIPTION("cl.help.description"),
    CL_HELP_OPTIONS("cl.help.options"),
    CL_HELP_OPTIONS_SWITCH_VALUE("cl.help.options.switch_value"),
    CL_ERROR_NO_SUCH_SWITCH("cl.error.no_such_switch"),
    CL_ERROR_SWITCH_MISSING_VALUE("cl.error.switch_missing_value"),
    CL_ERROR_UNEXPECTED_SWITCH_VALUE("cl.error.unexpected_switch_value"),
    CL_ERROR_BAD_SWITCH_VALUE("cl.error.bad_switch_value"),
    CL_ERROR_MISSING_LICENSE("cl.error.missing_license"),
    CONFIG_CL_WIDTH("config.cl.width");

    private final String key;

    private UtilTranslationKey(final String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return this.key;
    }
}
