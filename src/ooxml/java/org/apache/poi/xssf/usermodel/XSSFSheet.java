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

import org.apache.poi.hssf.util.PaneInformation;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.Footer;
import org.apache.poi.ss.usermodel.Header;
import org.apache.poi.ss.usermodel.Patriarch;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.helpers.ColumnHelper;
import org.apache.poi.xssf.util.CellReference;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBreak;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDialogsheet;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHeaderFooter;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageBreak;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageMargins;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetUpPr;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPrintOptions;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheet;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetProtection;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetViews;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorksheet;


public class XSSFSheet implements Sheet {

    protected CTSheet sheet;
    protected CTWorksheet worksheet;
    protected CTDialogsheet dialogsheet;
    protected List<Row> rows;
    protected ColumnHelper columnHelper;
    protected XSSFWorkbook workbook;

    public static final short LeftMargin = 0;
    public static final short RightMargin = 1;
    public static final short TopMargin = 2;
    public static final short BottomMargin = 3;
    public static final short HeaderMargin = 4;
    public static final short FooterMargin = 5;

	public XSSFSheet(CTSheet sheet, CTWorksheet worksheet, XSSFWorkbook workbook) {
        this.workbook = workbook;
        this.sheet = sheet;
        this.worksheet = worksheet;
        if (this.worksheet == null) {
        	this.worksheet = CTWorksheet.Factory.newInstance();
        }
        if (this.worksheet.getSheetData() == null) {
        	this.worksheet.addNewSheetData();
        }
        initRows(this.worksheet);
        initColumns(this.worksheet);
	}

    public XSSFSheet(XSSFWorkbook workbook) {
        this.workbook = workbook;
    }

    public XSSFWorkbook getWorkbook() {
        return this.workbook;
    }
    
    protected CTWorksheet getWorksheet() {
        return this.worksheet;
    }
    
    public ColumnHelper getColumnHelper() {
    	return columnHelper;
    }

    protected void initRows(CTWorksheet worksheet) {
        this.rows = new LinkedList<Row>();
        for (CTRow row : worksheet.getSheetData().getRowArray()) {
            this.rows.add(new XSSFRow(row, this));
        }
    }

    protected void initColumns(CTWorksheet worksheet) {
        columnHelper = new ColumnHelper(worksheet);
    }

    protected CTSheet getSheet() {
        return this.sheet;
    }
    
    public int addMergedRegion(Region region) {
        // TODO Auto-generated method stub
        return 0;
    }

    public void autoSizeColumn(short column) {
    	columnHelper.setColBestFit(column, true);
    }

    public Patriarch createDrawingPatriarch() {
        // TODO Auto-generated method stub
        return null;
    }

    public void createFreezePane(int colSplit, int rowSplit, int leftmostColumn, int topRow) {
        // TODO Auto-generated method stub

    }

    public void createFreezePane(int colSplit, int rowSplit) {
        // TODO Auto-generated method stub

    }

    protected XSSFRow addRow(int index, int rownum) {
        CTRow row = this.worksheet.getSheetData().insertNewRow(index);
        XSSFRow xrow = new XSSFRow(row, this);
        xrow.setRowNum(rownum);
        return xrow;
    }

    public Row createRow(int rownum) {
        int index = 0;
        for (Row r : this.rows) {
                if (r.getRowNum() == rownum) {
                        // Replace r with new row
                XSSFRow xrow = addRow(index, rownum);
                        rows.set(index, xrow);
                        return xrow;
                }
                if (r.getRowNum() > rownum) {
                        XSSFRow xrow = addRow(index, rownum);
                        rows.add(index, xrow);
                        return xrow;
                }
                ++index;
        }
        XSSFRow xrow = addRow(index, rownum);
        rows.add(xrow);
        return xrow;
    }

    public void createSplitPane(int splitPos, int splitPos2, int leftmostColumn, int topRow, int activePane) {
        // TODO Auto-generated method stub

    }

    public void dumpDrawingRecords(boolean fat) {
        // TODO Auto-generated method stub

    }

    public boolean getAlternateExpression() {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean getAlternateFormula() {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean getAutobreaks() {
        return getSheetTypePageSetUpPr().getAutoPageBreaks();
    }

	private CTPageSetUpPr getSheetTypePageSetUpPr() {
    	if (getSheetTypeSheetPr().getPageSetUpPr() == null) {
    		getSheetTypeSheetPr().setPageSetUpPr(CTPageSetUpPr.Factory.newInstance());
    	}
		return getSheetTypeSheetPr().getPageSetUpPr();
	}

	protected CTSheetPr getSheetTypeSheetPr() {
    	if (worksheet.getSheetPr() == null) {
    		worksheet.setSheetPr(CTSheetPr.Factory.newInstance());
    	}
		return worksheet.getSheetPr();
	}

    public Comment getCellComment(int row, int column) {
        // TODO Auto-generated method stub
        return null;
    }

    public short[] getColumnBreaks() {
        CTBreak[] brkArray = getSheetTypeColumnBreaks().getBrkArray();
        if (brkArray.length == 0) {
            return null;
        }
        short[] breaks = new short[brkArray.length];
        for (int i = 0 ; i < brkArray.length ; i++) {
            CTBreak brk = brkArray[i];
            breaks[i] = (short) brk.getId();
        }
        return breaks;
    }

	protected CTPageBreak getSheetTypeColumnBreaks() {
		if (worksheet.getColBreaks() == null) {
			worksheet.setColBreaks(CTPageBreak.Factory.newInstance());
		}
		return worksheet.getColBreaks();
	}

    public short getColumnWidth(short column) {
        return (short) columnHelper.getColumn(column).getWidth();
    }

    public short getDefaultColumnWidth() {
        return (short) getSheetTypeSheetFormatPr().getDefaultColWidth();
    }

    public short getDefaultRowHeight() {
        return (short) (getSheetTypeSheetFormatPr().getDefaultRowHeight() * 20);
    }

	protected CTSheetFormatPr getSheetTypeSheetFormatPr() {
		if (worksheet.getSheetFormatPr() == null) {
			worksheet.setSheetFormatPr(CTSheetFormatPr.Factory.newInstance());
		}
		return worksheet.getSheetFormatPr();
	}

    public float getDefaultRowHeightInPoints() {
        return (short) getSheetTypeSheetFormatPr().getDefaultRowHeight();
    }

    public boolean getDialog() {
    	if (dialogsheet != null) {
    		return true;
    	}
    	return false;
    }

    public boolean getDisplayGuts() {
        // TODO Auto-generated method stub
        return false;
    }

    public int getFirstRowNum() {
        for (Iterator<Row> it = rowIterator() ; it.hasNext() ; ) {
            Row row = it.next();
            if (row != null) {
                return row.getRowNum();
            }
        }
        return -1;
    }

    public boolean getFitToPage() {
        return getSheetTypePageSetUpPr().getFitToPage();
    }

    public Footer getFooter() {
        return getOddFooter();
    }
    
    public Footer getOddFooter() {
        return new XSSFOddFooter(getSheetTypeHeaderFooter());
    }

	protected CTHeaderFooter getSheetTypeHeaderFooter() {
		if (worksheet.getHeaderFooter() == null) {
			worksheet.setHeaderFooter(CTHeaderFooter.Factory.newInstance());
		}
		return worksheet.getHeaderFooter();
	}
    
    public Footer getEvenFooter() {
        return new XSSFEvenFooter(getSheetTypeHeaderFooter());
    }
    
    public Footer getFirstFooter() {
        return new XSSFFirstFooter(getSheetTypeHeaderFooter());
    }

    public Header getHeader() {
        return getOddHeader();
    }
    
    public Header getOddHeader() {
        return new XSSFOddHeader(getSheetTypeHeaderFooter());
    }
    
    public Header getEvenHeader() {
        return new XSSFEvenHeader(getSheetTypeHeaderFooter()
);
    }
    
    public Header getFirstHeader() {
        return new XSSFFirstHeader(getSheetTypeHeaderFooter());
    }

    public boolean getHorizontallyCenter() {
    	return getSheetTypePrintOptions().getHorizontalCentered();
    }

	protected CTPrintOptions getSheetTypePrintOptions() {
		if (worksheet.getPrintOptions() == null) {
			worksheet.setPrintOptions(CTPrintOptions.Factory.newInstance());
		}
		return worksheet.getPrintOptions();
	}

    public int getLastRowNum() {
        int lastRowNum = -1;
        for (Iterator<Row> it = rowIterator() ; it.hasNext() ; ) {
            Row row = it.next();
            if (row != null) {
                lastRowNum = row.getRowNum();
            }
        }
        return lastRowNum;
    }

    public short getLeftCol() {
    	String cellRef = worksheet.getSheetViews().getSheetViewArray(0).getTopLeftCell();
    	CellReference cellReference = new CellReference(cellRef);
        return cellReference.getCol();
    }

    public double getMargin(short margin) {
        CTPageMargins pageMargins = getSheetTypePageMargins();
        switch (margin) {
        case LeftMargin:
            return pageMargins.getLeft();
        case RightMargin:
            return pageMargins.getRight();
        case TopMargin:
            return pageMargins.getTop();
        case BottomMargin:
            return pageMargins.getBottom();
        case HeaderMargin:
            return pageMargins.getHeader();
        case FooterMargin:
            return pageMargins.getFooter();
        default :
            throw new RuntimeException( "Unknown margin constant:  " + margin );
        }
    }

	protected CTPageMargins getSheetTypePageMargins() {
		if (worksheet.getPageMargins() == null) {
			worksheet.setPageMargins(CTPageMargins.Factory.newInstance());
		}
		return worksheet.getPageMargins();
	}

    public Region getMergedRegionAt(int index) {
        // TODO Auto-generated method stub
        return null;
    }

    public int getNumMergedRegions() {
        // TODO Auto-generated method stub
        return 0;
    }

    public boolean getObjectProtect() {
        // TODO Auto-generated method stub
        return false;
    }

    public PaneInformation getPaneInformation() {
        // TODO Auto-generated method stub
        return null;
    }

    public short getPassword() {
        // TODO Auto-generated method stub
        return 0;
    }

    public int getPhysicalNumberOfRows() {
        int counter = 0;
        for (Iterator<Row> it = rowIterator() ; it.hasNext() ; ) {
            if (it.next() != null) {
                counter++;
            }
        }
        return counter;
    }

    public PrintSetup getPrintSetup() {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean getProtect() {
        // TODO Auto-generated method stub
        return false;
    }

    public Row getRow(int rownum) {
        for (Iterator<Row> it = rowIterator() ; it.hasNext() ; ) {
                Row row = it.next();
                if (row.getRowNum() == rownum) {
                        return row;
                }
        }
        return null;
    }

    public int[] getRowBreaks() {
        CTPageBreak rowBreaks = getSheetTypeRowBreaks();
        int breaksCount = rowBreaks.getBrkArray().length;
        if (breaksCount == 0) {
            return null;
        }
        int[] breaks = new int[breaksCount];
        for (int i = 0 ; i < breaksCount ; i++) {
            CTBreak brk = rowBreaks.getBrkArray(i);
            breaks[i] = (int) brk.getId();
        }
        return breaks;
    }

	protected CTPageBreak getSheetTypeRowBreaks() {
		if (worksheet.getRowBreaks() == null) {
			worksheet.setRowBreaks(CTPageBreak.Factory.newInstance());
		}
		return worksheet.getRowBreaks();
	}

    public boolean getRowSumsBelow() {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean getRowSumsRight() {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean getScenarioProtect() {
    	return getSheetTypeProtection().getScenarios();
    }

	protected CTSheetProtection getSheetTypeProtection() {
		if (worksheet.getSheetProtection() == null) {
			worksheet.setSheetProtection(CTSheetProtection.Factory.newInstance());
		}
		return worksheet.getSheetProtection();
	}

    public short getTopRow() {
    	String cellRef = getSheetTypeSheetView().getTopLeftCell();
    	CellReference cellReference = new CellReference(cellRef);
        return (short) cellReference.getRow();
    }

    // Right signature method. Remove the wrong one when it will be removed in HSSFSheet (and interface)
    public boolean getVerticallyCenter() {
    	return getVerticallyCenter(true);
    }

    public boolean getVerticallyCenter(boolean value) {
    	return getSheetTypePrintOptions().getVerticalCentered();
    }

    public void groupColumn(short fromColumn, short toColumn) {
        // TODO Auto-generated method stub

    }

    public void groupRow(int fromRow, int toRow) {
        // TODO Auto-generated method stub

    }

    public boolean isColumnBroken(short column) {
        CTBreak[] brkArray = getSheetTypeColumnBreaks().getBrkArray();
        for (int i = 0 ; i < brkArray.length ; i++) {
            if (brkArray[i].getId() == column) {
                return true;
            }
        }
        return false;
    }

    public boolean isColumnHidden(short column) {
        return columnHelper.getColumn(column).getHidden();
    }

    public boolean isDisplayFormulas() {
    	return getSheetTypeSheetView().getShowFormulas();
    }

    public boolean isDisplayGridlines() {
        return getSheetTypeSheetView().getShowGridLines();
    }

    public boolean isDisplayRowColHeadings() {
        return getSheetTypeSheetView().getShowRowColHeaders();
    }

    public boolean isGridsPrinted() {
    	return isPrintGridlines();
    }

    public boolean isPrintGridlines() {
    	return getSheetTypePrintOptions().getGridLines();
    }

    public boolean isRowBroken(int row) {
        int[] rowBreaks = getRowBreaks();
        if (rowBreaks == null) {
            return false;
        }
        for (int i = 0 ; i < rowBreaks.length ; i++) {
            if (rowBreaks[i] == row) {
                return true;
            }
        }
        return false;
    }

    public void protectSheet(String password) {
        // TODO Auto-generated method stub

    }

    public void removeColumnBreak(short column) {
        CTBreak[] brkArray = getSheetTypeColumnBreaks().getBrkArray();
        for (int i = 0 ; i < brkArray.length ; i++) {
            if (brkArray[i].getId() == column) {
                getSheetTypeColumnBreaks().removeBrk(i);
                continue;
            }
        }
    }

    public void removeMergedRegion(int index) {
        // TODO Auto-generated method stub

    }

    public void removeRow(Row row) {
        int counter = 0;
        for (Iterator<Row> it = rowIterator() ; it.hasNext() ; ) {
            Row r = it.next();
            if (r.getRowNum() == row.getRowNum()) {
                it.remove();
                worksheet.getSheetData().removeRow(counter);
            }
            counter++;
        }
    }

    public void removeRowBreak(int row) {
        CTBreak[] brkArray = getSheetTypeRowBreaks().getBrkArray();
        for (int i = 0 ; i < brkArray.length ; i++) {
            if (brkArray[i].getId() == row) {
                getSheetTypeRowBreaks().removeBrk(i);
                continue;
            }
        }
    }

    public Iterator<Row> rowIterator() {
        return rows.iterator();
    }
    /**
     * Alias for {@link #rowIterator()} to
     *  allow foreach loops
     */
    public Iterator<Row> iterator() {
    	return rowIterator();
    }

    public void setAlternativeExpression(boolean b) {
        // TODO Auto-generated method stub

    }

    public void setAlternativeFormula(boolean b) {
        // TODO Auto-generated method stub

    }

    public void setAutobreaks(boolean b) {
        getSheetTypePageSetUpPr().setAutoPageBreaks(b);
    }

    public void setColumnBreak(short column) {
        if (! isColumnBroken(column)) {
            CTBreak brk = getSheetTypeColumnBreaks().addNewBrk();
            brk.setId(column);
        }
    }

    public void setColumnGroupCollapsed(short columnNumber, boolean collapsed) {
        // TODO Auto-generated method stub

    }

    public void setColumnHidden(short column, boolean hidden) {
        columnHelper.setColHidden(column, hidden);
    }

    public void setColumnWidth(short column, short width) {
        columnHelper.setColWidth(column, width);
    }

    public void setDefaultColumnStyle(short column, CellStyle style) {
        // TODO Auto-generated method stub

    }

    public void setDefaultColumnWidth(short width) {
        getSheetTypeSheetFormatPr().setDefaultColWidth((double) width);
    }

    public void setDefaultRowHeight(short height) {
        getSheetTypeSheetFormatPr().setDefaultRowHeight(height / 20);

    }

    public void setDefaultRowHeightInPoints(float height) {
        getSheetTypeSheetFormatPr().setDefaultRowHeight(height);

    }

    public void setDialog(boolean b) {
        // TODO Auto-generated method stub
    }

    public void setDisplayFormulas(boolean show) {
    	getSheetTypeSheetView().setShowFormulas(show);
    }

	protected CTSheetView getSheetTypeSheetView() {
		if (getDefaultSheetView() == null) {
			getSheetTypeSheetViews().setSheetViewArray(0, CTSheetView.Factory.newInstance());
		}
		return getDefaultSheetView();
	}

    public void setDisplayGridlines(boolean show) {
    	getSheetTypeSheetView().setShowGridLines(show);
    }

    public void setDisplayGuts(boolean b) {
        // TODO Auto-generated method stub

    }

    public void setDisplayRowColHeadings(boolean show) {
    	getSheetTypeSheetView().setShowRowColHeaders(show);
    }

    public void setFitToPage(boolean b) {
        getSheetTypePageSetUpPr().setFitToPage(b);
    }

    public void setGridsPrinted(boolean value) {
    	setPrintGridlines(value);
    }

    public void setHorizontallyCenter(boolean value) {
    	getSheetTypePrintOptions().setHorizontalCentered(value);
    }

    public void setMargin(short margin, double size) {
        CTPageMargins pageMargins = getSheetTypePageMargins();
        switch (margin) {
        case LeftMargin:
            pageMargins.setLeft(size);
        case RightMargin:
            pageMargins.setRight(size);
        case TopMargin:
            pageMargins.setTop(size);
        case BottomMargin:
            pageMargins.setBottom(size);
        case HeaderMargin:
            pageMargins.setHeader(size);
        case FooterMargin:
            pageMargins.setFooter(size);
        }
    }

    public void setPrintGridlines(boolean newPrintGridlines) {
    	getSheetTypePrintOptions().setGridLines(newPrintGridlines);
    }

    public void setProtect(boolean protect) {
        // TODO Auto-generated method stub

    }

    public void setRowBreak(int row) {
        CTPageBreak pageBreak = getSheetTypeRowBreaks();
        if (! isRowBroken(row)) {
            CTBreak brk = pageBreak.addNewBrk();
            brk.setId(row);
        }
    }

    public void setRowGroupCollapsed(int row, boolean collapse) {
        // TODO Auto-generated method stub

    }

    public void setRowSumsBelow(boolean b) {
        // TODO Auto-generated method stub

    }

    public void setRowSumsRight(boolean b) {
        // TODO Auto-generated method stub

    }

    public void setVerticallyCenter(boolean value) {
    	getSheetTypePrintOptions().setVerticalCentered(value);
    }

    // HSSFSheet compatibility methods. See also the following zoom related methods
    public void setZoom(int numerator, int denominator) {
    	setZoom((numerator/denominator) * 100);
    }

    public void setZoom(long scale) {
    	getSheetTypeSheetView().setZoomScale(scale);
    }

    public void setZoomNormal(long scale) {
    	getSheetTypeSheetView().setZoomScaleNormal(scale);
    }

    public void setZoomPageLayoutView(long scale) {
    	getSheetTypeSheetView().setZoomScalePageLayoutView(scale);
    }

    public void setZoomSheetLayoutView(long scale) {
    	getSheetTypeSheetView().setZoomScaleSheetLayoutView(scale);
    }

    public void shiftRows(int startRow, int endRow, int n) {
        // TODO Auto-generated method stub

    }

    public void shiftRows(int startRow, int endRow, int n, boolean copyRowHeight, boolean resetOriginalRowHeight) {
        // TODO Auto-generated method stub

    }

    public void showInPane(short toprow, short leftcol) {
    	CellReference cellReference = new CellReference();
    	String cellRef = cellReference.convertRowColToString(toprow, leftcol);
    	getSheetTypeSheetView().setTopLeftCell(cellRef);
    }

    public void ungroupColumn(short fromColumn, short toColumn) {
        // TODO Auto-generated method stub

    }

    public void ungroupRow(int fromRow, int toRow) {
        // TODO Auto-generated method stub

    }

    public void setSelected(boolean flag) {
        CTSheetViews views = getSheetTypeSheetViews();
        for (CTSheetView view : views.getSheetViewArray()) {
            view.setTabSelected(flag);
        }
    }

	protected CTSheetViews getSheetTypeSheetViews() {
		if (worksheet.getSheetViews() == null) {
			worksheet.setSheetViews(CTSheetViews.Factory.newInstance());
			worksheet.getSheetViews().addNewSheetView();
		}
		return worksheet.getSheetViews();
	}
    
    public boolean isSelected() {
        CTSheetView view = getDefaultSheetView();
        return view != null && view.getTabSelected();
    }

    /**
     * Return the default sheet view. This is the last one if the sheet's views, according to sec. 3.3.1.83
     * of the OOXML spec: "A single sheet view definition. When more than 1 sheet view is defined in the file,
     * it means that when opening the workbook, each sheet view corresponds to a separate window within the 
     * spreadsheet application, where each window is showing the particular sheet. containing the same 
     * workbookViewId value, the last sheetView definition is loaded, and the others are discarded. 
     * When multiple windows are viewing the same sheet, multiple sheetView elements (with corresponding 
     * workbookView entries) are saved."
     */
    private CTSheetView getDefaultSheetView() {
        CTSheetViews views = getSheetTypeSheetViews();
        if (views == null || views.getSheetViewArray() == null || views.getSheetViewArray().length <= 0) {
            return null;
        }
        return views.getSheetViewArray(views.getSheetViewArray().length - 1);
    }
    
    protected XSSFSheet cloneSheet() {
    	XSSFSheet newSheet = new XSSFSheet(this.workbook);
    	newSheet.setSheet((CTSheet)sheet.copy());
        return newSheet;
    }
    
	private void setSheet(CTSheet sheet) {
		this.sheet = sheet;
	}

}
