package com.weibo.lodil.mmap.wrap;

import com.weibo.lodil.DictItem;
import com.weibo.lodil.LOG;
import com.weibo.lodil.mmap.api.HugeElement;
import com.weibo.lodil.mmap.api.HugeElementType;
import com.weibo.lodil.mmap.impl.AbstractHugeContainer;
import com.weibo.lodil.mmap.impl.AbstractHugeElement;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class DictKeyElement extends AbstractHugeElement<DictKeyWrap, DictAllocation> implements
HugeElement<DictKeyWrap>,
DictItem {

	String valueString = "";
	DictAllocation allocation;

	public DictKeyElement(final DictHugeMap hugeMap, final long n) {
		super((AbstractHugeContainer) hugeMap, n);
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
		LOG.debug("updateAllocation0:" + allocationSize);
		allocation = container.getAllocation(index);
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
		return hashCode();
	}

	public String getString() {
		return valueString;
	}

	public void setString(final String s) {
		this.valueString = s;
	}
	
	@Override
	public String toString() {
		return this.getClass() + ":" + getString() + " hash:" + getString().hashCode();
	}

}
