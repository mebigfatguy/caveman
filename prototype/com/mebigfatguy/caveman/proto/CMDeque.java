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

import com.mebigfatguy.caveman.proto.aux.CM;

/**
 * a linear collection that supports insertions and deletions at both ends.
 */
public interface CMDeque extends CMQueue {

    /**
     * Inserts the specified element at the front of this deque if it is
     * possible to do so immediately without violating capacity restrictions,
     * throwing an {@code IllegalStateException} if no space is currently
     * available.  When using a capacity-restricted deque, it is generally
     * preferable to use method offerFirst.
     */
    void addFirst(CM item);
    
    /**
     * Inserts the specified element at the end of this deque if it is
     * possible to do so immediately without violating capacity restrictions,
     * throwing an {@code IllegalStateException} if no space is currently
     * available.  When using a capacity-restricted deque, it is generally
     * preferable to use method offerLast.
     */
    void addLast(CM item);
    
    /**
     * Inserts the specified element at the front of this deque unless it would
     * violate capacity restrictions.  When using a capacity-restricted deque,
     * this method is generally preferable to the addFirst method,
     * which can fail to insert an element only by throwing an exception.
     */
    boolean offerFirst(CM item);
    
    /**
     * Inserts the specified element at the end of this deque unless it would
     * violate capacity restrictions.  When using a capacity-restricted deque,
     * this method is generally preferable to the addLast method,
     * which can fail to insert an element only by throwing an exception.
     */
    boolean offerLast(CM item);
    
    /**
     * Retrieves and removes the first element of this deque.  This method
     * differs from pollFirst only in that it throws an
     * exception if this deque is empty.
     */
    CM removeFirst();
    
    /**
     * Retrieves and removes the last element of this deque.  This method
     * differs from pollLast only in that it throws an
     * exception if this deque is empty.
     */
    CM removeLast();
    
    /**
     * Retrieves and removes the first element of this deque,
     * or returns a 'not-found' value if this deque is empty.
     */
    CM pollFirst();
    
    /**
     * Retrieves and removes the last element of this deque,
     * or returns a 'not-found' value if this deque is empty.
     */
    CM pollLast();
    
    /**
     * Retrieves, but does not remove, the first element of this deque.
     *
     * This method differs from peekFirst only in that it
     * throws an exception if this deque is empty.
     */
    CM getFirst();

    /**
     * Retrieves, but does not remove, the last element of this deque.
     *
     * This method differs from peekLast only in that it
     * throws an exception if this deque is empty.
     */
    CM getLast();
    
    /**
     * Retrieves, but does not remove, the first element of this deque,
     * or returns a 'non-found' value if this deque is empty.
     */
    CM peekFirst();
    
    /**
     * Retrieves, but does not remove, the last element of this deque,
     * or returns a 'non-found' value if this deque is empty.
     */
    CM peekLast();
    
    /**
     * Removes the first occurrence of the specified element from this deque.
     * If the deque does not contain the element, it is unchanged.
     * Returns true if this deque contained the specified element
     * (or equivalently, if this deque changed as a result of the call).
     */
    boolean removeFirstOccurrence(CM item);
    
    /**
     * Removes the last occurrence of the specified element from this deque.
     * If the deque does not contain the element, it is unchanged.
     * Returns true if this deque contained the specified element
     * (or equivalently, if this deque changed as a result of the call).
     */
    boolean removeLastOccurrence(CM item);
}
