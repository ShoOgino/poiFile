
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

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.hssf.record.RecordInputStream;
import org.apache.poi.hssf.usermodel.HSSFErrorConstants;

/**
 * @author Daniel Noll (daniel at nuix dot com dot au)
 */
public final class ErrPtg extends Ptg {
    
    // convenient access to namespace
    private static final HSSFErrorConstants EC = null;
    
    /** <b>#NULL!</b>  - Intersection of two cell ranges is empty */
    public static final ErrPtg NULL_INTERSECTION = new ErrPtg(EC.ERROR_NULL); 
    /** <b>#DIV/0!</b> - Division by zero */
    public static final ErrPtg DIV_ZERO = new ErrPtg(EC.ERROR_DIV_0);
    /** <b>#VALUE!</b> - Wrong type of operand */
    public static final ErrPtg VALUE_INVALID = new ErrPtg(EC.ERROR_VALUE);
    /** <b>#REF!</b> - Illegal or deleted cell reference */
    public static final ErrPtg REF_INVALID = new ErrPtg(EC.ERROR_REF);
    /** <b>#NAME?</b> - Wrong function or range name */
    public static final ErrPtg NAME_INVALID = new ErrPtg(EC.ERROR_NAME); 
    /** <b>#NUM!</b> - Value range overflow */
    public static final ErrPtg NUM_ERROR = new ErrPtg(EC.ERROR_NUM);
    /** <b>#N/A</b> - Argument or function not available */
    public static final ErrPtg N_A = new ErrPtg(EC.ERROR_NA);
    
    
    public static final short sid  = 0x1c;
    private static final int  SIZE = 2;
    private int              field_1_error_code;

    /** Creates new ErrPtg */

    public ErrPtg(int errorCode) {
        if(!HSSFErrorConstants.isValidCode(errorCode)) {
            throw new IllegalArgumentException("Invalid error code (" + errorCode + ")");
        }
        field_1_error_code = errorCode;
    }
 
    public ErrPtg(RecordInputStream in) {
        this(in.readByte());
    }

    public void writeBytes(byte [] array, int offset)
    {
        array[offset] = (byte) (sid + ptgClass);
        array[offset + 1] = (byte)field_1_error_code;
    }

    public String toFormulaString(Workbook book) {
        return HSSFErrorConstants.getText(field_1_error_code);
    }

    public int getSize() {
        return SIZE;
    }

    public byte getDefaultOperandClass() {
        return Ptg.CLASS_VALUE;
    }

    public Object clone() {
        return new ErrPtg(field_1_error_code);
    }

    public int getErrorCode() {
        return field_1_error_code;
    }
}
