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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Since: 3/14/12 9:46 PM
 */
public class EventBus {
    private static final Logger logger             = Logger.getLogger(EventBus.class.getName());
    private static final int    DEFAULT_QUEUE_SIZE = 16;

    private final Map<EventListener, Class> listeners;
    private final List<EventLog>            queue;
    private final int                       queueSize;

    public EventBus() {
        this(DEFAULT_QUEUE_SIZE);
    }

    public EventBus(final int queueSize) {
        this.listeners = new HashMap<EventListener, Class>();
        this.queue = new ArrayList<EventLog>(queueSize);
        this.queueSize = queueSize;
    }

    public <T extends Event> void add(final Class<T> clazz, final EventListener<T> listener) {
        this.listeners.put(listener, clazz);
    }

    public void remove(final EventListener listener) {
        this.listeners.remove(listener);
    }

    @SuppressWarnings("unchecked")
    public void fireEvent(final Event event) {
        logger.fine("event fired: " + event.toString());
        if (this.queueSize <= this.queue.size()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Event bus queue has exceeded ").append(this.queueSize).append(" entries.\n");
            int eventNdx = 0;
            for (EventLog eventLog : this.queue) {
                eventLog.dump(sb, eventNdx++);
            }
            throw new RuntimeException(sb.toString());
        }
        EventLog eventLog = new EventLog(event, Thread.currentThread().getStackTrace()[2]);
        this.queue.add(eventLog);

        for (Map.Entry<EventListener, Class> entry : this.listeners.entrySet()) {
            EventListener listener = entry.getKey();
            Class eventClass = entry.getValue();
            if (eventClass.isAssignableFrom(event.getClass())) {
                eventLog.addListener(listener);
                listener.notify(event);
            }
        }

        this.queue.remove(this.queue.size() - 1);
    }
}
