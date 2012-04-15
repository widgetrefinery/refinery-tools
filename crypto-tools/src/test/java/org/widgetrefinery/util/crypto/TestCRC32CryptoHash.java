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
import org.widgetrefinery.util.StringUtil;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @since 3/4/12 7:56 PM
 */
public class TestCRC32CryptoHash extends TestCase {
    public void testInputStreamHash() throws Exception {
        CryptoHash cryptoHash = new CRC32CryptoHash();
        InputStream input = new ByteArrayInputStream("foobar".getBytes());
        byte[] result = cryptoHash.getHash(input);
        assertEquals("9ef61f95", StringUtil.toHexString(result));
    }

    public void testStringHash() throws Exception {
        CryptoHash cryptoHash = new CRC32CryptoHash();
        byte[] result = cryptoHash.getHash("foobar");
        assertEquals("9ef61f95", StringUtil.toHexString(result));
    }

    public void testChainedInputStreamHash() throws Exception {
        final InputStream input = new ByteArrayInputStream("hello world".getBytes());

        Mockery context = new Mockery();
        final CryptoHash mockedCryptoHash = context.mock(CryptoHash.class);
        context.checking(new Expectations() {{
            oneOf(mockedCryptoHash).getHash(input);
            will(returnValue("foobar".getBytes()));
        }});

        CryptoHash cryptoHash = new CRC32CryptoHash(mockedCryptoHash);
        byte[] result = cryptoHash.getHash(input);
        assertEquals("9ef61f95", StringUtil.toHexString(result));

        context.assertIsSatisfied();
    }

    public void testChainedStringHash() throws Exception {
        final String input = "hello world";

        Mockery context = new Mockery();
        final CryptoHash mockedCryptoHash = context.mock(CryptoHash.class);
        context.checking(new Expectations() {{
            oneOf(mockedCryptoHash).getHash(input);
            will(returnValue("foobar".getBytes()));
        }});

        CryptoHash cryptoHash = new CRC32CryptoHash(mockedCryptoHash);
        byte[] result = cryptoHash.getHash(input);
        assertEquals("9ef61f95", StringUtil.toHexString(result));

        context.assertIsSatisfied();
    }
}
