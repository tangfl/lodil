/**
 * 
 */
package com.weibo.lodil;


/**
 * key - list dictionary interface
 * 
 * @author tangfulin
 * 
 */
public interface ListDictionary extends BasicDictionary {

	public long llen(DictKey key);

	/**
	 * 
	 * @param key
	 * @param value
	 * @return the list size after push
	 */
	public long lpush(DictKey key, DictValue value);

	public long rpush(DictKey key, DictValue value);

	/**
	 * only push if the list exist
	 * 
	 * @param key
	 * @param value
	 * @return 0 if the list of key not exist (and do nothing)
	 */
	public long lpushx(DictKey key, DictValue value);

	public long rpushx(DictKey key, DictValue value);

	/**
	 * remove and return
	 * 
	 * @param key
	 * @return null if list not exist, or empty
	 */
	public DictValue lpop(DictKey key);

	public DictValue rpop(DictKey key);

}
