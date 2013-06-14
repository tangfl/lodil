package com.weibo.lodil.impl;

import com.weibo.lodil.DictKey;
import com.weibo.lodil.DictValue;
import com.weibo.lodil.mmap.HugeMapBuilder;
import com.weibo.lodil.mmap.impl.AbstractHugeMap;
import com.weibo.lodil.mmap.impl.MappedFileChannel;
import com.weibo.lodil.mmap.model.Enumerated16FieldModel;

public class DictHugeMap extends AbstractHugeMap<DictKey, DictKeyElement, DictValue, DictValueElement, DictAllocation> {

	final Enumerated16FieldModel<String> stringEnumerated16FieldModel = new Enumerated16FieldModel<String>("text", 11,
			String.class);

	public DictHugeMap(final HugeMapBuilder<DictKey, DictValue> mapBuilder) {
		super(mapBuilder);
	}

	@Override
	protected DictValueElement createValueElement(final long n) {
		return new DictValueElement(this, n);
	}

	@Override
	protected DictKeyElement createKeyElement(final long n) {
		return new DictKeyElement(this, n);
	}

	@Override
	protected DictKey createKeyImpl() {
		return new DictKey();
	}

	@Override
	protected DictValue createValueImpl() {
		return new DictValue();
	}

	@Override
	protected DictAllocation createAllocation(final MappedFileChannel mfc) {
		return new DictAllocation(allocationSize, mfc);
	}

	@Override
	protected void compactStart() {
	}

	@Override
	protected void compactEnd() {
	}

	@Override
	protected void compactOnAllocation(final DictAllocation ta, final long i) {
	}

	/**
	 * @param args
	 */
	public static void main(final String[] args) {

	}

}
