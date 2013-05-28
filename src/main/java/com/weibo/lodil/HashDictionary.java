/**
 * 
 */
package com.weibo.lodil;

import java.util.Collection;
import java.util.Map;

/**
 * key - map/hash dictionary interface
 * 
 * @author tangfulin
 * 
 */
public interface HashDictionary extends BasicDictionary {

	public DictValue hget(DictKey key, DictHashField field);

	public Map<DictHashField, DictValue> hgetAll(DictKey key);

	public Map<DictHashField, DictValue> hmget(DictKey key, Collection<DictHashField> fields);

	public Collection<DictKey> hkeys(DictKey key);

	public Collection<DictHashField> hvals(DictKey key);

	/**
	 * XXX redis will return 0 for add new field and value, 1 for update old
	 * field's value
	 * 
	 * @param key
	 * @param field
	 * @param value
	 * @return true for succ, false otherwise
	 */
	public boolean hset(DictKey key, DictHashField field, DictValue value);

	public boolean hmset(DictKey key, Map<DictHashField, DictValue> values);

	public boolean hexist(DictKey key, DictHashField field);

	/**
	 * 
	 * @param key
	 * @param field
	 * @return true if and only if all fields deleted suc, false otherwise XXX
	 *         if deleted parts fields and return false, it will *NOT* rollback!
	 */
	public boolean hdel(DictKey key, DictHashField... field);

	public long hlen(DictKey key);

	/**
	 * deal with numbers
	 * 
	 * @param key
	 * @return
	 */
	public long hincr(DictKey key, DictHashField field);

	public long hdecr(DictKey key, DictHashField field);

	public long hincrBy(DictKey key, DictHashField field, long num);

	public long hdecrBy(DictKey key, DictHashField field, long num);


}
