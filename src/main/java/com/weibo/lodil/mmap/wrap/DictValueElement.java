package com.weibo.lodil.mmap.wrap;

import com.weibo.lodil.DictItem;
import com.weibo.lodil.DictKey;
import com.weibo.lodil.mmap.api.HugeElement;
import com.weibo.lodil.mmap.api.HugeElementType;
import com.weibo.lodil.mmap.impl.AbstractHugeElement;

public class DictValueElement extends AbstractHugeElement<DictValueWrap, DictAllocation> implements
HugeElement<DictValueWrap>,
DictItem {

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
	}

	@Override
	public void index(final long n) {
	}

	@Override
	public long index() {
		return 0;
	}

	public long longHashCode() {
		return 0;
	}

	public void copyOf(final DictKey t) {
	}

	@Override
	protected void updateAllocation0(final int allocationSize) {
	}

}
