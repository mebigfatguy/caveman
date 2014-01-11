/*
 * caveman - A primitive collection library
 * Copyright 2011-2014 MeBigFatGuy.com
 * Copyright 2011-2014 Dave Brosius
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
package com.mebigfatguy.caveman.proto.aux;


public interface CMKeySet extends CMKeyCollection {
	
	@Override
	int size();
	
	@Override
	boolean isEmpty();
	
	@Override
	boolean contains(CMKey item);
	
	@Override
	CMKeyIterator iterator();
	
	@Override
	CMKey[] toArray();
	
	@Override
	boolean add(CMKey item);
	
	@Override
	boolean remove(CMKey item);
	
	@Override
	void clear();
}
