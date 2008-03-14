/*
* Licensed to the Apache Software Foundation (ASF) under one or more
* contributor license agreements.  See the NOTICE file distributed with
* this work for additional information regarding copyright ownership.
* The ASF licenses this file to You under the Apache License, Version 2.0
* (the "License"); you may not use this file except in compliance with
* the License.  You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.apache.poi.hssf.usermodel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import junit.framework.TestCase;

/**
 * Test <code>HSSFTextbox</code>.
 *
 * @author Yegor Kozlov (yegor at apache.org)
 */
public final class TestHSSFTextbox extends TestCase{

    /**
     * Test that accessors to horizontal and vertical alignment work properly
     */
    public void testAlignment() {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sh1 = wb.createSheet();
        HSSFPatriarch patriarch = sh1.createDrawingPatriarch();

        HSSFTextbox textbox = patriarch.createTextbox(new HSSFClientAnchor(0, 0, 0, 0, (short) 1, 1, (short) 6, 4));
        HSSFRichTextString str = new HSSFRichTextString("Hello, World");
        textbox.setString(str);
        textbox.setHorizontalAlignment(HSSFTextbox.HORIZONTAL_ALIGNMENT_CENTERED);
        textbox.setVerticalAlignment(HSSFTextbox.VERTICAL_ALIGNMENT_CENTER);

        assertEquals(HSSFTextbox.HORIZONTAL_ALIGNMENT_CENTERED, textbox.getHorizontalAlignment());
        assertEquals(HSSFTextbox.VERTICAL_ALIGNMENT_CENTER, textbox.getVerticalAlignment());
    }

 }
