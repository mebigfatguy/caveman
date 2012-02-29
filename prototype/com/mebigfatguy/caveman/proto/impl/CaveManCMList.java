/*
 * caveman - A primitive collection library
 * Copyright 2011-2012 MeBigFatGuy.com
 * Copyright 2011-2012 Dave Brosius
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

import com.mebigfatguy.caveman.proto.CMCollection;
import com.mebigfatguy.caveman.proto.CMIterator;
import com.mebigfatguy.caveman.proto.CMList;
import com.mebigfatguy.caveman.proto.aux.CM;

public class CaveManCMList implements CMList {
	private static final int DEFAULT_SIZE = 20;
	private static final CaveManCMListExpander DEFAULT_EXPANDER = new CaveManCMListExpander();
	
	private final CMListExpander expander;
	private CM[] list;
	private int size;
	private int version;
	
	public CaveManCMList() {
		this(DEFAULT_SIZE);
	}
	
	public CaveManCMList(int defaultSize) {
		this(defaultSize, DEFAULT_EXPANDER);
	}
	
	public CaveManCMList(int defaultSize, CMListExpander listExpander) {
		expander = listExpander;
		list = new CM[defaultSize];
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
		for (int i = 0; i < size; i++) {
			if (item == list[i])
				return true;
		}
		
		return false;
	}
	
	@Override
	public CMIterator iterator() {
		return new CaveManCMListIterator(version);
	}
	
	@Override
	public CM[] toArray() {
		CM[] array = new CM[size];
		System.arraycopy(list,  0, array, 0, size);
		return array;
	}
	
	@Override
	public boolean add(CM item) {
		++version;
		
		ensureSize(size + 1);
		
		list[size++] = item;
		return true;
	}
	
	@Override
	public CM removeAt(int index) {
		++version;
		CM item = get(index);
		
		--size;		
		System.arraycopy(list, index + 1, list, index, size - index);
		return item;
	}
	
	@Override
	public boolean remove(CM item) {
		++version;
		int index = indexOf(item);
		if (index < 0) {
			return false;
		}
		
		--size;		
		System.arraycopy(list, index + 1, list, index, size - index);
		return true;
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
		int newSize = size + c.size();
		ensureSize(newSize);

		CMIterator it = c.iterator();
		while (it.hasNext()) {
			add(it.next());
		}
		
		return c.size() > 0;
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
	public boolean retainAll(CMCollection c) {
		++version;
		int startSize = size;
		
		for (int i = 0; i < size; i++) {
			if (!c.contains(list[i])) {
				removeAt(i);
			}
		}
		
		return startSize != size;
	}
	
	@Override
	public void clear() {
		++version;
		size = 0;
	}
	
	@Override
	public CM get(int index) {
		if ((index < 0) || (index >= size)) {
			throw new IllegalArgumentException("Index: " + index + " is out of bounds [0, " + (size - 1) + "]");
		}
		
		return list[index];
	}
	
	@Override
	public CM set(int index, CM item) {
		++version;
		if ((index < 0) || (index >= size)) {
			throw new IllegalArgumentException("Index: " + index + " is out of bounds [0, " + (size - 1) + "]");
		}
		
		CM oldItem = list[index];
		list[index] = item;
		return oldItem;
	}
	
	@Override
	public void add(int index, CM item) {
		++version;
		
		ensureSize(size + 1);
		
		System.arraycopy(list, index, list, index + 1, size - index);
		++size;
	}
	
	@Override
	public int indexOf(CM item) {
		for (int i = 0; i < size; i++) {
			if (item == list[i]) {
				return i;
			}
		}
		
		return -1;
	}
	
	@Override
	public int lastIndexOf(CM item) {
		for (int i = size - 1; i >= 0; i--) {
			if (item == list[i]) {
				return i;
			}
		}
		
		return -1;
	}	
	
	private void ensureSize(int newSize) {
		
		if (newSize > list.length) {
			int expansionSize = expander.grow(size,  newSize);
			if (expansionSize < newSize) {
				expansionSize = newSize;
			}
			
			CM[] newList = new CM[expansionSize];
			System.arraycopy(list, 0, newList, 0, size);
			list = newList;
		}
	}
	
	private class CaveManCMListIterator implements CMIterator {

		private int iteratorVersion;
		private int pos;
		
		CaveManCMListIterator(int vers) {
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
		public CM next() throws NoSuchElementException {
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
	
	private static final class CaveManCMListExpander implements CMListExpander {

		@Override
		public int grow(int oldSize, int newSize) {
			return (int)(oldSize + newSize * 1.4);
		}
	}
}

