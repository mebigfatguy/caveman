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
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

import com.mebigfatguy.caveman.proto.CMCollection;
import com.mebigfatguy.caveman.proto.CMDeque;
import com.mebigfatguy.caveman.proto.CMIterator;
import com.mebigfatguy.caveman.proto.aux.CM;

public class CaveManCMDeque implements CMDeque, Serializable {

    private static final long serialVersionUID = 6594083693968281501L;
    public static final CM DEFAULT_NOT_FOUND_VALUE = toCaveMan(0);
    private static final int DEFAULT_CAPACITY = 16;
    
    private CM[] items;
    private int head;
    private int tail;
    private final CM notFound;
    private int version;
    
    public CaveManCMDeque() {
        this(DEFAULT_NOT_FOUND_VALUE);
    }
    
    public CaveManCMDeque(CM notFoundValue) {
        this(notFoundValue, DEFAULT_CAPACITY);
    }
    
    public CaveManCMDeque(CM notFoundValue, int initialCapacity) {
        if (initialCapacity < 1) {
            initialCapacity = 1;
        }
        
        items = new CM[initialCapacity];
        notFound = notFoundValue;
        version = 0;
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
        return new CaveManCMDequeIterator(version);
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
        version++;
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
        if (size() == 0)
            throw new IllegalStateException("This deque is empty");
        
        return getFirst();
    }

    @Override
    public void addFirst(CM item) {
        head--;
        if (head < 0) {
            head = items.length - 1;
        }
        items[head] = item;
        
        if (head == tail)
            expand();
        version++;
    }

    @Override
    public void addLast(CM item) {
        items[tail] = item;
        tail++;
        if (tail >= items.length) {
            tail = 0;
        }
        
        if (head == tail)
            expand();
        version++;
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
        if (head == tail) {
            throw new NoSuchElementException();           
        }
        CM item = pollFirst();
        return item;
    }

    @Override
    public CM removeLast() {
        if (head == tail) {
            throw new NoSuchElementException();           
        }
        CM item = pollLast();
        return item;
    }

    @Override
    public CM pollFirst() {
        if (head == tail) {
            return notFound;
        }
        
        return removeFirst();
    }

    @Override
    public CM pollLast() {
        if (head == tail) {
            return notFound;
        }
        
        return removeLast();
   }

    @Override
    public CM getFirst() {
        if (head == tail) {
            throw new NoSuchElementException();
        }
        
        return peekFirst();
    }

    @Override
    public CM getLast() {
        if (head == tail) {
            throw new NoSuchElementException();
        }
        
        return peekLast();
    }

    @Override
    public CM peekFirst() {
        if (head == tail) {
            return notFound;
        }
        
        return items[head];
    }

    @Override
    public CM peekLast() {
        if (head == tail) {
            return notFound;
        }
        
        return items[--tail];
    }

    @Override
    public boolean removeFirstOccurrence(CM item) {
        version++;
        
        if (head == tail) {
            return false;
        }
        
        int idx = head;
        if (head > tail) {
            while (idx < items.length) {
                if (items[idx] == item) {
                    removeAt(idx);
                    return true;
                }
                idx++;
            }
            idx = 0;
        }
        
        while (idx < tail) {
            if (items[idx] == item) {
                removeAt(idx);
                return true;
            }
            idx++;
        }
        return false;
    }

    @Override
    public boolean removeLastOccurrence(CM item) {
        version++;
        
        if (head == tail) {
            return false;
        }
        
        int idx = head;
        if (head > tail) {
            while (idx >= 0) {
                if (items[idx] == item) {
                    removeAt(idx);
                    return true;
                }
                idx--;
            }
            idx = items.length - 1;
        }
        
        while (idx >= head) {
            if (items[idx] == item) {
                removeAt(idx);
                return true;
            }
            idx--;
        }
        
        return false;
    }
    
    private void removeAt(int pos) {
        if (head > tail) {
            if (pos > head) {
                System.arraycopy(items, head, items, head+1, pos-head);
                head++;
            } else {
                System.arraycopy(items, 0, items, 1, tail - 1);
                tail--;
            }
        } else {
            System.arraycopy(items, pos+1, items, pos, tail - pos - 1);
        }
    }

    private void expand() {
        CM[] newItems = new CM[items.length * 2];
        System.arraycopy(items,  head,  newItems,  0, items.length - head);
        System.arraycopy(items, 0,  newItems,  items.length - head, tail);
        tail = size();
        head = 0;
        items = newItems;
    }
    
    private class CaveManCMDequeIterator implements CMIterator {

        private int iteratorVersion;
        private int pos;

        CaveManCMDequeIterator(int vers) {
            iteratorVersion = vers;
            pos = head;
        }

        @Override
        public boolean hasNext() {
            if (iteratorVersion != version) {
                throw new ConcurrentModificationException((version - iteratorVersion) + " changes have been made since the iterator was created");
            }
            
            return pos != tail;
        }

        @Override
        public CM next() throws NoSuchElementException {
            if (iteratorVersion != version) {
                throw new ConcurrentModificationException((version - iteratorVersion) + " changes have been made since the iterator was created");
            }

            if (pos == tail) {
                throw new NoSuchElementException("Index " + size() + " is out of bounds");
            }
            CM item = items[pos++];
            if (pos == items.length) {
                pos = 0;
            }
            return item;
        }

        @Override
        public void remove() {
            if (iteratorVersion != version) {
                throw new ConcurrentModificationException((version - iteratorVersion) + " changes have been made since the iterator was created");
            }

            if (pos == tail) {
                throw new NoSuchElementException("Index " + size() + " is out of bounds");
            }

            removeAt(pos);
            ++iteratorVersion;
        }
    }
    
    
    private static CM toCaveMan(int i) {return null;}
}
