/*
 * caveman - A primitive collection library
 * Copyright 2011 MeBigFatGuy.com
 * Copyright 2011 Dave Brosius
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations
 * under the License.
 */
package com.mebigfatguy.caveman.proto.impl;

import com.mebigfatguy.caveman.proto.CMMapIterator;
import com.mebigfatguy.caveman.proto.aux.CM;


public class CaveManCMMap<V> {
	private static final int DEFAULT_CAPACITY = 31;
	private static final float DEFAULT_LOAD_FACTOR = 0.80f;

	private CMBucket<V>[] buckets;
	private int size;
	private float loadFactor;
	private int version;
	
	public CaveManCMMap() {
		this(DEFAULT_CAPACITY);
	}
	
	public CaveManCMMap(int initialCapacity) {
		this(initialCapacity, DEFAULT_LOAD_FACTOR);
	}
	
	public CaveManCMMap(int initialCapacity, float loadingFactor) {
		loadFactor = loadingFactor;
		size = 0;
		buckets = (CMBucket<V>[])new CMBucket[initialCapacity];
	}
	
	public int size() {
		return size;
	}
	
	public boolean isEmpty() {
		return size == 0;
	}
	
	public boolean containsKey(CM key) {
		int hash = fromCaveMan(key) % buckets.length;
		CMBucket<V> b = buckets[hash];
		
		if (b == null)
			return false;
		
		return b.indexOf(key) >= 0;
	}
	
	public boolean containsValue(V value) {
		for (CMBucket<V> bucket : buckets) {
			if (bucket != null) {
				for (int i = 0; i < bucket.bucketSize; ++i) {
					if (value == null) {
						if (bucket.values[i] == null)
							return true;
					} else if (value.equals(bucket.values[i])) {
						return true;
					}
				}
			}
		}
		
		return false;	
	}
	
	public V get(CM key) {
		
		int hash = fromCaveMan(key) % buckets.length;
		CMBucket<V> b = buckets[hash];
		V value = null;
		if (b != null) {
			value = (V)b.get(key);	
		}
		
		return value;
	}
	
	public void put(CM key, V value) {
		++version;
		
		ensureSize(size + 1);

		int hash = fromCaveMan(key) % buckets.length;
		CMBucket<V> b = buckets[hash];
		
		if (b == null) {
			b = new CMBucket<V>();
			buckets[hash] = b;
		}
		
		b.add(key, value);
	}
	
	public void remove(CM key) {
		++version;
		
		int hash = fromCaveMan(key) % buckets.length;
		CMBucket<V> b = buckets[hash];
		
		if (b != null) {
			b.remove(key);
		}
	}
	
	public void putAll(CaveManCMMap m) {
		++version;
		
		ensureSize(size + m.size());
		
		CMMapIterator iterator = m.iterator();
		
		while (iterator.hasNext()) {
			iterator.next();
			put(iterator.key(), (V)iterator.value());
		}
	}
	
	public void clear() {
		++version;
		
		for (CMBucket<V> b : buckets) {
			if (b != null) {
				b.clear();
			}
		}
	}
	
	public CMMapIterator iterator() {
		return null;
	}
	
	public CaveManCMSet keySet() {
		return null;
	}	
	
	private void ensureSize(int newSize) {
		if ((newSize / (double) buckets.length) > loadFactor) {
			int newBucketSize = (int) ((2.0 * loadFactor) * newSize);
			CMBucket<V>[] newBuckets = (CMBucket<V>[])new CMBucket[newBucketSize];
			
			for (CMBucket<V> oldBucket : buckets) {
				if (oldBucket != null) {
					for (int oldBucketIndex = 0; oldBucketIndex < oldBucket.bucketSize; ++oldBucketIndex) {
						CM key = oldBucket.keys[oldBucketIndex];
						int hash = fromCaveMan(key) % newBuckets.length;
						CMBucket<V> newBucket = newBuckets[hash];
						if (newBucket == null) {
							newBucket = new CMBucket<V>();
							newBuckets[hash] = newBucket;
						}
						newBucket.add(key, oldBucket.values[oldBucketIndex]);
					}
				}
			}
			buckets = newBuckets;		
		}
	}
	
	private static class CMBucket<V> {
		CM[] keys = new CM[1];
		V[] values = (V[])new Object[1];
		int bucketSize;
		
		public boolean add(CM key, V value) {
			int existingIndex = indexOf(key);
			if (existingIndex >= 0) {
				values[existingIndex] = value;
			} else {
				if (bucketSize >= keys.length) {
					CM[] newKeys = new CM[keys.length + 4];
					System.arraycopy(keys,  0, newKeys, 0, bucketSize);
					keys = newKeys;
					V[] newValues = (V[])new Object[values.length + 4];
					System.arraycopy(values,  0, newValues, 0, bucketSize);
					values = newValues;					
				}
				
				keys[bucketSize] = key;
				values[bucketSize++] = value;
			}
			
			return true;
		}
		
		public boolean remove(CM key) {
			for (int i = 0; i < bucketSize; i++) {
				if (key == keys[i]) {
					--bucketSize;
					System.arraycopy(keys, i + 1, keys, i, bucketSize - i);
					System.arraycopy(values, i + 1, values, i, bucketSize - i);
					return true;
				}
			}
			return false;
		}
		
		public int indexOf(CM key) {
			for (int i = 0; i < bucketSize; i++) {
				if (key == keys[i])
					return i;
			}
			
			return -1;
		}
		
		public V get(CM key) {
			for (int i = 0; i < bucketSize; i++) {
				if (key == keys[i])
					return values[i];
			}
			
			return null;
		}
		
		public void clear() {
			bucketSize = 0;
		}
	}
	
	
	private int fromCaveMan(CM key) {return 0;}
}