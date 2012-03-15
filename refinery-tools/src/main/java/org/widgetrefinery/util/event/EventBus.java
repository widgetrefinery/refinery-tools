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

package org.widgetrefinery.util.event;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Since: 3/14/12 9:46 PM
 */
public class EventBus {
    private static final Logger logger = Logger.getLogger(EventBus.class.getName());

    private final Set<EventListener> listeners;

    public EventBus() {
        this.listeners = new HashSet<EventListener>();
    }

    public void add(final EventListener listener) {
        this.listeners.add(listener);
    }

    public void remove(final EventListener listener) {
        this.listeners.remove(listener);
    }

    @SuppressWarnings("unchecked")
    public void fireEvent(final Event event) {
        for (EventListener listener : this.listeners) {
            try {
                listener.notify(event);
            } catch (ClassCastException e) {
                if (logger.isLoggable(Level.FINE)) {
                    StringWriter stringWriter = new StringWriter();
                    PrintWriter printWriter = new PrintWriter(stringWriter);
                    e.printStackTrace(printWriter);
                    logger.fine(stringWriter.toString());
                }
            }
        }
    }
}
