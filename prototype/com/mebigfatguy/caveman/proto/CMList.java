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

import java.io.Serializable;

import com.mebigfatguy.caveman.proto.aux.CM;
/**
 * An ordered collection (also known as a <i>sequence</i>).  The user of this
 * interface has precise control over where in the list each <b>CM</b> is
 * inserted.  The user can access <b>CM</b>s by their integer index (position in
 * the list), and search for <b>CM</b>s in the list.<p>
 */
public interface CMList extends CMCollection {
	
	interface CMListExpander {
		int grow(int oldSize, int newSize);
	}
	
	 /**
     * Removes the <b>CM</b> at the specified position in this list (optional
     * operation).  Shifts any subsequent <b>CM</b>s to the left (subtracts one
     * from their indices).  Returns the <b>CM</b> that was removed from the
     * list.
     *
     * @param index the index of the <b>CM</b> to be removed
     * @return the <b>CM</b> previously at the specified position
     * @throws UnsupportedOperationException if the <tt>remove</tt> operation
     *         is not supported by this list
     * @throws IndexOutOfBoundsException if the index is out of range
     *         (<tt>index &lt; 0 || index &gt;= size()</tt>)
     */
	CM removeAt(int index);
	
    /**
     * Returns the <b>CM</b> at the specified position in this list.
     *
     * @param index index of the <b>CM</b> to return
     * @return the <b>CM</b> at the specified position in this list
     * @throws IndexOutOfBoundsException if the index is out of range
     *         (<tt>index &lt; 0 || index &gt;= size()</tt>)
     */
	CM get(int index);
	
    /**
     * Replaces the <b>CM</b> at the specified position in this list with the
     * specified <b>CM</b> (optional operation).
     *
     * @param index index of the <b>CM</b> to replace
     * @param item <b>CM</b> to be stored at the specified position
     * @return the <b>CM</b> previously at the specified position
     * @throws UnsupportedOperationException if the <tt>set</tt> operation
     *         is not supported by this list
     * @throws IndexOutOfBoundsException if the index is out of range
     *         (<tt>index &lt; 0 || index &gt;= size()</tt>)
     */
	CM set(int index, CM item);
	
    /**
     * Inserts the specified <b>CM</b> at the specified position in this list
     * (optional operation).  Shifts the <b>CM</b> currently at that position
     * (if any) and any subsequent <b>CM</b>s to the right (adds one to their
     * indices).
     *
     * @param index index at which the specified <b>CM</b> is to be inserted
     * @param item <b>CM</b> to be inserted
     * @throws UnsupportedOperationException if the <tt>add</tt> operation
     *         is not supported by this list
     * @throws IndexOutOfBoundsException if the index is out of range
     *         (<tt>index &lt; 0 || index &gt; size()</tt>)
     */
	void add(int index, CM item);
	
    /**
     * Returns the index of the first occurrence of the specified <b>CM</b>
     * in this list, or -1 if this list does not contain the <b>CM</b>.
     *
     * @param item <b>CM</b> to search for
     * @return the index of the first occurrence of the specified <b>CM</b> in
     *         this list, or -1 if this list does not contain the <b>CM</b>
     */
	int indexOf(CM item);
	
    /**
     * Returns the index of the last occurrence of the specified <b>CM</b>
     * in this list, or -1 if this list does not contain the <b>CM</b>.
     *
     * @param item <b>CM</b> to search for
     * @return the index of the last occurrence of the specified <b>CM</b> in
     *         this list, or -1 if this list does not contain the <b>CM</b>
     */
	int lastIndexOf(CM item);	
}
