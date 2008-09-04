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

package org.apache.poi.hssf.record.formula.eval;

import org.apache.poi.hssf.record.formula.AreaI;
import org.apache.poi.hssf.record.formula.AreaI.OffsetArea;
import org.apache.poi.hssf.record.formula.Ref3DPtg;
import org.apache.poi.hssf.record.formula.RefPtg;
import org.apache.poi.hssf.usermodel.EvaluationCache;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;

/**
*
* @author Josh Micich 
*/
public final class LazyRefEval extends RefEvalBase {

	private final HSSFSheet _sheet;
	private final HSSFWorkbook _workbook;


	public LazyRefEval(RefPtg ptg, HSSFSheet sheet, HSSFWorkbook workbook) {
		super(ptg.getRow(), ptg.getColumn());
		_sheet = sheet;
		_workbook = workbook;
	}
	public LazyRefEval(Ref3DPtg ptg, HSSFSheet sheet, HSSFWorkbook workbook) {
		super(ptg.getRow(), ptg.getColumn());
		_sheet = sheet;
		_workbook = workbook;
	}

	public ValueEval getInnerValueEval() {
		int rowIx = getRow();
		int colIx = getColumn();
		
		HSSFRow row = _sheet.getRow(rowIx);
		if (row == null) {
			return BlankEval.INSTANCE;
		}
		HSSFCell cell = row.getCell(colIx);
		if (cell == null) {
			return BlankEval.INSTANCE;
		}
		return HSSFFormulaEvaluator.getEvalForCell(cell, _sheet, _workbook);
	}
	
	public AreaEval offset(int relFirstRowIx, int relLastRowIx, int relFirstColIx, int relLastColIx) {
		
		AreaI area = new OffsetArea(getRow(), getColumn(),
				relFirstRowIx, relLastRowIx, relFirstColIx, relLastColIx);

		return new LazyAreaEval(area, _sheet, _workbook);
	}
	
	public String toString() {
		CellReference cr = new CellReference(getRow(), getColumn());
		StringBuffer sb = new StringBuffer();
		sb.append(getClass().getName()).append("[");
		String sheetName = _workbook.getSheetName(_workbook.getSheetIndex(_sheet));
		sb.append(sheetName);
		sb.append('!');
		sb.append(cr.formatAsString());
		sb.append("]");
		return sb.toString();
	}
}
