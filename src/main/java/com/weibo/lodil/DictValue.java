/**
 * 
 */
package com.weibo.lodil;



/**
 * @author tangfulin
 *
 */
public class DictValue implements DictItem {

	String valueString;

	public DictValue() {
	}

	public DictValue(final String value) {
		this.valueString = value;
	}

	public DictValue(final byte[] bytes) {
		this.valueString = new String(bytes);
	}

	public void setString(final String s) {
		this.valueString = s;
	}

	public String getString() {
		return valueString;
	}

	@Override
	public int hashCode() {
		return valueString.hashCode();
	}

	@Override
	public String toString() {
		return this.getClass() + ":" + getString();
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if ((o == null) || !(o instanceof DictValue)) {
			return false;
		}

		final DictValue that = (DictValue) o;

		return that.getString().equals(getString());
	}

}
