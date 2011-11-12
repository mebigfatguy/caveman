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

import com.mebigfatguy.caveman.proto.aux.CMKey;
import com.mebigfatguy.caveman.proto.aux.CMKeySet;
import com.mebigfatguy.caveman.proto.aux.CMValue;
import com.mebigfatguy.caveman.proto.aux.CMValueBag;

public class CaveManCMKeyCMValueMap {
	private static final int DEFAULT_CAPACITY = 31;
	private static final float DEFAULT_LOAD_FACTOR = 0.80f;

	private int size;
	private float loadFactor;
	
	public CaveManCMKeyCMValueMap() {
		this(DEFAULT_CAPACITY);
	}
	
	public CaveManCMKeyCMValueMap(int initialCapacity) {
		this(initialCapacity, DEFAULT_LOAD_FACTOR);
	}
	
	public CaveManCMKeyCMValueMap(int initialCapacity, float loadingFactor) {
		loadFactor = loadingFactor;
		size = 0;
	}
	
	public int size() {
		return size;
	}
	
	public boolean isEmpty() {
		return size == 0;
	}
	
	public boolean containsKey(CMKey key) {
		return false;
	}
	
	public boolean containsValue(CMValue value) {
		return false;		
	}
	
	public CMValue get(CMKey key, CMValue notFoundValue) {
		return notFoundValue;
	}
	
	public void put(CMKey key, CMValue value) {
	}
	
	public void remove(CMKey key) {
	}
	
	public void putAll(CaveManCMKeyCMValueMap m) {
	}
	
	public void clear() {
	}
	
	public CMKeySet keySet() {
		return null;
	}
	
	public CMValueBag values() {
		return null;
	}

}
