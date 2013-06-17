package com.weibo.lodil.mmap.wrap;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.weibo.lodil.DictItem;
import com.weibo.lodil.mmap.api.HugeElement;
import com.weibo.lodil.mmap.api.HugeElementType;
import com.weibo.lodil.mmap.impl.AbstractHugeContainer;
import com.weibo.lodil.mmap.impl.AbstractHugeElement;
import com.weibo.lodil.mmap.model.Enumerated16FieldModel;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class DictKeyElement extends AbstractHugeElement<DictKeyWrap, DictAllocation> implements
HugeElement<DictKeyWrap>, Externalizable, DictItem {

	String valueString;

	public DictKeyElement(final DictHugeMap hugeMap, final long n) {
		super((AbstractHugeContainer) hugeMap, n);
	}

	@Override
	public HugeElementType hugeElementType() {
		return HugeElementType.KeyElement;
	}

	public void copyOf(final DictKeyWrap t) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected void updateAllocation0(final int allocationSize) {
	}

	@Override
	public int hashCode() {
		return getString().hashCode();
	}

	public long longHashCode() {
		return hashCode();
	}

	public String getString() {
		return valueString;
	}

	public void setString(final String s) {
		this.valueString = s;
	}

	public void writeExternal(final ObjectOutput out) throws IOException {
		Enumerated16FieldModel.write(out, String.class, getString());
	}

	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		setString(Enumerated16FieldModel.read(in, String.class));
	}

}
