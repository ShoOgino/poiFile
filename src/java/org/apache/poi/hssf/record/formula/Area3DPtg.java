
/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2002 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation" and
 *    "Apache POI" must not be used to endorse or promote products
 *    derived from this software without prior written permission. For
 *    written permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 *    "Apache POI", nor may "Apache" appear in their name, without
 *    prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */

package org.apache.poi.hssf.record.formula;

import org.apache.poi.util.LittleEndian;
import org.apache.poi.hssf.util.RangeAddress;
import org.apache.poi.hssf.util.AreaReference;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.hssf.util.SheetReferences;

import org.apache.poi.hssf.model.Workbook;
import org.apache.poi.util.BitField;

/**
 * Title:        Area 3D Ptg - 3D referecnce (Sheet + Area)<P>
 * Description:  Defined a area in Extern Sheet. <P>
 * REFERENCE:  <P>
 * @author Libin Roman (Vista Portal LDT. Developer)
 * @author avik
 * @version 1.0-pre
 */

public class Area3DPtg extends Ptg
{
    public final static byte sid  = 0x3b;
    private final static int  SIZE = 11; // 10 + 1 for Ptg
    private short             field_1_index_extern_sheet;
    private short             field_2_first_row;
    private short             field_3_last_row;
    private short             field_4_first_column;
    private short             field_5_last_column;
    
    private BitField         rowRelative = new BitField(0x8000);
    private BitField         colRelative = new BitField(0x4000);

    /** Creates new AreaPtg */
    public Area3DPtg() {}
   
   public Area3DPtg(String arearef, short externIdx) {
        AreaReference ar = new AreaReference(arearef);
        
        setFirstRow((short)ar.getCells()[0].getRow());
        setFirstColumn((short)ar.getCells()[0].getCol());
        setLastRow((short)ar.getCells()[1].getRow());
        setLastColumn((short)ar.getCells()[1].getCol());
        setFirstColRelative(!ar.getCells()[0].isColAbsolute());
        setLastColRelative(!ar.getCells()[1].isColAbsolute());
        setFirstRowRelative(!ar.getCells()[0].isRowAbsolute());
        setLastRowRelative(!ar.getCells()[1].isRowAbsolute());
        setExternSheetIndex(externIdx);
        
    }
    public Area3DPtg(byte[] data, int offset)
    {
        offset++;
        field_1_index_extern_sheet = LittleEndian.getShort(data, 0 + offset);
        field_2_first_row          = LittleEndian.getShort(data, 2 + offset);
        field_3_last_row           = LittleEndian.getShort(data, 4 + offset);
        field_4_first_column       = LittleEndian.getShort(data, 6 + offset);
        field_5_last_column        = LittleEndian.getShort(data, 8 + offset);
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer();

        buffer.append("AreaPtg\n");
        buffer.append("Index to Extern Sheet = " + getExternSheetIndex()).append("\n");
        buffer.append("firstRow = " + getFirstRow()).append("\n");
        buffer.append("lastRow  = " + getLastRow()).append("\n");
        buffer.append("firstCol = " + getFirstColumn()).append("\n");
        buffer.append("lastCol  = " + getLastColumn()).append("\n");
        buffer.append("firstColRel= "
                      + isFirstRowRelative()).append("\n");
        buffer.append("lastColRowRel = "
                      + isLastRowRelative()).append("\n");
        buffer.append("firstColRel   = " + isFirstColRelative()).append("\n");
        buffer.append("lastColRel    = " + isLastColRelative()).append("\n");
        return buffer.toString();
    }

    public void writeBytes(byte [] array, int offset)
    {
        array[ 0 + offset ] = (byte) (sid + ptgClass);
        LittleEndian.putShort(array, 1 + offset , getExternSheetIndex());
        LittleEndian.putShort(array, 3 + offset , getFirstRow());
        LittleEndian.putShort(array, 5 + offset , getLastRow());
        LittleEndian.putShort(array, 7 + offset , getFirstColumnRaw());
        LittleEndian.putShort(array, 9 + offset , getLastColumnRaw());
    }

    public int getSize()
    {
        return SIZE;
    }

    public short getExternSheetIndex(){
        return field_1_index_extern_sheet;
    }

    public void setExternSheetIndex(short index){
        field_1_index_extern_sheet = index;
    }

    public short getFirstRow()
    {
        return field_2_first_row;
    }

    public void setFirstRow(short row)
    {
        field_2_first_row = row;
    }

    public short getLastRow()
    {
        return field_3_last_row;
    }

    public void setLastRow(short row)
    {
        field_3_last_row = row;
    }

    public short getFirstColumn()
    {
        return ( short ) (field_4_first_column & 0xFF);
    }

    public short getFirstColumnRaw()
    {
        return field_4_first_column;
    }

    public boolean isFirstRowRelative()
    {
        return rowRelative.isSet(field_4_first_column);
    }
    
    public boolean isFirstColRelative()
    {
        return colRelative.isSet(field_4_first_column);
    }

    public void setFirstColumn(short column)
    {
        field_4_first_column &= 0xFF00;
        field_4_first_column |= column & 0xFF;
    }

    public void setFirstColumnRaw(short column)
    {
        field_4_first_column = column;
    }

    public short getLastColumn()
    {
        return ( short ) (field_5_last_column & 0xFF);
    }

    public short getLastColumnRaw()
    {
        return field_5_last_column;
    }

     public boolean isLastRowRelative()
    {
        return rowRelative.isSet(field_5_last_column);
    }
    public boolean isLastColRelative()
    {
        return colRelative.isSet(field_5_last_column);
    }
    
    public void setLastColumn(short column)
    {
        field_5_last_column &= 0xFF00;
        field_5_last_column |= column & 0xFF;
    }

    public void setLastColumnRaw(short column)
    {
        field_5_last_column = column;
    }
    
        /**
     * sets the first row to relative or not
     * @param isRelative or not.
     */
    public void setFirstRowRelative(boolean rel) {
        field_4_first_column=rowRelative.setShortBoolean(field_4_first_column,rel);
    }

    /**
     * set whether the first column is relative 
     */
    public void setFirstColRelative(boolean rel) {
        field_4_first_column=colRelative.setShortBoolean(field_4_first_column,rel);
    }
    
    /**
     * set whether the last row is relative or not
     * @param last row relative
     */
    public void setLastRowRelative(boolean rel) {
        field_5_last_column=rowRelative.setShortBoolean(field_5_last_column,rel);
    }
    
    /**
     * set whether the last column should be relative or not
     */
    public void setLastColRelative(boolean rel) {
        field_5_last_column=colRelative.setShortBoolean(field_5_last_column,rel);
    }
    

    /*public String getArea(){
        RangeAddress ra = new RangeAddress( getFirstColumn(),getFirstRow() + 1, getLastColumn(), getLastRow() + 1);
        String result = ra.getAddress();

        return result;
    }*/

    public void setArea(String ref){
        RangeAddress ra = new RangeAddress(ref);

        String from = ra.getFromCell();
        String to   = ra.getToCell();

        setFirstColumn((short) (ra.getXPosition(from) -1));
        setFirstRow((short) (ra.getYPosition(from) -1));
        setLastColumn((short) (ra.getXPosition(to) -1));
        setLastRow((short) (ra.getYPosition(to) -1));

    }

    public String toFormulaString(SheetReferences refs)
    {
        StringBuffer retval = new StringBuffer();
        if (refs != null) {
            retval.append(refs.getSheetName(this.field_1_index_extern_sheet));
            retval.append('!');
        }
        retval.append((new CellReference(getFirstRow(),getFirstColumn(),!isFirstRowRelative(),!isFirstColRelative())).toString()); 
        retval.append(':');
        retval.append((new CellReference(getLastRow(),getLastColumn(),!isLastRowRelative(),!isLastColRelative())).toString());
        return retval.toString();
    }

   public byte getDefaultOperandClass() {
       return Ptg.CLASS_REF;
   }

}
