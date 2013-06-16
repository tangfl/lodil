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
import java.lang.annotation.ElementType;

import com.weibo.lodil.LOG;
import com.weibo.lodil.mmap.ObjectTypes;
import com.weibo.lodil.mmap.impl.AbstractHugeElement;
import com.weibo.lodil.mmap.impl.AbstractHugeMap;
import com.weibo.lodil.mmap.model.Boolean2FieldModel;
import com.weibo.lodil.mmap.model.BooleanFieldModel;
import com.weibo.lodil.mmap.model.Byte2FieldModel;
import com.weibo.lodil.mmap.model.ByteFieldModel;
import com.weibo.lodil.mmap.model.CharFieldModel;
import com.weibo.lodil.mmap.model.DoubleFieldModel;
import com.weibo.lodil.mmap.model.Enum8FieldModel;
import com.weibo.lodil.mmap.model.Enumerated16FieldModel;
import com.weibo.lodil.mmap.model.FloatFieldModel;
import com.weibo.lodil.mmap.model.IntFieldModel;
import com.weibo.lodil.mmap.model.LongFieldModel;
import com.weibo.lodil.mmap.model.ObjectFieldModel;
import com.weibo.lodil.mmap.model.ShortFieldModel;

public class HandTypesValueElement extends AbstractHugeElement<HandTypes, HandTypesAllocation> implements HandTypes,
		Externalizable {
	HandTypesAllocation allocation;

	public HandTypesValueElement(
			final AbstractHugeMap<HandTypesKey, HandTypesKeyElement, HandTypes, HandTypesValueElement, HandTypesAllocation> map,
			final long n) {
		super(map, n);
	}

	public void setBoolean(final boolean b) {
		BooleanFieldModel.set(allocation.m_boolean, offset, b);
	}

	public boolean getBoolean() {
		return BooleanFieldModel.get(allocation.m_boolean, offset);
	}

	public void setBoolean2(final Boolean b) {
		Boolean2FieldModel.set(allocation.m_boolean2, offset, b);
	}

	public Boolean getBoolean2() {
		return Boolean2FieldModel.get(allocation.m_boolean2, offset);
	}

	public void setByte(final byte b) {
		ByteFieldModel.set(allocation.m_byte, offset, b);
	}

	public byte getByte() {
		return ByteFieldModel.get(allocation.m_byte, offset);
	}

	public void setByte2(final Byte b) {
		Byte2FieldModel.set(allocation.m_byte2, offset, b);
	}

	public Byte getByte2() {
		return Byte2FieldModel.get(allocation.m_byte2, offset);
	}

	public void setChar(final char ch) {
		CharFieldModel.set(allocation.m_char, offset, ch);
	}

	public char getChar() {
		return CharFieldModel.get(allocation.m_char, offset);
	}

	public void setShort(final short s) {
		ShortFieldModel.set(allocation.m_short, offset, s);
	}

	public short getShort() {
		return ShortFieldModel.get(allocation.m_short, offset);
	}

	public void setInt(final int i) {
		IntFieldModel.set(allocation.m_int, offset, i);
	}

	public int getInt() {
		return IntFieldModel.get(allocation.m_int, offset);
	}

	public void setFloat(final float f) {
		FloatFieldModel.set(allocation.m_float, offset, f);
	}

	public float getFloat() {
		return FloatFieldModel.get(allocation.m_float, offset);
	}

	public void setLong(final long l) {
		LongFieldModel.set(allocation.m_long, offset, l);
	}

	public long getLong() {
		return LongFieldModel.get(allocation.m_long, offset);
	}

	public void setDouble(final double d) {
		DoubleFieldModel.set(allocation.m_double, offset, d);
	}

	public double getDouble() {
		return DoubleFieldModel.get(allocation.m_double, offset);
	}

	public void setElementType(final ElementType elementType) {
		((HandTypesMap) container).elementTypeFieldModel.set(allocation.m_elementType, offset, elementType);
	}

	public ElementType getElementType() {
		return ((HandTypesMap) container).elementTypeFieldModel.get(allocation.m_elementType, offset);
	}

	public void setString(final String text) {
		((HandTypesMap) container).stringEnumerated16FieldModel.set(allocation.m_string, offset, text);
	}

	public String getString() {
		return ((HandTypesMap) container).stringEnumerated16FieldModel.get(allocation.m_string, offset);
	}

	public void setA(final ObjectTypes.A a) {
		allocation.m_a[offset] = a;
	}

	public ObjectTypes.A getA() {
		return allocation.m_a[offset];
	}

	@Override
	protected void updateAllocation0(final int allocationSize) {
		allocation = container.getAllocation(index);
		LOG.debug("updateAllocation0:" + allocationSize + " now:" + allocation);
	}

	@Override
	public String toString() {
//		return "HandTypesElement{" + "boolean=" + getBoolean() + ", boolean2=" + getBoolean2() + ", byte=" + getByte()
//				+ ", byte2=" + getByte2() + ", char=" + getChar() + ", short=" + getShort() + ", int=" + getInt()
//				+ ", float=" + getFloat() + ", long=" + getLong() + ", double=" + getDouble() + ", elementType="
//				+ getElementType() + ", string='" + getString() + '\'' + '}';
		return "HandTypesValueElement{int=" + getInt() + "} hash:" + longHashCode();
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if ((o == null) || !(o instanceof HandTypes)) {
			return false;
		}

		final HandTypes that = (HandTypes) o;

		if (getBoolean() != that.getBoolean()) {
			return false;
		}
		if (Boolean2FieldModel.notEquals(getBoolean2(), that.getBoolean2())) {
			return false;
		}
		if (getByte() != that.getByte()) {
			return false;
		}
		if (Byte2FieldModel.notEquals(getByte2(), that.getByte2())) {
			return false;
		}
		if (getChar() != that.getChar()) {
			return false;
		}
		if (getShort() != that.getShort()) {
			return false;
		}
		if (getInt() != that.getInt()) {
			return false;
		}
		if (FloatFieldModel.notEquals(getFloat(), that.getFloat())) {
			return false;
		}
		if (getLong() != that.getLong()) {
			return false;
		}
		if (DoubleFieldModel.notEquals(getDouble(), that.getDouble())) {
			return false;
		}
		if (getElementType() != that.getElementType()) {
			return false;
		}
		if (ObjectFieldModel.notEquals(getString(), that.getString())) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		return (int) longHashCode();
	}

	public long longHashCode() {
		return (((((((((((((((((((((((getBoolean() ? 1 : 0) * 31L) + Boolean2FieldModel.hashCode(getBoolean2())) * 31L) + getByte()) * 31L) + Byte2FieldModel
				.hashCode(getByte2())) * 31L) + getChar()) * 31L) + getShort()) * 31L) + getInt()) * 31L) + FloatFieldModel
				.hashCode(getFloat())) * 31L) + LongFieldModel.hashCode(getLong())) * 31L) + DoubleFieldModel
				.hashCode(getDouble())) * 31L) + Enum8FieldModel.hashCode(getElementType()))
				* 31L) + (Enumerated16FieldModel.hashCode(getString()) * 31L) + ObjectFieldModel.hashCode(getA()));
	}

	public void copyOf(final HandTypes t) {
		LOG.debug(t.toString());
		
		setBoolean(t.getBoolean());
		setBoolean2(t.getBoolean2());
		setByte2(t.getByte2());
		setA(t.getA());

		if (t instanceof HandTypesValueElement) {
			final HandTypesValueElement mte = (HandTypesValueElement) t;
			if (mte.container == container) {

				allocation.m_byte.put(offset, mte.allocation.m_byte.get(mte.offset));
				allocation.m_char.put(offset, mte.allocation.m_char.get(mte.offset));
				allocation.m_double.put(offset, mte.allocation.m_double.get(mte.offset));
				allocation.m_elementType.put(offset, mte.allocation.m_elementType.get(mte.offset));
				allocation.m_float.put(offset, mte.allocation.m_float.get(mte.offset));
				allocation.m_int.put(offset, mte.allocation.m_int.get(mte.offset));
				allocation.m_long.put(offset, mte.allocation.m_long.get(mte.offset));
				allocation.m_short.put(offset, mte.allocation.m_short.get(mte.offset));
				allocation.m_string.put(offset, mte.allocation.m_string.get(mte.offset));
				return;
			}
		}
		setByte(t.getByte());
		setChar(t.getChar());
		setDouble(t.getDouble());
		setElementType(t.getElementType());
		setFloat(t.getFloat());
		setInt(t.getInt());
		setLong(t.getLong());
		setShort(t.getShort());
		setString(t.getString());

	}

	public void writeExternal(final ObjectOutput out) throws IOException {
		BooleanFieldModel.write(out, getBoolean());
		Boolean2FieldModel.write(out, getBoolean2());
		ByteFieldModel.write(out, getByte());
		Byte2FieldModel.write(out, getByte2());
		CharFieldModel.write(out, getChar());
		DoubleFieldModel.write(out, getDouble());
		Enum8FieldModel.write(out, ElementType.class, getElementType());
		Enumerated16FieldModel.write(out, String.class, getString());
		FloatFieldModel.write(out, getFloat());
		IntFieldModel.write(out, getInt());
		LongFieldModel.write(out, getLong());
		ShortFieldModel.write(out, getShort());
		ObjectFieldModel.write(out, ObjectTypes.A.class, getA());
	}

	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		setBoolean(BooleanFieldModel.read(in));
		setBoolean2(Boolean2FieldModel.read(in));
		setByte(ByteFieldModel.read(in));
		setByte2(Byte2FieldModel.read(in));
		setChar(CharFieldModel.read(in));
		setDouble(DoubleFieldModel.read(in));
		setElementType(Enum8FieldModel.read(in, ElementType.class));
		setString(Enumerated16FieldModel.read(in, String.class));
		setFloat(FloatFieldModel.read(in));
		setInt(IntFieldModel.read(in));
		setLong(LongFieldModel.read(in));
		setShort(ShortFieldModel.read(in));
		setA(ObjectFieldModel.read(in, ObjectTypes.A.class));
	}
}
