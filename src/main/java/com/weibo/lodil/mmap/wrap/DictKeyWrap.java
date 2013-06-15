package com.weibo.lodil.mmap.wrap;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.weibo.lodil.DictItem;
import com.weibo.lodil.DictKey;
import com.weibo.lodil.LOG;
import com.weibo.lodil.mmap.api.HugeElement;
import com.weibo.lodil.mmap.api.HugeElementType;
import com.weibo.lodil.mmap.model.Enumerated16FieldModel;

public class DictKeyWrap extends DictKey implements DictItem, HugeElement<DictKey>, Externalizable {

	public DictKeyWrap() {
		super();
	}

	public DictKeyWrap(final DictKey key) {
		setString(key.getString());
	}

	public void writeExternal(final ObjectOutput out) throws IOException {
		Enumerated16FieldModel.write(out, String.class, getString());
	}

	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		setString(Enumerated16FieldModel.read(in, String.class));
	}

	public HugeElementType hugeElementType() {
		return HugeElementType.KeyImpl;
	}

	public void copyOf(final DictKey t) {
		LOG.debug(t.toString());
		this.setString(t.getString());
	}

	public void index(final long n) {
		LOG.debug(this.toString());
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
		return hashCode();
	}

}
