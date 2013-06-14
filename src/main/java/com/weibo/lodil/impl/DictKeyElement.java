package com.weibo.lodil.impl;

import com.weibo.lodil.DictKey;
import com.weibo.lodil.mmap.api.HugeElement;
import com.weibo.lodil.mmap.api.HugeElementType;
import com.weibo.lodil.mmap.impl.AbstractHugeContainer;
import com.weibo.lodil.mmap.impl.AbstractHugeElement;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class DictKeyElement extends AbstractHugeElement<DictKey, DictAllocation> implements HugeElement<DictKey> {

	public DictKeyElement(final DictHugeMap hugeMap, final long n) {
		super((AbstractHugeContainer) hugeMap, n);
	}

	@Override
	public HugeElementType hugeElementType() {
		return HugeElementType.KeyElement;
	}

	public void copyOf(final DictKey t) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected void updateAllocation0(final int allocationSize) {
	}

	public long longHashCode() {
		return 0;
	}

}
