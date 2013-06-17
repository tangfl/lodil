package com.weibo.lodil.mmap.wrap;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.weibo.lodil.DictItem;
import com.weibo.lodil.mmap.api.HugeElement;
import com.weibo.lodil.mmap.api.HugeElementType;
import com.weibo.lodil.mmap.impl.AbstractHugeElement;
import com.weibo.lodil.mmap.model.Enumerated16FieldModel;

public class DictValueElement extends AbstractHugeElement<DictValueWrap, DictAllocation> implements
HugeElement<DictValueWrap>, Externalizable, DictItem {

	DictAllocation allocation;

	public DictValueElement(final DictHugeMap hugeMap, final long n) {
		super(hugeMap, n);
	}

	@Override
	public HugeElementType hugeElementType() {
		return HugeElementType.Element;
	}

	public void setString(final String text) {
		((DictHugeMap) container).stringEnumerated16FieldModel.set(allocation.m_string, offset, text);
	}

	public String getString() {
		return ((DictHugeMap) container).stringEnumerated16FieldModel.get(allocation.m_string, offset);
	}

	public void copyOf(final DictValueWrap t) {
		setString(t.getString());
	}

	@Override
	public int hashCode() {
		return getString().hashCode();
	}

	public long longHashCode() {
		return hashCode();
	}

	@Override
	protected void updateAllocation0(final int allocationSize) {
		allocation = container.getAllocation(index);
	}

	public void writeExternal(final ObjectOutput out) throws IOException {
		Enumerated16FieldModel.write(out, String.class, getString());
	}

	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		setString(Enumerated16FieldModel.read(in, String.class));
	}

}
