package com.weibo.lodil.mmap.impl;

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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

import org.objectweb.asm.ClassWriter;

import com.weibo.lodil.mmap.model.TypeModel;

@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
public abstract class HugeCollectionBuilder<T> {
	public static final int MIN_ALLOCATION_SIZE = 32 * 1024;
	private final Class<T> type;
	protected final TypeModel<T> typeModel;
	protected int allocationSize = -1;
	protected boolean fixedSize;
	protected boolean entryBased;
	protected boolean setRemoveReturnsNull;
	protected long capacity = -1;
	protected ClassLoader classLoader;
	protected boolean disableCodeGeneration;
	protected String baseDirectory;

	protected HugeCollectionBuilder(final int typeParameter) {
		type = (Class) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[typeParameter];
		typeModel = new TypeModel<T>(type);
		classLoader = getClass().getClassLoader();
		try {
			final
			Class classWriter = ClassWriter.class;
			disableCodeGeneration = false;
		} catch (final NoClassDefFoundError ignored) {
			disableCodeGeneration = true;
		}
	}

	public HugeCollectionBuilder(final Class<T> type) {
		this.type = type;
		typeModel = new TypeModel<T>(type);
	}

	public HugeCollectionBuilder<T> allocationSize(final int allocationSize) {
		this.allocationSize = allocationSize;
		return this;
	}

	public int allocationSize() {
		return allocationSize;
	}

	public HugeCollectionBuilder<T> capacity(final int capacity) {
		this.capacity = capacity;
		return this;
	}

	public long capacity() {
		return Math.max(Math.max(allocationSize, capacity), MIN_ALLOCATION_SIZE);
	}

	public HugeCollectionBuilder<T> fixedSize(final boolean fixedSize) {
		this.fixedSize = fixedSize;
		return this;
	}

	public boolean fixedSize() {
		return fixedSize;
	}

	public HugeCollectionBuilder<T> entryBased(final boolean entryBased) {
		this.entryBased = entryBased;
		return this;
	}

	public boolean entryBased() {
		return entryBased;
	}

	public HugeCollectionBuilder<T> classLoader(final ClassLoader classLoader) {
		this.classLoader = classLoader;
		return this;
	}

	public ClassLoader classLoader() {
		return classLoader;
	}

	public HugeCollectionBuilder<T> disableCodeGeneration(final boolean disableCodeGeneration) {
		this.disableCodeGeneration = disableCodeGeneration;
		return this;
	}

	public boolean disableCodeGeneration() {
		return disableCodeGeneration;
	}

	public HugeCollectionBuilder setRemoveReturnsNull(final boolean setRemoveReturnsNull) {
		this.setRemoveReturnsNull = setRemoveReturnsNull;
		return this;
	}

	public boolean setRemoveReturnsNull() {
		return setRemoveReturnsNull;
	}

	public void baseDirectory(final String baseDirectory) {
		this.baseDirectory = baseDirectory;
	}

	public String baseDirectory() {
		return baseDirectory;
	}

	public TypeModel typeModel() {
		return typeModel;
	}

	private Class defineClass(final byte[] bytes) {
		try {
			final Method defineClass = ClassLoader.class.getDeclaredMethod("defineClass", String.class /* name */,
					byte[].class /* b */, int.class /* off */, int.class /* len */);
			defineClass.setAccessible(true);
			return (Class) defineClass.invoke(classLoader, null, bytes, 0, bytes.length);
		} catch (final NoSuchMethodException e) {
			throw new AssertionError(e);
		} catch (final IllegalAccessException e) {
			throw new AssertionError(e);
		} catch (final InvocationTargetException e) {
			throw new AssertionError(e.getCause());
		}
	}

	protected void normaliseArgs() {
		if (capacity < 1) {
			capacity = 1;
		}
		if (allocationSize < MIN_ALLOCATION_SIZE) {
			allocationSize = MIN_ALLOCATION_SIZE;
			while (((128 * allocationSize) < capacity) && (allocationSize < (64 * 1024 * 1024))) {
				allocationSize <<= 1;
			}
		}
	}
}
