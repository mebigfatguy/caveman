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

public class CaveManCMValueMap<K> {
	private static final int DEFAULT_CAPACITY = 31;
	private static final float DEFAULT_LOAD_FACTOR = 0.80f;

	private int size;
	private float loadFactor;
	
	public CaveManCMValueMap() {
		this(DEFAULT_CAPACITY);
	}
	
	public CaveManCMValueMap(int initialCapacity) {
		this(initialCapacity, DEFAULT_LOAD_FACTOR);
	}
	
	public CaveManCMValueMap(int initialCapacity, float loadingFactor) {
		loadFactor = loadingFactor;
		size = 0;
	}
	
	public int size() {
		return size;
	}
	
	public boolean isEmpty() {
		return size == 0;
	}
	
	public boolean containsKey(K key) {
		return false;
	}
	
	public boolean containsValue(CM value) {
		return false;		
	}
	
	public CM get(K key, CM notFoundValue) {
		return notFoundValue;
	}
	
	public void put(K key, CM value) {
	}
	
	public void remove(K key) {
	}
	
	public void putAll(CaveManCMValueMap<K> m) {
	}
	
	public void clear() {
	}
	
	public CaveManCMBag values() {
		return null;
	}
}
