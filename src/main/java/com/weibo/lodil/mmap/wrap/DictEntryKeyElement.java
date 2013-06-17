package com.weibo.lodil.mmap.wrap;

import com.weibo.lodil.mmap.api.HugeElementType;

public class DictEntryKeyElement extends DictEntryElement {

	public DictEntryKeyElement(final DictHugeEntryMap hugeMap, final long n) {
		super(hugeMap, n);
	}

	@Override
	public void setKey(final String key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setValue(final String value) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected String getKeyValueString() {
		// getkeyValue(false);
		return key == null ? "" : key.getString();
	}

	@Override
	public HugeElementType hugeElementType() {
		return HugeElementType.KeyElement;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if ((o == null) || !(o instanceof DictEntry)) {
			return false;
		}

		final DictEntry that = (DictEntry) o;

		return that.getKey().equals(getKey());
	}

}
