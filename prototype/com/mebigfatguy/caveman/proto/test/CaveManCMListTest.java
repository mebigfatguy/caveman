/*
 * caveman - A primitive collection library
 * Copyright 2011-2016 MeBigFatGuy.com
 * Copyright 2011-2016 Dave Brosius
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

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

import org.junit.Assert;
import org.junit.Test;

import com.mebigfatguy.caveman.proto.CMIterator;
import com.mebigfatguy.caveman.proto.aux.CM;
import com.mebigfatguy.caveman.proto.impl.CaveManCMList;

public class CaveManCMListTest {

	@Test
	public void testSizeEmpty() {
		CaveManCMList l = new CaveManCMList();
		Assert.assertEquals(0, l.size());
		Assert.assertTrue(l.isEmpty());
		
		l.add(toCaveMan(10));
		Assert.assertEquals(1, l.size());
		Assert.assertFalse(l.isEmpty());
		
		l.remove(toCaveMan(10));
		Assert.assertEquals(0, l.size());
		Assert.assertTrue(l.isEmpty());		
	}
	
	@Test
	public void testAddRemove() {
		CaveManCMList l = new CaveManCMList();
		for (int i = 0; i < 30; i++) {
			l.add(toCaveMan(i));
		}
		
		for (int i = 0; i < 30; i++) {
			Assert.assertEquals(toCaveMan(i), l.get(i));
		}
	}
	
	@Test
	public void testContains() {
		CaveManCMList l = new CaveManCMList();
		for (int i = 0; i < 30; i++) {
			l.add(toCaveMan(i));
		}
		
		for (int i = 0; i < 30; i++) {
			Assert.assertTrue(l.contains(toCaveMan(i)));
		}
	}
	
	@Test
	public void testRemoveAt() {
		CaveManCMList l = new CaveManCMList();
		for (int i = 0; i < 30; i++) {
			l.add(toCaveMan(i));
		}
		
		for (int i = 0; i < 30; i+= 2) {
			l.removeAt(i/2);
		}
		
		Assert.assertEquals(15, l.size());
	}
	
	@Test
	public void testSimpleIterator() {
		CaveManCMList l = new CaveManCMList();
		for (int i = 0; i < 30; i++) {
			l.add(toCaveMan(i));
		}
		
		CMIterator it = l.iterator();
		int i = 0;
		while (it.hasNext()) {
			Assert.assertEquals(toCaveMan(i), it.next());
			++i;
		}
	}
	
	@Test
	public void testConcurrentModException() {
		try {
			CaveManCMList l = new CaveManCMList();
			for (int i = 0; i < 30; i++) {
				l.add(toCaveMan(i));
			}
			
			CMIterator it = l.iterator();
			it.next();
			it.next();
			
			l.add(toCaveMan(100));
			
			it.next();
			Assert.assertTrue(false);
		} catch (ConcurrentModificationException cme) {
			Assert.assertTrue(true);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}
	
	@Test
	public void testOffTheEndIterator() {
		try {
			CaveManCMList l = new CaveManCMList();
			l.add(toCaveMan(10));
			
			CMIterator it = l.iterator();
			it.next();
			it.next();
			
		} catch (NoSuchElementException nsee) {
			Assert.assertTrue(true);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}
	
	private CM toCaveMan(int i) { return null; }
}
