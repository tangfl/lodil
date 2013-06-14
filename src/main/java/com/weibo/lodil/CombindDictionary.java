/**
 * 
 */
package com.weibo.lodil;

import java.util.Collection;
import java.util.Map;

/**
 * combind cache and storage impl, deal with write through or write back stuff
 * 
 * @author tangfulin
 * 
 */
public class CombindDictionary implements Dictionary {

	public DictValue get(final DictKey key) {
		return null;
	}

	public Map<DictKey, DictValue> mget(final Collection<DictKey> keys) {
		return null;
	}

	public DictValue set(final DictKey key, final DictValue value) {
		return null;
	}

	public boolean mset(final Map<DictKey, DictValue> keys) {
		return false;
	}

	public boolean add(final DictKey key, final DictValue value) {
		return false;
	}

	public boolean delete(final DictKey key) {
		return false;
	}

	public long incr(final DictKey key) {
		return 0;
	}

	public long decr(final DictKey key) {
		return 0;
	}

	public long incrBy(final DictKey key, final long num) {
		return 0;
	}

	public long decrBy(final DictKey key, final long num) {
		return 0;
	}

	public long size() {
		return 0;
	}

	public boolean contains(final DictKey key) {
		return false;
	}

	public long llen(final DictKey key) {
		return 0;
	}

	public long lpush(final DictKey key, final DictValue value) {
		return 0;
	}

	public long rpush(final DictKey key, final DictValue value) {
		return 0;
	}

	public long lpushx(final DictKey key, final DictValue value) {
		return 0;
	}

	public long rpushx(final DictKey key, final DictValue value) {
		return 0;
	}

	public DictValue lpop(final DictKey key) {
		return null;
	}

	public DictValue rpop(final DictKey key) {
		return null;
	}

	public DictValue hget(final DictKey key, final DictHashField field) {
		return null;
	}

	public Map<DictHashField, DictValue> hgetAll(final DictKey key) {
		return null;
	}

	public Map<DictHashField, DictValue> hmget(final DictKey key, final Collection<DictHashField> fields) {
		return null;
	}

	public Collection<DictKey> hkeys(final DictKey key) {
		return null;
	}

	public Collection<DictHashField> hvals(final DictKey key) {
		return null;
	}

	public boolean hset(final DictKey key, final DictHashField field, final DictValue value) {
		return false;
	}

	public boolean hmset(final DictKey key, final Map<DictHashField, DictValue> values) {
		return false;
	}

	public boolean hexist(final DictKey key, final DictHashField field) {
		return false;
	}

	public boolean hdel(final DictKey key, final DictHashField... field) {
		return false;
	}

	public long hlen(final DictKey key) {
		return 0;
	}

	public long hincr(final DictKey key, final DictHashField field) {
		return 0;
	}

	public long hdecr(final DictKey key, final DictHashField field) {
		return 0;
	}

	public long hincrBy(final DictKey key, final DictHashField field, final long num) {
		return 0;
	}

	public long hdecrBy(final DictKey key, final DictHashField field, final long num) {
		return 0;
	}

	/**
	 * @param args
	 */
	public static void main(final String[] args) {

	}

}
