package com.thefinestartist.royal.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by TheFinestArtist on 7/12/15.
 */
public class ByteUtil {

    public static int byteArrayToLeInt(byte[] b) {
        final ByteBuffer bb = ByteBuffer.wrap(b);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        return bb.getInt();
    }

    public static byte[] leIntToByteArray(int i) {
        final ByteBuffer bb = ByteBuffer.allocate(Integer.SIZE / Byte.SIZE);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.putInt(i);
        return bb.array();
    }

//    public static int byteArrayToLeInt(byte[] encodedValue) {
//        int value = (encodedValue[3] << (Byte.SIZE * 3));
//        value |= (encodedValue[2] & 0xFF) << (Byte.SIZE * 2);
//        value |= (encodedValue[1] & 0xFF) << (Byte.SIZE * 1);
//        value |= (encodedValue[0] & 0xFF);
//        return value;
//    }
//
//    public static byte[] leIntToByteArray(int value) {
//        byte[] encodedValue = new byte[Integer.SIZE / Byte.SIZE];
//        encodedValue[3] = (byte) (value >> Byte.SIZE * 3);
//        encodedValue[2] = (byte) (value >> Byte.SIZE * 2);
//        encodedValue[1] = (byte) (value >> Byte.SIZE);
//        encodedValue[0] = (byte) value;
//        return encodedValue;
//    }

}
