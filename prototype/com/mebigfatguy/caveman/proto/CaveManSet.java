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

import com.mebigfatguy.caveman.proto.aux.CaveMan;

public class CaveManSet {

	private static final int DEFAULT_CAPACITY = 31;
	private static final float DEFAULT_LOAD_FACTOR = 0.80f;
	
	private CaveManBucket[] buckets;
	private int size;
	private float loadFactor;
	
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
	}
	
	public int size() {
		return size;
	}
	
	public boolean isEmpty() {
		return size == 0;
	}
	
	public boolean contains(CaveMan item) {
		return false;
	}
	
	public CaveManIterator iterator() {
		return null;
	}
	
	public CaveMan[] toArray() {
		return null;
	}
	
	public boolean add(CaveMan item) {
		int hash = fromCaveMan(item) % buckets.length;
		CaveManBucket b = buckets[hash];
		if (b == null) {
			b = new CaveManBucket();
			buckets[hash] = b;
		}
		
		buckets[hash].add(item);
		return true;
	}
	
	public boolean remove(CaveMan item) {
		return false;
	}
	
	public boolean containsAll(CaveManSet c) {
		return false;
	}
	
	public boolean addAll(CaveManSet c) {
		return true;
	}
	
	public boolean retainAll(CaveManSet c) {
		return false;
	}
	
	public boolean removeAll(CaveManSet c) {
		return false;
	}	
	
	public void clear() {
		
	}
	
	private static class CaveManBucket {
		CaveMan[] list;
		int size;
		
		public void add(CaveMan item) {
			if (contains(item)) {
				return;
			}
			
			
			list[size++] = item;
		}
		
		public boolean contains(CaveMan item) {
			for (int i = 0; i < size; i++) {
				if (item == list[i])
					return true;
			}
			
			return false;
		}
		
		
	}
	
	
	private int fromCaveMan(CaveMan item) {return 0;}
}

