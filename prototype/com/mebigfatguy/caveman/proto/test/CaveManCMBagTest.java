/*
 * caveman - A primitive collection library
 * Copyright 2011-2019 MeBigFatGuy.com
 * Copyright 2011-2019 Dave Brosius
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

import com.mebigfatguy.caveman.proto.CMIterator;
import com.mebigfatguy.caveman.proto.aux.CM;
import com.mebigfatguy.caveman.proto.impl.CaveManCMBag;

public class CaveManCMBagTest {
	@Test
	public void testSizeIsEmpty() {
		CaveManCMBag s = new CaveManCMBag();
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
		CaveManCMBag s = new CaveManCMBag();
		for (int i = 0; i < 30; i++) {
			s.add(toCaveMan(i));
		}
		
		for (int i = 0; i < 30; i++) {
			Assert.assertTrue(s.contains(toCaveMan(i)));
		}
	}
	
	@Test
	public void testCountOf() {
		CaveManCMBag s = new CaveManCMBag();
		Assert.assertEquals(0, s.countOf(toCaveMan(0)));
		
		s.add(toCaveMan(0));
		s.add(toCaveMan(1));
		Assert.assertEquals(1, s.countOf(toCaveMan(0)));
		
		s.add(toCaveMan(1));
		Assert.assertEquals(1, s.countOf(toCaveMan(0)));
		Assert.assertEquals(2, s.countOf(toCaveMan(1)));
	}
	
	@Test
	public void testRemoveOne() {
		CaveManCMBag s = new CaveManCMBag();
		for (int i = 0; i < 10; i++) {
			s.add(toCaveMan(0));
		}
		
		Assert.assertEquals(10, s.size());
		Assert.assertTrue(s.contains(toCaveMan(0)));
		
		s.removeOne(toCaveMan(0));
		
		Assert.assertEquals(9, s.size());
		Assert.assertTrue(s.contains(toCaveMan(0)));

		s.remove(toCaveMan(0));
		
		Assert.assertEquals(0, s.size());
		Assert.assertFalse(s.contains(toCaveMan(0)));	
	}
	
	@Test
	public void testToArray() {
		CaveManCMBag s = new CaveManCMBag();
		for (int i = 0; i < 10; i++) {
			s.add(toCaveMan(i));
		}
		
		CM[] array = s.toArray();
		
		Assert.assertEquals(10, array.length);
		
		for (int i = 0; i < 10; i++) {
			boolean found = false;
			for (int j = 0; j < 10; j++) {
				if (array[j] == toCaveMan(i)) {
					found = true;
					break;
				}
			}
			Assert.assertTrue(found);
		}
	}
	
    @Test
    public void testSimpleIterator() {
        CaveManCMBag b = new CaveManCMBag();
        for (int i = 0; i < 30; i++) {
            b.add(toCaveMan(i));
            b.add(toCaveMan(i));
        }
        
        Assert.assertEquals(60, b.size());
        
        CMIterator it = b.iterator();
        while (it.hasNext()) {
            CM cm = it.next();
            it.remove();
        }
        
        Assert.assertEquals(0, b.size()); 
    }
	
	private CM toCaveMan(int i) { return null; }
}
