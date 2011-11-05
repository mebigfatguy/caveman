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

public class CaveManList {
	private static final int DEFAULT_SIZE = 20;
	
	private CaveMan[] list;
	private int size;
	
	public CaveManList() {
		this(DEFAULT_SIZE);
	}
	
	public CaveManList(int defaultSize) {
		list = new CaveMan[defaultSize];
		size = 0;
	}
	
	public int size() {
		return size;
	}
	
	public boolean isEmpty() {
		return size == 0;
	}
	
	public boolean contains(CaveMan item) {
		for (int i = 0; i < size; i++) {
			if (item == list[i])
				return true;
		}
		
		return false;
	}
	
	public CaveManIterator iterator() {
		return null;
	}
	
	public CaveMan[] toArray() {
		CaveMan[] array = new CaveMan[size];
		System.arraycopy(list,  0, array, 0, size);
		return array;
	}
	
	public boolean add(CaveMan item) {
		return false;
	}
	
	public CaveMan remove(int index) {

		CaveMan item = get(index);
		
		System.arraycopy(list, index + 1, list, index, size - index);
		--size;
		return item;
	}
	
	public boolean containsAll(CaveManList c) {
		return false;
	}
	
	public boolean addAll(CaveManList c) {
		return false;
	}
	
	public boolean removeAll(CaveManList c) {
		return false;
	}

	public boolean retainAll(CaveManList c) {
		return false;
	}
	
	public void clear() {
		size = 0;
	}
	
	public CaveMan get(int index) {
		if ((index < 0) || (index >= size)) {
			throw new IllegalArgumentException("Index: " + index + " is out of bounds [0, " + size + "]");
		}
		
		return list[index];
	}
	
	public CaveMan set(int index, CaveMan item) {
		if ((index < 0) || (index >= size)) {
			throw new IllegalArgumentException("Index: " + index + " is out of bounds [0, " + size + "]");
		}
		
		CaveMan oldItem = list[index];
		list[index] = item;
		return oldItem;
	}
	
	public void add(int index, CaveMan item) {
	}
	
	public int indexOf(CaveMan item) {
		for (int i = 0; i < size; i++) {
			if (item == list[i]) {
				return i;
			}
		}
		
		return -1;
	}
	
	public int lastIndexOf(CaveMan item) {
		for (int i = size - 1; i >= 0; i--) {
			if (item == list[i]) {
				return i;
			}
		}
		
		return -1;
	}	
}

