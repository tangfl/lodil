/**
 * 
 */
package com.weibo.lodil;


/**
 * @author tangfulin
 *
 */
public class DictValue {

	String valueString;
	byte[] valueBytes;
	Number valueNumber;

	public DictValue() {
	}

	public DictValue(final String value) {
		this.valueString = value;
		this.valueBytes = value.getBytes();
	}

	public DictValue(final byte[] bytes) {
		this.valueBytes = bytes;
		this.valueString = new String(bytes);
	}

	public void setString(final String s) {
		this.valueString = s;
		this.valueBytes = s.getBytes();
	}

	public String getString() {
		return valueString;
	}

	/**
	 * @param args
	 */
	public static void main(final String[] args) {

	}

}
