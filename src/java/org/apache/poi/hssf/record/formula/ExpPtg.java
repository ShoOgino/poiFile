
/* ====================================================================
   Copyright 2003-2004   Apache Software Foundation

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */

package org.apache.poi.hssf.record.formula;

import org.apache.poi.hssf.model.Workbook;
import org.apache.poi.hssf.record.RecordInputStream;

import org.apache.poi.util.LittleEndian;

/**
 *
 * @author  andy
 * @author Jason Height (jheight at chariot dot net dot au)
 * @author dmui (save existing implementation)
 */

public class ExpPtg
    extends Ptg
{
    private final static int  SIZE = 5;
    public final static short sid  = 0x1;
    private short            field_1_first_row;
    private short            field_2_first_col;

    /** Creates new ExpPtg */

    public ExpPtg()
    {
    }

    /** Creates new ExpPtg */

    public ExpPtg(RecordInputStream in)
    {
      field_1_first_row = in.readShort();
      field_2_first_col = in.readShort();
    }

    public void writeBytes(byte [] array, int offset)
    {
      array[offset+0]= (byte) (sid);
      LittleEndian.putShort(array,offset+1,field_1_first_row);
      LittleEndian.putShort(array,offset+3,field_2_first_col);
    }

    public int getSize()
    {
        return SIZE;
    }

    public String toFormulaString(Workbook book)
    {
        return "NO IDEA SHARED FORMULA EXP PTG";
    }
    
    public byte getDefaultOperandClass() {return Ptg.CLASS_VALUE;}
    
    public Object clone() {
	ExpPtg result = new ExpPtg();
        result.field_1_first_row = field_1_first_row;
        result.field_2_first_col = field_2_first_col;        
        return result;
    }

}
