package com.weibo.lodil.mmap.wrap;

public interface DictEntry {

	// connect key and value
	// TODO escape this char when found in key or value
	public final String connector = "_";

	public String getKey();

	public void setKey(String key);

	public String getValue();

	public void setValue(String value);

}
