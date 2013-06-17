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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.weibo.lodil.LOG;
import com.weibo.lodil.mmap.HugeMapBuilder;
import com.weibo.lodil.mmap.api.HugeAllocation;
import com.weibo.lodil.mmap.api.HugeElement;
import com.weibo.lodil.mmap.api.HugeMap;

@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class AbstractHugeMap<K, KE extends HugeElement<K>, V, VE extends HugeElement<V>, MA extends HugeAllocation>
extends AbstractHugeContainer<V, MA> implements HugeMap<K, V> {
	public static final long HASH_MASK = 0xFFFFFFFFFL; // so divide by 32 is
	// positive.
	protected final List<KE> keyElements = new ArrayList<KE>();
	protected final List<VE> valueElements = new ArrayList<VE>();
	protected final List<K> keyImpls = new ArrayList<K>();
	protected final List<V> valueImpls = new ArrayList<V>();
	protected final IntBuffer[] keysBuffers = new IntBuffer[256];
	private final Set<K> keySet = new AHMSet();
	private final Collection<V> values = new AHMValues();
	private final Set<Entry<K, V>> entrySet = new AHMEntrySet();

	protected AbstractHugeMap(final HugeMapBuilder<K, V> hmb) {
		super(hmb);
		for (int i = 0; i < keysBuffers.length; i++) {
			keysBuffers[i] = ByteBuffer.allocate(512 * 1024).order(ByteOrder.nativeOrder()).asIntBuffer();
		}
		ensureCapacity(1);
	}

	VE acquireValueElement(final long n) {
		if (valueElements.isEmpty()) {
			return createValueElement(n);
		}
		final VE mte = valueElements.remove(valueElements.size() - 1);
		mte.index(n);
		return mte;
	}

	protected abstract VE createValueElement(long n);

	KE acquireKeyElement(final long n) {
		if (keyElements.isEmpty()) {
			return createKeyElement(n);
		}
		final KE mte = keyElements.remove(keyElements.size() - 1);
		mte.index(n);
		return mte;
	}

	protected abstract KE createKeyElement(long n);

	public void recycle(final Object o) {
		if (o == null) {
			return;
		}
		switch (((HugeElement) o).hugeElementType()) {
		case Element:
			if (valueElements.size() < allocationSize) {
				//valueElements.add((VE) o);
			}
			break;
		case BeanImpl:
			if (valueImpls.size() < allocationSize) {
				//valueImpls.add((V) o);
			}
			break;
		case KeyElement:
			if (keyElements.size() < allocationSize) {
				//keyElements.add((KE) o);
			}
			break;
		case KeyImpl:
			if (keyImpls.size() < allocationSize) {
				//keyImpls.add((K) o);
			}
			break;
		}
	}

	protected K acquireKeyImpl() {
		if (valueImpls.isEmpty()) {
			return createKeyImpl();
		}
		return keyImpls.remove(keyImpls.size() - 1);
	}

	protected abstract K createKeyImpl();

	protected V acquireValueImpl() {
		if (valueImpls.isEmpty()) {
			return createValueImpl();
		}
		return valueImpls.remove(valueImpls.size() - 1);
	}

	protected abstract V createValueImpl();

	// Map
	protected long indexOf(final KE key, final boolean free, final boolean remove) {
		final long hash = key.longHashCode() & HASH_MASK;
		final int loHash = (int) (hash % keysBuffers.length);
		final int hiHash = (int) (hash / keysBuffers.length);
		final IntBuffer keysBuffer = keysBuffers[loHash];

		final KE ke = acquireKeyElement(0);
		//final VE ve = acquireValueElement(0);
		//LOG.debug("ke:" + ke + " ve:" + ve);

		try {
			for (int i = 0, len = keysBuffer.limit(); i < len; i++) {
				final int index = (hiHash + i) % len;
				final int i1 = keysBuffer.get(index);

				LOG.debug(loHash + " keysBuffer.get:" + index + ":" + i1);

				if (i1 == 0) {
					if (free) {
						final int loc = size();
						ensureCapacity(loc + 1);
						keysBuffer.put(index, loc + 1);

						LOG.debug(loHash + " keysBuffer.put:" + index + ":" + (loc + 1));

						if (keysBuffer.position() >= ((keysBuffer.limit() * 3) / 4)) {
							growBuffer(loHash);
						} else if ((i > 5) && (keysBuffer.position() >= ((keysBuffer.limit() * 2) / 3))) {
							growBuffer(loHash);
						} else if ((i > 15) && (keysBuffer.position() >= (keysBuffer.limit() / 2))) {
							growBuffer(loHash);
						} else {
							keysBuffer.position(keysBuffer.position() + 1);
						}
						longSize++;
						return loc;
					}
					return -1;
				}
				ke.index(i1 - 1);
				if (ke.equals(key)) {
					if (remove) {
						keysBuffer.put((hiHash + i) % len, 0);
						// used field.
						keysBuffer.position(keysBuffer.position() - 1);
					}
					return i1 - 1;
				}
			}
			return -1;
		} finally {
			recycle(ke);
		}
	}

	private void growBuffer(final int loHash) {
		final IntBuffer buffer1 = keysBuffers[loHash];
		final IntBuffer buffer2 = ByteBuffer.allocate(buffer1.capacity() * 8).order(ByteOrder.nativeOrder())
				.asIntBuffer();
		keysBuffers[loHash] = buffer2;
		final KE ke = acquireKeyElement(0);
		int used = 0;
		OUTER: for (int j = 0; j < buffer1.capacity(); j++) {
			final int index = buffer1.get(j);
			if (index == 0) {
				continue;
			}
			ke.index(index - 1);

			final int hiHash = (int) (ke.longHashCode() & (HASH_MASK / keysBuffers.length));
			for (int i = 0, len = buffer2.limit(); i < len; i++) {
				final int i1 = buffer2.get((hiHash + i) % len);
				if (i1 == 0) {
					buffer2.put((hiHash + i) % len, index);
					used++;
					continue OUTER;
				}
			}
		}
		recycle(ke);
		buffer2.position(used);
	}

	public boolean containsKey(final Object key) {
		return (key instanceof HugeElement) && (indexOf((KE) key, false, false) >= 0);
	}

	public boolean containsValue(final Object value) {
		for (final V v : values()) {
			if (v.equals(value)) {
				return true;
			}
		}
		return false;
	}

	public V get(final Object key) {
		if (!(key instanceof HugeElement)) {
			return null;
		}
		final long index = indexOf((KE) key, false, false);
		if (index < 0) {
			return null;
		}
		return (V) acquireValueElement(index);
	}

	public V put(final K key, final V value) {
		final long index = indexOf((KE) key, true, false);
		LOG.debug("put(key=" + key + ", value=" + value + ") at index:" + index);
		final VE ve = acquireValueElement(index);
		V v = null;
		if (!setRemoveReturnsNull) {
			v = acquireValueImpl();
			((HugeElement<V>) v).copyOf((V) ve);
		}
		ve.copyOf(value);
		recycle(ve);

		// FIXME copy ke ?
		//final KE ke =  acquireKeyElement(index);
		//ke.copyOf(key);
		//recycle(ke);

		return v;
	}

	public V remove(final Object key) {
		if (!(key instanceof HugeElement)) {
			return null;
		}
		final long index = indexOf((KE) key, false, true);
		if (index < 0) {
			return null;
		}

		return removeAt(index);
	}

	private V removeAt(final long index) {
		final VE ve2 = acquireValueElement(index);
		V v = null;
		if (!setRemoveReturnsNull) {
			v = acquireValueImpl();
			((HugeElement<V>) v).copyOf((V) ve2);
		}
		if (index < (longSize() - 1)) {
			final VE ve1 = acquireValueElement(longSize() - 1);
			ve1.copyOf((V) ve2);
			recycle(ve1);
		}
		recycle(ve2);
		return v;
	}

	public void putAll(final Map<? extends K, ? extends V> m) {
		for (final Entry<? extends K, ? extends V> entry : m.entrySet()) {
			put(entry.getKey(), entry.getValue());
		}
	}

	public int[] sizes() {
		final int[] sizes = new int[keysBuffers.length];
		for (int i = 0; i < keysBuffers.length; i++) {
			sizes[i] = keysBuffers[i].position();
		}
		return sizes;
	}

	public int[] capacities() {
		final int[] sizes = new int[keysBuffers.length];
		for (int i = 0; i < keysBuffers.length; i++) {
			sizes[i] = keysBuffers[i].capacity();
		}
		return sizes;
	}

	public Set<K> keySet() {
		return keySet;
	}

	public Collection<V> values() {
		return values;
	}

	public Set<Entry<K, V>> entrySet() {
		return entrySet;
	}

	private class AHMSet extends AbstractSet<K> {
		@Override
		public Iterator<K> iterator() {
			return new Iterator<K>() {
				final KE ke = acquireKeyElement(-1);

				public boolean hasNext() {
					return ke.index() < (longSize - 1);
				}

				public K next() {
					ke.index(ke.index() + 1);
					return (K) ke;
				}

				public void remove() {
					AbstractHugeMap.this.remove(ke);
				}
			};
		}

		@Override
		public int size() {
			return AbstractHugeMap.this.size();
		}
	}

	private class AHMValues extends AbstractCollection<V> {
		@Override
		public Iterator<V> iterator() {
			return new Iterator<V>() {
				final VE ve = acquireValueElement(-1);

				public boolean hasNext() {
					return ve.index() < (longSize - 1);
				}

				public V next() {
					ve.index(ve.index() + 1);
					return (V) ve;
				}

				public void remove() {
					AbstractHugeMap.this.remove(acquireKeyElement(ve.index()));
				}
			};
		}

		@Override
		public int size() {
			return AbstractHugeMap.this.size();
		}
	}

	private class AHMEntrySet extends AbstractSet<Entry<K, V>> {
		@Override
		public Iterator<Entry<K, V>> iterator() {
			return new EntryIterator();
		}

		@Override
		public int size() {
			return AbstractHugeMap.this.size();
		}

		class EntryIterator implements Iterator<Entry<K, V>>, Entry<K, V> {
			final KE ke = acquireKeyElement(-1);
			final VE ve = acquireValueElement(-1);

			public boolean hasNext() {
				return ve.index() < (longSize - 1);
			}

			public Entry<K, V> next() {
				final long idx = ke.index() + 1;
				ke.index(idx);
				ve.index(idx);
				return this;
			}

			public void remove() {
				AbstractHugeMap.this.remove(ke);
			}

			public K getKey() {
				return (K) ke;
			}

			public V getValue() {
				return (V) ve;
			}

			public V setValue(final V value) {
				ve.copyOf(value);
				return null;
			}
		}
	}
}
