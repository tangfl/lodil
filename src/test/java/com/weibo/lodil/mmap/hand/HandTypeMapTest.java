package com.weibo.lodil.mmap.hand;

/*
 * Copyright 2011 Peter Lawrey
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

import static junit.framework.Assert.assertEquals;

import java.util.Arrays;
import java.util.Map;

import org.junit.Test;

import com.weibo.lodil.mmap.HugeMapBuilder;

public class HandTypeMapTest {

	public static final String baseDir = System.getProperty("java.io.tmpdir");

	@Test
	public void putGetSize() {
		final HugeMapBuilder<HandTypesKey, HandTypes> dummy = new HugeMapBuilder<HandTypesKey, HandTypes>() {
			{
				baseDirectory = System.getProperty("java.io.tmpdir");
				allocationSize = 64 * 1024;
				baseDirectory = baseDir;
				setRemoveReturnsNull = true;
			}
		};

		final HandTypesMap map = new HandTypesMap(dummy);
		final HandTypesKeyImpl key = new HandTypesKeyImpl();
		final HandTypesValueImpl value = new HandTypesValueImpl();
		final long start = System.nanoTime();
		final int size = 110;
		for (int i = 100; i < size; i += 1) {
			put(map, key, value, i, false);
			put(map, key, value, i, true);
			// put2(map, key, value, i, false);
		}
		for (int i = 100; i < size; i += 1) {
			get(map, key, i, false);
			// get(map, key, i, true);
		}
		for (final Map.Entry<HandTypesKey, HandTypes> entry : map.entrySet()) {
			assertEquals(entry.getKey().getInt(), entry.getValue().getInt());
			assertEquals(entry.getKey().getBoolean(), entry.getValue().getBoolean());
		}
		final long time = System.nanoTime() - start;
		System.out.printf("Took an average of %,d ns to write/read", time / size);
		System.out.println(Arrays.toString(map.sizes()));
		System.out.println(Arrays.toString(map.capacities()));
	}

	static void put(final HandTypesMap map, final HandTypesKeyImpl key, final HandTypesValueImpl value, final int i,
			final boolean flag) {
		final int k = i;
		key.setBoolean(flag);
		key.setInt(k);
		value.setBoolean(flag);
		value.setInt(k);
		final int size = map.size();
		map.put(key, value);
		// if ((size + 1) != map.size()) {
		// map.put(key, value);
		// assertEquals(size + 1, map.size());
		// }
		HandTypes ht = map.get(key);
		if (ht == null) {
			ht = map.get(key);
		}
		assertEquals(i, ht.getInt());
		// if (flag != ht.getBoolean()) {
		// assertEquals(flag, ht.getBoolean());
		// }
	}

	// key value not equal
	static void put2(final HandTypesMap map, final HandTypesKeyImpl key, final HandTypesValueImpl value, final int i,
			final boolean flag) {
		final int k = i;
		key.setBoolean(flag);
		key.setInt(k);
		value.setBoolean(!flag);
		value.setInt(k * 2);
		final int size = map.size();
		map.put(key, value);
		// if ((size + 1) != map.size()) {
		// map.put(key, value);
		// assertEquals(size + 1, map.size());
		// }
		HandTypes ht = map.get(key);
		if (ht == null) {
			ht = map.get(key);
		}
		assertEquals(i * 2, ht.getInt());
		// if (flag != ht.getBoolean()) {
		// assertEquals(flag, ht.getBoolean());
		// }
	}

	static void get(final HandTypesMap map, final HandTypesKeyImpl key, final int i, final boolean flag) {
		// final int k = i;
		key.setBoolean(flag);
		key.setInt(i);
		final HandTypes ht = map.get(key);
		assertEquals(i, ht.getInt());
		if (flag != ht.getBoolean()) {
			assertEquals(!flag, ht.getBoolean());
		}
	}
}
