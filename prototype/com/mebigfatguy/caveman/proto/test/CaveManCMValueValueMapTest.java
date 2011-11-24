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
package com.mebigfatguy.caveman.proto.test;

import org.junit.Assert;
import org.junit.Test;

import com.mebigfatguy.caveman.proto.CMValueValueMap;
import com.mebigfatguy.caveman.proto.CMValueValueMapIterator;
import com.mebigfatguy.caveman.proto.aux.CM;
import com.mebigfatguy.caveman.proto.impl.CaveManCMValueValueMap;

public class CaveManCMValueValueMapTest {

	@Test
	public void testSizeEmpty() {
		CMValueValueMap<Integer> m = new CaveManCMValueValueMap<Integer>();
		
		for (int i = 0; i < 100; i++) {
			m.put(Integer.valueOf(i), toCaveMan(i));
		}
		
		for (int i = 0; i < 100; i++) {
			m.remove(Integer.valueOf(i));
		}
		
		Assert.assertEquals(0, m.size());
	}
	
	@Test
	public void testIteratorRemove() {
		CMValueValueMap<Integer> m = new CaveManCMValueValueMap<Integer>();
		
		for (int i = 0; i < 100; i++) {
			m.put(Integer.valueOf(i), toCaveMan(i));
		}
		
		CMValueValueMapIterator<Integer> it = m.iterator();
		while (it.hasNext()) {
			it.next();
			
			if (((it.key().intValue()) & 1) == 0) {
				it.remove();
			}
		}
		
		for (int i = 1; i < 100; i+=2) {
			Assert.assertTrue(m.containsKey(Integer.valueOf(i)));
		}
	}
	
	
	private CM toCaveMan(int i) { return null; }
}
