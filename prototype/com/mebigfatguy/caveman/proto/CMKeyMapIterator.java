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
package com.mebigfatguy.caveman.proto;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

import com.mebigfatguy.caveman.proto.aux.CM;

/**
 * An iterator over a collection of <b>CM/Object</b> elements
 */
public interface CMKeyMapIterator<V> {
	
    /**
     * Returns <tt>true</tt> if the iteration has more <b>CM/Object</b> elements. (In other
     * words, returns <tt>true</tt> if <tt>next</tt> would return an element
     * rather than throwing an exception.)
     *
     * @return <tt>true</tt> if the iterator has more elements.
     */
	boolean hasNext();
	
    /**
     * Positions the iterator at the next <b>CM/Object</b> element in the iteration.
     *
     * @exception NoSuchElementException iteration has no more elements.
     */
	void next() throws NoSuchElementException;
	
	/**
	 * Returns the <b>CM</b> key pointed at by the iterator
	 * 
	 * @return the key of the pair currently being pointed at by the iterator
	 */
	CM key();
	
	/**
	 * Returns the <b>Object</b> value pointed at by the iterator
	 * 
	 * @return the value of the pair currently being pointed at by the iterator
	 */
	V value();
	
    /**
     * 
     * Removes from the underlying collection the last <b>CM/Object</b> element returned by the
     * iterator (optional operation).  This method can be called only once per
     * call to <tt>next</tt>.  The behavior of an iterator is unspecified if
     * the underlying collection is modified while the iteration is in
     * progress in any way other than by calling this method.
     
     * @exception ConcurrentModificationException if the underlying collection has changed
     * since the iterator was created.
     */
	void remove();
}
