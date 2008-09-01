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

package org.apache.poi.hssf.record.formula;

import org.apache.poi.util.LittleEndian;
import org.apache.poi.util.BitField;
import org.apache.poi.util.BitFieldFactory;

import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.record.RecordInputStream;

/**
 * ReferencePtgBase - handles references (such as A1, A2, IA4)
 * @author  Andrew C. Oliver (acoliver@apache.org)
 * @author Jason Height (jheight at chariot dot net dot au)
 */
public abstract class RefPtgBase extends OperandPtg {

    private final static int SIZE = 5;
    private final static int MAX_ROW_NUMBER = 65536;

   /** The row index - zero based unsigned 16 bit value */
    private int            field_1_row;
    /** Field 2
     * - lower 8 bits is the zero based unsigned byte column index
     * - bit 16 - isRowRelative
     * - bit 15 - isColumnRelative
     */
    private int            field_2_col;
    private static final BitField         rowRelative = BitFieldFactory.getInstance(0x8000);
    private static final BitField         colRelative = BitFieldFactory.getInstance(0x4000);
    private static final BitField         column      = BitFieldFactory.getInstance(0x00FF);

    protected RefPtgBase() {
      //Required for clone methods
    }

    /**
     * Takes in a String representation of a cell reference and fills out the
     * numeric fields.
     */
    protected RefPtgBase(String cellref) {
        CellReference c= new CellReference(cellref);
        setRow(c.getRow());
        setColumn(c.getCol());
        setColRelative(!c.isColAbsolute());
        setRowRelative(!c.isRowAbsolute());
    }

    protected RefPtgBase(int row, int column, boolean isRowRelative, boolean isColumnRelative) {
      setRow(row);
      setColumn(column);
      setRowRelative(isRowRelative);
      setColRelative(isColumnRelative);
    }

    protected RefPtgBase(RecordInputStream in) {
        field_1_row = in.readUShort();
        field_2_col = in.readUShort();
    }

    public final String toString() {
        CellReference cr = new CellReference(getRow(), getColumn(), !isRowRelative(),!isColRelative());
        StringBuffer sb = new StringBuffer();
        sb.append(getClass().getName());
        sb.append(" [");
        sb.append(cr.formatAsString());
        sb.append("]");
        return sb.toString();
    }

    public final void writeBytes(byte [] array, int offset) {
        array[offset] = (byte) (getSid() + getPtgClass());

        LittleEndian.putShort(array, offset+1, (short)field_1_row);
        LittleEndian.putShort(array, offset+3, (short)field_2_col);
    }
    
    protected abstract byte getSid();

    public final void setRow(int row) {
        if(row < 0 || row >= MAX_ROW_NUMBER) {
           throw new IllegalArgumentException("The row number, when specified as an integer, must be between 0 and " + MAX_ROW_NUMBER);
        }
        field_1_row = row;
    }

    /**
     * Returns the row number as a short, which will be
     *  wrapped (negative) for values between 32769 and 65535
     */
    public final int getRow(){
        return field_1_row;
    }
    /**
     * Returns the row number as an int, between 0 and 65535
     */
    public final int getRowAsInt() {
        return field_1_row;
    }

    public final boolean isRowRelative() {
        return rowRelative.isSet(field_2_col);
    }

    public final void setRowRelative(boolean rel) {
        field_2_col=rowRelative.setBoolean(field_2_col,rel);
    }

    public final boolean isColRelative() {
        return colRelative.isSet(field_2_col);
    }

    public final void setColRelative(boolean rel) {
        field_2_col=colRelative.setBoolean(field_2_col,rel);
    }

    public final void setColumn(int col) {
        if(col < 0 || col >= 0x100) {
            throw new IllegalArgumentException("Specified colIx (" + col + ") is out of range");
        }
    	field_2_col = column.setValue(field_2_col, col);
    }

    public final int getColumn() {
    	return column.getValue(field_2_col);
    }

    public final int getSize() {
        return SIZE;
    }

    public final String toFormulaString(HSSFWorkbook book) {
        //TODO -- should we store a cellreference instance in this ptg?? but .. memory is an issue, i believe!
        return (new CellReference(getRowAsInt(),getColumn(),!isRowRelative(),!isColRelative())).formatAsString();
    }

    public final byte getDefaultOperandClass() {
        return Ptg.CLASS_REF;
    }
}
