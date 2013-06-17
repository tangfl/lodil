package com.weibo.lodil.mmap.wrap;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.weibo.lodil.DictKey;
import com.weibo.lodil.DictValue;
import com.weibo.lodil.LOG;
import com.weibo.lodil.mmap.api.HugeElement;
import com.weibo.lodil.mmap.api.HugeElementType;
import com.weibo.lodil.mmap.model.Enumerated16FieldModel;

public class DictEntryImpl implements DictEntry, HugeElement<DictEntry>, Externalizable {

	DictKey key;
	DictValue value;

	public DictEntryImpl() {
	}

	public DictEntryImpl(final DictKey key, final DictValue value) {
		this.key = key;
		this.value = value;
	}

	public DictEntryImpl(final String key, final String value) {
		this();
		setKey(key);
		setValue(value);
	}

	public String getKey() {
		return key == null ? null : key.getString();
	}

	public DictKey getDictKey() {
		return key;
	}

	public void setKey(final String key) {
		this.key = new DictKey(key);
	}

	public String getValue() {
		return value == null ? null : value.getString();
	}

	public DictValue getDictValue() {
		return value;
	}

	public void setValue(final String value) {
		this.value = new DictValue(value);
	}

	public HugeElementType hugeElementType() {
		return HugeElementType.BeanImpl;
	}

	public void copyOf(final DictEntry t) {
		LOG.debug(t.toString());
		setKey(t.getKey());
		setValue(t.getValue());
	}

	public void index(final long n) {
		LOG.debug(this.toString() + " n:" + n);
		throw new UnsupportedOperationException();
	}

	public long index() {
		return -1;
	}

	@Override
	public int hashCode() {
		return getKeyValueString().hashCode();
	}

	public long longHashCode() {
		return hashCode();
	}

	protected String getKeyValueString() {
		return getKey() + connector + getValue();
	}

	private void initFromString(final String result) {
		final String[] parts = result.split(connector);

		assert parts.length == 2;

		this.key = new DictKey(parts[0]);
		this.value = new DictValue(parts[1]);
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if ((o == null) || !(o instanceof DictEntry)) {
			return false;
		}

		final DictEntry that = (DictEntry) o;

		return that.getKey().equals(getKey()) && that.getValue().equals(getValue());
	}

	public void writeExternal(final ObjectOutput out) throws IOException {
		Enumerated16FieldModel.write(out, String.class, getKeyValueString());
	}

	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		initFromString(Enumerated16FieldModel.read(in, String.class));
	}

	@Override
	public String toString() {
		return this.getClass() + ":" + getKeyValueString();
	}

	public void recycle() {
		key = null;
		value = null;
	}

}
