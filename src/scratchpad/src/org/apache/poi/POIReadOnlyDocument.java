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
package org.apache.poi;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.poi.poifs.filesystem.DirectoryNode;
import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
import org.apache.poi.poifs.filesystem.OPOIFSFileSystem;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;


/**
 * This holds the common functionality for all read-only
 *  POI Document classes, i.e. ones which don't support writing.
 */
public abstract class POIReadOnlyDocument extends POIDocument {
    public POIReadOnlyDocument(DirectoryNode dir) {
        super(dir);
    }
    public POIReadOnlyDocument(NPOIFSFileSystem fs) {
        super(fs);
    }
    public POIReadOnlyDocument(OPOIFSFileSystem fs) {
        super(fs);
    }
    public POIReadOnlyDocument(POIFSFileSystem fs) {
        super(fs);
    }

    /**
     * Note - writing is not yet supported for this file format, sorry.
     */
    @Override
    public void write() throws IOException {
        throw new IllegalStateException("Writing is not yet implemented for this Document Format");
    }
    /**
     * Note - writing is not yet supported for this file format, sorry.
     */
    @Override
    public void write(File file) throws IOException {
        throw new IllegalStateException("Writing is not yet implemented for this Document Format");
    }
    /**
     * Note - writing is not yet supported for this file format, sorry.
     */
    @Override
    public void write(OutputStream out) throws IOException {
        throw new IllegalStateException("Writing is not yet implemented for this Document Format");
    }
}
