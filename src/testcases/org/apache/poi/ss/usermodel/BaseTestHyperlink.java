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

package org.apache.poi.ss.usermodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;

import java.util.List;

import org.junit.Test;

import org.apache.poi.ss.ITestDataProvider;

/**
 * Test diffrent types of Excel hyperlinks
 *
 * @author Yegor Kozlov
 */
public abstract class BaseTestHyperlink {

    private final ITestDataProvider _testDataProvider;

    protected BaseTestHyperlink(ITestDataProvider testDataProvider) {
        _testDataProvider = testDataProvider;
    }

    @Test
    public final void testBasicTypes(){
        Workbook wb = _testDataProvider.createWorkbook();
        CreationHelper createHelper = wb.getCreationHelper();

        Cell cell;
        Hyperlink link;
        Sheet sheet = wb.createSheet("Hyperlinks");

        //URL
        cell = sheet.createRow(0).createCell((short) 0);
        cell.setCellValue("URL Link");
        link = createHelper.createHyperlink(Hyperlink.LINK_URL);
        link.setAddress("http://poi.apache.org/");
        cell.setHyperlink(link);

        //link to a file in the current directory
        cell = sheet.createRow(1).createCell((short) 0);
        cell.setCellValue("File Link");
        link = createHelper.createHyperlink(Hyperlink.LINK_FILE);
        link.setAddress("hyperinks-beta4-dump.txt");
        cell.setHyperlink(link);

        //e-mail link
        cell = sheet.createRow(2).createCell((short) 0);
        cell.setCellValue("Email Link");
        link = createHelper.createHyperlink(Hyperlink.LINK_EMAIL);
        //note, if subject contains white spaces, make sure they are url-encoded
        link.setAddress("mailto:poi@apache.org?subject=Hyperlinks");
        cell.setHyperlink(link);

        //link to a place in this workbook

        //create a target sheet and cell
        Sheet sheet2 = wb.createSheet("Target Sheet");
        sheet2.createRow(0).createCell((short) 0).setCellValue("Target Cell");

        cell = sheet.createRow(3).createCell((short) 0);
        cell.setCellValue("Worksheet Link");
        link = createHelper.createHyperlink(Hyperlink.LINK_DOCUMENT);
        link.setAddress("'Target Sheet'!A1");
        cell.setHyperlink(link);

        wb = _testDataProvider.writeOutAndReadBack(wb);

        sheet = wb.getSheetAt(0);
        link = sheet.getRow(0).getCell(0).getHyperlink();

        assertEquals("http://poi.apache.org/", link.getAddress());
        link = sheet.getRow(1).getCell(0).getHyperlink();
        assertEquals("hyperinks-beta4-dump.txt", link.getAddress());
        link = sheet.getRow(2).getCell(0).getHyperlink();
        assertEquals("mailto:poi@apache.org?subject=Hyperlinks", link.getAddress());
        link = sheet.getRow(3).getCell(0).getHyperlink();
        assertEquals("'Target Sheet'!A1", link.getAddress());
    }
    
    // copy a hyperlink via the copy constructor
    @Test
    public void testCopyHyperlink() {
        final Workbook wb = _testDataProvider.createWorkbook();
        final CreationHelper createHelper = wb.getCreationHelper();

        final Sheet sheet = wb.createSheet("Hyperlinks");
        final Row row = sheet.createRow(0);
        final Cell cell1, cell2;
        final Hyperlink link1, link2;

        //URL
        cell1 = row.createCell(0);
        cell2 = row.createCell(1);
        cell1.setCellValue("URL Link");
        link1 = createHelper.createHyperlink(Hyperlink.LINK_URL);
        link1.setAddress("http://poi.apache.org/");
        cell1.setHyperlink(link1);
        
        link2 = copyHyperlink(link1);
        
        // Change address (type is not changeable)
        link2.setAddress("http://apache.org/");
        cell2.setHyperlink(link2);
        
        // Make sure hyperlinks were deep-copied, and modifying one does not modify the other. 
        assertNotSame(link1, link2);
        assertNotEquals(link1, link2);
        assertEquals("http://poi.apache.org/", link1.getAddress());
        assertEquals("http://apache.org/", link2.getAddress());
        assertEquals(link1, cell1.getHyperlink());
        assertEquals(link2, cell2.getHyperlink());
        
        // Make sure both hyperlinks were added to the sheet
        @SuppressWarnings("unchecked")
        final List<Hyperlink> actualHyperlinks = (List<Hyperlink>) sheet.getHyperlinkList();
        assertEquals(2, actualHyperlinks.size());
        assertEquals(link1, actualHyperlinks.get(0));
        assertEquals(link2, actualHyperlinks.get(1));
    }
    
    public abstract Hyperlink copyHyperlink(Hyperlink link);
}
