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

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import com.weibo.lodil.mmap.HugeArrayBuilder;
import com.weibo.lodil.mmap.api.HugeAllocation;
import com.weibo.lodil.mmap.api.HugeArrayList;
import com.weibo.lodil.mmap.api.HugeElement;
import com.weibo.lodil.mmap.api.HugeIterator;
import com.weibo.lodil.mmap.api.HugeListIterator;
import com.weibo.lodil.mmap.model.FieldModel;


@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class AbstractHugeArrayList<T, TA extends HugeAllocation, TE extends AbstractHugeElement<T, TA>>
extends AbstractHugeContainer<T, TA> implements HugeArrayList<T> {
	protected final List<TE> elements = new ArrayList<TE>();
	protected final List<T> impls = new ArrayList<T>();

	public AbstractHugeArrayList(final HugeArrayBuilder hab) {
		super(hab);
	}

	public T get(final long n) throws IndexOutOfBoundsException {
		return (T) acquireElement(n);
	}

	TE acquireElement(final long n) {
		if (elements.isEmpty()) {
			return createElement(n);
		}
		final TE mte = elements.remove(elements.size() - 1);
		mte.index(n);
		return mte;
	}

	protected abstract TE createElement(long n);

	public HugeIterator<T> iterator() {
		return listIterator();
	}

	public HugeListIterator<T> listIterator() {
		return new HugeListIteratorImpl<T, TA, TE>(this);
	}

	public ListIterator<T> listIterator(final int index) {
		final HugeListIterator<T> iterator = listIterator();
		iterator.index(index);
		return iterator;
	}

	public void recycle(final Object t) {
		if (!(t instanceof HugeElement)) {
			return;
		}
		switch (((HugeElement) t).hugeElementType()) {
		case Element:
			if (elements.size() < allocationSize) {
				elements.add((TE) t);
			}
			break;
		case BeanImpl:
			if (impls.size() < allocationSize) {
				impls.add((T) t);
			}
			break;
		default:
			// TODO
			break;
		}
	}

	public T get(final int index) {
		return get(index & 0xFFFFFFFFL);
	}

	public T set(final int index, final T element) {
		return set((long) index, element);
	}

	public T set(final long index, final T element) throws IndexOutOfBoundsException {
		if (index > longSize) {
			throw new IndexOutOfBoundsException();
		}
		if (index == longSize) {
			longSize++;
		}
		ensureCapacity(longSize);
		if (setRemoveReturnsNull) {
			final T t = get(index);
			((HugeElement<T>) t).copyOf(element);
			return null;
		}
		final T t0 = acquireImpl();
		final T t = get(index);
		((HugeElement<T>) t0).copyOf(t);
		((HugeElement<T>) t).copyOf(element);
		recycle(t);
		return t0;
	}

	public boolean add(final T t) {
		set(longSize(), t);
		return true;
	}

	public void add(final int index, final T element) {
		add((long) index, element);
	}

	public void add(final long index, final T element) {
		if (index != size()) {
			throw new UnsupportedOperationException();
		}
		set(index, element);
	}

	public T remove(final int index) {
		return remove((long) index);
	}

	public T remove(final long index) {
		if (index > longSize) {
			throw new IndexOutOfBoundsException();
		}
		if (setRemoveReturnsNull) {
			final T t = get(index);
			if (index < (longSize - 1)) {
				final T t2 = get(index);
				((HugeElement) t).copyOf(t2);
				recycle(t2);
			}
			recycle(t);
			longSize--;
			return null;
		}
		final T impl = acquireImpl();
		final T t = get(index);
		((HugeElement<T>) impl).copyOf(t);
		if (index < (longSize - 1)) {
			final T t2 = get(index);
			((HugeElement) t).copyOf(t2);
			recycle(t2);
		}
		recycle(t);
		longSize--;
		return impl;
	}

	protected T acquireImpl() {
		if (impls.isEmpty()) {
			return createImpl();
		}
		return impls.remove(impls.size() - 1);
	}

	protected abstract T createImpl();

	public void flush() throws IOException {
		for (final MappedFileChannel mfc : mfChannels) {
			try {
				mfc.flush();
			} catch (final IOException ignored) {
			}
		}
		for (final Field f : getClass().getDeclaredFields()) {
			if (!FieldModel.class.isAssignableFrom(f.getType())) {
				continue;
			}
			f.setAccessible(true);
			try {
				final FieldModel fm = (FieldModel) f.get(this);
				fm.flush();
			} catch (final IllegalAccessException e) {
				throw new AssertionError(e);
			}
		}
	}

	public void close() throws IOException {
		flush();
		for (final MappedFileChannel mfc : mfChannels) {
			try {
				mfc.close();
			} catch (final IOException ignored) {
			}
		}
	}

	// imported from AbstractList

	public List<T> subList(final int fromIndex, final int toIndex) {
		return new SubList(fromIndex, toIndex);
	}

	public boolean contains(final Object o) {
		return indexOf(o) >= 0;
	}

	public Object[] toArray() {
		throw new UnsupportedOperationException();
	}

	@SuppressWarnings("hiding")
	public <T> T[] toArray(final T[] a) {
		throw new UnsupportedOperationException();
	}

	public boolean remove(final Object o) {
		final int index = indexOf(o);
		if (index >= 0) {
			remove(index);
			return true;
		}
		return false;
	}

	public boolean containsAll(final Collection<?> c) {
		final List list = new ArrayList();
		for (final Object o : c) {
			if (list.contains(o)) {
				continue;
			}
			list.add(o);
		}
		for (final T t : this) {
			if (list.remove(t) && list.isEmpty()) {
				return true;
			}
		}
		return false;
	}

	public boolean addAll(final Collection<? extends T> c) {
		boolean ret = false;
		for (final T t : c) {
			ret |= add(t);
		}
		return ret;
	}

	public boolean addAll(final int index, final Collection<? extends T> c) {
		throw new UnsupportedOperationException();
	}

	public boolean removeAll(final Collection<?> c) {
		boolean b = false;
		for (final Iterator<T> iterator = this.iterator(); iterator.hasNext();) {
			final T t = iterator.next();
			if (c.contains(t)) {
				iterator.remove();
				b = true;
			}
		}
		return b;
	}

	public boolean retainAll(final Collection<?> c) {
		boolean b = false;
		for (final Iterator<T> iterator = this.iterator(); iterator.hasNext();) {
			final T t = iterator.next();
			if (!c.contains(t)) {
				iterator.remove();
				b = true;
			}
		}
		return b;
	}

	public int indexOf(final Object o) {
		final TE te = acquireElement(0);
		try {
			for (long i = 0; i <= Integer.MAX_VALUE; i++) {
				te.index = i;
				if (te.equals(o)) {
					return (int) i;
				}
			}
			return -1;
		} finally {
			recycle(te);
		}
	}

	public int lastIndexOf(final Object o) {
		final TE te = acquireElement(0);
		try {
			for (int i = (int) Math.min(Long.MAX_VALUE, longSize() - 1); i >= 0; i++) {
				te.index = i;
				if (te.equals(o)) {
					return i;
				}
			}
			return -1;
		} finally {
			recycle(te);
		}
	}

	class SubList extends AbstractList<T> {
		private final int offset;
		private int size;

		SubList(final int fromIndex, final int toIndex) {
			if (fromIndex < 0) {
				throw new IndexOutOfBoundsException("fromIndex = " + fromIndex);
			}
			if (toIndex > size()) {
				throw new IndexOutOfBoundsException("toIndex = " + toIndex);
			}
			if (fromIndex > toIndex) {
				throw new IllegalArgumentException("fromIndex(" + fromIndex + ") > toIndex(" + toIndex + ")");
			}
			offset = fromIndex;
			size = toIndex - fromIndex;
		}

		@Override
		public T set(final int index, final T element) {
			rangeCheck(index);
			return AbstractHugeArrayList.this.set(index + offset, element);
		}

		@Override
		public T get(final int index) {
			rangeCheck(index);
			return AbstractHugeArrayList.this.get(index + offset);
		}

		@Override
		public int size() {
			return size;
		}

		@Override
		public void add(final int index, final T element) {
			if ((index < 0) || (index > size)) {
				throw new IndexOutOfBoundsException();
			}
			AbstractHugeArrayList.this.add(index + offset, element);
			size++;
		}

		@Override
		public T remove(final int index) {
			rangeCheck(index);
			final T result = AbstractHugeArrayList.this.remove(index + offset);
			size--;
			return result;
		}

		@Override
		protected void removeRange(final int fromIndex, final int toIndex) {
			for (int i = fromIndex; i < toIndex; i++) {
				AbstractHugeArrayList.this.remove(i);
			}
			size -= (toIndex - fromIndex);
			modCount++;
		}

		@Override
		public boolean addAll(final Collection<? extends T> c) {
			return addAll(size, c);
		}

		@Override
		public boolean addAll(final int index, final Collection<? extends T> c) {
			if ((index < 0) || (index > size)) {
				throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
			}
			final int cSize = c.size();
			if (cSize == 0) {
				return false;
			}

			AbstractHugeArrayList.this.addAll(offset + index, c);
			size += cSize;
			modCount++;
			return true;
		}

		@Override
		public Iterator<T> iterator() {
			return listIterator();
		}

		@Override
		public ListIterator<T> listIterator(final int index) {
			if ((index < 0) || (index > size)) {
				throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
			}

			return new ListIterator<T>() {
				private final ListIterator<T> i = AbstractHugeArrayList.this.listIterator(index + offset);

				public boolean hasNext() {
					return nextIndex() < size;
				}

				public T next() {
					if (hasNext()) {
						return i.next();
					} else {
						throw new NoSuchElementException();
					}
				}

				public boolean hasPrevious() {
					return previousIndex() >= 0;
				}

				public T previous() {
					if (hasPrevious()) {
						return i.previous();
					} else {
						throw new NoSuchElementException();
					}
				}

				public int nextIndex() {
					return i.nextIndex() - offset;
				}

				public int previousIndex() {
					return i.previousIndex() - offset;
				}

				public void remove() {
					i.remove();
					size--;
					modCount++;
				}

				public void set(final T e) {
					i.set(e);
				}

				public void add(final T e) {
					i.add(e);
					size++;
					modCount++;
				}
			};
		}

		@Override
		public List<T> subList(final int fromIndex, final int toIndex) {
			return new SubList(fromIndex, toIndex);
		}

		void rangeCheck(final int index) {
			if ((index < 0) || (index >= size)) {
				throw new IndexOutOfBoundsException("Index: " + index + ",Size: " + size);
			}
		}
	}
}
