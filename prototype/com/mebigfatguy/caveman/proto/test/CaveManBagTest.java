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

import com.mebigfatguy.caveman.proto.CaveManBag;
import com.mebigfatguy.caveman.proto.aux.CaveMan;

public class CaveManBagTest {
	@Test
	public void testSizeIsEmpty() {
		CaveManBag s = new CaveManBag();
		Assert.assertEquals(0, s.size());
		Assert.assertTrue(s.isEmpty());
		
		for (int i = 0; i < 10; i++) {
			s.add(toCaveMan(0));
			s.add(toCaveMan(1));
		}
		
		Assert.assertEquals(20, s.size());
		Assert.assertFalse(s.isEmpty());
		
		s.remove(toCaveMan(0));
		s.remove(toCaveMan(1));
		
		Assert.assertEquals(0, s.size());
		Assert.assertTrue(s.isEmpty());
	}
	
	@Test
	public void testAddContains() {
		CaveManBag s = new CaveManBag();
		for (int i = 0; i < 30; i++) {
			s.add(toCaveMan(i));
		}
		
		for (int i = 0; i < 30; i++) {
			Assert.assertTrue(s.contains(toCaveMan(i)));
		}
	}
	
	
	private CaveMan toCaveMan(int i) { return null; }
}
