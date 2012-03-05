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

import org.widgetrefinery.util.clParser.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Since: 3/1/12 10:49 PM
 */
public class CryptoHashCli {
    private static final int BUFFER_SIZE = 1024;

    public static void main(String[] args) throws IOException {
        CLParser clParser = new CLParser(args,
                                         new Argument("e|encoding",
                                                      new ListArgumentType(new StringArgumentType()),
                                                      "Provide a sequence of encodings to apply. Possible values are:\n\tc: crc32\n\tm: md5\n\ts: sha1"),
                                         new Argument("h|help",
                                                      new BooleanArgumentType(),
                                                      "Displays this help message."));

        if (!clParser.hasArguments() || Boolean.TRUE == clParser.getValue("help")) {
            System.err.println(clParser.getHelpMessage(CryptoHashCli.class,
                                                       "Computes various hashes against the given input data. Input data can come\nfrom filenames on the command line or stdin."));
            System.exit(0);
        }

        List<String> encodings = clParser.getValue("encoding");
        List<CryptoHash> cryptoHashes = buildCryptoHashes(encodings);

        if (!clParser.getLeftovers().isEmpty()) {
            for (String filename : clParser.getLeftovers()) {
                List<String> results = processFile(filename, cryptoHashes);
                StringBuilder sb = new StringBuilder(filename);
                for (String result : results) {
                    sb.append(' ').append(result);
                }
                System.out.println(sb.toString());
            }
        } else {
            List<String> results = processInputStream(System.in, cryptoHashes);
            for (String result : results) {
                System.out.println(result);
            }
        }
    }

    protected static List<CryptoHash> buildCryptoHashes(final List<String> encodings) {
        List<CryptoHash> results = new ArrayList<CryptoHash>();
        if (null != encodings && !encodings.isEmpty()) {
            for (String encoding : encodings) {
                if (!"".equals(encoding)) {
                    CryptoHash cryptoHash = null;
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
                    results.add(cryptoHash);
                }
            }
        }
        return results;
    }

    protected static List<String> processFile(final String filename, final List<CryptoHash> cryptoHashes) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(filename);
        return processInputStream(fileInputStream, cryptoHashes);
    }

    private static List<String> processInputStream(final InputStream inputStream, final List<CryptoHash> cryptoHashes) throws IOException {
        List<PipedOutputStream> sources = new ArrayList<PipedOutputStream>(cryptoHashes.size());
        List<CryptoThread> threads = new ArrayList<CryptoThread>(cryptoHashes.size());
        List<String> results = new ArrayList<String>(cryptoHashes.size());
        boolean hasError = false;

        try {
            for (CryptoHash cryptoHash : cryptoHashes) {
                PipedOutputStream source = new PipedOutputStream();
                sources.add(source);
                CryptoThread thread = new CryptoThread(cryptoHash, source);
                threads.add(thread);
                thread.start();
            }
            byte[] buffer = new byte[BUFFER_SIZE];
            for (int bytesRead = inputStream.read(buffer); 0 < bytesRead; bytesRead = inputStream.read(buffer)) {
                for (PipedOutputStream source : sources) {
                    source.write(buffer, 0, bytesRead);
                }
            }
            for (PipedOutputStream source : sources) {
                source.flush();
                source.close();
            }
            Thread.yield();
        } finally {
            for (CryptoThread thread : threads) {
                try {
                    thread.join(1000);
                } catch (InterruptedException e) {
                    thread.stop();
                }
                if (null != thread.getError()) {
                    thread.getError().printStackTrace(System.err);
                    hasError = true;
                } else {
                    results.add(thread.getResult());
                }
            }
        }

        if (hasError) {
            throw new RuntimeException("unexpected error processing data");
        }

        return results;
    }

    protected static class CryptoThread extends Thread {
        private final CryptoHash       cryptoHash;
        private final PipedInputStream inputStream;
        private       String           result;
        private       Exception        error;

        public CryptoThread(final CryptoHash cryptoHash, final PipedOutputStream source) throws IOException {
            this.cryptoHash = cryptoHash;
            this.inputStream = new PipedInputStream(source);
        }

        @Override
        public void run() {
            try {
                byte[] result = this.cryptoHash.getHash(this.inputStream);
                this.result = this.cryptoHash.toString(result);
            } catch (Exception e) {
                this.error = e;
            }
        }

        public String getResult() {
            return this.result;
        }

        public Exception getError() {
            return this.error;
        }
    }
}
