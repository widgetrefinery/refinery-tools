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

import org.widgetrefinery.util.BadUserInputException;
import org.widgetrefinery.util.StringUtil;
import org.widgetrefinery.util.cl.*;
import org.widgetrefinery.util.lang.Translator;
import org.widgetrefinery.util.lang.UtilTranslationKey;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides a command-line interface to the hashing functions. This was built
 * mostly to test out {@link org.widgetrefinery.util.cl.CLParser}.
 *
 * @since 3/1/12 10:49 PM
 */
public class Cli extends AbstractCli {
    public static void main(String[] args) throws IOException {
        new Cli().start(args);
    }

    @Override
    protected void processCommandLine(final String[] args) throws IllegalArgumentException, IOException, BadUserInputException {
        CLParser clParser = new CLParser(args,
                                         new Argument("e|encoding",
                                                      new ListArgumentType(new StringArgumentType("[cms]+")),
                                                      "Provide a sequence of encodings to apply. Possible values are:\n\tc: crc32\n\tm: md5\n\ts: sha1"),
                                         new Argument("s|string",
                                                      new ListArgumentType(new StringArgumentType()),
                                                      "Encodes the given string."),
                                         new Argument("h|help",
                                                      new BooleanArgumentType(),
                                                      "Displays this help message."),
                                         new Argument("l|license",
                                                      new BooleanArgumentType(),
                                                      "Displays the GPLv3 license that this software is released under."));

        if (!clParser.hasArguments() || Boolean.TRUE == clParser.getValue("help")) {
            System.err.println(clParser.getHelpMessage(Cli.class));
            System.exit(0);
        }
        if (Boolean.TRUE == clParser.getValue("license")) {
            clParser.getLicense(System.out);
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

    protected MultiCryptoHash buildMultiCryptoHash(final List<String> encodings) throws BadUserInputException {
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

    protected CryptoHash buildCryptoHash(final String encoding) throws BadUserInputException {
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
                        throw new BadUserInputException(Translator.get(UtilTranslationKey.CL_ERROR_BAD_SWITCH_VALUE, "encoding", encodingKey), encodingKey);
                }
            }
        }
        return cryptoHash;
    }

    protected void outputResults(final String prefix, final byte[][] results) {
        StringBuilder sb = new StringBuilder(prefix).append(':');
        for (byte[] result : results) {
            sb.append(' ').append(StringUtil.toHexString(result));
        }
        System.out.println(sb.toString());
    }
}
