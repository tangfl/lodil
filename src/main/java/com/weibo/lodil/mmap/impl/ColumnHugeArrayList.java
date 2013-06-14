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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import com.weibo.lodil.mmap.api.HugeArrayList;
import com.weibo.lodil.mmap.api.HugeIterator;
import com.weibo.lodil.mmap.model.FieldModel;
import com.weibo.lodil.mmap.model.MethodModel;
import com.weibo.lodil.mmap.model.TypeModel;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ColumnHugeArrayList<T> extends AbstractList<T> implements HugeArrayList<T> {
	private final TypeModel<T> type;
	private final int allocationSize;
	private final List<Object[]> allocations = new ArrayList();
	private final List<T> proxies = new ArrayList();
	private long longSize = 0;

	public ColumnHugeArrayList(final TypeModel<T> type, final int allocationSize, final long capacity) {
		this.type = type;
		this.allocationSize = allocationSize;
		ensureCapacity(capacity);
	}

	public void ensureCapacity(final long size) {
		final long blocks = ((size + allocationSize) - 1) / allocationSize;
		final int fieldCount = type.fields().length;
		while (blocks > allocations.size()) {
			final Object[] allocation = new Object[fieldCount];
			for (int i = 0; i < fieldCount; i++) {
				allocation[i] = type.arrayOfField(i, allocationSize);
			}
			allocations.add(allocation);
		}
	}

	@Override
	public int size() {
		return longSize < Integer.MAX_VALUE ? (int) longSize : Integer.MAX_VALUE;
	}

	public long longSize() {
		return longSize;
	}

	public void setSize(final long length) {
		ensureCapacity(length);
		longSize = length;
	}

	public T get(final long n) throws IndexOutOfBoundsException {
		if ((n < 0) || (n >= longSize)) {
			throw new IndexOutOfBoundsException();
		}
		return acquireProxy(n);
	}

	public T set(final long n, final T t) throws IndexOutOfBoundsException {
		throw new UnsupportedOperationException();
	}

	@Override
	public HugeIterator<T> iterator() {
		return listIterator();
	}

	@Override
	public HugeIterator<T> listIterator() {
		return new MyHugeIterator();
	}

	@Override
	public ListIterator<T> listIterator(final int index) {
		final HugeIterator<T> iterator = listIterator();
		iterator.index(index);
		return iterator;
	}

	public void recycle(final Object t) {
		if (!(t instanceof Proxy)) {
			return;
		}
		proxies.add((T) t);
	}

	@Override
	public T get(final int index) {
		return get(index & 0xFFFFFFFFL);
	}

	public T remove(final long n) throws IndexOutOfBoundsException {
		throw new UnsupportedOperationException();
	}

	private T acquireProxy(final long n) {
		if (proxies.isEmpty()) {
			final MyInvocationHandler<T> h = new MyInvocationHandler<T>(this, n);
			final T ret = (T) Proxy.newProxyInstance(type.classLoader(), new Class[] { type.type() }, h);
			h.proxy = ret;
			return ret;
		}
		return proxies.remove(proxies.size() - 1);
	}

	static class MyInvocationHandler<T> implements InvocationHandler {
		private final ColumnHugeArrayList array;
		private long n;
		T proxy;

		MyInvocationHandler(final ColumnHugeArrayList array, final long n) {
			this.array = array;
			this.n = n;
		}

		public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
			if (method.getDeclaringClass() == Object.class) {
				if ("getClass".equals(method.getName())) {
					return array.type.type();
				} else {
					return method.invoke(this, args);
				}
			}
			final MethodModel hm = array.type.method(method);
			switch (hm.methodType()) {
			case SETTER:
				array.set(n, hm.fieldModel(), args[0]);
				return proxy;
			case GETTER:
				return array.get(n, hm.fieldModel());
			default:
				throw new UnsupportedOperationException();
			}
		}
	}

	Object get(final long index, final FieldModel fieldModel) {
		final int block = (int) (index / allocationSize);
		final int portion = (int) (index % allocationSize);
		return fieldModel.getAllocation(allocations.get(block), portion);
	}

	void set(final long index, final FieldModel fieldModel, final Object value) {
		final int block = (int) (index / allocationSize);
		final int portion = (int) (index % allocationSize);
		fieldModel.setAllocation(allocations.get(block), portion, value);
	}

	class MyHugeIterator implements HugeIterator<T> {
		private final T proxy;
		private final MyInvocationHandler<T> handler;

		MyHugeIterator() {
			proxy = acquireProxy(-1);
			handler = (MyInvocationHandler<T>) Proxy.getInvocationHandler(proxy);
		}

		public HugeIterator<T> toStart() {
			handler.n = -1;
			return this;
		}

		public HugeIterator<T> toEnd() {
			handler.n = longSize();
			return this;
		}

		public HugeIterator<T> index(final long n) {
			return this;
		}

		public long previousLongIndex() {
			return handler.n - 1;
		}

		public long nextLongIndex() {
			return handler.n + 1;
		}

		public void recycle() {
		}

		public boolean hasNext() {
			return handler.n < (longSize() - 1);
		}

		public T next() {
			handler.n++;
			return proxy;
		}

		public boolean hasPrevious() {
			return false;
		}

		public T previous() {
			handler.n--;
			return proxy;
		}

		public int nextIndex() {
			return (int) nextLongIndex();
		}

		public int previousIndex() {
			return (int) previousLongIndex();
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

		public void set(final T t) {
			for (final FieldModel field : type.fields()) {
				try {
					field.setter().invoke(proxy, field.getter().invoke(t));
				} catch (final IllegalAccessException e) {
					throw new AssertionError(e);
				} catch (final InvocationTargetException e) {
					throw new AssertionError(e.getCause());
				}
			}
		}

		public void add(final T t) {
			if (handler.n != longSize()) {
				throw new UnsupportedOperationException();
			}
			ensureCapacity(++longSize);
			set(t);
			handler.n++;
		}
	}

	public void flush() {
	}

	public void close() {
	}

	public void compact() {
		final int allocationsNeeded = (int) ((longSize() / allocationSize) + 1);
		while (allocations.size() > allocationsNeeded) {
			allocations.remove(allocations.size() - 1);
		}
	}
}
