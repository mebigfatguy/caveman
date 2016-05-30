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
package com.mebigfatguy.caveman.proto;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

import com.mebigfatguy.caveman.proto.aux.CM;

/**
 * An iterator over a collection of <b>CM</b> elements
 */
public interface CMIterator {

    /**
     * Returns <tt>true</tt> if the iteration has more <b>CM</b> elements. (In other
     * words, returns <tt>true</tt> if <tt>next</tt> would return an element
     * rather than throwing an exception.)
     *
     * @return <tt>true</tt> if the iterator has more elements.
     */
	boolean hasNext();
	
    /**
     * Returns the next <b>CM</b> element in the iteration.
     *
     * @return the next element in the iteration.
     * @exception NoSuchElementException iteration has no more elements.
     */
	CM next() throws NoSuchElementException;
	
    /**
     * 
     * Removes from the underlying collection the last <b>CM</b> element returned by the
     * iterator (optional operation).  This method can be called only once per
     * call to <tt>next</tt>.  The behavior of an iterator is unspecified if
     * the underlying collection is modified while the iteration is in
     * progress in any way other than by calling this method.
     
     * @exception ConcurrentModificationException if the underlying collection has changed
     * since the iterator was created.
     */
	void remove();
}
