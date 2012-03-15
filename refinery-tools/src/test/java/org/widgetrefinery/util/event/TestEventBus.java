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

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * Since: 3/14/12 9:58 PM
 */
public class TestEventBus extends TestCase {
    public void testEventBus() {
        //setup the event bus
        EventBus bus = new EventBus();
        StubEventListener1 listener1 = new StubEventListener1();
        StubEventListener2 listener2 = new StubEventListener2();
        StubEventListener3 listener3 = new StubEventListener3();
        bus.add(listener1);
        bus.add(listener2);
        bus.add(listener3);

        //fire StubEvent1; listeners 1 & 3 should be notified
        bus.fireEvent(new StubEvent1(10));
        assertEquals(1, listener1.values.size());
        assertEquals(0, listener2.values.size());
        assertEquals(1, listener3.values.size());
        assertEquals(Integer.valueOf(10), listener1.values.get(0));
        assertEquals(10, listener3.values.get(0));
        listener1.values.clear();
        listener3.values.clear();

        //fire StubEvent2; listeners 2 & 3 should be notified
        bus.fireEvent(new StubEvent2(20));
        assertEquals(0, listener1.values.size());
        assertEquals(1, listener2.values.size());
        assertEquals(1, listener3.values.size());
        assertEquals(Integer.valueOf(20), listener2.values.get(0));
        assertEquals(20, listener3.values.get(0));
        listener2.values.clear();
        listener3.values.clear();

        //remove listener 3; remaining listeners should continue to function
        bus.remove(listener3);
        bus.fireEvent(new StubEvent1(11));
        assertEquals(1, listener1.values.size());
        assertEquals(0, listener2.values.size());
        assertEquals(0, listener3.values.size());
        assertEquals(Integer.valueOf(11), listener1.values.get(0));
    }

    protected static class StubEvent1 extends Event<Integer> {
        public StubEvent1(final Integer payload) {
            super(payload);
        }
    }

    protected static class StubEvent2 extends Event<Integer> {
        public StubEvent2(final Integer payload) {
            super(payload);
        }
    }

    protected static class StubEventListener1 extends EventListener<StubEvent1> {
        private final List<Integer> values;

        public StubEventListener1() {
            super(StubEvent1.class);
            this.values = new ArrayList<Integer>();
        }

        @Override
        public void notify(final StubEvent1 event) {
            this.values.add(event.getPayload());
        }
    }

    protected static class StubEventListener2 extends EventListener<StubEvent2> {
        private final List<Integer> values;

        public StubEventListener2() {
            super(StubEvent2.class);
            this.values = new ArrayList<Integer>();
        }

        @Override
        public void notify(final StubEvent2 event) {
            this.values.add(event.getPayload());
        }
    }

    protected static class StubEventListener3 extends EventListener<Event> {
        private final List<Object> values;

        public StubEventListener3() {
            super(Event.class);
            this.values = new ArrayList<Object>();
        }

        @Override
        public void notify(final Event event) {
            this.values.add(event.getPayload());
        }
    }
}
