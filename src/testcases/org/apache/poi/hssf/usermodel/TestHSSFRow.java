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

package org.apache.poi.hssf.usermodel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import junit.framework.TestCase;

/**
 * Test HSSFRow is okay.
 *
 * @author Glen Stampoultzis (glens at apache.org)
 */
public final class TestHSSFRow extends TestCase {

    public void testLastAndFirstColumns() {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();
        HSSFRow row = sheet.createRow((short) 0);
        assertEquals(-1, row.getFirstCellNum());
        assertEquals(-1, row.getLastCellNum());

        row.createCell((short) 2);
        assertEquals(2, row.getFirstCellNum());
        assertEquals(3, row.getLastCellNum());

        row.createCell((short) 1);
        assertEquals(1, row.getFirstCellNum());
        assertEquals(3, row.getLastCellNum());

        // check the exact case reported in 'bug' 43901 - notice that the cellNum is '0' based
        row.createCell((short) 3);
        assertEquals(1, row.getFirstCellNum());
        assertEquals(4, row.getLastCellNum());
    }

    public void testRemoveCell() throws Exception {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();
        HSSFRow row = sheet.createRow((short) 0);
        assertEquals(-1, row.getLastCellNum());
        assertEquals(-1, row.getFirstCellNum());
        row.createCell((short) 1);
        assertEquals(2, row.getLastCellNum());
        assertEquals(1, row.getFirstCellNum());
        row.createCell((short) 3);
        assertEquals(4, row.getLastCellNum());
        assertEquals(1, row.getFirstCellNum());
        row.removeCell(row.getCell((short) 3));
        assertEquals(2, row.getLastCellNum());
        assertEquals(1, row.getFirstCellNum());
        row.removeCell(row.getCell((short) 1));
        assertEquals(-1, row.getLastCellNum());
        assertEquals(-1, row.getFirstCellNum());

        // all cells on this row have been removed
        // so check the row record actually writes it out as 0's
        byte[] data = new byte[100];
        row.getRowRecord().serialize(0, data);
        assertEquals(0, data[6]);
        assertEquals(0, data[8]);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        baos.close();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(baos.toByteArray());
        workbook = new HSSFWorkbook(inputStream);
        sheet = workbook.getSheetAt(0);
        inputStream.close();

        assertEquals(-1, sheet.getRow((short) 0).getLastCellNum());
        assertEquals(-1, sheet.getRow((short) 0).getFirstCellNum());
    }

    public void testMoveCell() {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();
        HSSFRow row = sheet.createRow((short) 0);
        HSSFRow rowB = sheet.createRow((short) 1);

        HSSFCell cellA2 = rowB.createCell((short)0);
        assertEquals(0, rowB.getFirstCellNum());
        assertEquals(0, rowB.getFirstCellNum());

        assertEquals(-1, row.getLastCellNum());
        assertEquals(-1, row.getFirstCellNum());
        HSSFCell cellB2 = row.createCell((short) 1);
        HSSFCell cellB3 = row.createCell((short) 2);
        HSSFCell cellB4 = row.createCell((short) 3);

        assertEquals(1, row.getFirstCellNum());
        assertEquals(4, row.getLastCellNum());

        // Try to move to somewhere else that's used
        try {
            row.moveCell(cellB2, (short)3);
            fail("IllegalArgumentException should have been thrown");
        } catch(IllegalArgumentException e) {
            // expected during successful test
        }

        // Try to move one off a different row
        try {
            row.moveCell(cellA2, (short)3);
            fail("IllegalArgumentException should have been thrown");
        } catch(IllegalArgumentException e) {
            // expected during successful test
        }

        // Move somewhere spare
        assertNotNull(row.getCell((short)1));
        row.moveCell(cellB2, (short)5);
        assertNull(row.getCell((short)1));
        assertNotNull(row.getCell((short)5));

        assertEquals(5, cellB2.getCellNum());
        assertEquals(2, row.getFirstCellNum());
        assertEquals(6, row.getLastCellNum());
    }

    public void testRowBounds() {
      HSSFWorkbook workbook = new HSSFWorkbook();
      HSSFSheet sheet = workbook.createSheet();
      //Test low row bound
      sheet.createRow( (short) 0);
      //Test low row bound exception
      try {
        sheet.createRow(-1);
        fail("IndexOutOfBoundsException should have been thrown");
      } catch (IndexOutOfBoundsException ex) {
        // expected during successful test
      }

      //Test high row bound
      sheet.createRow(65535);
      //Test high row bound exception
      try {
        sheet.createRow(65536);
        fail("IndexOutOfBoundsException should have been thrown");
      } catch (IndexOutOfBoundsException ex) {
        // expected during successful test
      }
    }

    /**
     * Prior to patch 43901, POI was producing files with the wrong last-column
     * number on the row
     */
    public void testLastCellNumIsCorrectAfterAddCell_bug43901(){
        HSSFWorkbook book = new HSSFWorkbook();
        HSSFSheet sheet = book.createSheet("test");
        HSSFRow row = sheet.createRow(0);

        // New row has last col -1
        assertEquals(-1, row.getLastCellNum());
        if(row.getLastCellNum() == 0) {
            fail("Identified bug 43901");
        }

        // Create two cells, will return one higher
        //  than that for the last number
        row.createCell((short) 0);
        assertEquals(1, row.getLastCellNum());
        row.createCell((short) 255);
        assertEquals(256, row.getLastCellNum());
    }
}
