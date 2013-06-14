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
import java.nio.ByteBuffer;

import com.weibo.lodil.mmap.impl.MappedFileChannel;

@SuppressWarnings({ "rawtypes" })
public class Byte2FieldModel extends AbstractFieldModel<Byte> {
	public Byte2FieldModel(final String fieldName, final int fieldNumber) {
		super(fieldName, fieldNumber);
	}

	public Object arrayOfField(final int size) {
		return newArrayOfField(size, null);
	}

	public int sizeOf(final int elements) {
		return sizeOf0(elements);
	}

	private static int sizeOf0(final int elements) {
		return ((elements * 9) + 7) / 8;
	}

	public static ByteBuffer newArrayOfField(final int size, final MappedFileChannel mfc) {
		return acquireByteBuffer(mfc, sizeOf0(size));
	}

	public Class storeType() {
		return ByteBuffer.class;
	}

	public Byte getAllocation(final Object[] arrays, final int index) {
		final ByteBuffer array = (ByteBuffer) arrays[fieldNumber];
		return get(array, index);
	}

	public static Byte get(final ByteBuffer array, final int index) {
		final int maskSize = array.capacity() / 9;
		final boolean isNotNull = ((array.get(index >>> 3) >> (index & 7)) & 1) != 0;
		return isNotNull ? array.get(index + maskSize) : null;
	}

	public void setAllocation(final Object[] arrays, final int index, final Byte value) {
		final ByteBuffer array = (ByteBuffer) arrays[fieldNumber];
		set(array, index, value);
	}

	public static void set(final ByteBuffer array, final int index, final Byte value) {
		final int maskSize = array.capacity() / 9;
		final int index2 = index >>> 3;
		final int mask = 1 << (index & 7);
		if (value == null) {
			// clear.
			array.put(index2, (byte) (array.get(index2) & ~mask));
		} else {
			array.put(index2, (byte) (array.get(index2) | mask));
			array.put(index + maskSize, value);
		}
	}

	public Class<Byte> type() {
		return Byte.class;
	}

	@Override
	public BCType bcType() {
		return BCType.Reference;
	}

	@Override
	public boolean isCallsNotEquals() {
		return true;
	}

	public static boolean notEquals(final Byte t1, final Byte t2) {
		return t1 == null ? t2 != null : !t1.equals(t2);
	}

	@UsedFromByteCode
	public static int hashCode(final Byte b) {
		return b == null ? Integer.MIN_VALUE : b;
	}

	@Override
	public boolean copySimpleValue() {
		return false;
	}

	public short equalsPreference() {
		return 7;
	}

	public static void write(final ObjectOutput out, final Byte byte2) throws IOException {
		out.writeShort(byte2 == null ? Short.MIN_VALUE : byte2);
	}

	public static Byte read(final ObjectInput in) throws IOException {
		final short s = in.readShort();
		return s < Byte.MIN_VALUE ? null : (byte) s;
	}
}
