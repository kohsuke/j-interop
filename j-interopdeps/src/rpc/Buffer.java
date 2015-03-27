/**
* Donated by Jarapac (http://jarapac.sourceforge.net/) and released under EPL.
* 
* j-Interop (Pure Java implementation of DCOM protocol)
*     
* Copyright (c) 2013 Vikram Roopchand
* 
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
* Vikram Roopchand  - Moving to EPL from LGPL v1.
*  
*/

package rpc;

public class Buffer {

    public static final int NO_INCREMENT = 0;

    private byte[] buffer;

    private int capacityIncrement = NO_INCREMENT;

    private int index = 0;

    private int length;

    public Buffer() {
        this(null, NO_INCREMENT);
    }

    public Buffer(int capacityIncrement) {
        this(null, capacityIncrement);
    }

    public Buffer(byte[] buffer) {
        this(buffer, NO_INCREMENT);
    }

    public Buffer(byte[] buffer, int capacityIncrement) {
        setBuffer(buffer);
        setCapacityIncrement(capacityIncrement);
    }

    public int getCapacity() {
        return buffer.length;
    }

    public int getCapacityIncrement() {
        return capacityIncrement;
    }

    public void setCapacityIncrement(int capacityIncrement) {
        this.capacityIncrement = capacityIncrement;
    }

    public byte[] getBuffer() {
        return buffer;
    }

    public void setBuffer(byte[] buffer) {
        this.buffer = (buffer != null) ? buffer : new byte[0];
        this.index = 0;
        this.length = 0;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
        if (length > buffer.length) grow(length);
    }

    public byte[] copy() {
        byte[] copy = new byte[length];
        System.arraycopy(buffer, 0, copy, 0, length);
        return copy;
    }

    public void reset() {
        length = 0;
        index = 0;
    }

    public int getIndex() {
        return index;
    }

    public int getIndex(int advance) {
        try {
            return index;
        } finally {
            index += advance;
            if (index > length) length = index;
            if (length > buffer.length) grow(length);
        }
    }

    public void setIndex(int index) {
        this.index = index;
        if (index > length) length = index;
        if (length > buffer.length) grow(length);
    }

    public int align(int boundary) {
        int align = index % boundary;
        if (align == 0) return 0;
        advance(align = boundary - align);
        return align;
    }

    public int align(int boundary, byte value) {
        int align = index % boundary;
        if (align == 0) return 0;
        advance(align = boundary - align, value);
        return align;
    }

    public int advance(int step) {
        index += step;
        if (index > length) length = index;
        if (length > buffer.length) grow(length);
        return index;
    }

    public int advance(int step, byte value) {
        for (int finish = index + step; index < finish; index++) {
            buffer[index] = value;
        }
        if (index > length) length = index;
        if (length > buffer.length) grow(length);
        return index;
    }

    private void grow(int length) {
        if (capacityIncrement <= 0) throw new IndexOutOfBoundsException();
        int newLength = buffer.length;
        while (newLength < length) newLength += capacityIncrement;
        byte[] newBuffer = new byte[newLength];
        System.arraycopy(buffer, 0, newBuffer, 0, buffer.length);
        buffer = newBuffer;
    }

}
