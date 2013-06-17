package com.weibo.lodil.mmap.wrap;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.weibo.lodil.DictItem;
import com.weibo.lodil.LOG;
import com.weibo.lodil.mmap.api.HugeElement;
import com.weibo.lodil.mmap.api.HugeElementType;
import com.weibo.lodil.mmap.impl.AbstractHugeContainer;
import com.weibo.lodil.mmap.impl.AbstractHugeElement;
import com.weibo.lodil.mmap.model.Enumerated16FieldModel;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class DictKeyElement extends AbstractHugeElement<DictKeyWrap, DictAllocation> implements
HugeElement<DictKeyWrap>, Externalizable, DictItem {

	DictAllocation allocation;
	DictHugeMap hugeMap;

	public DictKeyElement(final DictHugeMap hugeMap, final long n) {
		super((AbstractHugeContainer) hugeMap, n);
		this.hugeMap = hugeMap;
		LOG.debug("new DictKeyElement with n:" + n);
	}

	@Override
	public HugeElementType hugeElementType() {
		return HugeElementType.KeyElement;
	}

	public void copyOf(final DictKeyWrap t) {
		LOG.debug(t.toString());
		throw new UnsupportedOperationException();
	}

	@Override
	protected void updateAllocation0(final int allocationSize) {
		allocation = container.getAllocation(index);
		LOG.debug("updateAllocation0:" + allocationSize + " now:" + allocation);
	}
	
	public int hashCode(){
		return getString().hashCode();
	}

	@Override
	public int hashCode() {
		return getString().hashCode();
	}

	public long longHashCode() {
		return hashCode();
	}

	public String getString() {
		final String result = hugeMap.stringModelBuffer.get(allocation.keyBuffer, offset);
		LOG.debug(result + " getFrom " + allocation.keyBuffer + " at " + offset);
		return result;
	}

	public void setString(final String text) {
		LOG.debug(text + " setTo " + allocation.keyBuffer + " at " + offset);
		hugeMap.stringModelBuffer.set(allocation.keyBuffer, offset, text);
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
	public String toString() {
		return this.getClass() + ":" + getString() + " hash:" + getString().hashCode();
	}

	public void writeExternal(final ObjectOutput out) throws IOException {
		Enumerated16FieldModel.write(out, String.class, getString());
	}

	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		setString(Enumerated16FieldModel.read(in, String.class));
	}

}
