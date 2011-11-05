package com.mebigfatguy.caveman.proto.test;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

import org.junit.Assert;
import org.junit.Test;

import com.mebigfatguy.caveman.proto.CaveManIterator;
import com.mebigfatguy.caveman.proto.CaveManList;
import com.mebigfatguy.caveman.proto.aux.CaveMan;

public class CaveManListTest {

	@Test
	public void testSizeEmpty() {
		CaveManList l = new CaveManList();
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
		CaveManList l = new CaveManList();
		for (int i = 0; i < 30; i++) {
			l.add(toCaveMan(i));
		}
		
		for (int i = 0; i < 30; i++) {
			Assert.assertEquals(toCaveMan(i), (CaveMan) l.get(i));
		}
	}
	
	@Test
	public void testSimpleIterator() {
		CaveManList l = new CaveManList();
		for (int i = 0; i < 30; i++) {
			l.add(toCaveMan(i));
		}
		
		CaveManIterator it = l.iterator();
		int i = 0;
		while (it.hasNext()) {
			Assert.assertEquals(toCaveMan(i), (CaveMan) it.next());
			++i;
		}
	}
	
	@Test
	public void testConcurrentModException() {
		try {
			CaveManList l = new CaveManList();
			for (int i = 0; i < 30; i++) {
				l.add(toCaveMan(i));
			}
			
			CaveManIterator it = l.iterator();
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
			CaveManList l = new CaveManList();
			l.add(toCaveMan(10));
			
			CaveManIterator it = l.iterator();
			it.next();
			it.next();
			
		} catch (NoSuchElementException nsee) {
			Assert.assertTrue(true);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}
	
	private CaveMan toCaveMan(int i) { return null; }
}
