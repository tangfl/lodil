package com.weibo.lodil.mmap.wrap;

import com.weibo.lodil.DictItem;
import com.weibo.lodil.LOG;
import com.weibo.lodil.mmap.api.HugeElement;
import com.weibo.lodil.mmap.api.HugeElementType;
import com.weibo.lodil.mmap.impl.AbstractHugeElement;

public class DictValueElement extends AbstractHugeElement<DictValueWrap, DictAllocation> implements
HugeElement<DictValueWrap>,
DictItem {

	String valueString = "";
	DictAllocation allocation;

	public DictValueElement(final DictHugeMap hugeMap, final long n) {
		super(hugeMap, n);
		LOG.debug("new DictValueElement with n:" + n);
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
		LOG.debug(t.toString());
		setString(t.getString());
	}

	@Override
	public void index(final long n) {
		LOG.debug(this.toString() + " n:" + n);
		super.index(n);
	}

	@Override
	public long index() {
		LOG.debug(this.toString());
		return super.index();
	}

	public int hashCode(){
		return getString().hashCode();
	}

	public long longHashCode() {
		LOG.debug(this.toString());
		return hashCode();
	}


	@Override
	protected void updateAllocation0(final int allocationSize) {
		LOG.debug("updateAllocation0:" + allocationSize);
		allocation = container.getAllocation(index);
	}
	
	@Override
	public String toString() {
		return this.getClass() + ":" + getString() + " hash:" + getString().hashCode();
	}

}
