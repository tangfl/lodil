package com.weibo.lodil.mmap.wrap;

import java.util.HashMap;
import java.util.Map;

import com.weibo.lodil.DictKey;
import com.weibo.lodil.DictValue;
import com.weibo.lodil.mmap.HugeMapBuilder;
import com.weibo.lodil.mmap.api.HugeAllocation;
import com.weibo.lodil.mmap.impl.AbstractHugeMap;
import com.weibo.lodil.mmap.impl.MappedFileChannel;
import com.weibo.lodil.mmap.model.Enumerated16FieldModel;

public class DictHugeEntryMap extends
AbstractHugeMap<DictEntryImpl, DictEntryElement, DictEntryImpl, DictEntryElement, DictAllocation> {

	final Enumerated16FieldModel<String> stringModelBuffer = new Enumerated16FieldModel<String>("text", 11,
			String.class);

	public DictHugeEntryMap(final HugeMapBuilder<DictEntryImpl, DictEntryImpl> mapBuilder) {
		super(mapBuilder);
	}

	@Override
	protected DictEntryElement createValueElement(final long n) {
		return new DictEntryElement(this, n);
	}

	@Override
	protected DictEntryElement createKeyElement(final long n) {
		return new DictEntryElement(this, n);
	}

	@Override
	protected DictEntryImpl createKeyImpl() {
		return new DictEntryImpl();
	}

	@Override
	protected DictEntryImpl createValueImpl() {
		return new DictEntryImpl();
	}

	@Override
	protected DictAllocation createAllocation(final MappedFileChannel mfc) {
		return new DictAllocation(allocationSize, mfc);
	}

	@Override
	protected void compactStart() {
		stringModelBuffer.compactStart();
	}

	protected void compactOnAllocation0(final HugeAllocation allocation, final long thisSize) {
		compactOnAllocation((DictAllocation) allocation, thisSize);
	}

	@Override
	protected void compactEnd() {
		stringModelBuffer.compactEnd();
	}

	@Override
	protected void compactOnAllocation(final DictAllocation allocation, final long i) {
		stringModelBuffer.compactScan(allocation.keyBuffer, i);
	}

	public boolean contains(final DictKey key) {
		final DictEntryImpl wrap = new DictEntryImpl(key, null);
		return super.containsKey(wrap);
	}

	public DictValue get(final DictKey key) {
		final DictEntryImpl wrap = new DictEntryImpl(key, null);
		final DictEntryImpl result = super.get(wrap);
		if (result == null) {
			return null;
		}
		return result.getDictValue();
	}

	public DictValue put(final DictKey key, final DictValue value) {
		final DictEntryImpl wrap = new DictEntryImpl(key, value);
		final DictEntryImpl result = super.put(wrap, wrap);
		if (result == null) {
			return null;
		}
		return result.getDictValue();
	}

	public boolean mset(final Map<DictKey, DictValue> keyvalues) {
		final Map<DictEntryImpl, DictEntryImpl> wrapMap = new HashMap<DictEntryImpl, DictEntryImpl>();
		for (final DictKey key : keyvalues.keySet()) {
			final DictEntryImpl wrap = new DictEntryImpl(key, keyvalues.get(key));
			wrapMap.put(wrap, wrap);
		}
		super.putAll(wrapMap);
		return true;
	}

	public boolean remove(final DictKey key) {
		final DictEntryImpl wrap = new DictEntryImpl(key, null);
		super.remove(wrap);
		return true;
	}

	/**
	 * @param args
	 */
	public static void main(final String[] args) {

	}

}
