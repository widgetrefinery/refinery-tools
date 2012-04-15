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

/**
 * A base implementation of CryptoHash that other classes can build off of.
 *
 * @since 3/4/12 7:39 PM
 */
public abstract class AbstractCryptoHash implements CryptoHash {
    /**
     * Suggested read buffer size.
     */
    protected static final int BUFFER_SIZE = 1024;

    private final CryptoHash chain;

    /**
     * Creates an instance that will hash data given to it.
     */
    protected AbstractCryptoHash() {
        this(null);
    }

    /**
     * Creates an instance that will hash the result from the given CryptoHash.
     *
     * @param chain upstream CryptoHash
     */
    protected AbstractCryptoHash(final CryptoHash chain) {
        this.chain = chain;
    }

    /**
     * If an upstream CryptoHash was provided to the constructor, the upstream
     * CryptoHash will hash the data and the result will be hashed by this
     * class. Otherwise this class will hash the given data.
     *
     * @param input input data to hash
     * @return hashed data as a byte array
     * @throws IOException if an error occurred reading from input
     */
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

    /**
     * If an upstream CryptoHash was provided to the constructor, the upstream
     * CryptoHash will hash the data and the result will be hashed by this
     * class. Otherwise this class will hash the given data.
     *
     * @param input input data to hash
     * @return hashed data as a byte array
     */
    @Override
    public byte[] getHash(final String input) {
        byte[] result;
        if (null != this.chain) {
            result = this.chain.getHash(input);
            result = computeHash(result);
        } else {
            result = computeHash(input.getBytes());
        }
        return result;
    }

    /**
     * Hashes the given data.
     *
     * @param input input data to hash
     * @return hashed data as a byte array
     * @throws IOException if an error occurred reading from input
     */
    protected abstract byte[] computeHash(final InputStream input) throws IOException;

    /**
     * Hashes the given data.
     *
     * @param input input data to hash
     * @return hashed data as a byte array
     */
    protected abstract byte[] computeHash(final byte[] input);
}
