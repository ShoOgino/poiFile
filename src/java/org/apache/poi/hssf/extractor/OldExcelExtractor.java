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

package org.apache.poi.hssf.extractor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hssf.record.LabelRecord;
import org.apache.poi.hssf.record.OldLabelRecord;
import org.apache.poi.hssf.record.RecordInputStream;

/**
 * A text extractor for very old (pre-OLE2) Excel files,
 *  such as Excel 4 files.
 * <p>
 * Returns much (but not all) of the textual content of the file, 
 *  suitable for indexing by something like Apache Lucene, or used
 *  by Apache Tika, but not really intended for display to the user.
 * </p>
 */
public class OldExcelExtractor {
    private InputStream input;
    private boolean _includeSheetNames = true;

    public OldExcelExtractor(InputStream input) {
        this.input = input;
    }
    public OldExcelExtractor(File f) throws IOException {
        this.input = new FileInputStream(f);
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.err.println("Use:");
            System.err.println("   OldExcelExtractor <filename>");
            System.exit(1);
        }
        OldExcelExtractor extractor = new OldExcelExtractor(new File(args[0]));
        System.out.println(extractor.getText());
    }

    /**
     * Should sheet names be included? Default is true
     */
    public void setIncludeSheetNames(boolean includeSheetNames) {
        _includeSheetNames = includeSheetNames;
    }

    /**
     * Retrieves the text contents of the file, as best we can
     *  for these old file formats
     */
    public String getText() {
        StringBuffer text = new StringBuffer();

        RecordInputStream ris = new RecordInputStream(input);
        while (ris.hasNextRecord()) {
            int sid = ris.getNextSid();
            ris.nextRecord();

            switch (sid) {
                case LabelRecord.sid:
                    OldLabelRecord lr = new OldLabelRecord(ris);
                    text.append(lr.getValue());
                    text.append('\n');
                    break;
                default:
                    ris.readFully(new byte[ris.remaining()]);
            }

            // label - 5.63 - TODO Needs codepages
            // number - 5.71
            // rk - 5.87
            // string - 5.102

        }

        return text.toString();
    }
}
