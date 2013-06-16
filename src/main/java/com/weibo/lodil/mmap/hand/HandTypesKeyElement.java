package com.weibo.lodil.mmap.hand;

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

import com.weibo.lodil.LOG;
import com.weibo.lodil.mmap.api.HugeElementType;
import com.weibo.lodil.mmap.impl.AbstractHugeContainer;
import com.weibo.lodil.mmap.impl.AbstractHugeElement;
import com.weibo.lodil.mmap.impl.AbstractHugeMap;
import com.weibo.lodil.mmap.model.BooleanFieldModel;
import com.weibo.lodil.mmap.model.IntFieldModel;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class HandTypesKeyElement extends AbstractHugeElement<HandTypesKey, HandTypesAllocation> implements HandTypesKey {
	HandTypesAllocation allocation;

	public HandTypesKeyElement(
			final AbstractHugeMap<HandTypesKey, HandTypesKeyElement, HandTypes, HandTypesValueElement, HandTypesAllocation> map,
			final long n) {
		super((AbstractHugeContainer) map, n);
	}

	public void setBoolean(final boolean b) {
		throw new UnsupportedOperationException();
	}

	public boolean getBoolean() {
		return BooleanFieldModel.get(allocation.m_boolean, offset);
	}

	public void setInt(final int i) {
		throw new UnsupportedOperationException();
	}

	public int getInt() {
		return IntFieldModel.get(allocation.m_int, offset);
	}

	@Override
	protected void updateAllocation0(final int allocationSize) {
		allocation = container.getAllocation(index);
		LOG.debug("updateAllocation0:" + allocationSize + " now:" + allocation);
	}

	@Override
	public String toString() {
		return "HandTypesKeyElement{" + "int=" + getInt() + ", boolean=" + getBoolean() + '}';
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if ((o == null) || !(o instanceof HandTypesKey)) {
			return false;
		}

		final HandTypesKey that = (HandTypesKey) o;

		if (getInt() != that.getInt()) {
			return false;
		}
		if (getBoolean() != that.getBoolean()) {
			return false;
		}

		return true;
	}

	public long longHashCode() {
		return (getInt() * 31L) + (getBoolean() ? 1 : 0);
	}

	@Override
	public int hashCode() {
		return (int) longHashCode();
	}

	public void copyOf(final HandTypesKey t) {
		LOG.debug(t.toString());

		throw new UnsupportedOperationException();
	}

	@Override
	public HugeElementType hugeElementType() {
		return HugeElementType.KeyElement;
	}
}
