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

public class CaveManList {
	private static final int DEFAULT_SIZE = 20;
	
	private CaveMan[] list;
	private int size;
	private int version;
	
	public CaveManList() {
		this(DEFAULT_SIZE);
	}
	
	public CaveManList(int defaultSize) {
		list = new CaveMan[defaultSize];
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
		for (int i = 0; i < size; i++) {
			if (item == list[i])
				return true;
		}
		
		return false;
	}
	
	public CaveManIterator iterator() {
		return new CaveManListIterator(version);
	}
	
	public CaveMan[] toArray() {
		CaveMan[] array = new CaveMan[size];
		System.arraycopy(list,  0, array, 0, size);
		return array;
	}
	
	public boolean add(CaveMan item) {
		++version;
		if (size >= list.length) {
			grow();		
		}
		
		list[size++] = item;
		return true;
	}
	
	public CaveMan removeAt(int index) {
		++version;
		CaveMan item = get(index);
		
		--size;		
		System.arraycopy(list, index + 1, list, index, size - index);
		return item;
	}
	
	public boolean remove(CaveMan item) {
		++version;
		int index = indexOf(item);
		if (index < 0) {
			return false;
		}
		
		--size;		
		System.arraycopy(list, index + 1, list, index, size - index);
		return true;
	}
	
	public boolean containsAll(CaveManList c) {
		for (int i = 0; i < c.size; i++) {
			if (!contains(c.list[i])) {
				return false;
			}
		}
		
		return true;
	}
	
	public boolean addAll(CaveManList c) {
		++version;
		int newSize = size + c.size;
		ensureSize(newSize);

		for (int i = 0; i < c.size; i++) {
			add(c.list[i]);
		}
		
		return true;
	}
	
	public boolean removeAll(CaveManList c) {
		++version;
		int startSize = size;
		
		for (int i = 0; i < c.size; i++) {
			remove(c.list[i]);
		}
		
		return startSize != size;
	}

	public boolean retainAll(CaveManList c) {
		++version;
		int startSize = size;
		
		for (int i = 0; i < size; i++) {
			if (!c.contains(list[i])) {
				removeAt(i);
			}
		}
		
		return startSize != size;
	}
	
	public void clear() {
		++version;
		size = 0;
	}
	
	public CaveMan get(int index) {
		if ((index < 0) || (index >= size)) {
			throw new IllegalArgumentException("Index: " + index + " is out of bounds [0, " + (size - 1) + "]");
		}
		
		return list[index];
	}
	
	public CaveMan set(int index, CaveMan item) {
		++version;
		if ((index < 0) || (index >= size)) {
			throw new IllegalArgumentException("Index: " + index + " is out of bounds [0, " + (size - 1) + "]");
		}
		
		CaveMan oldItem = list[index];
		list[index] = item;
		return oldItem;
	}
	
	public void add(int index, CaveMan item) {
		++version;
		if (size >= list.length) {
			grow();		
		}
		
		System.arraycopy(list, index, list, index + 1, size - index);
		++size;
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
	
	private void grow() {
		int increase = (int)(size * 1.3);
		increase = Math.max(20, increase);
		
		CaveMan[] newList = new CaveMan[size + increase];
		System.arraycopy(list, 0, newList, 0, size);
		list = newList;
	}
	
	private void ensureSize(int newSize) {
		
		if (newSize > list.length) {
			CaveMan[] newList = new CaveMan[newSize];
			System.arraycopy(list, 0, newList, 0, size);
			list = newList;
		}
	}
	
	private class CaveManListIterator implements CaveManIterator {

		private int iteratorVersion;
		private int pos;
		
		CaveManListIterator(int vers) {
			iteratorVersion = vers;
		}
		
		@Override
		public boolean hasNext() {
			if (iteratorVersion != version) {
				throw new ConcurrentModificationException((version - iteratorVersion) + " changes have been made since the iterator was created");
			}
			return (pos < size);
		}

		@Override
		public CaveMan next() throws NoSuchElementException {
			if (iteratorVersion != version) {
				throw new ConcurrentModificationException((version - iteratorVersion) + " changes have been made since the iterator was created");
			}
			
			if (pos >= size) {
				throw new NoSuchElementException("Index " + pos + " is out of bounds [0, " + (size - 1) + "]");
			}
			return list[pos++];
		}

		@Override
		public void remove() {
			if (iteratorVersion != version) {
				throw new ConcurrentModificationException((version - iteratorVersion) + " changes have been made since the iterator was created");
			}
			
			if (pos >= size) {
				throw new NoSuchElementException("Index " + pos + " is out of bounds [0, " + (size - 1) + "]");
			}
			
			removeAt(pos);
			++iteratorVersion;
		}
	}
}

