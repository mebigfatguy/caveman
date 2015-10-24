/*
 * caveman - A primitive collection library
 * Copyright 2011-2015 MeBigFatGuy.com
 * Copyright 2011-2015 Dave Brosius
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
package com.mebigfatguy.caveman.proto.impl;

import java.io.Serializable;
import java.util.NoSuchElementException;

import com.mebigfatguy.caveman.proto.CMCollection;
import com.mebigfatguy.caveman.proto.CMDeque;
import com.mebigfatguy.caveman.proto.CMIterator;
import com.mebigfatguy.caveman.proto.aux.CM;
import com.mebigfatguy.caveman.proto.aux.CMValue;

public class CaveManCMDeque implements CMDeque, Serializable {

    public static final CM DEFAULT_NOT_FOUND_VALUE = toCaveMan(0);
    private static final int DEFAULT_CAPACITY = 16;
    
    private CM[] items;
    private int head;
    private int tail;
    private final CM notFound;
    
    public CaveManCMDeque() {
        this(DEFAULT_NOT_FOUND_VALUE);
    }
    
    public CaveManCMDeque(CM notFoundValue) {
        this(notFoundValue, DEFAULT_CAPACITY);
    }
    
    public CaveManCMDeque(CM notFoundValue, int initialCapacity) {
        items = new CM[initialCapacity];
        notFound = notFoundValue;
    }
    
    @Override
    public boolean offer(CM item) {
        return offerLast(item);
    }

    @Override
    public CM poll() {
        return pollFirst();
    }

    @Override
    public CM element() {
        return getFirst();
    }

    @Override
    public CM peek() {
        return peekFirst();
    }

    @Override
    public int size() {
        if (head < tail) {
            return tail - head;
        }
        
        return items.length - head + tail;
    }

    @Override
    public boolean isEmpty() {
        return head == tail;
    }

    @Override
    public boolean contains(CM item) {
        if (head == tail) {
            return false;
        }
        
        int leftEnd;
        if (head > tail) {
            for (int i = head; i < items.length; i++) {
                if (item == items[i]) {
                    return true;
                }
            }
            leftEnd = 0;
        } else {
            leftEnd = head;
        }

        for (int i = leftEnd; i < tail; i++) {
            if (item == items[i]) {
                return true;
            }
        }
        
        return false;
    }

    @Override
    public CMIterator iterator() {
        throw new UnsupportedOperationException("iterator()");
    }

    @Override
    public CM[] toArray() {
        CM[] copy = new CM[size()];
        if (head < tail) {
            System.arraycopy(items, head, copy, 0, size());
        } else if (head > tail) {
            int endLen = items.length - head;
            System.arraycopy(items, head, copy, 0, endLen);
            System.arraycopy(items, 0, copy, endLen, tail);
        }
        return copy;
    }

    @Override
    public boolean add(CM item) {
        addLast(item);
        return true;
    }

    @Override
    public boolean remove(CM item) {
        return removeFirstOccurrence(item);
    }

    @Override
    public void clear() {
        head = 0;
        tail = 0;
    }

    @Override
    public boolean containsAll(CMCollection c) {
        CMIterator it = c.iterator();
        while (it.hasNext()) {
            if (!contains(it.next())) {
                return true;
            }
        }
        
        return false;
    }

    @Override
    public boolean addAll(CMCollection c) {
        CMIterator it = c.iterator();
        while (it.hasNext()) {
            add(it.next());
        }
        
        return true;
    }

    @Override
    public boolean retainAll(CMCollection c) {
        throw new UnsupportedOperationException("retainAll(CMCollection c)");
    }

    @Override
    public boolean removeAll(CMCollection c) {
        boolean removed = false;
        CMIterator it = c.iterator();
        while (it.hasNext()) {
            removed |= remove(it.next());
        }
        
        return removed;
    }

    @Override
    public CM getOne() throws IllegalStateException {
        throw new UnsupportedOperationException("getOne()");
    }

    @Override
    public void addFirst(CM item) {
        throw new UnsupportedOperationException("addFirst(CM item)");
    }

    @Override
    public void addLast(CM item) {
        throw new UnsupportedOperationException("addLast(CM item)");
    }

    @Override
    public boolean offerFirst(CM item) {
        addFirst(item);
        return true;
    }

    @Override
    public boolean offerLast(CM item) {
        addLast(item);
        return true;
    }

    @Override
    public CM removeFirst() {
        CM item = pollFirst();
        if (item == notFound)
            throw new NoSuchElementException();
        return item;
    }

    @Override
    public CM removeLast() {
        CM item = pollLast();
        if (item == notFound)
            throw new NoSuchElementException();
        return item;
    }

    @Override
    public CM pollFirst() {
        throw new UnsupportedOperationException("pollFirst()");
    }

    @Override
    public CM pollLast() {
        throw new UnsupportedOperationException("pollLast()");
   }

    @Override
    public CM getFirst() {
        throw new UnsupportedOperationException("getFirst()");
    }

    @Override
    public CM getLast() {
        throw new UnsupportedOperationException("getLast()");
    }

    @Override
    public CM peekFirst() {
        throw new UnsupportedOperationException("peekFirst()");
    }

    @Override
    public CM peekLast() {
        throw new UnsupportedOperationException("peekLast()");
    }

    @Override
    public boolean removeFirstOccurrence(CM item) {
        throw new UnsupportedOperationException("removeFirstOccurrence(CM item)");
    }

    @Override
    public boolean removeLastOccurrence(CM item) {
        throw new UnsupportedOperationException("removeLastOccurrence(CM item)");
    }

    
    private static CM toCaveMan(int i) {return null;}
}
