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
package com.mebigfatguy.caveman.proto;

import com.mebigfatguy.caveman.proto.aux.CM;

/** 
 * A collection that may contain duplicate <b>CM</b>s. As implied by its name, this interface models 
 * the mathematical <i>bag</i> abstraction.
 */
public interface CMBag extends CMCollection {
	
	/**
	 * Removes one copy of the <b>CM</b> item from bag. If there is only one item in the bag
	 * at the time of removal, there will be no more <b>CM</b> items for that value in the bag.
	 * 
	 * @param item the <b>CM</b> item to remove
	 * @return whether the Bag was modified by the operation
	 */
	boolean removeOne(CM item);
	
	/**
	 * Returns the number of <b>CM</b> items with value specified in the bag.
	 * This operation is supplied for completeness, but as such is not particularly performant for large
	 * bags. If large number of duplicates are to be added, and counts of those items is important, the use of a 
	 * CMIntMap should be considered instead, where the value is the count of keys in the map.
	 * 
	 * @param item the <b>CM</b> value that is to be searched in the bag for a count
	 * @return the count of items in the bag with value <i>item</i>.
	 */
	int countOf(CM item);
}
