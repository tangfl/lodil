package com.weibo.lodil.mmap.wrap;

import com.weibo.lodil.DictItem;
import com.weibo.lodil.LOG;
import com.weibo.lodil.mmap.api.HugeElement;
import com.weibo.lodil.mmap.api.HugeElementType;
import com.weibo.lodil.mmap.impl.AbstractHugeElement;

public class DictValueElement extends AbstractHugeElement<DictValueWrap, DictAllocation> implements
HugeElement<DictValueWrap>,
DictItem {

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
		LOG.debug(text + " setTo " + allocation.valueBuffer + " at " + offset);
		((DictHugeMap) container).stringModelBuffer.set(allocation.valueBuffer, offset, text);
	}

	public String getString() {
		final String result = ((DictHugeMap) container).stringModelBuffer.get(allocation.valueBuffer, offset);
		LOG.debug(result + " getFrom " + allocation.valueBuffer + " at " + offset);
		return result;
	}

	public void copyOf(final DictValueWrap t) {
		LOG.debug(t.toString());
		setString(t.getString());
	}

	@Override
	public void index(final long n) {
		//LOG.debug(this.toString() + " n:" + n);
		super.index(n);
	}

	@Override
	public long index() {
		//LOG.debug(this.toString());
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
		allocation = container.getAllocation(index);
		LOG.debug("updateAllocation0:" + allocationSize + " now:" + allocation);
	}
	
	@Override
	public String toString() {
		return this.getClass() + ":" + getString() + " hash:" + getString().hashCode();
	}

}
