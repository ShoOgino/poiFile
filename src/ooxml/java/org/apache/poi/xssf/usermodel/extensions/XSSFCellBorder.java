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
package org.apache.poi.xssf.usermodel.extensions;


import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorder;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STBorderStyle.Enum;


public class XSSFCellBorder {
	private CTBorder border;
	
	/**
	 * Creates a Cell Border from the supplied XML definition
	 */
	public XSSFCellBorder(CTBorder border) {
		this.border = border;
	}
	/**
	 * Creates a new, empty Cell Border, on the
	 *  given Styles Table
	 */
	public XSSFCellBorder() {
		border = CTBorder.Factory.newInstance();
	}
	
	public static enum BorderSides {
		TOP, RIGHT, BOTTOM, LEFT
	}
	
	/**
	 * TODO - is this the best way to allow StylesTable
	 *  to record us?
	 */
	public CTBorder getCTBorder() {
		return border;
	}
	
	public Enum getBorderStyle(BorderSides side) {
		return getBorder(side).getStyle();
	}
	
	public XSSFColor getBorderColor(BorderSides side) {
		CTBorderPr borderPr = getBorder(side);
		if (!borderPr.isSetColor()) {
			borderPr.addNewColor();
		}
		return new XSSFColor(getBorder(side).getColor());
	}
	
	private CTBorderPr getBorder(BorderSides side) {
		switch (side) {
		case TOP: return border.getTop();
		case RIGHT: return border.getRight();
		case BOTTOM: return border.getBottom();
		case LEFT: return border.getLeft();
		default: throw new IllegalArgumentException("No suitable side specified for the border");
		}
	}
}