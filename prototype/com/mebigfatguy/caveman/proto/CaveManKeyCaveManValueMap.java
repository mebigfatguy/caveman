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

import com.mebigfatguy.caveman.proto.aux.CaveManKey;
import com.mebigfatguy.caveman.proto.aux.CaveManKeySet;
import com.mebigfatguy.caveman.proto.aux.CaveManValue;
import com.mebigfatguy.caveman.proto.aux.CaveManValueBag;

public class CaveManKeyCaveManValueMap {
	private static final int DEFAULT_CAPACITY = 31;
	private static final float DEFAULT_LOAD_FACTOR = 0.80f;

	private int size;
	private float loadFactor;
	
	public CaveManKeyCaveManValueMap() {
		this(DEFAULT_CAPACITY);
	}
	
	public CaveManKeyCaveManValueMap(int initialCapacity) {
		this(initialCapacity, DEFAULT_LOAD_FACTOR);
	}
	
	public CaveManKeyCaveManValueMap(int initialCapacity, float loadingFactor) {
		loadFactor = loadingFactor;
		size = 0;
	}
	
	public int size() {
		return size;
	}
	
	public boolean isEmpty() {
		return size == 0;
	}
	
	public boolean containsKey(CaveManKey key) {
		return false;
	}
	
	public boolean containsValue(CaveManKey value) {
		return false;		
	}
	
	public CaveManValue get(CaveManKey key, CaveManValue notFoundValue) {
		return notFoundValue;
	}
	
	public void put(CaveManKey key, CaveManValue value) {
	}
	
	public void remove(CaveManKey key) {
	}
	
	public void putAll(CaveManKeyCaveManValueMap m) {
	}
	
	public void clear() {
	}
	
	public CaveManKeySet keySet() {
		return null;
	}
	public CaveManValueBag values() {
		return null;
	}

}
