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

package org.widgetrefinery.util.cl;

import org.widgetrefinery.util.BadUserInputException;
import org.widgetrefinery.util.lang.Translator;

import java.text.MessageFormat;
import java.util.Date;
import java.util.logging.*;

/**
 * Base class for application entry points. It performs common initialization
 * work such as setting up logging.
 *
 * @since 4/14/12 2:24 PM
 */
public abstract class AbstractCli {
    /**
     * The main method should call this, passing in the command line arguments.
     * It performs common initialization work before calling
     * {@link #processCommandLine(String[])}.
     *
     * @param args command line arguments
     */
    protected void start(final String[] args) {
        boolean debugMode = null != System.getProperty("debug");

        if (debugMode) {
            Logger globalLogger = Logger.getLogger("");
            for (Handler h : globalLogger.getHandlers()) {
                globalLogger.removeHandler(h);
            }
            Handler handler = new ConsoleHandler();
            handler.setLevel(Level.FINEST);
            handler.setFormatter(new LogFormatter());
            Logger logger = Logger.getLogger("org.widgetrefinery");
            logger.setLevel(Level.FINEST);
            logger.addHandler(handler);
        }

        try {
            Translator.configure();
            processCommandLine(args);
        } catch (BadUserInputException e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        } catch (Exception e) {
            if (debugMode) {
                e.printStackTrace(System.err);
            } else {
                System.err.println(e.getMessage());
            }
            System.exit(-1);
        }
    }

    /**
     * The application takes over from here. It should parse the command
     * line and perform whatever work is desired.
     *
     * @param args command line arguments
     * @throws Exception allows for any exceptions to be thrown
     */
    protected abstract void processCommandLine(final String[] args) throws Exception;

    /**
     * Custom log formatter which formats log entries as:
     * <p/>
     * <code>yyyy-MM-dd HH:mm:ss.SSS Z [thread] [level] [class] [msg]</code>
     */
    protected static class LogFormatter extends Formatter {
        private final MessageFormat format;

        public LogFormatter() {
            this.format = new MessageFormat("{0,date,yyyy-MM-dd HH:mm:ss.SSS Z} {1} {2} {3} {4}\n");
        }

        @Override
        public String format(final LogRecord logRecord) {
            return this.format.format(new Object[]{
                    new Date(logRecord.getMillis()),
                    logRecord.getThreadID(),
                    logRecord.getLevel(),
                    logRecord.getSourceClassName(),
                    logRecord.getMessage(),
            });
        }
    }
}
