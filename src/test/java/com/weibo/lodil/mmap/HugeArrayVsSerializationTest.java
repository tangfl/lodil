package com.weibo.lodil.mmap;

/*
 * Copyright 2011 Peter Lawrey
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

import static com.weibo.lodil.mmap.HugeArrayBuilderTest.gcPrintUsed;
import static com.weibo.lodil.mmap.HugeArrayBuilderTest.printUsed;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.weibo.lodil.mmap.api.HugeArrayList;



public class HugeArrayVsSerializationTest {
	static final int length = 1000 * 1000;

	static {
		gcPrintUsed();
	}

	@Test
	public void testSearchAndUpdateCollection() {
		gcPrintUsed();
		final int repeats = 1000; // five seconds.
		final HugeArrayBuilder<MutableTypes> mtb = new HugeArrayBuilder<MutableTypes>() {
		};
		final HugeArrayList<MutableTypes> mts = mtb.create();
		mts.setSize(length);
		for (final MutableTypes mt : mts) {
			mt.setBoolean2(true);
			mt.setByte2((byte) 1);
		}
		gcPrintUsed();

		final long start = System.nanoTime();
		for (int i = 0; i < repeats; i++) {
			for (final MutableTypes mt : mts) {
				mt.setInt(mt.getInt() + 1);
			}
		}
		final long time = System.nanoTime() - start;
		printUsed();
		System.out.printf("Huge Collection update one field, took an average %.1f ns.%n", (double) time / length
				/ repeats);
	}

	@Test
	public void testSearchAndUpdateCollectionHeap() {
		gcPrintUsed();
		final int repeats = 1000; // five seconds.
		final int length = 1000 * 1000;
		final HugeArrayBuilder<MutableTypes> mtb = new HugeArrayBuilder<MutableTypes>() {
		};
		final List<MutableTypes> mts = new ArrayList<MutableTypes>();
		for (int i = 0; i < length; i++) {
			final MutableTypes bean = mtb.createBean();
			bean.setBoolean2(true);
			bean.setByte2((byte) 1);
			mts.add(bean);
		}

		gcPrintUsed();

		final long start = System.nanoTime();
		for (int i = 0; i < repeats; i++) {
			for (final MutableTypes mt : mts) {
				mt.setInt(mt.getInt() + 1);
			}
		}
		final long time = System.nanoTime() - start;
		printUsed();
		System.out.printf("List<JavaBean>, update one field took an average %.1f ns.%n", (double) time / length
				/ repeats);
	}

	@Test
	public void testSearchAndUpdateCollectionSerialization() throws IOException, ClassNotFoundException {
		gcPrintUsed();
		final int length = 1000 * 1000; // about 8 seconds
		final List<byte[]> mts = new ArrayList<byte[]>();
		final HugeArrayBuilder<MutableTypes> mtb = new HugeArrayBuilder<MutableTypes>() {
		};
		final MutableTypes bean = mtb.createBean();
		bean.setBoolean2(true);
		bean.setByte2((byte) 1);
		final byte[] bytes = toBytes(bean);
		for (int i = 0; i < length; i++) {
			mts.add(bytes.clone());
		}
		System.out.println("Per object size is " + (4 + (((bytes.length + 7 + 12) / 8) * 8)));

		gcPrintUsed();
		final long start = System.nanoTime();
		for (int i = 0, mtsSize = mts.size(); i < mtsSize; i++) {
			final MutableTypes mt = (MutableTypes) fromBytes(mts.get(i));
			mt.setInt(mt.getInt() + 1);
			mts.set(i, toBytes(mt));
		}
		final long time = System.nanoTime() - start;
		printUsed();
		System.out.printf("List<byte[]> with readObject/writeObject update one field took an average %,d ns.%n", time
				/ length);
	}

	@Test
	public void testSearchAndUpdateCollectionSerialization2() throws IOException, ClassNotFoundException {
		gcPrintUsed();
		final int length = 1000 * 1000; // about 8 seconds
		final List<byte[]> mts = new ArrayList<byte[]>();
		final PlainMutableTypes bean = new PlainMutableTypes();
		bean.setBoolean2(true);
		bean.setByte2((byte) 1);
		final byte[] bytes = toBytes(bean);
		for (int i = 0; i < length; i++) {
			mts.add(bytes.clone());
		}
		System.out.println("Per object size is " + (4 + (((bytes.length + 7 + 12) / 8) * 8)));

		gcPrintUsed();
		final long start = System.nanoTime();
		for (int i = 0, mtsSize = mts.size(); i < mtsSize; i++) {
			final MutableTypes mt = (MutableTypes) fromBytes(mts.get(i));
			mt.setInt(mt.getInt() + 1);
			mts.set(i, toBytes(mt));
		}
		final long time = System.nanoTime() - start;
		printUsed();
		System.out.printf("List<byte[]> update one field took an average %,d ns.%n", time / length);
	}

	public static byte[] toBytes(final Object obj) throws IOException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(obj);
		oos.close();
		return baos.toByteArray();
	}

	public static Object fromBytes(final byte[] bytes) throws IOException, ClassNotFoundException {
		final ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
		return ois.readObject();
	}

	static class PlainMutableTypes implements MutableTypes, Serializable {
		private boolean m_boolean;
		private Boolean m_boolean2;
		private byte m_byte;
		private Byte m_byte2;
		private char m_char;
		private short m_short;
		private int m_int;
		private float m_float;
		private long m_long;
		private double m_double;
		private ElementType m_elementType;
		private String m_string;


		public void setBoolean(final boolean b) {
			this.m_boolean = b;
		}


		public boolean getBoolean() {
			return m_boolean;
		}


		public void setBoolean2(final Boolean b) {
			this.m_boolean2 = b;
		}


		public Boolean getBoolean2() {
			return m_boolean2;
		}


		public void setByte(final byte b) {
			this.m_byte = b;
		}


		public byte getByte() {
			return m_byte;
		}


		public void setByte2(final Byte b) {
			this.m_byte2 = b;
		}


		public Byte getByte2() {
			return m_byte2;
		}


		public void setChar(final char ch) {
			this.m_char = ch;
		}


		public char getChar() {
			return m_char;
		}


		public void setShort(final short s) {
			this.m_short = s;
		}


		public short getShort() {
			return m_short;
		}


		public void setInt(final int i) {
			this.m_int = i;
		}


		public int getInt() {
			return m_int;
		}


		public void setFloat(final float f) {
			this.m_float = f;
		}


		public float getFloat() {
			return m_float;
		}


		public void setLong(final long l) {
			this.m_long = l;
		}


		public long getLong() {
			return m_long;
		}


		public void setDouble(final double d) {
			this.m_double = d;
		}


		public double getDouble() {
			return m_double;
		}


		public void setElementType(final ElementType elementType) {
			this.m_elementType = elementType;
		}


		public ElementType getElementType() {
			return m_elementType;
		}


		public void setString(final String text) {
			this.m_string = text;
		}


		public String getString() {
			return m_string;
		}
	}
}
