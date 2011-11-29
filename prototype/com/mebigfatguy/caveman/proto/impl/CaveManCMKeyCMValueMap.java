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

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

import com.mebigfatguy.caveman.proto.CMKeyCMValueMap;
import com.mebigfatguy.caveman.proto.CMKeyCMValueMapIterator;
import com.mebigfatguy.caveman.proto.aux.CMKey;
import com.mebigfatguy.caveman.proto.aux.CMKeyCollection;
import com.mebigfatguy.caveman.proto.aux.CMKeyIterator;
import com.mebigfatguy.caveman.proto.aux.CMKeySet;
import com.mebigfatguy.caveman.proto.aux.CMValue;
import com.mebigfatguy.caveman.proto.aux.CMValueBag;
import com.mebigfatguy.caveman.proto.aux.CMValueCollection;
import com.mebigfatguy.caveman.proto.aux.CMValueIterator;

public class CaveManCMKeyCMValueMap implements CMKeyCMValueMap {
	public static final CMValue DEFAULT_NOT_FOUND_VALUE = toCaveManValue(0);
	private static final int DEFAULT_CAPACITY = 31;
	private static final float DEFAULT_LOAD_FACTOR = 0.80f;

	private final CMValue notFound;
	private CMBucket[] buckets;
	private int size;
	private final float loadFactor;
	private int version;
	
	public CaveManCMKeyCMValueMap() {
		this(DEFAULT_NOT_FOUND_VALUE, DEFAULT_CAPACITY);
	}
	
	public CaveManCMKeyCMValueMap(CMValue notFoundValue, int initialCapacity) {
		this(notFoundValue, initialCapacity, DEFAULT_LOAD_FACTOR);
	}
	
	public CaveManCMKeyCMValueMap(CMValue notFoundValue, int initialCapacity, float loadingFactor) {
		notFound = notFoundValue;
		loadFactor = loadingFactor;
		size = 0;
		buckets = new CMBucket[initialCapacity];
	}
	
	@Override
	public int size() {
		return size;
	}
	
	@Override
	public boolean isEmpty() {
		return size == 0;
	}
	
	@Override
	public boolean containsKey(CMKey key) {
		int hash = Math.abs(fromCaveManKey(key) % buckets.length);
		CMBucket b = buckets[hash];
		
		if (b == null)
			return false;
		
		return b.indexOf(key) >= 0;
	}
	
	@Override
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
	
	@Override
	public CMValue get(CMKey key) {
		
		int hash = Math.abs(fromCaveManKey(key) % buckets.length);
		CMBucket b = buckets[hash];
		
		if (b != null) {
			return b.get(key, notFound);	
		}
		
		return notFound;
	}
	
	@Override
	public void put(CMKey key, CMValue value) {
		++version;
		
		ensureSize(size + 1);

		int hash = Math.abs(fromCaveManKey(key) % buckets.length);
		CMBucket b = buckets[hash];
		
		if (b == null) {
			b = new CMBucket();
			buckets[hash] = b;
		}
		
		if (b.add(key, value)) {
			++size;
		}
	}
	
	@Override
	public void remove(CMKey key) {
		++version;
		
		int hash = Math.abs(fromCaveManKey(key) % buckets.length);
		CMBucket b = buckets[hash];
		
		if (b != null) {
			if (b.remove(key)) {
				--size;
			}
		}
	}
	
	@Override
	public void putAll(CMKeyCMValueMap m) {
		++version;
		
		ensureSize(size + m.size());
		
		CMKeyCMValueMapIterator iterator = m.iterator();
		
		while (iterator.hasNext()) {
			iterator.next();
			put(iterator.key(), iterator.value());
		}
	}
	
	@Override
	public void clear() {
		++version;
		
		for (CMBucket b : buckets) {
			if (b != null) {
				b.clear();
			}
		}
		size = 0;
	}
	
	@Override
	public CMKeyCMValueMapIterator iterator() {
		return new CaveManCMKeyCMValueMapIterator(version);
	}
	
	@Override
	public CMKeySet keySet() {
		return new CaveManCMKeyCMValueKeySet();
	}
	
	@Override
	public CMValueBag values() {
		return new CaveManCMKeyCMValueValuesBag();
	}
	
	private void ensureSize(int newSize) {
		if ((newSize / (double) buckets.length) > loadFactor) {
			int newBucketSize = (int) (2.0 * newSize);
			CMBucket[] newBuckets = new CMBucket[newBucketSize];
			
			for (CMBucket oldBucket : buckets) {
				if (oldBucket != null) {
					int oldBucketSize = oldBucket.bucketSize;
					CMBucket reusableBucket = oldBucket;
					boolean reusedBucket = false;
					for (int oldBucketIndex = 0; oldBucketIndex < oldBucketSize; ++oldBucketIndex) {
						CMKey key = oldBucket.keys[oldBucketIndex];
						int hash = Math.abs(fromCaveManKey(key) % newBuckets.length);
						CMBucket newBucket = newBuckets[hash];
						if (newBucket == null) {
							if (reusableBucket != null) {
								newBuckets[hash] = reusableBucket;
								reusableBucket.bucketSize = 1;
								reusedBucket = true;
							} else {
								newBucket = new CMBucket();
								newBuckets[hash] = newBucket;
							}
						}
						reusableBucket = null;
						
						if (!reusedBucket) {
							newBucket.add(key, oldBucket.values[oldBucketIndex]);
						}
					}
				}
			}
			buckets = newBuckets;		
		}
	}
	
	private static class CMBucket {
		CMKey[] keys = new CMKey[2];
		CMValue[] values = new CMValue[2];
		int bucketSize;
		
		public boolean add(CMKey key, CMValue value) {
			int existingIndex = indexOf(key);
			if (existingIndex >= 0) {
				values[existingIndex] = value;
				return false;
			}
			
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
		
		public void clear() {
			bucketSize = 0;
		}
	}
	
	private class CaveManCMKeyCMValueMapIterator implements CMKeyCMValueMapIterator {

		private final int iteratorVersion;
		private int bucketIndex;
		private int bucketSubIndex;
		private int pos;
		private CMKey key;
		private CMValue value;
		
		public CaveManCMKeyCMValueMapIterator(int version) {
			iteratorVersion = version;
			
			pos = 0;
			if (size > 0) {
				for (bucketIndex = 0; bucketIndex < buckets.length; bucketIndex++) {
					CMBucket b = buckets[bucketIndex];
					if ((b != null) && (b.bucketSize > 0)) {
						bucketSubIndex = 0;
						break;
					}
				}
				//?? shouldn't get here
			}
		}
		
		@Override
		public boolean hasNext() {
			if (iteratorVersion != version) {
				throw new ConcurrentModificationException((version - iteratorVersion) + " changes have been made since the iterator was created");
			}

			return pos >= size;
		}

		@Override
		public void next() throws NoSuchElementException {
			if (iteratorVersion != version) {
				throw new ConcurrentModificationException((version - iteratorVersion) + " changes have been made since the iterator was created");
			}
			

			if (pos >= size) {
				throw new NoSuchElementException("Index " + pos + " is out of bounds [0, " + (size - 1) + "]");
			}

			CMBucket b = buckets[bucketIndex];
			key = b.keys[bucketSubIndex];
			value = b.values[bucketSubIndex++];
			
			if (bucketSubIndex >= b.keys.length) {
				bucketSubIndex = 0;
				for (;bucketIndex < buckets.length; bucketIndex++) {
					b = buckets[bucketIndex];
					if ((b != null) && (b.bucketSize > 0)) {
						break;
					}
				}
			}
			++pos;
		}

		@Override
		public CMKey key() {
			if (iteratorVersion != version) {
				throw new ConcurrentModificationException((version - iteratorVersion) + " changes have been made since the iterator was created");
			}

			return key;
		}

		@Override
		public CMValue value() {
			if (iteratorVersion != version) {
				throw new ConcurrentModificationException((version - iteratorVersion) + " changes have been made since the iterator was created");
			}

			return value;
		}

		@Override
		public void remove() {
			if (iteratorVersion != version) {
				throw new ConcurrentModificationException((version - iteratorVersion) + " changes have been made since the iterator was created");
			}
			
			if (pos >= size) {
				throw new NoSuchElementException("Index " + pos + " is out of bounds [0, " + (size - 1) + "]");
			}
			
			CMBucket b = buckets[bucketIndex];
			System.arraycopy(b.keys, bucketSubIndex + 1, b.keys, bucketSubIndex, b.bucketSize - bucketSubIndex);
			System.arraycopy(b.values, bucketSubIndex + 1, b.values, bucketSubIndex, b.bucketSize - bucketSubIndex);
			--b.bucketSize;
			if (bucketSubIndex >= b.bucketSize) {
				bucketSubIndex = 0;
				for (;bucketIndex < buckets.length; bucketIndex++) {
					b = buckets[bucketIndex];
					if ((b != null) && (b.bucketSize > 0)) {
						break;
					}
				}
			}
			--pos;
		}	
	}
	
	private class CaveManCMKeyCMValueKeySet implements CMKeySet {

		@Override
		public int size() {
			return CaveManCMKeyCMValueMap.this.size();
		}

		@Override
		public boolean isEmpty() {
			return CaveManCMKeyCMValueMap.this.isEmpty();
		}

		@Override
		public boolean contains(CMKey item) {
			return CaveManCMKeyCMValueMap.this.containsKey(item);
		}

		@Override
		public CMKeyIterator iterator() {
			return new CaveManCMKeyCMValueKeySetIterator();
		}

		@Override
		public CMKey[] toArray() {
			CMKey[] data = new CMKey[size];
			
			int pos = 0;
			
			for (CMBucket bucket : buckets) {
				if (bucket != null) {
					for (int i = 0; i < bucket.bucketSize; ++i) {
						data[pos++] = bucket.keys[i];
					}
				}
			}
			
			return data;
		}

		@Override
		public boolean add(CMKey item) {
			int originalSize = size;
			
			CaveManCMKeyCMValueMap.this.put(item, notFound);
			return originalSize != size;
		}

		@Override
		public boolean remove(CMKey item) {
			int originalSize = size;
			
			CaveManCMKeyCMValueMap.this.remove(item);
			return originalSize != size;
		}

		@Override
		public void clear() {
			CaveManCMKeyCMValueMap.this.clear();
		}

		@Override
		public boolean containsAll(CMKeyCollection c) {
			CMKeyIterator it = c.iterator();
			while (it.hasNext()) {
				if (!CaveManCMKeyCMValueMap.this.containsKey(it.next())) {
					return false;
				}
			}
			
			return true;
		}

		@Override
		public boolean addAll(CMKeyCollection c) {
			int originalSize = size;
			
			CMKeyIterator it = c.iterator();
			while (it.hasNext()) {
				CaveManCMKeyCMValueMap.this.put(it.next(), notFound);
			}	
			
			return originalSize != size;
		}

		@Override
		public boolean retainAll(CMKeyCollection c) {
			boolean modified = false;
			
			CMKeyCMValueMapIterator it = CaveManCMKeyCMValueMap.this.iterator();
			while (it.hasNext()) {
				it.next();
				CMKey key = it.key();
				if (!c.contains(key)) {
					it.remove();
					modified = true;
				}
			}
			
			return modified;
		}

		@Override
		public boolean removeAll(CMKeyCollection c) {
			int originalSize = size;
			
			CMKeyIterator it = c.iterator();
			while (it.hasNext()) {
				CaveManCMKeyCMValueMap.this.remove(it.next());
			}
			
			return originalSize != size;
		}
		
		private class CaveManCMKeyCMValueKeySetIterator implements CMKeyIterator {

			private final CMKeyCMValueMapIterator iterator = CaveManCMKeyCMValueMap.this.iterator();
			@Override
			public boolean hasNext() {
				return iterator.hasNext();
			}

			@Override
			public CMKey next() throws NoSuchElementException {
				iterator.next();
				return iterator.key();
			}

			@Override
			public void remove() {
				iterator.remove();
			}	
		}
	}
	
	private class CaveManCMKeyCMValueValuesBag implements CMValueBag {

		@Override
		public int size() {
			return CaveManCMKeyCMValueMap.this.size();
		}

		@Override
		public boolean isEmpty() {
			return CaveManCMKeyCMValueMap.this.isEmpty();
		}

		@Override
		public boolean contains(CMValue item) {
			return CaveManCMKeyCMValueMap.this.containsValue(item);
		}

		@Override
		public CMValueIterator iterator() {
			return new CaveManCMKeyCMValueValuesBagIterator();
		}

		@Override
		public CMValue[] toArray() {
			CMValue[] data = new CMValue[size];
			
			int pos = 0;
			
			for (CMBucket bucket : buckets) {
				if (bucket != null) {
					for (int i = 0; i < bucket.bucketSize; ++i) {
						data[pos++] = bucket.values[i];
					}
				}
			}
			
			return data;
		}

		@Override
		public boolean add(CMValue item) {
			throw new UnsupportedOperationException("add is not supported from the values bag of a CaveManCMKeyCMValueMap as there's no obvious key");
		}

		@Override
		public boolean remove(CMValue item) {
			int originalSize = size;
			
			for (CMBucket bucket : buckets) {
				if (bucket != null) {
					for (int i = 0; i < bucket.bucketSize; ++i) {
						if (item == bucket.values[i]) {
							--bucket.bucketSize;
							System.arraycopy(bucket.keys, i + 1, bucket.keys, i, bucket.bucketSize - i);
							System.arraycopy(bucket.values, i + 1, bucket.values, i, bucket.bucketSize - i);
							--size;
						}
					}
				}
			}
			
			return originalSize != size;
		}

		@Override
		public void clear() {
			CaveManCMKeyCMValueMap.this.clear();
		}

		@Override
		public boolean containsAll(CMValueCollection c) {
			CMValueIterator it = c.iterator();
			while (it.hasNext()) {
				if (!CaveManCMKeyCMValueMap.this.containsValue(it.next())) {
					return false;
				}
			}
			
			return true;
		}

		@Override
		public boolean addAll(CMValueCollection c) {
			throw new UnsupportedOperationException("addAll is not supported from the values bag of a CaveManCMKeyCMValueMap as there's no obvious keys");
		}

		@Override
		public boolean retainAll(CMValueCollection c) {
			
			boolean modified = false;
			CMKeyCMValueMapIterator it = CaveManCMKeyCMValueMap.this.iterator();
			
			while (it.hasNext()) {
				it.next();
				if (!c.contains(it.value())) {
					it.remove();
					modified = true;
				}
			}
			
			return modified;
		}

		@Override
		public boolean removeAll(CMValueCollection c) {
			int originalSize = size;
			
			CMValueIterator it = c.iterator();
			while (it.hasNext()) {
				remove(it.next());
			}
			
			return originalSize != size;
		}

		@Override
		public boolean removeOne(CMValue item) {
		
			for (CMBucket bucket : buckets) {
				if (bucket != null) {
					for (int i = 0; i < bucket.bucketSize; ++i) {
						if (item == bucket.values[i]) {
							--bucket.bucketSize;
							System.arraycopy(bucket.keys, i + 1, bucket.keys, i, bucket.bucketSize - i);
							System.arraycopy(bucket.values, i + 1, bucket.values, i, bucket.bucketSize - i);
							--size;
							return true;
						}
					}
				}
			}
			
			return false;
		}

		@Override
		public int countOf(CMValue item) {
			int count = 0;
			for (CMBucket bucket : buckets) {
				if (bucket != null) {
					for (int i = 0; i < bucket.bucketSize; ++i) {
						if (item == bucket.values[i]) {
							++count;
						}
					}
				}
			}
			
			return count;
		}
		
		private class CaveManCMKeyCMValueValuesBagIterator implements CMValueIterator {

			private final CMKeyCMValueMapIterator iterator = CaveManCMKeyCMValueMap.this.iterator();
			
			@Override
			public boolean hasNext() {
				return iterator.hasNext();
			}

			@Override
			public CMValue next() throws NoSuchElementException {
				iterator.next();
				return iterator.value();
			}

			@Override
			public void remove() {
				iterator.remove();
			}
		}
	}

	
	
	private static int fromCaveManKey(CMKey key) {return 0;}
	private static CMValue toCaveManValue(int i) {return null;}
}
