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
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Since: 3/4/12 10:36 PM
 */
public class MultiCryptoHash {
    private static final int BUFFER_SIZE = 1024;

    private final CryptoHash[] cryptoHashes;

    public MultiCryptoHash(final CryptoHash... cryptoHashes) {
        this.cryptoHashes = cryptoHashes;
    }

    public byte[][] getHashes(InputStream input) throws IOException {
        List<PipedOutputStream> sources = new ArrayList<PipedOutputStream>(this.cryptoHashes.length);
        List<CryptoThread> threads = new ArrayList<CryptoThread>(this.cryptoHashes.length);
        byte[][] results = new byte[this.cryptoHashes.length][];
        int resultsNdx = 0;
        CryptoHashException errors = new CryptoHashException();

        try {
            for (CryptoHash cryptoHash : this.cryptoHashes) {
                PipedOutputStream source = new PipedOutputStream();
                CryptoThread thread = new CryptoThread(cryptoHash, source);
                sources.add(source);
                threads.add(thread);
                thread.start();
            }
            byte[] buffer = new byte[BUFFER_SIZE];
            for (int bytesRead = input.read(buffer); 0 < bytesRead; bytesRead = input.read(buffer)) {
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
                    errors.getErrors().add(thread.getError());
                } else {
                    results[resultsNdx++] = thread.getResult();
                }
            }
        }

        if (!errors.getErrors().isEmpty()) {
            throw errors;
        }

        return results;
    }

    public byte[][] getHashes(String input) {
        byte[][] results = new byte[this.cryptoHashes.length][];
        for (int ndx = 0; ndx < results.length; ndx++) {
            results[ndx] = this.cryptoHashes[ndx].getHash(input);
        }
        return results;
    }

    public static class CryptoHashException extends RuntimeException {
        private final List<Exception> errors;

        public CryptoHashException() {
            super("unexpected error processing data");
            this.errors = new ArrayList<Exception>();
        }

        public List<Exception> getErrors() {
            return this.errors;
        }
    }

    protected static class CryptoThread extends Thread {
        private final CryptoHash       cryptoHash;
        private final PipedInputStream inputStream;
        private       byte[]           result;
        private       Exception        error;

        public CryptoThread(final CryptoHash cryptoHash, final PipedOutputStream source) throws IOException {
            this.cryptoHash = cryptoHash;
            this.inputStream = new PipedInputStream(source);
        }

        @Override
        public void run() {
            try {
                this.result = this.cryptoHash.getHash(this.inputStream);
            } catch (Exception e) {
                this.error = e;
            }
        }

        public byte[] getResult() {
            return this.result;
        }

        public Exception getError() {
            return this.error;
        }
    }
}
