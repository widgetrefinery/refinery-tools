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
 * Represents a utility class that knows how to compute hashes.
 *
 * @since 3/4/12 7:37 PM
 */
public interface CryptoHash {
    /**
     * Hashes the given data.
     *
     * @param input input data to hash
     * @return hashed data as a byte array
     * @throws IOException if an error occurred reading from input
     */
    byte[] getHash(InputStream input) throws IOException;

    /**
     * Hashes the given data.
     *
     * @param input input data to hash
     * @return hashed data as a byte array
     */
    byte[] getHash(String input);
}
