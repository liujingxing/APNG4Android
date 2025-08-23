package com.github.penfeizhou.animation.webp.io;

import com.github.penfeizhou.animation.io.ByteBufferWriter;

/**
 * @Description: WebPWriter
 * @Author: pengfei.zhou
 * @CreateDate: 2019-05-12
 */
public class WebPWriter extends ByteBufferWriter {

    public void putUInt16(int val) {
        putByte((byte) (val & 0xff));
        putByte((byte) ((val >> 8) & 0xff));
    }

    public void putUInt24(int val) {
        putByte((byte) (val & 0xff));
        putByte((byte) ((val >> 8) & 0xff));
        putByte((byte) ((val >> 16) & 0xff));
    }

    public void putUInt32(int val) {
        putByte((byte) (val & 0xff));
        putByte((byte) ((val >> 8) & 0xff));
        putByte((byte) ((val >> 16) & 0xff));
        putByte((byte) ((val >> 24) & 0xff));
    }

    public void put1Based(int i) {
        putUInt24(i - 1);
    }

    public void putFourCC(String fourCC) {
        if (fourCC == null || fourCC.length() != 4) {
            skip(4);
            return;
        }
        putByte((byte) (fourCC.charAt(0) & 0xff));
        putByte((byte) (fourCC.charAt(1) & 0xff));
        putByte((byte) (fourCC.charAt(2) & 0xff));
        putByte((byte) (fourCC.charAt(3) & 0xff));
    }
}
