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

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.mebigfatguy.caveman.proto.CMCollection;
import com.mebigfatguy.caveman.proto.CMIterator;
import com.mebigfatguy.caveman.proto.CMKeyMap;
import com.mebigfatguy.caveman.proto.CMKeyMapIterator;
import com.mebigfatguy.caveman.proto.CMSet;
import com.mebigfatguy.caveman.proto.aux.CM;


public class CaveManCMKeyMap<V> implements CMKeyMap<V> {
	private static final int DEFAULT_CAPACITY = 31;
	private static final float DEFAULT_LOAD_FACTOR = 0.80f;

	private CMBucket<V>[] buckets;
	private int size;
	private float loadFactor;
	private int version;
	
	public CaveManCMKeyMap() {
		this(DEFAULT_CAPACITY);
	}
	
	public CaveManCMKeyMap(int initialCapacity) {
		this(initialCapacity, DEFAULT_LOAD_FACTOR);
	}
	
	@SuppressWarnings("unchecked")
	public CaveManCMKeyMap(int initialCapacity, float loadingFactor) {
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
	public boolean containsKey(CM key) {
		int hash = fromCaveMan(key) % buckets.length;
		CMBucket<V> b = buckets[hash];
		
		if (b == null)
			return false;
		
		return b.indexOf(key) >= 0;
	}
	
	@Override
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
	
	@Override
	public V get(CM key) {
		
		int hash = fromCaveMan(key) % buckets.length;
		CMBucket<V> b = buckets[hash];
		V value = null;
		if (b != null) {
			value = b.get(key);	
		}
		
		return value;
	}
	
	@Override
	public void put(CM key, V value) {
		++version;
		
		ensureSize(size + 1);

		int hash = fromCaveMan(key) % buckets.length;
		CMBucket<V> b = buckets[hash];
		
		if (b == null) {
			b = new CMBucket<V>();
			buckets[hash] = b;
		}
		
		if (b.add(key, value)) {
			++size;
		}
	}
	
	@Override
	public void remove(CM key) {
		++version;
		
		int hash = fromCaveMan(key) % buckets.length;
		CMBucket<V> b = buckets[hash];
		
		if (b != null) {
			if (b.remove(key)) {
				--size;
			}
		}
	}
	
	@Override
	public void putAll(CMKeyMap<V> m) {
		++version;
		
		ensureSize(size + m.size());
		
		CMKeyMapIterator<V> iterator = m.iterator();
		
		while (iterator.hasNext()) {
			iterator.next();
			put(iterator.key(), iterator.value());
		}
	}
	
	@Override
	public void clear() {
		++version;
		
		for (CMBucket<V> b : buckets) {
			if (b != null) {
				b.clear();
			}
		}
		size = 0;
	}
	
	@Override
	public CMKeyMapIterator<V> iterator() {
		return new CaveManCMKeyMapIterator(version);
	}
	
	@Override
	public CMSet keySet() {
		return new CaveManCMKeySet();
	}	
	
	@Override
	public Collection<V> values() {
		return new CavemanCollection();
	}
	
	@SuppressWarnings("unchecked")
	private void ensureSize(int newSize) {
		if ((newSize / (double) buckets.length) > loadFactor) {
			int newBucketSize = (int) ((2.0 * loadFactor) * newSize);
			CMBucket<V>[] newBuckets = new CMBucket[newBucketSize];
			
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

	@SuppressWarnings("unchecked")
	private static class CMBucket<V> {
		CM[] keys = new CM[1];
		V[] values = (V[])new Object[1];
		int bucketSize;
		
		public boolean add(CM key, V value) {
			int existingIndex = indexOf(key);
			if (existingIndex >= 0) {
				values[existingIndex] = value;
				return false;
			}
			
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
	
	private class CaveManCMKeyMapIterator implements CMKeyMapIterator<V> {

		private final int iteratorVersion;
		private int bucketIndex;
		private int bucketSubIndex;
		private int pos;
		private CM key;
		private V value;
		
		public CaveManCMKeyMapIterator(int version) {
			iteratorVersion = version;
			
			pos = 0;
			if (size > 0) {
				for (bucketIndex = 0; bucketIndex < buckets.length; bucketIndex++) {
					CMBucket<V> b = buckets[bucketIndex];
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

			CMBucket<V> b = buckets[bucketIndex];
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
		public CM key() {
			if (iteratorVersion != version) {
				throw new ConcurrentModificationException((version - iteratorVersion) + " changes have been made since the iterator was created");
			}

			return key;
		}

		@Override
		public V value() {
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
			
			CMBucket<V> b = buckets[bucketIndex];
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
	
	private class CaveManCMKeySet implements CMSet {

		@Override
		public int size() {
			return CaveManCMKeyMap.this.size();
		}

		@Override
		public boolean isEmpty() {
			return CaveManCMKeyMap.this.isEmpty();
		}

		@Override
		public boolean contains(CM item) {
			return CaveManCMKeyMap.this.containsKey(item);
		}

		@Override
		public CMIterator iterator() {
			return new CaveManCMKeySetIterator();
		}

		@Override
		public CM[] toArray() {
			CM[] data = new CM[size];
			
			int pos = 0;
			for (CMBucket<V> bucket : buckets) {
				if (bucket != null) {
					for (int bucketIndex = 0; bucketIndex < bucket.bucketSize; ++bucketIndex) {
						data[pos++] = bucket.keys[bucketIndex];
					}
				}
			}
			
			return data;
		}

		@Override
		public boolean add(CM item) {
			int originalSize = CaveManCMKeyMap.this.size();
			CaveManCMKeyMap.this.put(item,  null);			
			return originalSize != CaveManCMKeyMap.this.size();
		}

		@Override
		public boolean remove(CM item) {
			int originalSize = CaveManCMKeyMap.this.size();
			CaveManCMKeyMap.this.remove(item);			
			return originalSize != CaveManCMKeyMap.this.size();
		}

		@Override
		public void clear() {
			CaveManCMKeyMap.this.clear();
		}

		@Override
		public boolean containsAll(CMCollection c) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean addAll(CMCollection c) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean retainAll(CMCollection c) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean removeAll(CMCollection c) {
			throw new UnsupportedOperationException();
		}
		
		private class CaveManCMKeySetIterator implements CMIterator {

			CMKeyMapIterator<V> iterator = CaveManCMKeyMap.this.iterator();
			
			@Override
			public boolean hasNext() {
				return iterator.hasNext();
			}

			@Override
			public CM next() throws NoSuchElementException {
				iterator.next();
				return iterator.key();
			}

			@Override
			public void remove() {
				iterator.remove();
			}
		}
	}
	
	private class CavemanCollection implements Collection<V> {

		@Override
		public int size() {
			return CaveManCMKeyMap.this.size();
		}

		@Override
		public boolean isEmpty() {
			return CaveManCMKeyMap.this.isEmpty();
		}

		@Override
		@SuppressWarnings("unchecked")
		public boolean contains(Object o) {
			return CaveManCMKeyMap.this.containsValue((V) o);
		}

		@Override
		public Iterator<V> iterator() {
			return new CaveManCMKeyMapValuesIterator();
		}

		@Override
		public Object[] toArray() {
			throw new UnsupportedOperationException();
		}

		@Override
		public <T> T[] toArray(T[] a) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean add(V e) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean remove(Object o) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean containsAll(Collection<?> c) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean addAll(Collection<? extends V> c) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean removeAll(Collection<?> c) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean retainAll(Collection<?> c) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void clear() {
			CaveManCMKeyMap.this.clear();
		}
		
		private class CaveManCMKeyMapValuesIterator implements Iterator<V> {

			private final CMKeyMapIterator<V> iterator = CaveManCMKeyMap.this.iterator();
			
			@Override
			public boolean hasNext() {
				return iterator.hasNext();
			}

			@Override
			public V next() {
				iterator.next();
				return iterator.value();
			}

			@Override
			public void remove() {
				iterator.remove();
			}
		}
	}

	
	
	private int fromCaveMan(CM key) {return 0;}
}