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
 * A simple event bus for sending messages between different parts of an
 * application. This was built to assist swing applications so it uses a
 * single-thread design. This means you can run into problems if a listener
 * fires an event that triggers a listener that fires an event that triggers
 * a listener that, etc. The event bus protects against this by monitoring
 * the number of events in flight and will fail fast if this number exceeds
 * a certain threshold.
 *
 * @since 3/14/12 9:46 PM
 */
public class EventBus {
    private static final Logger logger             = Logger.getLogger(EventBus.class.getName());
    private static final int    DEFAULT_QUEUE_SIZE = 16;

    private final Map<EventListener, Class> listeners;
    private final List<EventLog>            queue;
    private final int                       queueSize;

    /**
     * Creates a new instance with the default queue size.
     */
    public EventBus() {
        this(DEFAULT_QUEUE_SIZE);
    }

    /**
     * Creates a new instance with the given queue size.
     *
     * @param queueSize max allowed events in flight
     */
    public EventBus(final int queueSize) {
        this.listeners = new HashMap<EventListener, Class>();
        this.queue = new ArrayList<EventLog>(queueSize);
        this.queueSize = queueSize;
    }

    /**
     * Adds a new listener to the event bus.
     *
     * @param clazz    event class to register the listener for
     * @param listener listener to register
     * @param <T>      event class
     */
    public <T extends Event> void add(final Class<T> clazz, final EventListener<T> listener) {
        this.listeners.put(listener, clazz);
    }

    /**
     * Removes the given listener from the event bus.
     *
     * @param listener listener to remove
     */
    public void remove(final EventListener listener) {
        this.listeners.remove(listener);
    }

    /**
     * Sends an event to the listeners that are interested.
     *
     * @param event event to send
     * @throws RuntimeException if the number of events in flight have exceeded the threshold
     */
    @SuppressWarnings("unchecked")
    public void fireEvent(final Event event) throws RuntimeException {
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
