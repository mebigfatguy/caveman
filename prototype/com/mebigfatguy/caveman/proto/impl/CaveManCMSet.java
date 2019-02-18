/*
 * caveman - A primitive collection library
 * Copyright 2011-2019 MeBigFatGuy.com
 * Copyright 2011-2019 Dave Brosius
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

import java.io.Serializable;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

import com.mebigfatguy.caveman.proto.CMCollection;
import com.mebigfatguy.caveman.proto.CMIterator;
import com.mebigfatguy.caveman.proto.CMSet;
import com.mebigfatguy.caveman.proto.aux.CM;

public class CaveManCMSet implements CMSet, Serializable {
    private static final long serialVersionUID = 2206411569130634386L;
    private static final int DEFAULT_CAPACITY = 31;
	private static final float DEFAULT_LOAD_FACTOR = 0.80f;

	private CMBucket[] buckets;
	private int size;
	private final float loadFactor;
	private int version;

	public CaveManCMSet() {
		this(DEFAULT_CAPACITY);
	}

	public CaveManCMSet(int initialCapacity) {
		this(initialCapacity, DEFAULT_LOAD_FACTOR);
	}

	public CaveManCMSet(int initialCapacity, float loadingFactor) {
		buckets = new CMBucket[initialCapacity];
		loadFactor = loadingFactor;
		size = 0;
		version = 0;
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
	public boolean contains(CM item) {
		int hash = Math.abs(fromCaveMan(item) % buckets.length);
		CMBucket b = buckets[hash];

		if (b == null)
			return false;

		return b.contains(item);
	}

	@Override
	public CMIterator iterator() {
		return new CaveManCMSetIterator(version);
	}

	@Override
	public CM[] toArray() {
		CM[] array = new CM[size];
		int index = 0;
		for (CMBucket b : buckets) {
			if (b != null) {
				for (int i = 0; i < b.bucketSize; i++) {
					array[index++] = b.list[i];
				}
			}
		}

		return array;
	}

	@Override
	public boolean add(CM item) {
		++version;

		ensureSize(size+1);

		int hash = Math.abs(fromCaveMan(item) % buckets.length);
		CMBucket b = buckets[hash];
		if (b == null) {
			b = new CMBucket();
			buckets[hash] = b;
		}

		boolean added = b.add(item);
		if (added) {
			++size;
		}
		return added;
	}

	@Override
	public boolean remove(CM item) {
		++version;
		int hash = Math.abs(fromCaveMan(item) % buckets.length);
		CMBucket b = buckets[hash];
		if (b == null) {
			return false;
		}

		boolean removed = b.remove(item);
		if (removed) {
			--size;
		}
		return removed;
	}

	@Override
	public boolean containsAll(CMCollection c) {
		CMIterator it = c.iterator();
		while (it.hasNext()) {
			if (!contains(it.next())) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean addAll(CMCollection c) {
		++version;

		ensureSize(size + c.size());

		int startSize = size;
		CMIterator it = c.iterator();
		while (it.hasNext()) {
			add(it.next());
		}
		return startSize != size;
	}

	@Override
	public boolean retainAll(CMCollection c) {
		++version;
		int startSize = size;
		CMIterator it = iterator();
		while (it.hasNext()) {
			CM item = it.next();
			if (!c.contains(item)) {
				it.remove();
			}
		}
		return startSize != size;
	}

	@Override
	public boolean removeAll(CMCollection c) {
		++version;
		int startSize = size;
		CMIterator it = c.iterator();
		while (it.hasNext()) {
			remove(it.next());
		}
		return startSize != size;
	}

	@Override
	public void clear() {
		++version;
		for (int i = 0; i < buckets.length; i++) {
			buckets[i] = null;
		}
		size = 0;
	}

	@Override
	public CM getOne() {
	    if (size > 0) {
	        for (CMBucket bucket : buckets) {
	            if ((bucket != null) && (bucket.bucketSize > 0)) {
	                return bucket.list[0];
	            }
	        }
	    }

	    throw new IllegalStateException("set has no items");
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
						CM item = oldBucket.list[oldBucketIndex];
						int hash = Math.abs(fromCaveMan(item) % newBuckets.length);
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
							newBucket.add(item);
						}
					}
				}
			}
			buckets = newBuckets;
		}
	}

	private static class CMBucket implements Serializable {
        private static final long serialVersionUID = 7608069521020561888L;

        CM[] list = new CM[1];
		int bucketSize;

		public boolean add(CM item) {
			if (contains(item)) {
				return false;
			}

			if (bucketSize >= list.length) {
				CM[] newList = new CM[list.length + 4];
				System.arraycopy(list,  0, newList, 0, bucketSize);
				list = newList;
			}

			list[bucketSize++] = item;
			return true;
		}

		public boolean contains(CM item) {
			for (int i = 0; i < bucketSize; i++) {
				if (item == list[i])
					return true;
			}

			return false;
		}

		public boolean remove(CM item) {
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

	private class CaveManCMSetIterator implements CMIterator {

		private final int iteratorVersion;
		private int bucketIndex;
		private int bucketSubIndex;
		private int pos;

		CaveManCMSetIterator(int vers) {
			iteratorVersion = vers;

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
		public CM next() throws NoSuchElementException {
			if (iteratorVersion != version) {
				throw new ConcurrentModificationException((version - iteratorVersion) + " changes have been made since the iterator was created");
			}

			if (pos >= size) {
				throw new NoSuchElementException("Index " + pos + " is out of bounds [0, " + (size - 1) + "]");
			}

			CMBucket b = buckets[bucketIndex];
			CM item = b.list[bucketSubIndex++];
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

			CMBucket b = buckets[bucketIndex];
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


	private int fromCaveMan(CM item) {return 0;}
}

