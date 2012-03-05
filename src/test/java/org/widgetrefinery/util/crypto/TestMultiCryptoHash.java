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

package org.widgetrefinery.util.crypto;

import junit.framework.TestCase;
import org.jmock.Expectations;
import org.jmock.Mockery;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;

/**
 * Since: 3/4/12 11:55 PM
 */
public class TestMultiCryptoHash extends TestCase {
    public void testInputStreamHash() throws Exception {
        final InputStream input = new ByteArrayInputStream("foobar".getBytes());

        Mockery context1 = new Mockery();
        Mockery context2 = new Mockery();
        final CryptoHash mockedCryptoHash1 = context1.mock(CryptoHash.class);
        final CryptoHash mockedCryptoHash2 = context2.mock(CryptoHash.class);
        context1.checking(new Expectations() {{
            oneOf(mockedCryptoHash1).getHash(with(any(PipedInputStream.class)));
            will(returnValue("first".getBytes()));
        }});
        context2.checking(new Expectations() {{
            oneOf(mockedCryptoHash2).getHash(with(any(PipedInputStream.class)));
            will(returnValue("second".getBytes()));
        }});

        MultiCryptoHash multiCryptoHash = new MultiCryptoHash(mockedCryptoHash1, mockedCryptoHash2);
        byte[][] results = multiCryptoHash.getHashes(input);
        assertEquals(2, results.length);
        assertEquals("first", new String(results[0]));
        assertEquals("second", new String(results[1]));

        context1.assertIsSatisfied();
        context2.assertIsSatisfied();
    }

    public void testStringHash() {
        final String input = "foobar";

        Mockery context1 = new Mockery();
        Mockery context2 = new Mockery();
        final CryptoHash mockedCryptoHash1 = context1.mock(CryptoHash.class);
        final CryptoHash mockedCryptoHash2 = context2.mock(CryptoHash.class);
        context1.checking(new Expectations() {{
            oneOf(mockedCryptoHash1).getHash(input);
            will(returnValue("first".getBytes()));
        }});
        context2.checking(new Expectations() {{
            oneOf(mockedCryptoHash2).getHash(input);
            will(returnValue("second".getBytes()));
        }});

        MultiCryptoHash multiCryptoHash = new MultiCryptoHash(mockedCryptoHash1, mockedCryptoHash2);
        byte[][] results = multiCryptoHash.getHashes(input);
        assertEquals(2, results.length);
        assertEquals("first", new String(results[0]));
        assertEquals("second", new String(results[1]));

        context1.assertIsSatisfied();
        context2.assertIsSatisfied();
    }

    public void testExceptionHandling() throws Exception {
        final InputStream input = new ByteArrayInputStream("foobar".getBytes());

        Mockery context1 = new Mockery();
        Mockery context2 = new Mockery();
        final CryptoHash mockedCryptoHash1 = context1.mock(CryptoHash.class);
        final CryptoHash mockedCryptoHash2 = context2.mock(CryptoHash.class);
        context1.checking(new Expectations() {{
            oneOf(mockedCryptoHash1).getHash(with(any(PipedInputStream.class)));
            will(throwException(new IOException("dummy io error")));
        }});
        context2.checking(new Expectations() {{
            oneOf(mockedCryptoHash2).getHash(with(any(PipedInputStream.class)));
            will(throwException(new RuntimeException("dummy generic error")));
        }});

        try {
            MultiCryptoHash multiCryptoHash = new MultiCryptoHash(mockedCryptoHash1, mockedCryptoHash2);
            multiCryptoHash.getHashes(input);
            assertTrue("getHashes() did not throw exception", false);
        } catch (Exception e) {
            assertEquals(MultiCryptoHash.CryptoHashException.class, e.getClass());
            MultiCryptoHash.CryptoHashException error = (MultiCryptoHash.CryptoHashException) e;
            assertEquals(2, error.getErrors().size());
            assertEquals("dummy io error", error.getErrors().get(0).getMessage());
            assertEquals("dummy generic error", error.getErrors().get(1).getMessage());
        }

        context1.assertIsSatisfied();
        context2.assertIsSatisfied();
    }
}
