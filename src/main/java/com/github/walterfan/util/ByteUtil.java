package com.github.walterfan.util;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;

public class ByteUtil {
    public static void main(String[] args) {
        short s = -20;
        byte[] b = new byte[2];
        putShort(b, s, 0);
        ByteBuffer buf = ByteBuffer.allocate(2);
        buf.put(b);
        buf.flip();
        System.out.println(getShort(b, 0));
        System.out.println(buf.getShort());
        System.out.println("========== int =================");
        int i = -40;
        b = new byte[4];
        putInt(b, i, 0);
        buf = ByteBuffer.allocate(4);
        buf.put(b);
        buf.flip();
        System.out.println(getInt(b, 0));
        System.out.println(buf.getInt());
        System.out.println("=========== long ================");
        long l = -40;
        b = new byte[8];
        putLong(b, l, 0);
        buf = ByteBuffer.allocate(8);
        buf.put(b);
        buf.flip();
        System.out.println(getLong(b, 0));
        System.out.println(buf.getLong());
        System.out.println("========== object2bytes =================");
        
        try {
            String name = "walter";
            byte[] aBytes = object2Bytes(name);
            String name1 = (String)bytes2Object(aBytes);
            System.out.println("name1=" + name1);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void putShort(byte b[], short s, int index) {
        b[index] = (byte) (s >> 8);
        b[index + 1] = (byte) (s >> 0);
    }

    public static short getShort(byte[] b, int index) {
        return (short) (((b[index] << 8) | b[index + 1] & 0xff));
    }

    // ///////////////////////////////////////////////////////
    public static void putInt(byte[] bb, int x, int index) {
        bb[index + 0] = (byte) (x >> 24);
        bb[index + 1] = (byte) (x >> 16);
        bb[index + 2] = (byte) (x >> 8);
        bb[index + 3] = (byte) (x >> 0);
    }

    public static int getInt(byte[] bb, int index) {
        return (int) ((((bb[index + 0] & 0xff) << 24)
                | ((bb[index + 1] & 0xff) << 16)
                | ((bb[index + 2] & 0xff) << 8) | ((bb[index + 3] & 0xff) << 0)));
    }

    // /////////////////////////////////////////////////////////
    public static void putLong(byte[] bb, long x, int index) {
        for (int i = 0; i < 8; i++) {
            bb[index + i] = (byte) (x >> ((7 - i) * 8));
        }

    }

    public static long getLong(byte[] bb, int index) {
        return ((((long) bb[index + 0] & 0xff) << 56)
                | (((long) bb[index + 1] & 0xff) << 48)
                | (((long) bb[index + 2] & 0xff) << 40)
                | (((long) bb[index + 3] & 0xff) << 32)
                | (((long) bb[index + 4] & 0xff) << 24)
                | (((long) bb[index + 5] & 0xff) << 16)
                | (((long) bb[index + 6] & 0xff) << 8) | (((long) bb[index + 7] & 0xff) << 0));
    }

    public static byte[] object2Bytes(Serializable data) throws IOException {
        ByteArrayOutputStream baOut = null;
        ObjectOutputStream objOut = null;
        try {
            baOut = new ByteArrayOutputStream();
            objOut = new ObjectOutputStream(baOut);
            objOut.writeObject(data);
            objOut.flush();
            return baOut.toByteArray();
        } finally {
            IOUtils.closeQuietly(objOut);
            IOUtils.closeQuietly(baOut);
        }

    }

    public static Serializable bytes2Object(byte[] bytes) throws IOException,
            ClassNotFoundException {

        ByteArrayInputStream baIn = null;
        ObjectInputStream objIn = null;
        try {
            baIn = new ByteArrayInputStream(bytes);
            objIn = new ObjectInputStream(baIn);
            return (Serializable) objIn.readObject();
        } finally {
            IOUtils.closeQuietly(objIn);
            
            IOUtils.closeQuietly(baIn);
        }

    }

}
