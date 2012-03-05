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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Since: 3/4/12 7:39 PM
 */
public abstract class AbstractCryptoHash implements CryptoHash {
    protected static final int BUFFER_SIZE = 1024;

    private final CryptoHash chain;

    public AbstractCryptoHash() {
        this(null);
    }

    public AbstractCryptoHash(final CryptoHash chain) {
        this.chain = chain;
    }

    @Override
    public byte[] getHash(final InputStream input) throws IOException {
        byte[] result;
        if (null != this.chain) {
            result = this.chain.getHash(input);
            result = computeHash(result);
        } else {
            result = computeHash(input);
        }
        return result;
    }

    @Override
    public byte[] getHash(final String input) {
        try {
            InputStream inputStream = new ByteArrayInputStream(input.getBytes());
            return getHash(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("unexpected io error reading from byte array", e);
        }
    }

    protected abstract byte[] computeHash(final InputStream input) throws IOException;

    protected abstract byte[] computeHash(final byte[] input);

    @Override
    public String toString(final byte[] input) {
        StringBuilder sb = new StringBuilder();
        for (byte value : input) {
            String hex = Integer.toHexString(0xFF & (int) value);
            if (1 == hex.length()) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}
