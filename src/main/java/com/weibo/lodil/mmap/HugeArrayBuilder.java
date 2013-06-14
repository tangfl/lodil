package com.weibo.lodil.mmap;

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

import com.weibo.lodil.mmap.api.HugeArrayList;
import com.weibo.lodil.mmap.impl.GenerateHugeArrays;
import com.weibo.lodil.mmap.impl.HugeCollectionBuilder;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class HugeArrayBuilder<T> extends HugeCollectionBuilder<T> {
	private Class<?> arrayListClass;

	protected HugeArrayBuilder() {
		super(0);
	}

	public HugeArrayList<T> create() {
		normaliseArgs();

		try {
			if (arrayListClass == null) {
				arrayListClass = classLoader().loadClass(typeModel.type().getName() + "ArrayList");
			}

		} catch (final ClassNotFoundException e) {
			acquireImplClass();
			defineClass(GenerateHugeArrays.dumpElement(typeModel));
			defineClass(GenerateHugeArrays.dumpAllocation(typeModel));
			arrayListClass = defineClass(GenerateHugeArrays.dumpArrayList(typeModel));
		}
		try {
			return (HugeArrayList<T>) arrayListClass.getConstructor(HugeArrayBuilder.class).newInstance(this);
		} catch (final NoSuchMethodException e) {
			throw new AssertionError(e);
		} catch (final InstantiationException e) {
			throw new AssertionError(e);
		} catch (final IllegalAccessException e) {
			throw new AssertionError(e);
		} catch (final InvocationTargetException e) {
			throw new AssertionError(e.getCause());
		}
	}

	public T createBean() {
		final Class implClass = acquireImplClass();
		try {
			return (T) implClass.newInstance();
		} catch (final InstantiationException e) {
			throw new AssertionError(e);
		} catch (final IllegalAccessException e) {
			throw new AssertionError(e);
		}
	}

	Class implClass = null;

	private Class acquireImplClass() {
		try {
			if (implClass == null) {
				implClass = classLoader().loadClass(typeModel.type().getName() + "Impl");
			}

		} catch (final ClassNotFoundException e) {
			implClass = defineClass(GenerateHugeArrays.dumpImpl(typeModel));
		}
		return implClass;
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
}
