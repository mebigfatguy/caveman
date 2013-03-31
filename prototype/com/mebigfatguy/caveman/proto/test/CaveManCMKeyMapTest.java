/*
 * caveman - A primitive collection library
 * Copyright 2011-2013 MeBigFatGuy.com
 * Copyright 2011-2013 Dave Brosius
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

import com.mebigfatguy.caveman.proto.CMKeyMap;
import com.mebigfatguy.caveman.proto.CMKeyMapIterator;
import com.mebigfatguy.caveman.proto.aux.CM;
import com.mebigfatguy.caveman.proto.impl.CaveManCMKeyMap;

public class CaveManCMKeyMapTest {

	@Test
	public void testSizeEmpty() {
		CMKeyMap<Integer> m = new CaveManCMKeyMap<Integer>();
		
		for (int i = 0; i < 100; i++) {
			m.put(toCaveMan(i), Integer.valueOf(i));
		}
		
		for (int i = 0; i < 100; i++) {
			m.remove(toCaveMan(i));
		}
		
		Assert.assertEquals(0, m.size());
	}
	
	@Test
	public void testIteratorRemove() {
		CMKeyMap<Integer> m = new CaveManCMKeyMap<Integer>();
		
		for (int i = 0; i < 100; i++) {
			m.put(toCaveMan(i), Integer.valueOf(i));
		}
		
		CMKeyMapIterator<Integer> it = m.iterator();
		while (it.hasNext()) {
			it.next();
			
			if ((fromCaveMan(it.key()) & 1) == 0) {
				it.remove();
			}
		}
		
		for (int i = 1; i < 100; i+=2) {
			Assert.assertTrue(m.containsKey(toCaveMan(i)));
		}
	}
	
	
	private int fromCaveMan(CM k) { return 0; }
	private CM toCaveMan(int i) { return null; }
}
