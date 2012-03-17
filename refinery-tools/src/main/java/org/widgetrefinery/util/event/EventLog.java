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

import java.util.ArrayList;
import java.util.List;

/**
 * Since: 3/16/12 10:49 PM
 */
public class EventLog {
    private final Event               event;
    private final List<EventListener> listeners;
    private final StackTraceElement   eventSource;

    public EventLog(final Event event, final StackTraceElement eventSource) {
        this.event = event;
        this.listeners = new ArrayList<EventListener>();
        this.eventSource = eventSource;
    }

    public void addListener(final EventListener listener) {
        this.listeners.add(listener);
    }

    public void dump(final StringBuilder sb, final int eventNdx) {
        sb.append("Event ").append(eventNdx).append(": ").append(this.event).append(" from ").append(this.eventSource).append('\n');
        int listenerNdx = 0;
        for (EventListener listener : this.listeners) {
            sb.append("Listener ").append(listenerNdx++).append(": ").append(listener).append('\n');
        }
    }
}
