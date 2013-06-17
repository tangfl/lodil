package com.weibo.lodil.mmap.wrap;

import com.weibo.lodil.DictKey;
import com.weibo.lodil.DictValue;
import com.weibo.lodil.mmap.api.HugeElementType;

public class DictEntryKeyImpl extends DictEntryImpl {

	public DictEntryKeyImpl() {
	}

	public DictEntryKeyImpl(final DictKey key, final DictValue value) {
		super(key, value);
	}

	public DictEntryKeyImpl(final String key, final String value) {
		super(key, value);
	}

	@Override
	public String getKeyValueString() {
		return getKey();
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

	@Override
	public HugeElementType hugeElementType() {
		return HugeElementType.KeyImpl;
	}

}
