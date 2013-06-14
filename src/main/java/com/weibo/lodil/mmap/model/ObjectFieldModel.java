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
import java.lang.reflect.Array;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ObjectFieldModel<T> extends AbstractFieldModel<T> {
	private final Class<T> type;

	public ObjectFieldModel(final String fieldName, final int fieldNumber, final Class<T> type) {
		super(fieldName, fieldNumber);
		this.type = type;
	}

	public Object arrayOfField(final int size) {
		return newArrayOfField(type, size);
	}

	public int sizeOf(final int elements) {
		throw new UnsupportedOperationException("Cannot map object fields to disk yet.");
	}

	public Class storeType() {
		return Object[].class;
	}

	@UsedFromByteCode
	public static <T> T[] newArrayOfField(final Class<T> type, final int size) {
		return (T[]) Array.newInstance(type, size);
	}

	public T getAllocation(final Object[] arrays, final int index) {
		final T[] array = (T[]) arrays[fieldNumber];
		return get(array, index);
	}

	@UsedFromByteCode
	public static <T> T get(final T[] array, final int index) {
		return array[index];
	}

	public void setAllocation(final Object[] arrays, final int index, final T value) {
		final T[] array = (T[]) arrays[fieldNumber];
		set(array, index, value);
	}

	@UsedFromByteCode
	public static <T> void set(final T[] array, final int index, final T value) {
		array[index] = value;
	}

	@Override
	public boolean copySimpleValue() {
		return false;
	}

	@Override
	public String bcStoreType() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String bcLStoreType() {
		return "[Ljava/lang/Object;";
	}

	@Override
	public String bcFieldType() {
		return type().getName().replace('.', '/');
	}

	@Override
	public String bcLFieldType() {
		return "L" + bcFieldType() + ";";
	}

	@Override
	public String bcLSetType() {
		return "Ljava/lang/Object;";
	}

	public Class<T> type() {
		return type;
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
	public static <T> boolean notEquals(final T t1, final T t2) {
		return t1 == null ? t2 != null : !t1.equals(t2);
	}

	@UsedFromByteCode
	public static <T> int hashCode(final T t) {
		return t == null ? 0 : t.hashCode();
	}

	@Override
	public boolean isBufferStore() {
		return true;
	}

	public short equalsPreference() {
		return 0;
	}

	public static <T> void write(final ObjectOutput out, final Class<T> clazz, final T t) throws IOException {
		if (clazz == String.class) {
			out.writeUTF((String) t);
		} else {
			out.writeObject(t);
		}
	}

	public static <T> T read(final ObjectInput in, final Class<T> aClass) throws IOException, ClassNotFoundException {
		return (T) (aClass == String.class ? in.readUTF() : in.readObject());
	}
}
