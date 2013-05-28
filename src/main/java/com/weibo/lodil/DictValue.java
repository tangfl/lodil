/**
 * 
 */
package com.weibo.lodil;


/**
 * @author tangfulin
 *
 */
public class DictValue {

	final String valueString;
	final byte[] valueBytes;
	Number valueNumber;

	public DictValue(final String value) {
		this.valueString = value;
		this.valueBytes = value.getBytes();
	}

	public DictValue(final byte[] bytes) {
		this.valueBytes = bytes;
		this.valueString = new String(bytes);
	}

	/**
	 * @param args
	 */
	public static void main(final String[] args) {

	}

}
