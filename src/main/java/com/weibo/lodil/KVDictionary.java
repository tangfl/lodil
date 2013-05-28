/**
 * 
 */
package com.weibo.lodil;

import java.util.Collection;
import java.util.Map;

/**
 * key - value dictionary interface
 * 
 * @author tangfulin
 * 
 */
public interface KVDictionary extends BasicDictionary {

	public DictValue get(DictKey key);

	public Map<DictKey, DictValue> mget(Collection<DictKey> keys);

	/**
	 * return old value, or null if not seted yet
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public DictValue set(DictKey key, DictValue value);

	/**
	 * XXX this MAYBE *NOT* thread safe! XXX if parts of keys are seted and
	 * something go wrong, it will *NOT* rollback!
	 * 
	 * @return true if and only if all keys are seted, false otherwise
	 */
	public boolean mset(Map<DictKey, DictValue> keys);

	/**
	 * 
	 * @param key
	 * @return false and do nothing if key already seted, otherwise true
	 */
	public boolean add(DictKey key);

	/**
	 * 
	 * @param key
	 * @return true if and only if modify num==1, otherwise false
	 */
	public boolean delete(DictKey key);

	/**
	 * deal with numbers
	 * 
	 * @param key
	 * @return
	 */
	public long incr(DictKey key);

	public long decr(DictKey key);

	public long incrBy(DictKey key, long num);

	public long decrBy(DictKey key, long num);

}
