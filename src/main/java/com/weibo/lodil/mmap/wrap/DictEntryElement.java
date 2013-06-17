package com.weibo.lodil.mmap.wrap;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.weibo.lodil.DictKey;
import com.weibo.lodil.DictValue;
import com.weibo.lodil.LOG;
import com.weibo.lodil.mmap.api.HugeElement;
import com.weibo.lodil.mmap.impl.AbstractHugeElement;
import com.weibo.lodil.mmap.model.Enumerated16FieldModel;

public class DictEntryElement extends AbstractHugeElement<DictEntry, DictAllocation> implements HugeElement<DictEntry>,
Externalizable, DictEntry {

	DictAllocation allocation;
	DictHugeEntryMap hugeMap;

	DictKey key;
	DictValue value;

	public DictEntryElement(final DictHugeEntryMap hugeMap, final long n) {
		super(hugeMap, n);
		this.hugeMap = hugeMap;
		// LOG.debug(this.getClass() + " new with n:" + n);
	}

	public String getKey() {
		getkeyValue(false);
		return key.getString();
	}

	public void setKey(final String key) {
		this.key = new DictKey(key);
		setkeyValue();
	}

	public String getValue() {
		getkeyValue(false);
		return value.getString();
	}

	public void setValue(final String value) {
		this.value = new DictValue(value);
		setkeyValue();
	}

	private void setkeyValue() {
		if ((key == null) || (value == null)) {
			// LOG.debug(" ignore set: " + key + " : " + value);
			return;
		}
		final String keyvalue = getKeyValueString();
		LOG.debug(keyvalue + " setTo " + allocation.keyBuffer.hashCode() + " at " + offset);
		hugeMap.stringModelBuffer.set(allocation.keyBuffer, offset, keyvalue);
	}

	protected String getKeyValueString() {
		final String keyvalue = (key == null ? "" : key.getString()) + connector
				+ (value == null ? "" : value.getString());
		return keyvalue;
	}

	protected void getkeyValue(final boolean forceGetFromBuffer) {
		if (forceGetFromBuffer || ((key == null) && (value == null))) {
			final String result = hugeMap.stringModelBuffer.get(allocation.keyBuffer, offset);
			initFromString(result);

			LOG.debug(result + " getFrom " + allocation.keyBuffer.hashCode() + " at " + offset);
		}
	}

	protected void initFromString(final String result) {
		final String[] parts = result.split(connector);

		assert parts.length == 2;

		this.key = new DictKey(parts[0]);
		this.value = new DictValue(parts[1]);
	}


	public void writeExternal(final ObjectOutput out) throws IOException {
		Enumerated16FieldModel.write(out, String.class, getKeyValueString());
	}

	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		initFromString(Enumerated16FieldModel.read(in, String.class));
	}

	public void copyOf(final DictEntry t) {
		LOG.debug(t.toString());
		setKey(t.getKey());
		setValue(t.getValue());
	}

	// when in reuse, index to anther place means must read key/value again
	@Override
	public void index(final long n) {
		super.index(n);
	}

	@Override
	public long index() {
		return super.index();
	}

	@Override
	public int hashCode() {
		return getKeyValueString().hashCode();
	}

	public long longHashCode() {
		return hashCode();
	}

	@Override
	protected void updateAllocation0(final int allocationSize) {
		allocation = container.getAllocation(index);
		// LOG.debug("updateAllocation0:" + allocationSize + " now:" +
		// allocation);
	}

	@Override
	public String toString() {
		return this.getClass() + ":" + getKeyValueString() + " hash:" + hashCode();
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

		return that.getKey().equals(getKey()) && that.getValue().equals(getValue());
	}

	@Override
	public void recycle() {
		key = null;
		value = null;
	}

}
