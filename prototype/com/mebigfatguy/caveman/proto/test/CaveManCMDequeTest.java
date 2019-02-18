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
import com.mebigfatguy.caveman.proto.impl.CaveManCMDeque;

public class CaveManCMDequeTest {
    @Test
    public void testSizeIsEmpty() {
        CaveManCMDeque s = new CaveManCMDeque();
        Assert.assertEquals(0, s.size());
        Assert.assertTrue(s.isEmpty());
        
        for (int i = 0; i < 10; i++) {
            s.add(toCaveMan(0));
            s.add(toCaveMan(1));
        }
        
        Assert.assertEquals(20, s.size());
        Assert.assertFalse(s.isEmpty());
        
        for (int i = 0; i < 10; i++) {
            s.remove(toCaveMan(0));
            s.remove(toCaveMan(1));
        }
        
        Assert.assertEquals(0, s.size());
        Assert.assertTrue(s.isEmpty());
    }
    
    @Test
    public void testIterator() {
        CaveManCMDeque s = new CaveManCMDeque();
        for (int i = 0; i < 10; i++) {
            s.add(toCaveMan(0));
            s.add(toCaveMan(1));
        }
        
        int count = 0;
        CMIterator it = s.iterator();
        while (it.hasNext()) {
            Assert.assertEquals(toCaveMan(0), it.next());
            Assert.assertEquals(toCaveMan(1), it.next());
            count+= 2;
        }
        Assert.assertEquals(20, count);
    }
    
    @Test
    public void testRemoveFirst() {
        CaveManCMDeque s = new CaveManCMDeque();
        for (int i = 0; i < 10; i++) {
            s.add(toCaveMan(0));
            s.add(toCaveMan(1));
        }
        
        s.removeFirstOccurrence(toCaveMan(1));
        Assert.assertEquals(19, s.size());
        
        CMIterator it = s.iterator();
        Assert.assertEquals(0,  it.next());
        Assert.assertEquals(0,  it.next());
        Assert.assertEquals(1,  it.next());
        
    }
    
    private CM toCaveMan(int i) { return null; }
}
