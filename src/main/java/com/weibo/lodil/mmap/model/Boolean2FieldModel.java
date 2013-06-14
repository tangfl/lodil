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
public class Boolean2FieldModel extends AbstractFieldModel<Boolean> {

	public Boolean2FieldModel(final String fieldName, final int fieldNumber) {
		super(fieldName, fieldNumber);
	}

	public Object arrayOfField(final int size) {
		return newArrayOfField(size, null);
	}

	public int sizeOf(final int elements) {
		return sizeOf0(elements);
	}

	private static int sizeOf0(final int elements) {
		return (elements + 3) / 4;
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

	public static Boolean get(final IntBuffer array, final int index) {
		final int value = array.get(index >>> 4) >> ((index & 15) << 1);
		switch (value & 3) {
		case 0:
			return false;
		case 1:
			return true;
		case 2:
			return null;
		default:
			throw new AssertionError();
		}
	}

	public void setAllocation(final Object[] arrays, final int index, final Boolean value) {
		final IntBuffer array = (IntBuffer) arrays[fieldNumber];
		set(array, index, value);
	}

	public static void set(final IntBuffer array, final int index, final Boolean value) {
		final int value2 = value == null ? 2 : value ? 1 : 0;
		final int byteIndex = index >>> 4;
		final int index2 = (index & 15) << 1;
		final int mask = ~(3 << index2);
		final int value3 = (array.get(byteIndex) & mask) | (value2 << index2);
		array.put(byteIndex, value3);
	}

	public static void write(final ObjectOutput oo, final Boolean b) throws IOException {
		oo.writeByte(b == null ? Byte.MIN_VALUE : b ? 1 : 0);
	}

	public static Boolean read(final ObjectInput oi) throws IOException {
		final byte b = oi.readByte();
		return b < 0 ? null : (Boolean) (b > 0);
	}

	public Class<Boolean> type() {
		return Boolean.class;
	}

	@Override
	public BCType bcType() {
		return BCType.Reference;
	}

	@Override
	public boolean isCallsNotEquals() {
		return true;
	}

	@UsedFromByteCode
	public static boolean notEquals(final Boolean t1, final Boolean t2) {
		return t1 == null ? t2 != null : !t1.equals(t2);
	}

	@UsedFromByteCode
	public static int hashCode(final Boolean b) {
		return b == null ? 0 : b.hashCode();
	}

	@Override
	public boolean copySimpleValue() {
		return false;
	}

	public short equalsPreference() {
		return 0;
	}
}
