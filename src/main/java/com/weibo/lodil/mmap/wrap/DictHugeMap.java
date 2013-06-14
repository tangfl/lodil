package com.weibo.lodil.mmap.wrap;

import java.util.HashMap;
import java.util.Map;

import com.weibo.lodil.DictKey;
import com.weibo.lodil.DictValue;
import com.weibo.lodil.mmap.HugeMapBuilder;
import com.weibo.lodil.mmap.impl.AbstractHugeMap;
import com.weibo.lodil.mmap.impl.MappedFileChannel;
import com.weibo.lodil.mmap.model.Enumerated16FieldModel;

public class DictHugeMap extends
AbstractHugeMap<DictKeyWrap, DictKeyElement, DictValueWrap, DictValueElement, DictAllocation> {

	final Enumerated16FieldModel<String> stringEnumerated16FieldModel = new Enumerated16FieldModel<String>("text", 11,
			String.class);

	public DictHugeMap(final HugeMapBuilder<DictKeyWrap, DictValueWrap> mapBuilder) {
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
	protected DictKeyWrap createKeyImpl() {
		return new DictKeyWrap();
	}

	@Override
	protected DictValueWrap createValueImpl() {
		return new DictValueWrap();
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

	public boolean contains(final DictKey key) {
		final DictKeyWrap wrap = new DictKeyWrap(key);
		return super.containsKey(wrap);
	}

	public DictValue get(final DictKey key) {
		final DictKeyWrap wrap = new DictKeyWrap(key);
		return super.get(wrap);
	}

	public DictValue put(final DictKey key, final DictValue value) {
		final DictKeyWrap kwrap = new DictKeyWrap(key);
		final DictValueWrap vwrap = new DictValueWrap(value);
		return super.put(kwrap, vwrap);
	}

	public boolean mset(final Map<DictKey, DictValue> keyvalues) {
		final Map<DictKeyWrap, DictValueWrap> wrapMap = new HashMap<DictKeyWrap, DictValueWrap>();
		for (final DictKey key : keyvalues.keySet()) {
			final DictKeyWrap kwrap = new DictKeyWrap(key);
			final DictValueWrap vwrap = new DictValueWrap(keyvalues.get(key));
			wrapMap.put(kwrap, vwrap);
		}
		super.putAll(wrapMap);
		return true;
	}

	public boolean remove(final DictKey key) {
		final DictKeyWrap wrap = new DictKeyWrap(key);
		super.remove(wrap);
		return true;
	}

	/**
	 * @param args
	 */
	public static void main(final String[] args) {

	}

}
