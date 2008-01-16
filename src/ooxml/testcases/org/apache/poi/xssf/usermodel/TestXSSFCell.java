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

package org.apache.poi.xssf.usermodel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.SharedStringSource;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCell;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STCellType;


public class TestXSSFCell extends TestCase {
    
    /**
     * Test setting and getting boolean values.
     */
    public void testSetGetBoolean() throws Exception {
        XSSFCell cell = new XSSFCell(new XSSFRow());
        cell.setCellValue(true);
        assertEquals(Cell.CELL_TYPE_BOOLEAN, cell.getCellType());
        assertTrue(cell.getBooleanCellValue());
        cell.setCellValue(false);
        assertFalse(cell.getBooleanCellValue());
        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
        try {
            cell.getBooleanCellValue();
            fail("Exception expected");
        } catch (NumberFormatException e) {
            // success
        }
    }
    
    /**
     * Test setting and getting numeric values.
     */
    public void testSetGetNumeric() throws Exception {
        XSSFCell cell = new XSSFCell(new XSSFRow());
        cell.setCellValue(10d);
        assertEquals(Cell.CELL_TYPE_NUMERIC, cell.getCellType());
        assertEquals(10d, cell.getNumericCellValue());
        cell.setCellValue(-23.76);
        assertEquals(-23.76, cell.getNumericCellValue());        
    }
    
    /**
     * Test setting and getting numeric values.
     */
    public void testSetGetDate() throws Exception {
        XSSFCell cell = new XSSFCell(new XSSFRow());
        Date now = new Date();
        cell.setCellValue(now);
        assertEquals(Cell.CELL_TYPE_NUMERIC, cell.getCellType());
        assertEquals(now, cell.getDateCellValue());
        
        // Test case for 1904 hack
        Calendar cal = Calendar.getInstance();
        cal.set(1903, 1, 8);
        Date before1904 = cal.getTime();
        cell.setCellValue(before1904);
        assertEquals(before1904, cell.getDateCellValue());
        
        cell.setCellType(Cell.CELL_TYPE_BOOLEAN);        
        try {
            cell.getDateCellValue();
            fail("Exception expected");
        } catch (NumberFormatException e) {
            // success
        }
    }
    
    public void testSetGetError() throws Exception {
        XSSFCell cell = new XSSFCell(new XSSFRow());
        cell.setCellErrorValue((byte)255);
        assertEquals(Cell.CELL_TYPE_ERROR, cell.getCellType());

        assertEquals((byte)255, cell.getErrorCellValue());
    }
    
    public void testSetGetFormula() throws Exception {
        XSSFCell cell = new XSSFCell(new XSSFRow());
        String formula = "SQRT(C2^2+D2^2)";
        
        cell.setCellFormula(formula);
        assertEquals(Cell.CELL_TYPE_FORMULA, cell.getCellType());
        assertEquals(formula, cell.getCellFormula());
        
        assertTrue( Double.isNaN( cell.getNumericCellValue() ));
    }
    
    public void testSetGetStringInline() throws Exception {
        CTCell rawCell = CTCell.Factory.newInstance();
        XSSFCell cell = new XSSFCell(new XSSFRow(), rawCell);
        cell.setSharedStringSource(new DummySharedStringSource());
        
        // Default is shared string mode, so have to do this explicitly
        rawCell.setT(STCellType.INLINE_STR);
        
        assertEquals(STCellType.INT_INLINE_STR, rawCell.getT().intValue());
        assertEquals(Cell.CELL_TYPE_STRING, cell.getCellType());
        assertEquals("", cell.getRichStringCellValue().getString());
        
        cell.setCellValue(new XSSFRichTextString("Foo"));
        assertEquals(STCellType.INT_INLINE_STR, rawCell.getT().intValue());
        assertEquals(Cell.CELL_TYPE_STRING, cell.getCellType());
        assertEquals("Foo", cell.getRichStringCellValue().getString());
        
        // To number and back to string, stops being inline
        cell.setCellValue(1.4);
        cell.setCellValue(new XSSFRichTextString("Foo2"));
        assertEquals(STCellType.INT_S, rawCell.getT().intValue());
        assertEquals(Cell.CELL_TYPE_STRING, cell.getCellType());
        assertEquals("Foo2", cell.getRichStringCellValue().getString());
    }
    
    public void testSetGetStringShared() throws Exception {
        XSSFCell cell = new XSSFCell(new XSSFRow());
        cell.setSharedStringSource(new DummySharedStringSource());

        cell.setCellValue(new XSSFRichTextString(""));
        assertEquals(Cell.CELL_TYPE_STRING, cell.getCellType());
        assertEquals("", cell.getRichStringCellValue().getString());

        cell.setCellValue(new XSSFRichTextString("Foo"));
        assertEquals(Cell.CELL_TYPE_STRING, cell.getCellType());
        assertEquals("Foo", cell.getRichStringCellValue().getString());
    }
    
    /**
     * Test that empty cells (no v element) return default values.
     */
    public void testGetEmptyCellValue() throws Exception {
        XSSFCell cell = new XSSFCell(new XSSFRow());
        cell.setCellType(Cell.CELL_TYPE_BOOLEAN);
        assertFalse(cell.getBooleanCellValue());
        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
        assertTrue(Double.isNaN( cell.getNumericCellValue() ));
        assertNull(cell.getDateCellValue());
        cell.setCellType(Cell.CELL_TYPE_ERROR);
        assertEquals(0, cell.getErrorCellValue());
        cell.setCellType(Cell.CELL_TYPE_STRING);
        assertEquals("", cell.getRichStringCellValue().getString());
    }
    
    public static class DummySharedStringSource implements SharedStringSource {
        ArrayList<String> strs = new ArrayList<String>();
        public String getSharedStringAt(int idx) {
            return strs.get(idx);
        }

        public synchronized int putSharedString(String s) {
            if(strs.contains(s)) {
                return strs.indexOf(s);
            }
            strs.add(s);
            return strs.size() - 1;
        }
    }
}
