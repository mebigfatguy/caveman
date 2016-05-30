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
package com.mebigfatguy.caveman.proto.aux;


public interface CMKeyCollection {
	int size();
	
	boolean isEmpty();
	
	boolean contains(CMKey item);
	
	CMKeyIterator iterator();
	
	CMKey[] toArray();
	
	boolean add(CMKey item);
	
	boolean remove(CMKey item);
	
	void clear();
	
	boolean containsAll(CMKeyCollection c);
	
	boolean addAll(CMKeyCollection c);
	
	boolean retainAll(CMKeyCollection c);
	
	boolean removeAll(CMKeyCollection c);

	CMKey getOne() throws IllegalStateException;
}
