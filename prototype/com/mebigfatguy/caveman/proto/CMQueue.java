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

import java.util.NoSuchElementException;

import com.mebigfatguy.caveman.proto.aux.CM;

/**
 * a collection used for holding items waiting to be processed
 */
public interface CMQueue extends CMCollection {

    /**
     * Inserts the specified element into this queue if it is possible to do
     * so immediately without violating capacity restrictions.
     * When using a capacity-restricted queue, this method is generally
     * preferable to {@link #add}, which can fail to insert an element only
     * by throwing an exception.
     * 
     * * @param item the <b>CM</b> item to remove
     * @return if the element was added to this queue, else false
     */
    boolean offer(CM item);
    
    /**
     * Retrieves and removes the head of this queue,
     * or returns a 'not-found' value if this queue is empty.
     *
     * @return the head of this queue, or a'not-found' value if this queue is empty
     */
    CM poll();
    
    /**
     * Retrieves, but does not remove, the head of this queue.  This method
     * differs from peek only in that it throws an exception
     * if this queue is empty.
     *
     * @return the head of this queue
     * @throws NoSuchElementException if this queue is empty
     */
    CM element();
    
    /**
     * Retrieves, but does not remove, the head of this queue,
     * or returns a 'not-found' value if this queue is empty.
     *
     * @return the head of this queue, or a 'not-found' value if this queue is empty
     */
    CM peek();
}
