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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * Extends ResourceBundle.Control to load properties files using UTF-8 encoding
 * instead of the default ISO-8859-1 encoding for PropertyResourceBundle.
 *
 * @since 4/16/12 10:07 PM
 */
public class UTF8PropertiesControl extends ResourceBundle.Control {
    /**
     * Overrides the base implementation to return PropertyResourceBundles
     * from UTF-8 encoded properties files. The implementation is the same as
     * the base implementation except for how it constructs a
     * PropertyResourceBundle.
     *
     * @param baseName the base bundle name of the resource bundle, a fully qualified class name
     * @param locale   the locale for which the resource bundle should be instantiated
     * @param format   the resource bundle format to be loaded
     * @param loader   the ClassLoader to use to load the bundle
     * @param reload   the flag to indicate bundle reloading; true if reloading an expired resource bundle, false otherwise
     * @return the resource bundle instance, or null if none could be found.
     * @throws IllegalAccessException if the class or its default constructor is not accessible
     * @throws InstantiationException if the instantiation of a class fails for some other reason
     * @throws IOException            if an error occurred when reading resources using any I/O operations
     */
    @Override
    public ResourceBundle newBundle(String baseName,
                                    Locale locale,
                                    String format,
                                    ClassLoader loader,
                                    boolean reload) throws IllegalAccessException,
                                                           InstantiationException,
                                                           IOException {
        ResourceBundle bundle = null;
        if ("java.properties".equals(format)) {
            String bundleName = toBundleName(baseName, locale);
            String resourceName = toResourceName(bundleName, "properties");
            InputStream stream = null;
            if (reload) {
                URL url = loader.getResource(resourceName);
                if (null != url) {
                    URLConnection connection = url.openConnection();
                    if (connection != null) {
                        connection.setUseCaches(false);
                        stream = connection.getInputStream();
                    }
                }
            } else {
                stream = loader.getResourceAsStream(resourceName);
            }
            if (stream != null) {
                try {
                    InputStreamReader reader = new InputStreamReader(stream, "UTF-8");
                    bundle = new PropertyResourceBundle(reader);
                } finally {
                    stream.close();
                }
            }
        } else {
            bundle = super.newBundle(baseName, locale, format, loader, reload);
        }
        return bundle;
    }
}
