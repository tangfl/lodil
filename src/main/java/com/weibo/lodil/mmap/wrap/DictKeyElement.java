package com.weibo.lodil.mmap.wrap;

import com.weibo.lodil.DictItem;
import com.weibo.lodil.mmap.api.HugeElement;
import com.weibo.lodil.mmap.api.HugeElementType;
import com.weibo.lodil.mmap.impl.AbstractHugeContainer;
import com.weibo.lodil.mmap.impl.AbstractHugeElement;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class DictKeyElement extends AbstractHugeElement<DictKeyWrap, DictAllocation> implements
HugeElement<DictKeyWrap>,
DictItem {

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

	public long longHashCode() {
		return 0;
	}

	public String getString() {
		return valueString;
	}

	public void setString(final String s) {
		this.valueString = s;
	}

}
