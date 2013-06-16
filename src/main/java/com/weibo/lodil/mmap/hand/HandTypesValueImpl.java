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
import com.weibo.lodil.mmap.api.HugeElement;
import com.weibo.lodil.mmap.api.HugeElementType;
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

public class HandTypesValueImpl implements HandTypes, HugeElement<HandTypes>, Externalizable {
	private boolean m_boolean;
	private Boolean m_boolean2;
	private byte m_byte;
	private Byte m_byte2;
	private char m_char;
	private short m_short;
	private int m_int;
	private float m_float;
	private long m_long;
	private double m_double;
	private ElementType m_elementType;
	private String m_string;
	private ObjectTypes.A m_a;

	public void setBoolean(final boolean b) {
		this.m_boolean = b;
	}

	public boolean getBoolean() {
		return m_boolean;
	}

	public void setBoolean2(final Boolean b) {
		this.m_boolean2 = b;
	}

	public Boolean getBoolean2() {
		return m_boolean2;
	}

	public void setByte(final byte b) {
		this.m_byte = b;
	}

	public byte getByte() {
		return m_byte;
	}

	public void setByte2(final Byte b) {
		this.m_byte2 = b;
	}

	public Byte getByte2() {
		return m_byte2;
	}

	public void setChar(final char ch) {
		this.m_char = ch;
	}

	public char getChar() {
		return m_char;
	}

	public void setShort(final short s) {
		this.m_short = s;
	}

	public short getShort() {
		return m_short;
	}

	public void setInt(final int i) {
		this.m_int = i;
	}

	public int getInt() {
		return m_int;
	}

	public void setFloat(final float f) {
		this.m_float = f;
	}

	public float getFloat() {
		return m_float;
	}

	public void setLong(final long l) {
		this.m_long = l;
	}

	public long getLong() {
		return m_long;
	}

	public void setDouble(final double d) {
		this.m_double = d;
	}

	public double getDouble() {
		return m_double;
	}

	public void setElementType(final ElementType elementType) {
		this.m_elementType = elementType;
	}

	public ElementType getElementType() {
		return m_elementType;
	}

	public void setString(final String text) {
		this.m_string = text;
	}

	public String getString() {
		return m_string;
	}

	public ObjectTypes.A getA() {
		return m_a;
	}

	public void setA(final ObjectTypes.A a) {
		this.m_a = a;
	}

	public void index(final long n) {
		throw new UnsupportedOperationException();
	}

	public long index() {
		return 0;
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

	public void copyOf(final HandTypes t) {
		LOG.debug(t.toString());
		
		setBoolean(t.getBoolean());
		setBoolean2(t.getBoolean2());
		setByte(t.getByte());
		setByte2(t.getByte2());
		setChar(t.getChar());
		setDouble(t.getDouble());
		setElementType(t.getElementType());
		setFloat(t.getFloat());
		setInt(t.getInt());
		setLong(t.getLong());
		setShort(t.getShort());
		setString(t.getString());
		setA(t.getA());
	}

	public HugeElementType hugeElementType() {
		return HugeElementType.BeanImpl;
	}

	@Override
	public String toString() {
//		return "HandTypesValueImpl{" + "boolean=" + getBoolean() + ", boolean2=" + getBoolean2() + ", byte=" + getByte()
//				+ ", byte2=" + getByte2() + ", char=" + getChar() + ", short=" + getShort() + ", int=" + getInt()
//				+ ", float=" + getFloat() + ", long=" + getLong() + ", double=" + getDouble() + ", elementType="
//				+ getElementType() + ", string='" + getString() + '\'' + '}';
		return "HandTypesValueImpl{int=" + getInt() + "} hash:" + longHashCode();
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if ((o == null) || (getClass() != o.getClass())) {
			return false;
		}

		final HandTypesListElement that = (HandTypesListElement) o;

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
}
