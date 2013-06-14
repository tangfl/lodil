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
import java.nio.LongBuffer;

import com.weibo.lodil.mmap.impl.MappedFileChannel;

@SuppressWarnings({ "rawtypes" })
public class LongFieldModel extends AbstractFieldModel<Long> {
	public LongFieldModel(final String fieldName, final int fieldNumber) {
		super(fieldName, fieldNumber);
	}

	public Object arrayOfField(final int size) {
		return newArrayOfField(size, null);
	}

	public int sizeOf(final int elements) {
		return sizeOf0(elements);
	}

	private static int sizeOf0(final int elements) {
		return elements * 8;
	}

	public static LongBuffer newArrayOfField(final int size, final MappedFileChannel mfc) {
		return acquireByteBuffer(mfc, sizeOf0(size)).asLongBuffer();
	}

	public Class storeType() {
		return LongBuffer.class;
	}

	public Long getAllocation(final Object[] arrays, final int index) {
		final LongBuffer array = (LongBuffer) arrays[fieldNumber];
		return get(array, index);
	}

	public static long get(final LongBuffer array, final int index) {
		return array.get(index);
	}

	public void setAllocation(final Object[] arrays, final int index, final Long value) {
		final LongBuffer array = (LongBuffer) arrays[fieldNumber];
		set(array, index, value);
	}

	public static void set(final LongBuffer array, final int index, final long value) {
		array.put(index, value);
	}

	public Class<Long> type() {
		return long.class;
	}

	@Override
	public int bcFieldSize() {
		return 2;
	}

	@Override
	public String bcLFieldType() {
		return "J";
	}

	@Override
	public BCType bcType() {
		return BCType.Long;
	}

	@Override
	public boolean isCallsNotEquals() {
		return true;
	}

	public static boolean equals(final long l1, final long l2) {
		return l1 == l2;
	}

	public static int hashCode(final long l) {
		return (int) ((l >>> 32) ^ l);
	}

	public short equalsPreference() {
		return 30; // 64; lower due to the increased memory requirement
	}

	public static void write(final ObjectOutput out, final long l) throws IOException {
		out.writeLong(l);
	}

	public static long read(final ObjectInput in) throws IOException {
		return in.readLong();
	}
}
