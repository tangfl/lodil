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
import java.nio.FloatBuffer;

import com.weibo.lodil.mmap.impl.MappedFileChannel;

@SuppressWarnings({ "rawtypes" })
public class FloatFieldModel extends AbstractFieldModel<Float> {
	public FloatFieldModel(final String fieldName, final int fieldNumber) {
		super(fieldName, fieldNumber);
	}

	public Object arrayOfField(final int size) {
		return newArrayOfField(size, null);
	}

	public int sizeOf(final int elements) {
		return sizeOf0(elements);
	}

	private static int sizeOf0(final int elements) {
		return elements * 4;
	}

	public static FloatBuffer newArrayOfField(final int size, final MappedFileChannel mfc) {
		return acquireByteBuffer(mfc, sizeOf0(size)).asFloatBuffer();
	}

	public Class storeType() {
		return FloatBuffer.class;
	}

	public Float getAllocation(final Object[] arrays, final int index) {
		final FloatBuffer array = (FloatBuffer) arrays[fieldNumber];
		return get(array, index);
	}

	public static float get(final FloatBuffer array, final int index) {
		return array.get(index);
	}

	public void setAllocation(final Object[] arrays, final int index, final Float value) {
		final FloatBuffer array = (FloatBuffer) arrays[fieldNumber];
		set(array, index, value);
	}

	public static void set(final FloatBuffer array, final int index, final float value) {
		array.put(index, value);
	}

	public Class<Float> type() {
		return float.class;
	}

	@Override
	public String bcLFieldType() {
		return "F";
	}

	@Override
	public BCType bcType() {
		return BCType.Float;
	}

	@Override
	public boolean isCallsNotEquals() {
		return true;
	}

	@UsedFromByteCode
	public static boolean notEquals(final float d1, final float d2) {
		return Float.floatToIntBits(d1) != Float.floatToIntBits(d2);
	}

	@UsedFromByteCode
	public static int hashCode(final float f) {
		return Float.floatToIntBits(f);
	}

	public short equalsPreference() {
		return 31;
	}

	public static void write(final ObjectOutput out, final float f) throws IOException {
		out.writeFloat(f);
	}

	public static float read(final ObjectInput in) throws IOException {
		return in.readFloat();
	}
}
