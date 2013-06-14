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
import java.nio.IntBuffer;

import com.weibo.lodil.mmap.impl.MappedFileChannel;

@SuppressWarnings({ "rawtypes" })
public class BooleanFieldModel extends AbstractFieldModel<Boolean> {
	public BooleanFieldModel(final String fieldName, final int fieldNumber) {
		super(fieldName, fieldNumber);
	}

	public Object arrayOfField(final int size) {
		return newArrayOfField(size, null);
	}

	public int sizeOf(final int elements) {
		return sizeOf0(elements);
	}

	private static int sizeOf0(final int elements) {
		return (elements + 7) / 8;
	}

	public static IntBuffer newArrayOfField(final int size, final MappedFileChannel mfc) {
		return acquireByteBuffer(mfc, sizeOf0(size)).asIntBuffer();
	}

	public Class storeType() {
		return IntBuffer.class;
	}

	public Boolean getAllocation(final Object[] arrays, final int index) {
		final IntBuffer array = (IntBuffer) arrays[fieldNumber];
		return get(array, index);
	}

	public static boolean get(final IntBuffer array, final int index) {
		return ((array.get(index >>> 5) >> index) & 1) != 0;
	}

	public void setAllocation(final Object[] arrays, final int index, final Boolean value) {
		final IntBuffer array = (IntBuffer) arrays[fieldNumber];
		set(array, index, value);
	}

	public static void set(final IntBuffer array, final int index, final boolean value) {
		final int index2 = index >>> 5;
		if (value) {
			array.put(index2, (array.get(index2) | (1 << index)));
		} else {
			array.put(index2, (array.get(index2) & ~(1 << index)));
		}
	}

	public static void write(final ObjectOutput oo, final boolean b) throws IOException {
		oo.writeByte(b ? 1 : 0);
	}

	public static boolean read(final ObjectInput oi) throws IOException {
		final byte b = oi.readByte();
		return (b > 0);
	}

	public Class<Boolean> type() {
		return boolean.class;
	}

	@Override
	public String bcLFieldType() {
		return "Z";
	}

	@Override
	public boolean isCallsHashCode() {
		return true;
	}

	public static int hashCode(final boolean b) {
		return b ? 1 : 0;
	}

	@Override
	public boolean copySimpleValue() {
		return false;
	}

	public short equalsPreference() {
		return 1;
	}
}
