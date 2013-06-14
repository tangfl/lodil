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

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.weibo.lodil.mmap.api.HugeElement;
import com.weibo.lodil.mmap.api.HugeElementType;
import com.weibo.lodil.mmap.model.BooleanFieldModel;
import com.weibo.lodil.mmap.model.IntFieldModel;

public class HandTypesKeyImpl implements HandTypesKey, HugeElement<HandTypes>, Externalizable {
	private boolean m_boolean;
	private int m_int;

	public void setBoolean(final boolean b) {
		this.m_boolean = b;
	}

	public boolean getBoolean() {
		return m_boolean;
	}

	public void setInt(final int i) {
		this.m_int = i;
	}

	public int getInt() {
		return m_int;
	}

	public void index(final long n) {
		throw new UnsupportedOperationException();
	}

	public long index() {
		return -1;
	}

	public void writeExternal(final ObjectOutput out) throws IOException {
		BooleanFieldModel.write(out, getBoolean());
		IntFieldModel.write(out, getInt());
	}

	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		setBoolean(BooleanFieldModel.read(in));
		setInt(IntFieldModel.read(in));
	}

	public void copyOf(final HandTypes t) {
		setBoolean(t.getBoolean());
		setInt(t.getInt());
	}

	public HugeElementType hugeElementType() {
		return HugeElementType.KeyImpl;
	}

	@Override
	public String toString() {
		return "HandTypesElement{" + "int=" + getInt() + ", boolean=" + getBoolean() + '}';
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

	@Override
	public int hashCode() {
		return (int) longHashCode();
	}

	public long longHashCode() {
		return (getInt() * 31L) + (getBoolean() ? 1 : 0);
	}
}
