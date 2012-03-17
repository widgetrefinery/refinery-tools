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

import java.util.HashMap;
import java.util.Map;

/**
 * Since: 3/14/12 9:46 PM
 */
public class EventBus {
    private final Map<EventListener, Class> listeners;

    public EventBus() {
        this.listeners = new HashMap<EventListener, Class>();
    }

    public <T extends Event> void add(final Class<T> clazz, final EventListener<T> listener) {
        this.listeners.put(listener, clazz);
    }

    public void remove(final EventListener listener) {
        this.listeners.remove(listener);
    }

    @SuppressWarnings("unchecked")
    public void fireEvent(final Event event) {
        for (Map.Entry<EventListener, Class> listener : this.listeners.entrySet()) {
            if (listener.getValue().isAssignableFrom(event.getClass())) {
                listener.getKey().notify(event);
            }
        }
    }
}
