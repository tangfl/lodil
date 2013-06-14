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

import static com.weibo.lodil.mmap.HugeArrayBuilderTest.populate;
import static com.weibo.lodil.mmap.HugeArrayBuilderTest.validate;
import static junit.framework.Assert.assertEquals;

import java.io.IOException;

import com.weibo.lodil.mmap.api.HugeArrayList;

public class MemoryMappedTest {

	public static final String TEMPORARY_SPACE = System.getProperty("java.io.tmpdir");

	interface MutableBooleans {
		public void setOne(boolean b);

		public boolean getOne();

		public void setTwo(boolean b);

		public boolean getTwo();

		public void setThree(Boolean b);

		public Boolean getThree();

		public void setFour(Boolean b);

		public Boolean getFour();

	}

	// @Test
	public void testMemoryMappedBoolean() throws IOException {
		final int length = 1024 * 1024;
		final HugeArrayBuilder<MutableBooleans> hab = new HugeArrayBuilder<MutableBooleans>() {
			{
				baseDirectory = TEMPORARY_SPACE;
				capacity = length;
				allocationSize = 256 * 1024;
			}
		};

		final HugeArrayList<MutableBooleans> list = hab.create();
		list.setSize(length);
		int i = 0;
		for (final MutableBooleans mi : list) {
			mi.setOne((i % 11) < 6);
			mi.setTwo((i % 13) < 7);
			mi.setThree((i % 15) < 8);
			mi.setFour((i % 17) < 9);
			i++;
		}
		list.close();

		final HugeArrayBuilder<MutableBooleans> hab2 = new HugeArrayBuilder<MutableBooleans>() {
			{
				baseDirectory = TEMPORARY_SPACE;
				capacity = length;
				allocationSize = 256 * 1024;
			}
		};
		final HugeArrayList<MutableBooleans> list2 = hab2.create();
		list2.setSize(length);
		int j = 0;
		for (final MutableBooleans mi : list) {
			assertEquals((j % 11) < 6, mi.getOne());
			assertEquals((j % 13) < 7, mi.getTwo());
			assertEquals((Boolean) ((j % 15) < 8), mi.getThree());
			assertEquals((Boolean) ((j % 17) < 9), mi.getFour());
			j++;
		}
		list2.close();
	}

	interface MutableBytes {
		public void setOne(byte b);

		public byte getOne();

		public void setTwo(byte b);

		public byte getTwo();

		public void setThree(Byte b);

		public Byte getThree();

		public void setFour(Byte b);

		public Byte getFour();

	}

	// @Test
	public void testMemoryMappedByte() throws IOException {
		final int length = 1024 * 1024;
		final HugeArrayBuilder<MutableBytes> hab = new HugeArrayBuilder<MutableBytes>() {
			{
				baseDirectory = TEMPORARY_SPACE;
				capacity = length;
				allocationSize = 256 * 1024;
			}
		};
		final HugeArrayList<MutableBytes> list = hab.create();
		list.setSize(length);
		int i = 0;
		for (final MutableBytes mi : list) {
			mi.setOne((byte) i);
			mi.setTwo((byte) (i * 13));
			mi.setThree((byte) (i * 31));
			mi.setFour((byte) (i * 37));
			i++;
		}
		list.close();

		final HugeArrayBuilder<MutableBytes> hab2 = new HugeArrayBuilder<MutableBytes>() {
			{
				baseDirectory = TEMPORARY_SPACE;
				capacity = length;
				allocationSize = 256 * 1024;
			}
		};
		final HugeArrayList<MutableBytes> list2 = hab2.create();
		list2.setSize(length);
		int j = 0;
		for (final MutableBytes mi : list) {
			assertEquals((byte) j, mi.getOne());
			assertEquals((byte) (j * 13), mi.getTwo());
			assertEquals((Byte) (byte) (j * 31), mi.getThree());
			assertEquals((Byte) (byte) (j * 37), mi.getFour());
			j++;
		}
		list2.close();
	}

	interface MutableInts {
		public void setInt(int i);

		public int getInt();

		public void setInt2(int i);

		public int getInt2();
	}

	// @Test
	public void testMemoryMappedInt() throws IOException {
		final int length = 1024 * 1024;
		final HugeArrayBuilder<MutableInts> hab = new HugeArrayBuilder<MutableInts>() {
			{
				baseDirectory = TEMPORARY_SPACE;
				capacity = length;
				allocationSize = 256 * 1024;
			}
		};
		final HugeArrayList<MutableInts> list = hab.create();
		list.setSize(length);
		int i = 0;
		for (final MutableInts mi : list) {
			mi.setInt(i++);
			mi.setInt2(i++);
		}
		list.close();

		final HugeArrayBuilder<MutableInts> hab2 = new HugeArrayBuilder<MutableInts>() {
			{
				baseDirectory = TEMPORARY_SPACE;
				capacity = length;
				allocationSize = 256 * 1024;
			}
		};
		final HugeArrayList<MutableInts> list2 = hab2.create();
		list2.setSize(length);
		int j = 0;
		for (final MutableInts mi : list) {
			assertEquals(j++, mi.getInt());
			assertEquals(j++, mi.getInt2());
		}
		list2.close();
	}

	interface MutableString {
		public void setOne(String s1);

		public String getOne();

		public void setTwo(String s2);

		public String getTwo();

	}

	// @Test
	public void testMemoryMappedString() throws IOException {
		System.out.println("run on dir: " + TEMPORARY_SPACE);

		final int length = 1024;
		final HugeArrayBuilder<MutableString> hab = new HugeArrayBuilder<MutableString>() {
			{
				baseDirectory = TEMPORARY_SPACE;
				capacity = length;
				allocationSize = 256 * 1024;
			}
		};
		final HugeArrayList<MutableString> list = hab.create();
		list.setSize(length);
		int i = 0;
		for (final MutableString mi : list) {
			mi.setOne("one" + i);
			mi.setTwo("two" + i);
			i++;
		}
		list.close();

		final HugeArrayBuilder<MutableString> hab2 = new HugeArrayBuilder<MutableString>() {
			{
				baseDirectory = TEMPORARY_SPACE;
				capacity = length;
				allocationSize = 256 * 1024;
			}
		};
		final HugeArrayList<MutableString> list2 = hab2.create();
		list2.setSize(length);
		int j = 0;
		for (final MutableString mi : list) {
			assertEquals("one" + j, mi.getOne());
			assertEquals("two" + j, mi.getTwo());
			j++;
		}
		list.clear();
		list2.close();
	}

	// @Test
	public void testMemoryMapped2() throws IOException {
		System.out.println("Writing/scanning " + TEMPORARY_SPACE);
		final long start = System.nanoTime();
		final int length = 15 * 1000 * 1000;
		final HugeArrayBuilder<MutableTypes> hab = new HugeArrayBuilder<MutableTypes>() {
			{
				baseDirectory = TEMPORARY_SPACE;
				capacity = length;
				allocationSize = 3 * 1024 * 1024;
			}
		};
		final HugeArrayList<MutableTypes> list = hab.create();
		list.setSize(length);
		populate(list);
		System.out.println("... flushing");
		list.close();
		final long mid = System.nanoTime();

		System.out.println("Sequential read test");
		final HugeArrayBuilder<MutableTypes> hab2 = new HugeArrayBuilder<MutableTypes>() {
			{
				baseDirectory = TEMPORARY_SPACE;
				capacity = length;
				allocationSize = 3 * 1024 * 1024;
			}
		};
		final HugeArrayList<MutableTypes> list2 = hab2.create();
		list2.setSize(length);
		validate(list2);
		list2.close();
		final long mid2 = System.nanoTime();
		// Jump around the array

		final HugeArrayBuilder<MutableTypes> hab3 = new HugeArrayBuilder<MutableTypes>() {
			{
				baseDirectory = TEMPORARY_SPACE;
				capacity = length;
				allocationSize = 3 * 1024 * 1024;
			}
		};
		System.out.println("Random access test");
		final HugeArrayList<MutableTypes> list3 = hab3.create();
		list3.setSize(length);
		for (int i = length - 1; i >= 0; i -= 101) {
			final MutableTypes mb = list3.get(i);
			validate(mb, i);
			list3.recycle(mb);
		}
		list3.close();
		final long end = System.nanoTime();

		System.out.printf("Took average of %,d ns to write, %,d ns to read and %,d ns to random read each element%n",
				(mid - start) / length, (mid2 - mid) / length, (101 * (end - mid2)) / length);
	}

}
