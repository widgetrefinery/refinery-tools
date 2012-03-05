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

import org.widgetrefinery.util.clParser.Argument;
import org.widgetrefinery.util.clParser.BooleanArgumentType;
import org.widgetrefinery.util.clParser.CLParser;
import org.widgetrefinery.util.clParser.StringArgumentType;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.zip.CRC32;

/**
 * Since: 3/1/12 10:49 PM
 */
public class CryptoHashUtil {
    private static final int BUFFER_SIZE = 1024;

    public byte[] crc32(final byte[] input) {
        CRC32 crc32 = new CRC32();
        crc32.update(input);
        return toByteArray((int) crc32.getValue());
    }

    public byte[] crc32(final InputStream input) {
        try {
            CRC32 crc32 = new CRC32();
            byte[] data = new byte[BUFFER_SIZE];
            for (int bytesRead = input.read(data); 0 < bytesRead; bytesRead = input.read(data)) {
                crc32.update(data, 0, bytesRead);
            }
            return toByteArray((int) crc32.getValue());
        } catch (IOException e) {
            throw new RuntimeException("unexpected error reading data: " + e.getMessage(), e);
        }
    }

    protected byte[] toByteArray(final int input) {
        return new byte[]{
                (byte) ((input >> 24) & 0xFF),
                (byte) ((input >> 16) & 0xFF),
                (byte) ((input >> 8) & 0xFF),
                (byte) (input & 0xFF)
        };
    }

    public byte[] md5(final byte[] input) {
        return digest("MD5", input);
    }

    public byte[] md5(final InputStream input) {
        return digest("MD5", input);
    }

    public byte[] sha1(final byte[] input) {
        return digest("SHA-1", input);
    }

    public byte[] sha1(final InputStream input) {
        return digest("SHA-1", input);
    }

    protected byte[] digest(final String type, final byte[] input) {
        try {
            MessageDigest md = MessageDigest.getInstance(type);
            return md.digest(input);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("no " + type + " provider found", e);
        }
    }

    protected byte[] digest(final String type, final InputStream input) {
        try {
            MessageDigest md = MessageDigest.getInstance(type);
            byte[] data = new byte[BUFFER_SIZE];
            for (int bytesRead = input.read(data); 0 < bytesRead; bytesRead = input.read(data)) {
                md.update(data, 0, bytesRead);
            }
            return md.digest();
        } catch (IOException e) {
            throw new RuntimeException("unexpected error reading data: " + e.getMessage(), e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("no " + type + " provider found", e);
        }
    }

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

    public static void main(String[] args) {
        CLParser clParser = new CLParser(args,
                                         new Argument("e|encoding",
                                                      new StringArgumentType(),
                                                      "Provide a sequence of encodings to apply. Possible values are:\n\tc: crc32\n\tm: md5\n\ts: sha1"),
                                         new Argument("h|help",
                                                      new BooleanArgumentType(),
                                                      "Displays this help message."));

        if (!clParser.hasValues() || Boolean.TRUE == clParser.getValue("help")) {
            System.err.println(clParser.getHelpMessage(CryptoHashUtil.class,
                                                       "Computes various hashes against the given input data. Input data can come\nfrom filenames on the command line or stdin."));
            System.exit(0);
        }

        String encoding = clParser.getValue("encoding");
        if (null != encoding && !encoding.trim().isEmpty()) {
            CryptoHashUtil cryptoHashUtil = new CryptoHashUtil();
            if (!clParser.getLeftovers().isEmpty()) {
                for (String filename : clParser.getLeftovers()) {
                    processFile(cryptoHashUtil, Arrays.asList(encoding), filename);
                }
            } else {
                byte[] data = applyEncodings(cryptoHashUtil, encoding.toCharArray(), System.in);
                String hexString = cryptoHashUtil.toString(data);
                System.out.println(hexString);
            }
        }
    }

    protected static void processFile(final CryptoHashUtil cryptoHashUtil, final List<String> encodings, final String filename) {
        System.out.print(filename);
        try {
            FileInputStream fileInputStream = new FileInputStream(filename);
            for (String encoding : encodings) {
                fileInputStream.reset();
                byte[] data = applyEncodings(cryptoHashUtil, encoding.trim().toCharArray(), fileInputStream);
                String hexString = cryptoHashUtil.toString(data);
                System.out.print(" " + hexString);
            }
            System.out.println();
        } catch (IOException e) {
            throw new RuntimeException("failed to read file " + filename + ": " + e.getMessage(), e);
        }
    }

    protected static byte[] applyEncodings(final CryptoHashUtil cryptoHashUtil, final char[] encoding, final InputStream input) {
        byte[] data;
        char encodingKey = encoding[0];
        switch (encodingKey) {
            case 'c':
                data = cryptoHashUtil.crc32(input);
                break;
            case 'm':
                data = cryptoHashUtil.md5(input);
                break;
            case 's':
                data = cryptoHashUtil.sha1(input);
                break;
            default:
                throw new RuntimeException("invalid encoding " + encodingKey);
        }
        if (1 < encoding.length) {
            data = applyRemainingEncodings(cryptoHashUtil, encoding, data);
        }
        return data;
    }

    protected static byte[] applyRemainingEncodings(final CryptoHashUtil cryptoHashUtil, final char[] encoding, byte[] data) {
        for (int ndx = 1; ndx < encoding.length; ndx++) {
            char encodingKey = encoding[ndx];
            switch (encodingKey) {
                case 'c':
                    data = cryptoHashUtil.crc32(data);
                    break;
                case 'm':
                    data = cryptoHashUtil.md5(data);
                    break;
                case 's':
                    data = cryptoHashUtil.sha1(data);
                    break;
                default:
                    throw new RuntimeException("invalid encoding " + encodingKey);
            }
        }
        return data;
    }

}
