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

import com.mebigfatguy.caveman.proto.aux.CM;
import com.mebigfatguy.caveman.proto.aux.CMKey;
import com.mebigfatguy.caveman.proto.aux.CMKeySet;
import com.mebigfatguy.caveman.proto.aux.CMValue;
import com.mebigfatguy.caveman.proto.aux.CMValueBag;

public class CaveManCMKeyCMValueMap {
	private static final int DEFAULT_CAPACITY = 31;
	private static final float DEFAULT_LOAD_FACTOR = 0.80f;

	private CMBucket[] buckets;
	private int size;
	private float loadFactor;
	private int version;
	
	public CaveManCMKeyCMValueMap() {
		this(DEFAULT_CAPACITY);
	}
	
	public CaveManCMKeyCMValueMap(int initialCapacity) {
		this(initialCapacity, DEFAULT_LOAD_FACTOR);
	}
	
	public CaveManCMKeyCMValueMap(int initialCapacity, float loadingFactor) {
		loadFactor = loadingFactor;
		size = 0;
	}
	
	public int size() {
		return size;
	}
	
	public boolean isEmpty() {
		return size == 0;
	}
	
	public boolean containsKey(CMKey key) {
		int hash = fromCaveMan(key) % buckets.length;
		CMBucket b = buckets[hash];
		
		if (b == null)
			return false;
		
		return b.indexOf(key) >= 0;
	}
	
	public boolean containsValue(CMValue value) {
		for (CMBucket bucket : buckets) {
			if (bucket != null) {
				for (int i = 0; i < bucket.bucketSize; ++i) {
					if (bucket.values[i] == value) {
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	public CMValue get(CMKey key, CMValue notFoundValue) {
		
		int hash = fromCaveMan(key) % buckets.length;
		CMBucket b = buckets[hash];
		
		if (b != null) {
			CMValue value = b.get(key, notFoundValue);
			
		}
		
		return notFoundValue;
	}
	
	public void put(CMKey key, CMValue value) {
		++version;
		
		int hash = fromCaveMan(key) % buckets.length;
		CMBucket b = buckets[hash];
		
		b.add(key, value);
	}
	
	public void remove(CMKey key) {
	}
	
	public void putAll(CaveManCMKeyCMValueMap m) {
	}
	
	public void clear() {
	}
	
	public CMKeySet keySet() {
		return null;
	}
	
	public CMValueBag values() {
		return null;
	}
	
	private static class CMBucket {
		CMKey[] keys = new CMKey[1];
		CMValue[] values = new CMValue[1];
		int bucketSize;
		
		public boolean add(CMKey key, CMValue value) {
			int existingIndex = indexOf(key);
			if (existingIndex >= 0) {
				values[existingIndex] = value;
			} else {
				if (bucketSize >= keys.length) {
					CMKey[] newKeys = new CMKey[keys.length + 4];
					System.arraycopy(keys,  0, newKeys, 0, bucketSize);
					keys = newKeys;
					CMValue[] newValues = new CMValue[values.length + 4];
					System.arraycopy(values,  0, newValues, 0, bucketSize);
					values = newValues;					
				}
				
				keys[bucketSize] = key;
				values[bucketSize++] = value;
			}
			
			return true;
		}
		
		public boolean remove(CMKey key) {
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
		
		public int indexOf(CMKey key) {
			for (int i = 0; i < bucketSize; i++) {
				if (key == keys[i])
					return i;
			}
			
			return -1;
		}
		
		public CMValue get(CMKey key, CMValue notFoundValue) {
			for (int i = 0; i < bucketSize; i++) {
				if (key == keys[i])
					return values[i];
			}
			
			return notFoundValue;
		}
	}
	
	private int fromCaveMan(CMKey key) {return 0;}
}
