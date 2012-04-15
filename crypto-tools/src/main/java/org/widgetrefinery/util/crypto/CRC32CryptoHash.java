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

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;

/**
 * Utility class for computing CRC32 hashes.
 *
 * @since 3/4/12 7:44 PM
 */
public class CRC32CryptoHash extends AbstractCryptoHash {
    /**
     * Creates an instance that will hash data given to it.
     */
    public CRC32CryptoHash() {
    }

    /**
     * Creates an instance that will hash the result from the given CryptoHash.
     *
     * @param chain upstream CryptoHash
     */
    public CRC32CryptoHash(final CryptoHash chain) {
        super(chain);
    }

    @Override
    protected byte[] computeHash(final InputStream input) throws IOException {
        CRC32 crc32 = new CRC32();
        byte[] data = new byte[BUFFER_SIZE];
        for (int bytesRead = input.read(data); 0 < bytesRead; bytesRead = input.read(data)) {
            crc32.update(data, 0, bytesRead);
        }
        return toByteArray((int) crc32.getValue());
    }

    @Override
    protected byte[] computeHash(final byte[] input) {
        CRC32 crc32 = new CRC32();
        crc32.update(input);
        return toByteArray((int) crc32.getValue());
    }

    /**
     * Converts the CRC32 result into a byte array.
     *
     * @param input CRC32 hash
     * @return byte array representing the hash
     */
    protected byte[] toByteArray(final int input) {
        return new byte[]{
                (byte) ((input >> 24) & 0xFF),
                (byte) ((input >> 16) & 0xFF),
                (byte) ((input >> 8) & 0xFF),
                (byte) (input & 0xFF)
        };
    }
}
