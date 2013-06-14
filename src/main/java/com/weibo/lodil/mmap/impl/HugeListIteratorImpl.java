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

import com.weibo.lodil.mmap.api.HugeAllocation;
import com.weibo.lodil.mmap.api.HugeListIterator;

@SuppressWarnings({ "unchecked" })
public class HugeListIteratorImpl<T, TA extends HugeAllocation, TE extends AbstractHugeElement<T, TA>> implements
HugeListIterator<T> {
	private final AbstractHugeArrayList<T, TA, TE> list;
	private final TE mte;

	public HugeListIteratorImpl(final AbstractHugeArrayList<T, TA, TE> list) {
		this.list = list;
		mte = list.acquireElement(-1);
	}

	public boolean hasNext() {
		return (mte.index + 1) < list.longSize;
	}

	public T next() {
		mte.next();
		return (T) mte;
	}

	public boolean hasPrevious() {
		return mte.index > 0;
	}

	public T previous() {
		mte.previous();
		return (T) mte;
	}

	public int nextIndex() {
		return (int) Math.min(nextLongIndex(), Integer.MAX_VALUE);
	}

	public int previousIndex() {
		return (int) Math.min(previousLongIndex(), Integer.MAX_VALUE);
	}

	public void remove() {
	}

	public HugeListIterator<T> toStart() {
		mte.index(-1);
		return this;
	}

	public HugeListIterator<T> toEnd() {
		mte.index(list.longSize);
		return this;
	}

	public HugeListIterator<T> index(final long n) {
		mte.index(n);
		return this;
	}

	public long previousLongIndex() {
		return mte.index - 1;
	}

	public long nextLongIndex() {
		return mte.index + 1;
	}

	public void recycle() {
	}

	public void index(final int index) {
		mte.index(index);
	}

	public void set(final T t) {
		throw new UnsupportedOperationException();
	}

	public void add(final T t) {
		throw new UnsupportedOperationException();
	}
}
