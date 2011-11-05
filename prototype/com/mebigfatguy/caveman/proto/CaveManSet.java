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
package com.mebigfatguy.caveman.proto;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

import com.mebigfatguy.caveman.proto.aux.CaveMan;

public class CaveManSet {

	private static final int DEFAULT_CAPACITY = 31;
	private static final float DEFAULT_LOAD_FACTOR = 0.80f;
	
	private CaveManBucket[] buckets;
	private int size;
	private float loadFactor;
	private int version;
	
	public CaveManSet() {
		this(DEFAULT_CAPACITY);
	}
	
	public CaveManSet(int initialCapacity) {
		this(initialCapacity, DEFAULT_LOAD_FACTOR);
	}
	
	public CaveManSet(int initialCapacity, float loadingFactor) {
		buckets = new CaveManBucket[initialCapacity];
		loadFactor = loadingFactor;
		size = 0;
		version = 0;
	}
	
	public int size() {
		return size;
	}
	
	public boolean isEmpty() {
		return size == 0;
	}
	
	public boolean contains(CaveMan item) {
		int hash = fromCaveMan(item) % buckets.length;
		CaveManBucket b = buckets[hash];
		
		if (b == null)
			return false;
		
		return b.contains(item);
	}
	
	public CaveManIterator iterator() {
		return new CaveManSetIterator(version);
	}
	
	public CaveMan[] toArray() {
		CaveMan[] array = new CaveMan[size];
		int index = 0;
		for (CaveManBucket b : buckets) {
			if (b != null) {
				for (int i = 0; i < b.bucketSize; i++) {
					array[index++] = b.list[i];
				}
			}
		}
		
		return array;
	}
	
	public boolean add(CaveMan item) {
		++version;
		int hash = fromCaveMan(item) % buckets.length;
		CaveManBucket b = buckets[hash];
		if (b == null) {
			b = new CaveManBucket();
			buckets[hash] = b;
		}
		
		boolean added = b.add(item);
		if (added) {
			++size;
		}
		return added;
	}
	
	public boolean remove(CaveMan item) {
		++version;
		int hash = fromCaveMan(item) % buckets.length;
		CaveManBucket b = buckets[hash];
		if (b == null) {
			return false;
		}
		
		boolean removed = b.remove(item);
		if (removed) {
			--size;
		}
		return removed;
	}
	
	public boolean containsAll(CaveManSet c) {
		return false;
	}
	
	public boolean addAll(CaveManSet c) {
		++version;
		return true;
	}
	
	public boolean retainAll(CaveManSet c) {
		++version;
		return false;
	}
	
	public boolean removeAll(CaveManSet c) {
		return false;
	}	
	
	public void clear() {
		++version;
		for (int i = 0; i < buckets.length; i++) {
			buckets[i] = null;
		}
	}
	
	private static class CaveManBucket {
		CaveMan[] list = new CaveMan[1];
		int bucketSize;
		
		public boolean add(CaveMan item) {
			if (contains(item)) {
				return false;
			}
			
			if (bucketSize >= list.length) {
				CaveMan[] newList = new CaveMan[list.length + 4];
				System.arraycopy(list,  0, newList, 0, bucketSize);
				list = newList;
			}
			
			list[bucketSize++] = item;
			return true;
		}
		
		public boolean contains(CaveMan item) {
			for (int i = 0; i < bucketSize; i++) {
				if (item == list[i])
					return true;
			}
			
			return false;
		}
		
		public boolean remove(CaveMan item) {
			for (int i = 0; i < bucketSize; i++) {
				if (item == list[i]) {
					--bucketSize;
					System.arraycopy(list, i + 1, list, i, bucketSize - i);
					return true;
				}
			}
			return false;
		}
	}
	
	private class CaveManSetIterator implements CaveManIterator {

		private int iteratorVersion;
		private int bucketIndex;
		private int bucketSubIndex;
		private int pos;
		
		CaveManSetIterator(int vers) {
			iteratorVersion = vers;
			
			if (size > 0) {
				for (int bucketIndex = 0; bucketIndex < buckets.length; bucketIndex++) {
					CaveManBucket b = buckets[bucketIndex];
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
		public CaveMan next() throws NoSuchElementException {
			if (iteratorVersion != version) {
				throw new ConcurrentModificationException((version - iteratorVersion) + " changes have been made since the iterator was created");
			}

			if (pos >= size) {
				throw new NoSuchElementException("Index " + pos + " is out of bounds [0, " + (size - 1) + "]");
			}

			CaveManBucket b = buckets[bucketIndex];
			CaveMan item = b.list[bucketSubIndex++];
			if (bucketSubIndex >= b.list.length) {
				bucketSubIndex = 0;
				for (;bucketIndex < buckets.length; bucketIndex++) {
					b = buckets[bucketIndex];
					if ((b != null) && (b.bucketSize > 0)) {
						break;
					}
				}
			}
			++pos;
			
			return item;
		}

		@Override
		public void remove() {
			if (iteratorVersion != version) {
				throw new ConcurrentModificationException((version - iteratorVersion) + " changes have been made since the iterator was created");
			}
			
			if (pos >= size) {
				throw new NoSuchElementException("Index " + pos + " is out of bounds [0, " + (size - 1) + "]");
			}
			
			CaveManBucket b = buckets[bucketIndex];
			System.arraycopy(b.list, bucketSubIndex + 1, b.list, bucketSubIndex, b.bucketSize - bucketSubIndex);
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
	
	
	private int fromCaveMan(CaveMan item) {return 0;}
}

