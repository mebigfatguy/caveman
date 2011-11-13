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

import com.mebigfatguy.caveman.proto.CMKeyCMValueMap;
import com.mebigfatguy.caveman.proto.aux.CMKey;
import com.mebigfatguy.caveman.proto.aux.CMValue;
import com.mebigfatguy.caveman.proto.impl.CaveManCMKeyCMValueMap;

public class CaveManCMKeyCMValueMapTest {

	@Test
	public void testSizeEmpty() {
		CMKeyCMValueMap m = new CaveManCMKeyCMValueMap();
		
		for (int i = 0; i < 100; i++) {
			m.put(toCaveManKey(i), toCaveManValue(i));
		}
		
		for (int i = 0; i < 100; i++) {
			m.remove(toCaveManKey(i));
		}
		
		Assert.assertEquals(0, m.size());
	}
	
	
	
	private CMKey toCaveManKey(int i) { return null; }
	private CMValue toCaveManValue(int i) { return null; }
}
