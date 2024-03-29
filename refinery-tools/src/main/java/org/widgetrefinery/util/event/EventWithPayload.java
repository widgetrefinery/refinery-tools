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

/**
 * An event carrying a value.
 *
 * @see org.widgetrefinery.util.event.EventBus
 * @since 4/12/12 9:29 PM
 */
public class EventWithPayload<V> implements Event {
    private final V value;

    protected EventWithPayload(final V value) {
        this.value = value;
    }

    public V getValue() {
        return this.value;
    }

    /**
     * Returns the event class name and payload value.
     *
     * @return event class name and payload value
     */
    @Override
    public String toString() {
        return getClass().getName() + ": " + getValue();
    }
}
