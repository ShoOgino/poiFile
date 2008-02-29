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

import java.util.Iterator;

import junit.framework.TestCase;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.helpers.ColumnHelper;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCol;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDialogsheet;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheet;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorksheet;


public class TestXSSFSheet extends TestCase {
    
    public void testRowIterator() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet 1");
        Row row1 = sheet.createRow(0);
        Row row2 = sheet.createRow(1);
        Iterator<Row> it = sheet.rowIterator();
        assertNotNull(it);
        assertTrue(it.hasNext());
        assertEquals(row1, it.next());
        assertTrue(it.hasNext());
        assertEquals(row2, it.next());
        assertFalse(it.hasNext());
    }
    
    public void testGetRow() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet 1");
        Row row1 = sheet.createRow(0);
        Cell cell = row1.createCell((short) 0);
        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
        cell.setCellValue((double) 1000);
        
        // Test getting a row and check its cell's value
        Row row_got = sheet.getRow(0);
        Cell cell_got = row_got.getCell((short) 0);
        assertEquals((double) 1000, cell_got.getNumericCellValue());
    }
    
    public void testCreateRow() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet 1");
        
        // Test row creation with consecutive indexes
        Row row1 = sheet.createRow(0);
        Row row2 = sheet.createRow(1);
        assertEquals(0, row1.getRowNum());
        assertEquals(1, row2.getRowNum());
        Iterator<Row> it = sheet.rowIterator();
        assertTrue(it.hasNext());
        assertEquals(row1, it.next());
        assertTrue(it.hasNext());
        assertEquals(row2, it.next());
        
        // Test row creation with non consecutive index
        Row row101 = sheet.createRow(100);
        assertNotNull(row101);
        
        // Test overwriting an existing row
        Row row2_ovrewritten = sheet.createRow(1);
        Cell cell = row2_ovrewritten.createCell((short) 0);
        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
        cell.setCellValue((double) 100);
        Iterator<Row> it2 = sheet.rowIterator();
        assertTrue(it2.hasNext());
        assertEquals(row1, it2.next());
        assertTrue(it2.hasNext());
        Row row2_overwritten_copy = it2.next();
        assertEquals(row2_ovrewritten, row2_overwritten_copy);
        assertEquals(row2_overwritten_copy.getCell((short) 0).getNumericCellValue(), (double) 100);
    }
    
    public void testRemoveRow() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet 1");
        Row row1 = sheet.createRow(1);
        Row row2 = sheet.createRow(2);
        assertNotNull(sheet.getRow(1));
        sheet.removeRow(row2);
        assertNull(sheet.getRow(0));
        assertNull(sheet.getRow(2));
        assertNotNull(sheet.getRow(1));
    }
    
    public void testGetSetDefaultRowHeight() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet 1");
        // Test that default height set by the constructor
        assertEquals((short) 0, sheet.getDefaultRowHeight());
        assertEquals((float) 0, sheet.getDefaultRowHeightInPoints());
        // Set a new default row height in twips and test getting the value in points
        sheet.setDefaultRowHeight((short) 360);
        assertEquals((float) 18, sheet.getDefaultRowHeightInPoints());
        // Set a new default row height in points and test getting the value in twips
        sheet.setDefaultRowHeightInPoints((short) 17);
        assertEquals((short) 340, sheet.getDefaultRowHeight());
    }
    
    public void testGetSetDefaultColumnWidth() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet 1");
        // Test that default column width set by the constructor
        assertEquals((short) 0, sheet.getDefaultColumnWidth());
        // Set a new default column width and get its value
        sheet.setDefaultColumnWidth((short) 14);
        assertEquals((short) 14, sheet.getDefaultColumnWidth());
    }
    
    public void testGetFirstLastRowNum() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet 1");
        Row row10 = sheet.createRow(9);
        Row row1 = sheet.createRow(0);
        Row row2 = sheet.createRow(1);
        assertEquals(0, sheet.getFirstRowNum());
        assertEquals(9, sheet.getLastRowNum());    
    }
    
    public void testGetPhysicalNumberOfRows() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet 1");
        Row row10 = sheet.createRow(9);
        Row row1 = sheet.createRow(0);
        Row row2 = sheet.createRow(1);
        assertEquals(3, sheet.getPhysicalNumberOfRows());
    }
    
    public void testGetSetRowBreaks() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet 1");
        assertNull(sheet.getRowBreaks());
        sheet.setRowBreak(1);
        sheet.setRowBreak(15);
        assertNotNull(sheet.getRowBreaks());
        assertEquals(1, sheet.getRowBreaks()[0]);
        assertEquals(15, sheet.getRowBreaks()[1]);
        sheet.setRowBreak(1);
        assertEquals(2, sheet.getRowBreaks().length);
    }
    
    public void testRemoveRowBreak() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet 1");
        sheet.setRowBreak(1);
        assertEquals(1, sheet.getRowBreaks().length);
        sheet.setRowBreak(2);
        assertEquals(2, sheet.getRowBreaks().length);
        sheet.removeRowBreak(1);
        assertEquals(1, sheet.getRowBreaks().length);
    }
    
    public void testGetSetColumnBreaks() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet 1");
        assertNull(sheet.getColumnBreaks());
        sheet.setColumnBreak((short) 11);
        assertNotNull(sheet.getColumnBreaks());
        assertEquals(11, sheet.getColumnBreaks()[0]);
        sheet.setColumnBreak((short) 11223);
        assertEquals(2, sheet.getColumnBreaks().length);
    }
    
    public void testRemoveColumnBreak() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet 1");
        assertNull(sheet.getColumnBreaks());
        sheet.setColumnBreak((short) 11);
        assertNotNull(sheet.getColumnBreaks());
        sheet.setColumnBreak((short) 12);
        assertEquals(2, sheet.getColumnBreaks().length);
        sheet.removeColumnBreak((short) 11);
        assertEquals(1, sheet.getColumnBreaks().length);
        sheet.removeColumnBreak((short) 15);
        assertEquals(1, sheet.getColumnBreaks().length);
    }
    
    public void testIsRowColumnBroken() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet 1");
        assertFalse(sheet.isRowBroken(0));
        sheet.setRowBreak(3);
        assertTrue(sheet.isRowBroken(3));
        assertFalse(sheet.isColumnBroken((short) 0));
        sheet.setColumnBreak((short) 3);
        assertTrue(sheet.isColumnBroken((short) 3));
    }
    
    public void testGetSetAutoBreaks() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet 1");
        assertTrue(sheet.getAutobreaks());
        sheet.setAutobreaks(false);
        assertFalse(sheet.getAutobreaks());
    }
    
    public void testIsSetFitToPage() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet 1");
        assertFalse(sheet.getFitToPage());
        sheet.setFitToPage(true);
        assertTrue(sheet.getFitToPage());
        sheet.setFitToPage(false);
        assertFalse(sheet.getFitToPage());
    }
    
    public void testGetSetMargin() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet 1");
        assertEquals((double) 0, sheet.getMargin((short) 0));
        sheet.setMargin((short) 0, 10);
        assertEquals((double) 10, sheet.getMargin((short) 0));
        assertEquals((double) 10, sheet.getMargin((short) 1));
        assertEquals((double) 10, sheet.getMargin((short) 2));
        assertEquals((double) 10, sheet.getMargin((short) 3));
        assertEquals((double) 10, sheet.getMargin((short) 4));
        assertEquals((double) 10, sheet.getMargin((short) 5));
        sheet.setMargin((short) 1, 11);
        assertEquals((double) 11, sheet.getMargin((short) 1));
        assertEquals((double) 11, sheet.getMargin((short) 2));
        assertEquals((double) 11, sheet.getMargin((short) 3));
        assertEquals((double) 11, sheet.getMargin((short) 4));
        assertEquals((double) 11, sheet.getMargin((short) 5));
        sheet.setMargin((short) 2, 12);
        assertEquals((double) 12, sheet.getMargin((short) 2));
        assertEquals((double) 12, sheet.getMargin((short) 3));
        assertEquals((double) 12, sheet.getMargin((short) 4));
        assertEquals((double) 12, sheet.getMargin((short) 5));
        sheet.setMargin((short) 3, 13);
        assertEquals((double) 13, sheet.getMargin((short) 3));
        assertEquals((double) 13, sheet.getMargin((short) 4));
        assertEquals((double) 13, sheet.getMargin((short) 5));
        sheet.setMargin((short) 4, 14);
        assertEquals((double) 14, sheet.getMargin((short) 4));
        assertEquals((double) 14, sheet.getMargin((short) 5));
        sheet.setMargin((short) 5, 15);
        assertEquals((double) 15, sheet.getMargin((short) 5));
        
        // Test that nothing happens if another margin constant is given (E.G. 65)
        sheet.setMargin((short) 65, 15);
        assertEquals((double) 10, sheet.getMargin((short) 0));
        assertEquals((double) 11, sheet.getMargin((short) 1));
        assertEquals((double) 12, sheet.getMargin((short) 2));
        assertEquals((double) 13, sheet.getMargin((short) 3));
        assertEquals((double) 14, sheet.getMargin((short) 4));
        assertEquals((double) 15, sheet.getMargin((short) 5));
    }
    
    public void testGetFooter() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet 1");
        assertNotNull(sheet.getFooter());
        sheet.getFooter().setCenter("test center footer");
        assertEquals("test center footer", sheet.getFooter().getCenter());
    }
    
    public void testGetAllHeadersFooters() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = (XSSFSheet) workbook.createSheet("Sheet 1");
        assertNotNull(sheet.getOddFooter());
        assertNotNull(sheet.getEvenFooter());
        assertNotNull(sheet.getFirstFooter());
        assertNotNull(sheet.getOddHeader());
        assertNotNull(sheet.getEvenHeader());
        assertNotNull(sheet.getFirstHeader());
        
        assertEquals("", sheet.getOddFooter().getLeft());
        sheet.getOddFooter().setLeft("odd footer left");
        assertEquals("odd footer left", sheet.getOddFooter().getLeft());
        
        assertEquals("", sheet.getEvenFooter().getLeft());
        sheet.getEvenFooter().setLeft("even footer left");
        assertEquals("even footer left", sheet.getEvenFooter().getLeft());
        
        assertEquals("", sheet.getFirstFooter().getLeft());
        sheet.getFirstFooter().setLeft("first footer left");
        assertEquals("first footer left", sheet.getFirstFooter().getLeft());
        
        assertEquals("", sheet.getOddHeader().getLeft());
        sheet.getOddHeader().setLeft("odd header left");
        assertEquals("odd header left", sheet.getOddHeader().getLeft());
        
        assertEquals("", sheet.getOddHeader().getRight());
        sheet.getOddHeader().setRight("odd header right");
        assertEquals("odd header right", sheet.getOddHeader().getRight());
        
        assertEquals("", sheet.getOddHeader().getCenter());
        sheet.getOddHeader().setCenter("odd header center");
        assertEquals("odd header center", sheet.getOddHeader().getCenter());

    }
    
    public void testGetSetColumnWidth() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet 1");
        sheet.setColumnWidth((short) 1,(short)  22);
        assertEquals(22, sheet.getColumnWidth((short) 1));
    }
    
    public void testGetSetColumnHidden() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet 1");
        sheet.setColumnHidden((short) 2, true);
        assertTrue(sheet.isColumnHidden((short) 2));
    }
    
    public void testAutoSizeColumn() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = (XSSFSheet) workbook.createSheet("Sheet 1");
        ColumnHelper columnHelper = sheet.getColumnHelper();
        CTCol col = columnHelper.getColumn(13);
        assertNull(col);
        sheet.autoSizeColumn((short)13);
        col = columnHelper.getColumn(13);
        assertNotNull(col);
        assertTrue(col.getBestFit());	
    }
    
    public void testGetDialog() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = (XSSFSheet) workbook.createSheet("Sheet 1");
        assertFalse(sheet.getDialog());
        XSSFSheet dialogsheet = (XSSFSheet) workbook.createDialogsheet("Dialogsheet 1", null);
        assertTrue(dialogsheet.getDialog());
    	
    }
    
    public void testGetSetHorizontallyCentered() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = (XSSFSheet) workbook.createSheet("Sheet 1");
        assertFalse(sheet.getHorizontallyCenter());
        sheet.setHorizontallyCenter(true);
        assertTrue(sheet.getHorizontallyCenter());
        sheet.setHorizontallyCenter(false);
        assertFalse(sheet.getHorizontallyCenter());
    }
    
    public void testGetSetVerticallyCentered() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = (XSSFSheet) workbook.createSheet("Sheet 1");
        assertFalse(sheet.getVerticallyCenter());
        sheet.setVerticallyCenter(true);
        assertTrue(sheet.getVerticallyCenter());
        sheet.setVerticallyCenter(false);
        assertFalse(sheet.getVerticallyCenter());
    }
    
    public void testIsSetPrintGridlines() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = (XSSFSheet) workbook.createSheet("Sheet 1");
        assertFalse(sheet.isPrintGridlines());
        sheet.setPrintGridlines(true);
        assertTrue(sheet.isPrintGridlines());
    }
    
    public void testIsSetDisplayFormulas() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = (XSSFSheet) workbook.createSheet("Sheet 1");
        assertFalse(sheet.isDisplayFormulas());
        sheet.setDisplayFormulas(true);
        assertTrue(sheet.isDisplayFormulas());
    }
    
    public void testIsSetDisplayGridLines() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = (XSSFSheet) workbook.createSheet("Sheet 1");
        assertTrue(sheet.isDisplayGridlines());
        sheet.setDisplayGridlines(false);
        assertFalse(sheet.isDisplayGridlines());
    }
    
    public void testIsSetDisplayRowColHeadings() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = (XSSFSheet) workbook.createSheet("Sheet 1");
        assertTrue(sheet.isDisplayRowColHeadings());
        sheet.setDisplayRowColHeadings(false);
        assertFalse(sheet.isDisplayRowColHeadings());
    }
    
    public void testGetScenarioProtect() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = (XSSFSheet) workbook.createSheet("Sheet 1");
        assertFalse(sheet.getScenarioProtect());
    }
    
    public void testTopRowLeftCol() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = (XSSFSheet) workbook.createSheet("Sheet 1");
        sheet.showInPane((short)1, (short)1);
        assertEquals((short) 1, sheet.getTopRow());
        assertEquals((short) 1, sheet.getLeftCol());
        sheet.showInPane((short)2, (short)26);
        assertEquals((short) 2, sheet.getTopRow());
        assertEquals((short) 26, sheet.getLeftCol());
    }
}
