/**
 * 
 */
package com.weibo.lodil.mmap.wrap;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.weibo.lodil.DictKey;
import com.weibo.lodil.DictValue;
import com.weibo.lodil.KVDictionary;
import com.weibo.lodil.LOG;
import com.weibo.lodil.mmap.HugeMapBuilder;

/**
 * 
 * @author tangfulin
 * 
 */
public class MmapKVDictionary implements KVDictionary {

	public static final String TEMPORARY_SPACE = System.getProperty("java.io.tmpdir");

	public final int size;
	public final String baseDir;
	public final HugeMapBuilder<DictEntry, DictEntry> mapBuilder;
	public final DictHugeEntryMap dictMap;

	// this is just for test
	public MmapKVDictionary() {
		this(64 * 1024, TEMPORARY_SPACE);
	}

	public MmapKVDictionary(final int size, final String baseDir) {
		this.size = size;
		this.baseDir = baseDir;
		mapBuilder = new HugeMapBuilder<DictEntry, DictEntry>() {
			{
				allocationSize = size;
				baseDirectory = baseDir;
				setRemoveReturnsNull = true;
			}
		};
		dictMap = new DictHugeEntryMap(mapBuilder);
	}

	public long size() {
		return dictMap.size();
	}

	public boolean contains(final DictKey key) {
		return dictMap.containsKey(key);
	}

	public DictValue get(final DictKey key) {
		return dictMap.get(key);
	}

	// TODO optimize
	public Map<DictKey, DictValue> mget(final Collection<DictKey> keys) {
		final Map<DictKey, DictValue> result = new HashMap<DictKey, DictValue>();
		for (final DictKey key : keys) {
			final DictValue value = dictMap.get(key);
			if (value != null) {
				result.put(key, value);
			}
		}
		return result;
	}

	public DictValue set(final DictKey key, final DictValue value) {
		return dictMap.put(key, value);
	}

	public boolean mset(final Map<DictKey, DictValue> keyvalues) {
		dictMap.mset(keyvalues);
		return true;
	}

	// XXX this is not thread safe!
	public boolean add(final DictKey key, final DictValue value) {
		if (dictMap.get(key) != null) {
			return false;
		}
		dictMap.put(key, value);
		return true;
	}

	public boolean delete(final DictKey key) {
		dictMap.remove(key);
		return true;
	}

	// TODO
	public long incr(final DictKey key) {
		return 0;
	}

	// TODO
	public long decr(final DictKey key) {
		return 0;
	}

	// TODO
	public long incrBy(final DictKey key, final long num) {
		return 0;
	}

	// TODO
	public long decrBy(final DictKey key, final long num) {
		return 0;
	}

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		LOG.info("File at: " + TEMPORARY_SPACE);
		final MmapKVDictionary md = new MmapKVDictionary();
		final int size = 102;

		for (int i = 100; i < size; ++i) {
			final long mapsize = md.size();
			md.set(new DictKey("key:" + i), new DictValue("value:" + i));
			if (md.size() != (mapsize + 1)){
				LOG.warn("map size error:" + md.size() + " expect:" + (mapsize + 1));
			}
		}
		for (int i = 100; i < size; ++i) {
			final DictKey key = new DictKey("key:" + i);
			final DictValue value = md.get(key);
			if (value == null){
				//value = md.get(key);
			}
			if ((value == null) || !value.equals(new DictValue("value:" + i))) {
				System.out.println("BANG!");
			}
		}
		System.out.println("BINGGO!");
	}

}
