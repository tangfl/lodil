package com.weibo.lodil.mmap.wrap;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.weibo.lodil.DictItem;
import com.weibo.lodil.DictValue;
import com.weibo.lodil.LOG;
import com.weibo.lodil.mmap.api.HugeElement;
import com.weibo.lodil.mmap.api.HugeElementType;
import com.weibo.lodil.mmap.model.Enumerated16FieldModel;

public class DictValueWrap extends DictValue implements DictItem, HugeElement<DictValue>, Externalizable {

	public DictValueWrap() {
	}

	public DictValueWrap(final DictValue value) {
		setString(value.getString());
	}

	public void writeExternal(final ObjectOutput out) throws IOException {
		Enumerated16FieldModel.write(out, String.class, getString());
	}

	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		setString(Enumerated16FieldModel.read(in, String.class));
	}

	public HugeElementType hugeElementType() {
		return HugeElementType.BeanImpl;
	}

	public void copyOf(final DictValue t) {
		LOG.debug(t.toString());
		setString(t.getString());
	}

	public void index(final long n) {
		LOG.debug(this.toString() + " n:" + n);
		throw new UnsupportedOperationException();
	}

	public long index() {
		LOG.debug(this.toString());
		return -1;
	}

	@Override
	public String toString() {
		return this.getClass() + ":" + getString() + " hash:" + getString().hashCode();
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if ((o == null) || !(o instanceof DictItem)) {
			return false;
		}

		final DictItem that = (DictItem) o;

		return that.getString().equals(getString());
	}

	@Override
	public int hashCode() {
		return getString().hashCode();
	}

	public long longHashCode() {
		LOG.debug(this.toString());
		return hashCode();
	}

}
