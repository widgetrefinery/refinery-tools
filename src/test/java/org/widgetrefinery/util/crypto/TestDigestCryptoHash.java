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
 * Since: 3/4/12 8:10 PM
 */
public class TestDigestCryptoHash extends TestCase {
    public void testMd5GetHash() throws Exception {
        CryptoHash cryptoHash = DigestCryptoHash.createMD5();
        byte[] result = cryptoHash.getHash("foobar");
        assertEquals("3858f62230ac3c915f300c664312c63f", StringUtil.toHexString(result));
    }

    public void testChainedMd5GetHash() throws Exception {
        final InputStream input = new ByteArrayInputStream("hello world".getBytes());

        Mockery context = new Mockery();
        final CryptoHash mockedCryptoHash = context.mock(CryptoHash.class);
        context.checking(new Expectations() {{
            oneOf(mockedCryptoHash).getHash(input);
            will(returnValue("foobar".getBytes()));
        }});

        CryptoHash cryptoHash = DigestCryptoHash.createMD5(mockedCryptoHash);
        byte[] result = cryptoHash.getHash(input);
        assertEquals("3858f62230ac3c915f300c664312c63f", StringUtil.toHexString(result));

        context.assertIsSatisfied();
    }

    public void testSha1GetHash() throws Exception {
        CryptoHash cryptoHash = DigestCryptoHash.createSHA1();
        byte[] result = cryptoHash.getHash("foobar");
        assertEquals("8843d7f92416211de9ebb963ff4ce28125932878", StringUtil.toHexString(result));
    }

    public void testChainedSha1GetHash() throws Exception {
        final InputStream input = new ByteArrayInputStream("hello world".getBytes());

        Mockery context = new Mockery();
        final CryptoHash mockedCryptoHash = context.mock(CryptoHash.class);
        context.checking(new Expectations() {{
            oneOf(mockedCryptoHash).getHash(input);
            will(returnValue("foobar".getBytes()));
        }});

        CryptoHash cryptoHash = DigestCryptoHash.createSHA1(mockedCryptoHash);
        byte[] result = cryptoHash.getHash(input);
        assertEquals("8843d7f92416211de9ebb963ff4ce28125932878", StringUtil.toHexString(result));

        context.assertIsSatisfied();
    }
}
