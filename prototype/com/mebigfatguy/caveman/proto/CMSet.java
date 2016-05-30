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


/** 
 * A collection that contains no duplicate <b>CM</b>s.  More formally, sets
 * contain no pair of <b>CM</b>s <code>e1</code> and <code>e2</code> such that
 * <code>e1 == e2</code>.  As implied by its name, this interface models 
 * the mathematical <i>set</i> abstraction.
 */
public interface CMSet extends CMCollection {
}
