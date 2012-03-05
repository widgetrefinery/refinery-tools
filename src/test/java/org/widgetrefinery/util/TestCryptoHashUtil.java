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

package org.widgetrefinery.util;

import junit.framework.TestCase;
import org.widgetrefinery.util.crypto.CryptoHashUtil;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Since: 3/4/12 12:20 AM
 */
public class TestCryptoHashUtil extends TestCase {
    public void testCrc32() {
        CryptoHashUtil cryptoHashUtil = new CryptoHashUtil();

        byte[] fromByteArray = cryptoHashUtil.crc32("foobar".getBytes());
        assertEquals("9ef61f95", cryptoHashUtil.toString(fromByteArray));

        InputStream inputStream = new ByteArrayInputStream("9ef61f95".getBytes());
        byte[] fromInputStream = cryptoHashUtil.crc32(inputStream);
        assertEquals("8a1834d3", cryptoHashUtil.toString(fromInputStream));
    }

    public void testMd5() {
        CryptoHashUtil cryptoHashUtil = new CryptoHashUtil();

        byte[] fromByteArray = cryptoHashUtil.md5("foobar".getBytes());
        assertEquals("3858f62230ac3c915f300c664312c63f", cryptoHashUtil.toString(fromByteArray));

        InputStream inputStream = new ByteArrayInputStream("3858f62230ac3c915f300c664312c63f".getBytes());
        byte[] fromInputStream = cryptoHashUtil.md5(inputStream);
        assertEquals("9e71fc2a99a71b722ead746b776b25ac", cryptoHashUtil.toString(fromInputStream));
    }

    public void testSha1() {
        CryptoHashUtil cryptoHashUtil = new CryptoHashUtil();

        byte[] fromByteArray = cryptoHashUtil.sha1("foobar".getBytes());
        assertEquals("8843d7f92416211de9ebb963ff4ce28125932878", cryptoHashUtil.toString(fromByteArray));

        InputStream inputStream = new ByteArrayInputStream("8843d7f92416211de9ebb963ff4ce28125932878".getBytes());
        byte[] fromInputStream = cryptoHashUtil.sha1(inputStream);
        assertEquals("72833f1c7d3b80aadc836d5d035745ffa3a65894", cryptoHashUtil.toString(fromInputStream));
    }
}
