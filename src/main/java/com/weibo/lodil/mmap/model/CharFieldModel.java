package com.weibo.lodil.mmap.model;

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

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.nio.CharBuffer;

import com.weibo.lodil.mmap.impl.MappedFileChannel;

@SuppressWarnings({ "rawtypes" })
public class CharFieldModel extends AbstractFieldModel<Character> {
	public CharFieldModel(final String fieldName, final int fieldNumber) {
		super(fieldName, fieldNumber);
	}

	public Object arrayOfField(final int size) {
		return newArrayOfField(size, null);
	}

	public int sizeOf(final int elements) {
		return sizeOf0(elements);
	}

	private static int sizeOf0(final int elements) {
		return elements * 2;
	}

	public static CharBuffer newArrayOfField(final int size, final MappedFileChannel mfc) {
		return acquireByteBuffer(mfc, sizeOf0(size)).asCharBuffer();
	}

	public Class storeType() {
		return CharBuffer.class;
	}

	public Character getAllocation(final Object[] arrays, final int index) {
		final CharBuffer array = (CharBuffer) arrays[fieldNumber];
		return get(array, index);
	}

	public static char get(final CharBuffer array, final int index) {
		return array.get(index);
	}

	public void setAllocation(final Object[] arrays, final int index, final Character value) {
		final CharBuffer array = (CharBuffer) arrays[fieldNumber];
		set(array, index, value);
	}

	public static void set(final CharBuffer array, final int index, final char value) {
		array.put(index, value);
	}

	public Class<Character> type() {
		return char.class;
	}

	@Override
	public String bcLFieldType() {
		return "C";
	}

	public short equalsPreference() {
		return 16;
	}

	public static void write(final ObjectOutput out, final char ch) throws IOException {
		out.writeChar(ch);
	}

	public static char read(final ObjectInput in) throws IOException {
		return in.readChar();
	}
}
