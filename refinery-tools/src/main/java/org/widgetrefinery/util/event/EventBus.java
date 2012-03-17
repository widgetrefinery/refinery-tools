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

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Since: 3/14/12 9:46 PM
 */
public class EventBus {
    private final Map<EventListener, Set<Class>> listeners;

    public EventBus() {
        this.listeners = new HashMap<EventListener, Set<Class>>();
    }

    public void add(final EventListener listener) {
        Set<Class> classes = new HashSet<Class>();
        for (Method method : listener.getClass().getMethods()) {
            Class[] parameters = method.getParameterTypes();
            if ("notify".equals(method.getName()) && !method.isBridge() && 1 == parameters.length && Event.class.isAssignableFrom(parameters[0])) {
                classes.add(parameters[0]);
            }
        }
        this.listeners.put(listener, classes);
    }

    public void remove(final EventListener listener) {
        this.listeners.remove(listener);
    }

    @SuppressWarnings("unchecked")
    public void fireEvent(final Event event) {
        for (Map.Entry<EventListener, Set<Class>> listener : this.listeners.entrySet()) {
            for (Class clazz : listener.getValue()) {
                if (clazz.isAssignableFrom(event.getClass())) {
                    listener.getKey().notify(event);
                    break;
                }
            }
        }
    }
}
