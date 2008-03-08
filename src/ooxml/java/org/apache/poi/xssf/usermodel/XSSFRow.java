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
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCell;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow;


public class XSSFRow implements Row {

    private CTRow row;
    
    private List<Cell> cells;
    
    private XSSFSheet sheet;
    
    /**
     * Create a new XSSFRow. This method is protected to be used only by
     * tests.
     */
    protected XSSFRow(XSSFSheet sheet) {
        this(CTRow.Factory.newInstance(), sheet);
    }
    
    /**
     * Create a new XSSFRow.
     * 
     * @param row The underlying XMLBeans row.
     * @param sheet The parent sheet.
     */
    public XSSFRow(CTRow row, XSSFSheet sheet) {
        this.row = row;
        this.sheet = sheet;
        this.cells = new LinkedList<Cell>(); 
        for (CTCell c : row.getCArray()) {
            this.cells.add(new XSSFCell(this, c));
        }
        
    }

    public XSSFSheet getSheet() {
        return this.sheet;
    }
    
    public Iterator<Cell> cellIterator() {
        return cells.iterator();
    }
    /**
     * Alias for {@link #cellIterator()} to allow
     *  foreach loops
     */
    public Iterator<Cell> iterator() {
    	return cellIterator();
    }

    public int compareTo(Object obj) {
        // TODO Auto-generated method stub
        return 0;
    }

    public Cell createCell(short column) {
    	return createCell(column, Cell.CELL_TYPE_BLANK);
    }

    /**
     * Add a new empty cell to this row.
     * 
     * @param column Cell column number.
     * @param index Position where to insert cell.
     * @param type TODO
     * @return The new cell.
     */
    protected XSSFCell addCell(short column, int index, int type) {
        CTCell ctcell = row.insertNewC(index);
        XSSFCell xcell = new XSSFCell(this, ctcell);
        xcell.setCellNum(column);
        if (type != Cell.CELL_TYPE_BLANK) {
        	xcell.setCellType(type);
        }
        return xcell;
    }

    public Cell createCell(short column, int type) {
        int index = 0;
        for (Cell c : this.cells) {
            if (c.getCellNum() == column) {
                // Replace c with new Cell
                XSSFCell xcell = addCell(column, index, type);
                cells.set(index, xcell);
                return xcell;
            }
            if (c.getCellNum() > column) {
                XSSFCell xcell = addCell(column, index, type);
                cells.add(index, xcell);
                return xcell;
            }
            ++index;
        }
        XSSFCell xcell = addCell(column, index, type);
        cells.add(xcell);
        return xcell;
    }

    public Cell getCell(int cellnum) {
        Iterator<Cell> it = cellIterator();
        for ( ; it.hasNext() ; ) {
        	Cell cell = it.next();
        	if (cell.getCellNum() == cellnum) {
        		return cell; 
        	}
        }
        return null;
    }

    public short getFirstCellNum() {
    	for (Iterator<Cell> it = cellIterator() ; it.hasNext() ; ) {
    		Cell cell = it.next();
    		if (cell != null) {
    			return cell.getCellNum();
    		}
    	}
    	return -1;
    }

    public short getHeight() {
    	if (this.row.getHt() > 0) {
    		return (short) (this.row.getHt() * 20);
    	}
        return -1;
    }

    public float getHeightInPoints() {
    	if (this.row.getHt() > 0) {
    		return (short) this.row.getHt();
    	}
        return -1;
    }

    public short getLastCellNum() {
    	short lastCellNum = -1;
    	for (Iterator<Cell> it = cellIterator() ; it.hasNext() ; ) {
    		Cell cell = it.next();
    		if (cell != null) {
    			lastCellNum = cell.getCellNum();
    		}
    	}
    	return lastCellNum;
    }

    public int getPhysicalNumberOfCells() {
    	int count = 0;
    	for (Iterator<Cell> it = cellIterator() ; it.hasNext() ; ) {
    		if (it.next() != null) {
    			count++;
    		}
    	}
    	return count;
    }

    public int getRowNum() {
        return (int) (row.getR() - 1);
    }

    public boolean getZeroHeight() {
    	return this.row.getHidden();
    }

    public void removeCell(Cell cell) {
    	int counter = 0;
    	for (Iterator<Cell> it = cellIterator(); it.hasNext(); ) {
    		Cell c = it.next();
    		if (c.getCellNum() == cell.getCellNum()) {
    			it.remove();
    			row.removeC(counter);
    			continue;
    		}
    		counter++;
    	}
    }

    public void setHeight(short height) {
    	this.row.setHt((double) height / 20);
    }

    public void setHeightInPoints(float height) {
    	this.row.setHt((double) height);
    }

    public void setRowNum(int rowNum) {
        this.row.setR(rowNum + 1);

    }

    public void setZeroHeight(boolean height) {
    	this.row.setHidden(height);

    }

}
