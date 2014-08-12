/*
 * hoidla: various algorithms for Big Data solutions
 * Author: Pranab Ghosh
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0 
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package org.hoidla.window;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author pranab
 *
 */
public class SizeBoundWindow<T> extends DataWindow<T>{
	private int maxSize;
	
	public SizeBoundWindow(int maxSize) {
		super();
		this.maxSize = maxSize;
	}
	
	public void expire() {
		if (dataWindow.size() > maxSize) {
			dataWindow.remove();
		}
	}
	
	
}
