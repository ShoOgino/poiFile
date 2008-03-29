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


package org.apache.poi.hssf.record.formula.functions;

import org.apache.poi.hssf.record.formula.Area3DPtg;
import org.apache.poi.hssf.record.formula.AreaPtg;
import org.apache.poi.hssf.record.formula.eval.Area3DEval;
import org.apache.poi.hssf.record.formula.eval.AreaEval;
import org.apache.poi.hssf.record.formula.eval.BoolEval;
import org.apache.poi.hssf.record.formula.eval.ErrorEval;
import org.apache.poi.hssf.record.formula.eval.Eval;
import org.apache.poi.hssf.record.formula.eval.NumericValueEval;
import org.apache.poi.hssf.record.formula.eval.Ref3DEval;
import org.apache.poi.hssf.record.formula.eval.RefEval;
import org.apache.poi.hssf.record.formula.eval.StringEval;
import org.apache.poi.hssf.record.formula.eval.ValueEval;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
/**
 * Implementation for Excel function OFFSET()<p/>
 * 
 * OFFSET returns an area reference that is a specified number of rows and columns from a 
 * reference cell or area.<p/>
 * 
 * <b>Syntax</b>:<br/>
 * <b>OFFSET</b>(<b>reference</b>, <b>rows</b>, <b>cols</b>, height, width)<p/>
 * <b>reference</b> is the base reference.<br/>
 * <b>rows</b> is the number of rows up or down from the base reference.<br/>
 * <b>cols</b> is the number of columns left or right from the base reference.<br/>
 * <b>height</b> (default same height as base reference) is the row count for the returned area reference.<br/>
 * <b>width</b> (default same width as base reference) is the column count for the returned area reference.<br/>
 * 
 * @author Josh Micich
 */
public final class Offset implements FreeRefFunction {
	// These values are specific to BIFF8
	private static final int LAST_VALID_ROW_INDEX = 0xFFFF;
	private static final int LAST_VALID_COLUMN_INDEX = 0xFF;
	

	/**
	 * Exceptions are used within this class to help simplify flow control when error conditions
	 * are encountered 
	 */
	private static final class EvalEx extends Exception {
		private final ErrorEval _error;

		public EvalEx(ErrorEval error) {
			_error = error;
		}
		public ErrorEval getError() {
			return _error;
		}
	}
	
	/** 
	 * A one dimensional base + offset.  Represents either a row range or a column range.
	 * Two instances of this class together specify an area range.
	 */
	/* package */ static final class LinearOffsetRange {

		private final int _offset;
		private final int _length;

		public LinearOffsetRange(int offset, int length) {
			if(length == 0) {
				// handled that condition much earlier
				throw new RuntimeException("length may not be zero");
			}
			_offset = offset;
			_length = length;
		}
		
		public short getFirstIndex() {
			return (short) _offset;
		}
		public short getLastIndex() {
			return (short) (_offset + _length - 1);
		}
		/**
		 * Moves the range by the specified translation amount.<p/>
		 * 
		 * This method also 'normalises' the range: Excel specifies that the width and height 
		 * parameters (length field here) cannot be negative.  However, OFFSET() does produce
		 * sensible results in these cases.  That behavior is replicated here. <p/>
		 * 
		 * @param translationAmount may be zero negative or positive
		 * 
		 * @return the equivalent <tt>LinearOffsetRange</tt> with a positive length, moved by the
		 * specified translationAmount.
		 */
		public LinearOffsetRange normaliseAndTranslate(int translationAmount) {
			if (_length > 0) {
				if(translationAmount == 0) {
					return this;
				}
				return new LinearOffsetRange(translationAmount + _offset, _length);
			}
			return new LinearOffsetRange(translationAmount + _offset + _length + 1, -_length);
		}

		public boolean isOutOfBounds(int lowValidIx, int highValidIx) {
			if(_offset < lowValidIx) {
				return true;
			}
			if(getLastIndex() > highValidIx) {
				return true;
			}
			return false;
		}
		public String toString() {
			StringBuffer sb = new StringBuffer(64);
			sb.append(getClass().getName()).append(" [");
			sb.append(_offset).append("...").append(getLastIndex());
			sb.append("]");
			return sb.toString();
		}
	}
	
	
	/**
	 * Encapsulates either an area or cell reference which may be 2d or 3d.
	 */
	private static final class BaseRef {
		private static final int INVALID_SHEET_INDEX = -1;
		private final int _firstRowIndex;
		private final int _firstColumnIndex;
		private final int _width;
		private final int _height;
		private final int _externalSheetIndex;
		
		public BaseRef(RefEval re) {
			_firstRowIndex = re.getRow();
			_firstColumnIndex = re.getColumn();
			_height = 1;
			_width = 1;
			if (re instanceof Ref3DEval) {
				Ref3DEval r3e = (Ref3DEval) re;
				_externalSheetIndex = r3e.getExternSheetIndex();
			} else {
				_externalSheetIndex = INVALID_SHEET_INDEX;
			}
		}

		public BaseRef(AreaEval ae) {
			_firstRowIndex = ae.getFirstRow();
			_firstColumnIndex = ae.getFirstColumn();
			_height = ae.getLastRow() - ae.getFirstRow() + 1;
			_width = ae.getLastColumn() - ae.getFirstColumn() + 1;
			if (ae instanceof Area3DEval) {
				Area3DEval a3e = (Area3DEval) ae;
				_externalSheetIndex = a3e.getExternSheetIndex();
			} else {
				_externalSheetIndex = INVALID_SHEET_INDEX;
			}
		}

		public int getWidth() {
			return _width;
		}

		public int getHeight() {
			return _height;
		}

		public int getFirstRowIndex() {
			return _firstRowIndex;
		}

		public int getFirstColumnIndex() {
			return _firstColumnIndex;
		}

		public boolean isIs3d() {
			return _externalSheetIndex > 0;
		}

		public short getExternalSheetIndex() {
			if(_externalSheetIndex < 0) {
				throw new IllegalStateException("external sheet index only available for 3d refs");
			}
			return (short) _externalSheetIndex;
		}

	}
	
	public ValueEval evaluate(Eval[] args, int srcCellRow, short srcCellCol, Workbook workbook, Sheet sheet) {
		
		if(args.length < 3 || args.length > 5) {
			return ErrorEval.VALUE_INVALID;
		}
		
		
		try {
			BaseRef baseRef = evaluateBaseRef(args[0]);
			int rowOffset = evaluateIntArg(args[1], srcCellRow, srcCellCol);
			int columnOffset = evaluateIntArg(args[2], srcCellRow, srcCellCol);
			int height = baseRef.getHeight();
			int width = baseRef.getWidth();
			switch(args.length) {
				case 5:
					width = evaluateIntArg(args[4], srcCellRow, srcCellCol);
				case 4:
					height = evaluateIntArg(args[3], srcCellRow, srcCellCol);
			}
			// Zero height or width raises #REF! error
			if(height == 0 || width == 0) {
				return ErrorEval.REF_INVALID;
			}
			LinearOffsetRange rowOffsetRange = new LinearOffsetRange(rowOffset, height);
			LinearOffsetRange colOffsetRange = new LinearOffsetRange(columnOffset, width);
			return createOffset(baseRef, rowOffsetRange, colOffsetRange, workbook, sheet);
		} catch (EvalEx e) {
			return e.getError();
		}
	}


	private static AreaEval createOffset(BaseRef baseRef, 
			LinearOffsetRange rowOffsetRange, LinearOffsetRange colOffsetRange, 
			Workbook workbook, Sheet sheet) throws EvalEx {

		LinearOffsetRange rows = rowOffsetRange.normaliseAndTranslate(baseRef.getFirstRowIndex());
		LinearOffsetRange cols = colOffsetRange.normaliseAndTranslate(baseRef.getFirstColumnIndex());
		
		if(rows.isOutOfBounds(0, LAST_VALID_ROW_INDEX)) {
			throw new EvalEx(ErrorEval.REF_INVALID);
		}
		if(cols.isOutOfBounds(0, LAST_VALID_COLUMN_INDEX)) {
			throw new EvalEx(ErrorEval.REF_INVALID);
		}
		if(baseRef.isIs3d()) {
			Area3DPtg a3dp = new Area3DPtg(rows.getFirstIndex(), rows.getLastIndex(), 
					cols.getFirstIndex(), cols.getLastIndex(),
					false, false, false, false,
					baseRef.getExternalSheetIndex());
			return HSSFFormulaEvaluator.evaluateArea3dPtg(workbook, a3dp);
		}
		
		AreaPtg ap = new AreaPtg(rows.getFirstIndex(), rows.getLastIndex(), 
				cols.getFirstIndex(), cols.getLastIndex(),
				false, false, false, false);
		return HSSFFormulaEvaluator.evaluateAreaPtg(sheet, workbook, ap);
	}


	private static BaseRef evaluateBaseRef(Eval eval) throws EvalEx {
		
		if(eval instanceof RefEval) {
			return new BaseRef((RefEval)eval);
		}
		if(eval instanceof AreaEval) {
			return new BaseRef((AreaEval)eval);
		}
		if (eval instanceof ErrorEval) {
			throw new EvalEx((ErrorEval) eval);
		}
		throw new EvalEx(ErrorEval.VALUE_INVALID);
	}


	/**
	 * OFFSET's numeric arguments (2..5) have similar processing rules
	 */
	private static int evaluateIntArg(Eval eval, int srcCellRow, short srcCellCol) throws EvalEx {

		double d = evaluateDoubleArg(eval, srcCellRow, srcCellCol);
		return convertDoubleToInt(d);
	}

	/**
	 * Fractional values are silently truncated by Excel.
	 * Truncation is toward negative infinity.
	 */
	/* package */ static int convertDoubleToInt(double d) {
		// Note - the standard java type conversion from double to int truncates toward zero.
		// but Math.floor() truncates toward negative infinity
		return (int)Math.floor(d);
	}
	
	
	private static double evaluateDoubleArg(Eval eval, int srcCellRow, short srcCellCol) throws EvalEx {
		ValueEval ve = evaluateSingleValue(eval, srcCellRow, srcCellCol);
		
		if (ve instanceof NumericValueEval) {
			return ((NumericValueEval) ve).getNumberValue();
		}
		if (ve instanceof StringEval) {
			StringEval se = (StringEval) ve;
			Double d = parseDouble(se.getStringValue());
			if(d == null) {
				throw new EvalEx(ErrorEval.VALUE_INVALID);
			}
			return d.doubleValue();
		}
		if (ve instanceof BoolEval) {
			// in the context of OFFSET, booleans resolve to 0 and 1.
			if(((BoolEval) ve).getBooleanValue()) {
				return 1;
			}
			return 0;
		}
		throw new RuntimeException("Unexpected eval type (" + ve.getClass().getName() + ")");
	}
	
	private static Double parseDouble(String s) {
		// TODO - find a home for this method
		// TODO - support various number formats: sign char, dollars, commas
		// OFFSET and COUNTIF seem to handle these
		return Countif.parseDouble(s);
	}
	
	private static ValueEval evaluateSingleValue(Eval eval, int srcCellRow, short srcCellCol) throws EvalEx {
		if(eval instanceof RefEval) {
			return ((RefEval)eval).getInnerValueEval();
		}
		if(eval instanceof AreaEval) {
			return chooseSingleElementFromArea((AreaEval)eval, srcCellRow, srcCellCol);
		}
		if (eval instanceof ValueEval) {
			return (ValueEval) eval;
		}
		throw new RuntimeException("Unexpected eval type (" + eval.getClass().getName() + ")");
	}

	// TODO - this code seems to get repeated a bit
	private static ValueEval chooseSingleElementFromArea(AreaEval ae, int srcCellRow, short srcCellCol) throws EvalEx {
		if (ae.isColumn()) {
			if (ae.isRow()) {
				return ae.getValues()[0];
			}
			if (!ae.containsRow(srcCellRow)) {
				throw new EvalEx(ErrorEval.VALUE_INVALID);
			}
			return ae.getValueAt(srcCellRow, ae.getFirstColumn());
		}
		if (!ae.isRow()) {
			throw new EvalEx(ErrorEval.VALUE_INVALID);
		}
		if (!ae.containsColumn(srcCellCol)) {
			throw new EvalEx(ErrorEval.VALUE_INVALID);
		}
		return ae.getValueAt(ae.getFirstRow(), srcCellCol);
	}
}
