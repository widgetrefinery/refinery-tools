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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility class for computing hashes supported by
 * {@link java.security.MessageDigest}.
 *
 * @see java.security.MessageDigest
 * @since 3/4/12 7:51 PM
 */
public class DigestCryptoHash extends AbstractCryptoHash {
    private final String type;

    /**
     * Creates an instance that will hash data given to it.
     *
     * @param type digest type
     */
    protected DigestCryptoHash(final String type) {
        this.type = type;
    }

    /**
     * Creates an instance that will hash the result from the given CryptoHash.
     *
     * @param chain upstream CryptoHash
     * @param type  digest type
     */
    protected DigestCryptoHash(final CryptoHash chain, final String type) {
        super(chain);
        this.type = type;
    }

    @Override
    protected byte[] computeHash(final InputStream input) throws IOException {
        try {
            MessageDigest md = MessageDigest.getInstance(this.type);
            byte[] data = new byte[BUFFER_SIZE];
            for (int bytesRead = input.read(data); 0 < bytesRead; bytesRead = input.read(data)) {
                md.update(data, 0, bytesRead);
            }
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("no " + this.type + " provider found", e);
        }
    }

    @Override
    protected byte[] computeHash(final byte[] input) {
        try {
            MessageDigest md = MessageDigest.getInstance(this.type);
            return md.digest(input);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("no " + this.type + " provider found", e);
        }
    }

    /**
     * Creates a new instance that computes MD5 hashes.
     *
     * @return MD5 instance
     */
    public static DigestCryptoHash createMD5() {
        return new DigestCryptoHash("MD5");
    }

    /**
     * Creates a new instance that computes MD5 hashes.
     *
     * @param chain upstream CryptoHash
     * @return MD5 instance
     */
    public static DigestCryptoHash createMD5(final CryptoHash chain) {
        return new DigestCryptoHash(chain, "MD5");
    }

    /**
     * Creates a new instance that computes SHA1 hashes.
     *
     * @return SHA1 instance
     */
    public static DigestCryptoHash createSHA1() {
        return new DigestCryptoHash("SHA-1");
    }

    /**
     * Creates a new instance that computes SHA1 hashes.
     *
     * @param chain upstream CryptoHash
     * @return SHA1 instance
     */
    public static DigestCryptoHash createSHA1(final CryptoHash chain) {
        return new DigestCryptoHash(chain, "SHA-1");
    }
}
