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

package org.apache.poi.ss.formula;

import junit.framework.TestCase;

import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.formula.ptg.AreaErrPtg;
import org.apache.poi.ss.formula.ptg.AreaPtg;
import org.apache.poi.ss.formula.ptg.Ptg;

/**
 * Tests for {@link FormulaShifter}.
 *
 * @author Josh Micich
 */
public final class TestFormulaShifter extends TestCase {
	// Note - the expected result row coordinates here were determined/verified
	// in Excel 2007 by manually testing.

	/**
	 * Tests what happens to area refs when a range of rows from inside, or overlapping are
	 * moved
	 */
	public void testShiftAreasSourceRows() {

		// all these operations are on an area ref spanning rows 10 to 20
		AreaPtg aptg  = createAreaPtg(10, 20);

		confirmAreaShift(aptg,  9, 21, 20, 30, 40);
		confirmAreaShift(aptg, 10, 21, 20, 30, 40);
		confirmAreaShift(aptg,  9, 20, 20, 30, 40);

		confirmAreaShift(aptg, 8, 11,  -3, 7, 20); // simple expansion of top
		// rows containing area top being shifted down:
		confirmAreaShift(aptg, 8, 11,  3, 13, 20);
		confirmAreaShift(aptg, 8, 11,  7, 17, 20);
		confirmAreaShift(aptg, 8, 11,  8, 18, 20);
		confirmAreaShift(aptg, 8, 11,  9, 12, 20); // note behaviour changes here
		confirmAreaShift(aptg, 8, 11, 10, 12, 21);
		confirmAreaShift(aptg, 8, 11, 12, 12, 23);
		confirmAreaShift(aptg, 8, 11, 13, 10, 20);  // ignored

		// rows from within being moved:
		confirmAreaShift(aptg, 12, 16,  3, 10, 20);  // stay within - no change
		confirmAreaShift(aptg, 11, 19, 20, 10, 20);  // move completely out - no change
		confirmAreaShift(aptg, 16, 17, -6, 10, 20);  // moved exactly to top - no change
		confirmAreaShift(aptg, 16, 17, -7, 11, 20);  // truncation at top
		confirmAreaShift(aptg, 12, 16,  4, 10, 20);  // moved exactly to bottom - no change
		confirmAreaShift(aptg, 12, 16,  6, 10, 17);  // truncation at bottom

		// rows containing area bottom being shifted up:
		confirmAreaShift(aptg, 18, 22, -1, 10, 19); // simple contraction at bottom
		confirmAreaShift(aptg, 18, 22, -7, 10, 13); // simple contraction at bottom
		confirmAreaShift(aptg, 18, 22, -8, 10, 17); // top calculated differently here
		confirmAreaShift(aptg, 18, 22, -9,  9, 17);
		confirmAreaShift(aptg, 18, 22,-15, 10, 20); // no change because range would be turned inside out
		confirmAreaShift(aptg, 15, 19, -7, 13, 20); // dest truncates top (even though src is from inside range)
		confirmAreaShift(aptg, 19, 23,-12,  7, 18); // complex: src encloses bottom, dest encloses top

		confirmAreaShift(aptg, 18, 22,  5, 10, 25); // simple expansion at bottom
	}
	
	public void testCopyAreasSourceRowsRelRel() {

		// all these operations are on an area ref spanning rows 10 to 20
		final AreaPtg aptg  = createAreaPtg(10, 20, true, true);

		confirmAreaCopy(aptg,  0, 30, 20, 30, 40, true);
		confirmAreaCopy(aptg,  15, 25, -15, -1, -1, true); //DeletedRef
	}
	
	public void testCopyAreasSourceRowsRelAbs() {

		// all these operations are on an area ref spanning rows 10 to 20
		final AreaPtg aptg  = createAreaPtg(10, 20, true, false);

		// Only first row should move
		confirmAreaCopy(aptg,  0, 30, 20, 20, 30, true);
		confirmAreaCopy(aptg,  15, 25, -15, -1, -1, true); //DeletedRef
	}
	
	public void testCopyAreasSourceRowsAbsRel() {
		// aptg is part of a formula in a cell that was just copied to another row
		// aptg row references should be updated by the difference in rows that the cell was copied
		// No other references besides the cells that were involved in the copy need to be updated
		// this makes the row copy significantly different from the row shift, where all references
		// in the workbook need to track the row shift

		// all these operations are on an area ref spanning rows 10 to 20
		final AreaPtg aptg  = createAreaPtg(10, 20, false, true);

		// Only last row should move
		confirmAreaCopy(aptg,  0, 30, 20, 10, 40, true);
		confirmAreaCopy(aptg,  15, 25, -15, 5, 10, true); //sortTopLeftToBottomRight swapped firstRow and lastRow because firstRow is absolute
	}
	
	public void testCopyAreasSourceRowsAbsAbs() {
		// aptg is part of a formula in a cell that was just copied to another row
		// aptg row references should be updated by the difference in rows that the cell was copied
		// No other references besides the cells that were involved in the copy need to be updated
		// this makes the row copy significantly different from the row shift, where all references
		// in the workbook need to track the row shift
		
		// all these operations are on an area ref spanning rows 10 to 20
		final AreaPtg aptg  = createAreaPtg(10, 20, false, false);

		//AbsFirstRow AbsLastRow references should't change when copied to a different row
		confirmAreaCopy(aptg,  0, 30, 20, 10, 20, false);
		confirmAreaCopy(aptg,  15, 25, -15, 10, 20, false);
	}
	
	/**
	 * Tests what happens to an area ref when some outside rows are moved to overlap
	 * that area ref
	 */
	public void testShiftAreasDestRows() {
		// all these operations are on an area ref spanning rows 20 to 25
		AreaPtg aptg  = createAreaPtg(20, 25);

		// no change because no overlap:
		confirmAreaShift(aptg,  5, 10,  9, 20, 25);
		confirmAreaShift(aptg,  5, 10, 21, 20, 25);

		confirmAreaShift(aptg, 11, 14, 10, 20, 25);

		confirmAreaShift(aptg,   7, 17, 10, -1, -1); // converted to DeletedAreaRef
		confirmAreaShift(aptg,   5, 15,  7, 23, 25); // truncation at top
		confirmAreaShift(aptg,  13, 16, 10, 20, 22); // truncation at bottom
	}

	private static void confirmAreaShift(AreaPtg aptg,
			int firstRowMoved, int lastRowMoved, int numberRowsMoved,
			int expectedAreaFirstRow, int expectedAreaLastRow) {

		FormulaShifter fs = FormulaShifter.createForRowShift(0, "", firstRowMoved, lastRowMoved, numberRowsMoved, SpreadsheetVersion.EXCEL2007);
		boolean expectedChanged = aptg.getFirstRow() != expectedAreaFirstRow || aptg.getLastRow() != expectedAreaLastRow;

		AreaPtg copyPtg = (AreaPtg) aptg.copy(); // clone so we can re-use aptg in calling method
		Ptg[] ptgs = { copyPtg, };
		boolean actualChanged = fs.adjustFormula(ptgs, 0);
		if (expectedAreaFirstRow < 0) {
			assertEquals(AreaErrPtg.class, ptgs[0].getClass());
			return;
		}
		assertEquals(expectedChanged, actualChanged);
		assertEquals(copyPtg, ptgs[0]);  // expected to change in place (although this is not a strict requirement)
		assertEquals(expectedAreaFirstRow, copyPtg.getFirstRow());
		assertEquals(expectedAreaLastRow, copyPtg.getLastRow());

	}
	
	
	private static void confirmAreaCopy(AreaPtg aptg,
			int firstRowCopied, int lastRowCopied, int rowOffset,
			int expectedFirstRow, int expectedLastRow, boolean expectedChanged) {

		final AreaPtg copyPtg = (AreaPtg) aptg.copy(); // clone so we can re-use aptg in calling method
		final Ptg[] ptgs = { copyPtg, };
		final FormulaShifter fs = FormulaShifter.createForRowCopy(0, null, firstRowCopied, lastRowCopied, rowOffset, SpreadsheetVersion.EXCEL2007);
		final boolean actualChanged = fs.adjustFormula(ptgs, 0);
		
		// DeletedAreaRef
		if (expectedFirstRow < 0 || expectedLastRow < 0) {
			assertEquals("Reference should have shifted off worksheet, producing #REF! error: " + ptgs[0],
					AreaErrPtg.class, ptgs[0].getClass());
			return;
		}
		
		assertEquals("Should this AreaPtg change due to row copy?", expectedChanged, actualChanged);
		assertEquals("AreaPtgs should be modified in-place when a row containing the AreaPtg is copied", copyPtg, ptgs[0]);  // expected to change in place (although this is not a strict requirement)
		assertEquals("AreaPtg first row", expectedFirstRow, copyPtg.getFirstRow());
		assertEquals("AreaPtg last row", expectedLastRow, copyPtg.getLastRow());

	}
	
	private static AreaPtg createAreaPtg(int initialAreaFirstRow, int initialAreaLastRow) {
		return createAreaPtg(initialAreaFirstRow, initialAreaLastRow, false, false);
	}
	
	private static AreaPtg createAreaPtg(int initialAreaFirstRow, int initialAreaLastRow, boolean firstRowRelative, boolean lastRowRelative) {
		return new AreaPtg(initialAreaFirstRow, initialAreaLastRow, 2, 5, firstRowRelative, lastRowRelative, false, false);
	}
}
