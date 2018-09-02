/*
 * Copyright (c) 2010-2020 IISI.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of IISI.
 */
package gtu.itext.iisi;

import java.io.IOException;
import java.io.OutputStream;

class CounterOutputStream extends OutputStream {
    int size = 0;

    final OutputStream out;

    public CounterOutputStream(OutputStream out) {
        super();
        this.out = out;
    }

    @Override
    public void write(int b) throws IOException {
        this.out.write(b);
        this.size++;
    }

    @Override
    public int hashCode() {
        return this.out.hashCode();
    }

    @Override
    public void write(byte[] b) throws IOException {
        this.out.write(b);
        this.size += b.length;
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        this.out.write(b, off, len);
        this.size += len;
    }

    @Override
    public boolean equals(Object obj) {
        return this.out.equals(obj);
    }

    @Override
    public void flush() throws IOException {
        this.out.flush();
    }

    @Override
    public void close() throws IOException {
        this.out.close();
    }

    @Override
    public String toString() {
        return this.out.toString();
    }

    /**
     * @return the size
     */
    public int getSize() {
        return this.size;
    }

}
