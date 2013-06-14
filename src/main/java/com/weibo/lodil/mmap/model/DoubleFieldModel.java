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
import java.nio.DoubleBuffer;

import com.weibo.lodil.mmap.impl.MappedFileChannel;

@SuppressWarnings({ "rawtypes" })
public class DoubleFieldModel extends AbstractFieldModel<Double> {
	public DoubleFieldModel(final String fieldName, final int fieldNumber) {
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

	public static DoubleBuffer newArrayOfField(final int size, final MappedFileChannel mfc) {
		return acquireByteBuffer(mfc, sizeOf0(size)).asDoubleBuffer();
	}

	public Class storeType() {
		return DoubleBuffer.class;
	}

	public Double getAllocation(final Object[] arrays, final int index) {
		final DoubleBuffer array = (DoubleBuffer) arrays[fieldNumber];
		return get(array, index);
	}

	public static double get(final DoubleBuffer array, final int index) {
		return array.get(index);
	}

	public void setAllocation(final Object[] arrays, final int index, final Double value) {
		final DoubleBuffer array = (DoubleBuffer) arrays[fieldNumber];
		set(array, index, value);
	}

	public static void set(final DoubleBuffer array, final int index, final double value) {
		array.put(index, value);
	}

	public Class<Double> type() {
		return double.class;
	}

	@Override
	public int bcFieldSize() {
		return 2;
	}

	@Override
	public String bcLFieldType() {
		return "D";
	}

	@Override
	public BCType bcType() {
		return BCType.Double;
	}

	@Override
	public boolean isCallsNotEquals() {
		return true;
	}

	@UsedFromByteCode
	public static boolean notEquals(final double d1, final double d2) {
		return Double.doubleToLongBits(d1) != Double.doubleToLongBits(d2);
	}

	@UsedFromByteCode
	public static int hashCode(final double d) {
		return LongFieldModel.hashCode(Double.doubleToLongBits(d));
	}

	public short equalsPreference() {
		return 29; // 63, lower due to the increase memory requirement.
	}

	public static void write(final ObjectOutput out, final double d) throws IOException {
		out.writeDouble(d);
	}

	public static double read(final ObjectInput in) throws IOException {
		return in.readDouble();
	}
}
