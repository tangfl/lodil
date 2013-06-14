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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.weibo.lodil.mmap.impl.MappedFileChannel;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class Enumerated16FieldModel<T> extends AbstractFieldModel<T> {
	private static final String NULL_STRING = "\0\0";
	private final Class<T> type;
	private final Map<T, Character> map = new LinkedHashMap<T, Character>();
	private final List<T> list = new ArrayList<T>();
	private int addPosition;

	public Enumerated16FieldModel(final String fieldName, final int fieldNumber, final Class<T> type) {
		super(fieldName, fieldNumber);
		this.type = type;
		clear();
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

	public T getAllocation(final Object[] arrays, final int index) {
		final CharBuffer array = (CharBuffer) arrays[fieldNumber];
		return get(array, index);
	}

	public T get(final CharBuffer array, final int index) {
		final char c = array.get(index);
		return list.get(c);
	}

	public void setAllocation(final Object[] arrays, final int index, final T value) {
		final CharBuffer array = (CharBuffer) arrays[fieldNumber];
		set(array, index, value);
	}

	public void set(final CharBuffer array, final int index, final T value) {
		Character ordinal = map.get(value);
		if (ordinal == null) {
			final int size = map.size();
			OUTER: do {
				for (; addPosition < map.size(); addPosition++) {
					if (list.get(addPosition) == null) {
						ordinal = addEnumValue(value, addPosition);
						break OUTER;
					}
				}
				ordinal = addEnumValue(value, size);
			} while (false);
			addPosition++;
		}
		array.put(index, ordinal);
	}

	private Character addEnumValue(final T value, final int position) {
		final char ordinal = (char) position;
		if (ordinal != position) {
			throw new IndexOutOfBoundsException("Too many values in Enumerated16 field, try calling compact()");
		}
		map.put(value, ordinal);
		if (ordinal == list.size()) {
			list.add(ordinal, value);
		} else {
			list.set(ordinal, value);
		}
		return ordinal;
	}

	public Class<T> type() {
		return type;
	}

	@Override
	public BCType bcType() {
		return BCType.Reference;
	}

	@Override
	public String bcLSetType() {
		return "Ljava/lang/Object;";
	}

	@Override
	public String bcLStoredType() {
		return "C";
	}

	@Override
	public boolean virtualGetSet() {
		return true;
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
	public static <T> int hashCode(final T elementType) {
		return elementType == null ? Integer.MIN_VALUE : elementType.hashCode();
	}

	@Override
	public void clear() {
		map.clear();
		list.clear();
		map.put(null, (char) 0);
		list.add(null);
		addPosition = 1;
	}

	private final BitSet compactIndexUsed = new BitSet();

	public void compactStart() {
		compactIndexUsed.clear();
	}

	public void compactScan(final CharBuffer charBuffer, final long size) {
		for (int i = 0; i < size; i++) {
			final char ch = charBuffer.get(i);
			compactIndexUsed.set(ch);
		}
	}

	public void compactEnd() {
		final int compactSize = compactIndexUsed.cardinality();
		if (compactSize == map.size()) {
			return;
		}
		for (int i = 1; i < list.size(); i++) {
			if (compactIndexUsed.get(i)) {
				continue;
			}
			// to be removed
			final T t = list.get(i);
			list.set(i, null);
			map.remove(t);
			if (addPosition > i) {
				addPosition = i;
			}
		}
	}

	public Map<T, Character> map() {
		return map;
	}

	public List<T> list() {
		return list;
	}

	@Override
	public boolean isCompacting() {
		return true;
	}

	public short equalsPreference() {
		return 15;
	}

	public static <T> void write(final ObjectOutput out, final Class<T> clazz, final T t) throws IOException {
		if (clazz == String.class) {
			String s = (String) t;
			if (s == null) {
				s = "\0";
			}
			out.writeUTF(s);
		} else {
			out.writeObject(t);
		}
	}

	public static <T> T read(final ObjectInput in, final Class<T> clazz) throws IOException, ClassNotFoundException {
		if (clazz == String.class) {
			final String s = in.readUTF();
			if (s.equals(NULL_STRING)) {
				return null;
			}
			return (T) s;
		} else {
			return (T) in.readObject();
		}
	}

	@Override
	public void flush() throws IOException {
		super.flush();
		if (baseDirectory() != null) {
			final ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(fileName())));
			oos.writeObject(list());
			oos.close();
		}
	}

	@Override
	public void baseDirectory(final String baseDirectory) throws IOException {
		super.baseDirectory(baseDirectory);
		if (baseDirectory != null) {
			try {
				final ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(fileName())));
				list().clear();
				list().addAll((List) ois.readObject());
				ois.close();
				map.clear();
				for (final T t : list) {
					map.put(t, (char) map().size());
				}
			} catch (final ClassNotFoundException cnfe) {
				throw new IOException("Unable to load class", cnfe);
			} catch (final FileNotFoundException ignored) {
				// ignored
			}
		}
	}

	private String fileName() {
		return baseDirectory() + "/" + fieldName() + "-enum";
	}
}
