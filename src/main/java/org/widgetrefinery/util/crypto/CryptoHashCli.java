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

import org.widgetrefinery.util.StringUtil;
import org.widgetrefinery.util.clParser.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Since: 3/1/12 10:49 PM
 */
public class CryptoHashCli {
    public static void main(String[] args) throws IOException {
        CLParser clParser = new CLParser(args,
                                         new Argument("e|encoding",
                                                      new ListArgumentType(new StringArgumentType("[cms]+")),
                                                      "Provide a sequence of encodings to apply. Possible values are:\n\tc: crc32\n\tm: md5\n\ts: sha1"),
                                         new Argument("s|string",
                                                      new ListArgumentType(new StringArgumentType()),
                                                      "Encodes the given string."),
                                         new Argument("h|help",
                                                      new BooleanArgumentType(),
                                                      "Displays this help message."));

        if (!clParser.hasArguments() || Boolean.TRUE == clParser.getValue("help")) {
            System.err.println(clParser.getHelpMessage(CryptoHashCli.class,
                                                       new String[]{"[input filename]", "[input filename]", "..."},
                                                       "Computes various hashes against the given input data. Input data can come\nfrom filenames on the command line or stdin."));
            System.exit(0);
        }

        List<String> encodings = clParser.getValue("encoding");
        MultiCryptoHash multiCryptoHash = buildMultiCryptoHash(encodings);
        if (null != multiCryptoHash) {
            boolean processStdin = true;
            if (!clParser.getLeftovers().isEmpty()) {
                for (String filename : clParser.getLeftovers()) {
                    byte[][] results = multiCryptoHash.getHashes(new FileInputStream(filename));
                    outputResults("file|" + filename, results);
                }
                processStdin = false;
            }
            List<String> stringValues = clParser.getValue("string");
            if (null != stringValues && !stringValues.isEmpty()) {
                for (String stringValue : stringValues) {
                    byte[][] results = multiCryptoHash.getHashes(stringValue);
                    outputResults("input|" + stringValue, results);
                }
                processStdin = false;
            }
            if (processStdin) {
                byte[][] results = multiCryptoHash.getHashes(System.in);
                outputResults("stdin", results);
            }
        }
    }

    protected static MultiCryptoHash buildMultiCryptoHash(final List<String> encodings) {
        List<CryptoHash> cryptoHashes = new ArrayList<CryptoHash>();
        if (null != encodings && !encodings.isEmpty()) {
            for (String encoding : encodings) {
                CryptoHash cryptoHash = buildCryptoHash(encoding);
                if (null != cryptoHash) {
                    cryptoHashes.add(cryptoHash);
                }
            }
        }

        MultiCryptoHash result = null;
        if (!cryptoHashes.isEmpty()) {
            CryptoHash[] args = new CryptoHash[cryptoHashes.size()];
            cryptoHashes.toArray(args);
            result = new MultiCryptoHash(args);
        }

        return result;
    }

    protected static CryptoHash buildCryptoHash(final String encoding) {
        CryptoHash cryptoHash = null;
        if (StringUtil.isNotBlank(encoding)) {
            for (char encodingKey : encoding.toCharArray()) {
                switch (encodingKey) {
                    case 'c':
                        cryptoHash = new CRC32CryptoHash(cryptoHash);
                        break;
                    case 'm':
                        cryptoHash = DigestCryptoHash.createMD5(cryptoHash);
                        break;
                    case 's':
                        cryptoHash = DigestCryptoHash.createSHA1(cryptoHash);
                        break;
                    default:
                        throw new RuntimeException("invalid encoding " + encodingKey);
                }
            }
        }
        return cryptoHash;
    }

    protected static void outputResults(final String prefix, final byte[][] results) {
        StringBuilder sb = new StringBuilder(prefix).append(':');
        for (byte[] result : results) {
            sb.append(' ').append(StringUtil.toHexString(result));
        }
        System.out.println(sb.toString());
    }
}
