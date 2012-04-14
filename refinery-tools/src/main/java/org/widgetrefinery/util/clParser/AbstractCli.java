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

import org.widgetrefinery.util.lang.Translator;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @since 4/14/12 2:24 PM
 */
public abstract class AbstractCli {
    protected void start(final String[] args) {
        boolean debugMode = null != System.getProperty("debug");

        if (debugMode) {
            Handler handler = new ConsoleHandler();
            handler.setLevel(Level.FINEST);
            Logger logger = Logger.getLogger("org.widgetrefinery");
            logger.setLevel(Level.FINEST);
            logger.addHandler(handler);
        }

        try {
            Translator.configure();
            processCommandLine(args);
        } catch (Exception e) {
            if (debugMode) {
                e.printStackTrace(System.err);
            } else {
                System.err.println(e.getMessage());
            }
            System.exit(-1);
        }
    }

    protected abstract void processCommandLine(final String[] args) throws Exception;
}
