/* ====================================================================
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */
package org.apache.poi.hpbf.model;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.poifs.filesystem.DirectoryNode;

/**
 * The main Contents. Not yet understood
 */
public class MainContents extends HPBFPart {
	public MainContents(DirectoryNode baseDir)
			throws FileNotFoundException, IOException {
		super(baseDir);
	}

	public String[] getPath() {
		return new String[] { "Contents" };
	}

	protected void generateData() {
		// We don't parse the contents, so
		//  nothing will have changed
	}
}
