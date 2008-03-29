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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Palette;
import org.apache.poi.ss.usermodel.PictureData;
import org.apache.poi.ss.usermodel.SharedStringSource;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.StylesSource;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.model.XSSFModel;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.openxml4j.exceptions.InvalidFormatException;
import org.openxml4j.opc.Package;
import org.openxml4j.opc.PackagePart;
import org.openxml4j.opc.PackagePartName;
import org.openxml4j.opc.PackageRelationship;
import org.openxml4j.opc.PackageRelationshipCollection;
import org.openxml4j.opc.PackageRelationshipTypes;
import org.openxml4j.opc.PackagingURIHelper;
import org.openxml4j.opc.TargetMode;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBookView;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBookViews;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDialogsheet;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheet;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbook;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorksheet;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.WorkbookDocument;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.WorksheetDocument;


public class XSSFWorkbook extends POIXMLDocument implements Workbook {
	public static final XSSFRelation WORKBOOK = new XSSFRelation(
			"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet.main+xml",
			"http://schemas.openxmlformats.org/officeDocument/2006/relationships/workbook",
			"/xl/workbook.xml",
			null
	);
	public static final XSSFRelation WORKSHEET = new XSSFRelation(
			"application/vnd.openxmlformats-officedocument.spreadsheetml.worksheet+xml",
			"http://schemas.openxmlformats.org/officeDocument/2006/relationships/worksheet",
			"/xl/worksheets/sheet#.xml",
			null
	);
	public static final XSSFRelation SHARED_STRINGS = new XSSFRelation(
			"application/vnd.openxmlformats-officedocument.spreadsheetml.sharedStrings+xml",
			"http://schemas.openxmlformats.org/officeDocument/2006/relationships/sharedStrings",
			"/xl/sharedStrings.xml",
			SharedStringsTable.class
	);
	public static final XSSFRelation STYLES = new XSSFRelation(
		    "application/vnd.openxmlformats-officedocument.spreadsheetml.styles+xml",
		    "http://schemas.openxmlformats.org/officeDocument/2006/relationships/styles",
		    "/xl/styles.xml",
		    StylesTable.class
	);
	public static final XSSFRelation DRAWINGS = new XSSFRelation(
			"application/vnd.openxmlformats-officedocument.drawingml.chart+xml",
			"http://schemas.openxmlformats.org/officeDocument/2006/relationships/drawing",
			"/xl/drawings/drawing#.xml",
			null
	);
    public static final XSSFRelation IMAGES = new XSSFRelation(
    		null, // TODO
    		"http://schemas.openxmlformats.org/officeDocument/2006/relationships/image",
    		"/xl/image#.xml",
    		null
    );
    
	public static class XSSFRelation {
		private String TYPE;
		private String REL;
		private String DEFAULT_NAME;
		private Class<? extends XSSFModel> CLASS;
		private XSSFRelation(String TYPE, String REL, String DEFAULT_NAME, Class<? extends XSSFModel> CLASS) {
			this.TYPE = TYPE;
			this.REL = REL;
			this.DEFAULT_NAME = DEFAULT_NAME;
			this.CLASS = CLASS;
		}
		public String getContentType() { return TYPE; }
		public String getRelation() { return REL; }
		public String getDefaultFileName() { return DEFAULT_NAME; }

		/**
		 * Load, off the specified core part
		 */
		private XSSFModel load(PackagePart corePart) throws Exception {
			Constructor<? extends XSSFModel> c = CLASS.getConstructor(InputStream.class);
			XSSFModel model = null;
			
            PackageRelationshipCollection prc =
            	corePart.getRelationshipsByType(REL);
            Iterator<PackageRelationship> it = prc.iterator();
            if(it.hasNext()) {
                PackageRelationship rel = it.next();
                PackagePartName relName = PackagingURIHelper.createPartName(rel.getTargetURI());
                PackagePart part = corePart.getPackage().getPart(relName);
                InputStream is = part.getInputStream();
                try {
                	model = c.newInstance(is);
                } finally {
                	is.close();
                }
            } else {
            	log.log(POILogger.WARN, "No part " + DEFAULT_NAME + " found");
            }
            return model;
		}
		/**
		 * Save, with the default name
		 * @return The internal reference ID it was saved at, normally then used as an r:id
		 */
		private String save(XSSFModel model, PackagePart corePart) throws IOException {
			return save(model, corePart, DEFAULT_NAME);
		}
		/**
		 * Save, with the specified name
		 * @return The internal reference ID it was saved at, normally then used as an r:id
		 */
		private String save(XSSFModel model, PackagePart corePart, String name) throws IOException {
            PackagePartName ppName = null;
            try {
            	ppName = PackagingURIHelper.createPartName(name);
            } catch(InvalidFormatException e) {
            	throw new IllegalStateException(e);
            }
            PackageRelationship rel =
            	corePart.addRelationship(ppName, TargetMode.INTERNAL, REL);
            PackagePart part = corePart.getPackage().createPart(ppName, TYPE);
            
            OutputStream out = part.getOutputStream();
            model.writeTo(out);
            out.close();
            
            return rel.getId();
		}
	}
	
    private CTWorkbook workbook;
    
    private List<XSSFSheet> sheets = new LinkedList<XSSFSheet>();
    
    private SharedStringSource sharedStringSource;
    private StylesSource stylesSource;

    private static POILogger log = POILogFactory.getLogger(XSSFWorkbook.class);
    
    public XSSFWorkbook() {
        this.workbook = CTWorkbook.Factory.newInstance();
        CTBookViews bvs = this.workbook.addNewBookViews();
        CTBookView bv = bvs.addNewWorkbookView();
        bv.setActiveTab(0);
        this.workbook.addNewSheets();
        
        // We always require styles and shared strings
        sharedStringSource = new SharedStringsTable();
        stylesSource = new StylesTable();
    }
    
    public XSSFWorkbook(String path) throws IOException {
    	this(openPackage(path));
    }
    public XSSFWorkbook(Package pkg) throws IOException {
        super(pkg);
        try {
            WorkbookDocument doc = WorkbookDocument.Factory.parse(getCorePart().getInputStream());
            this.workbook = doc.getWorkbook();
            
            try {
	            // Load shared strings
	            this.sharedStringSource = (SharedStringSource)
	            	SHARED_STRINGS.load(getCorePart());
            } catch(Exception e) {
            	throw new IOException("Unable to load shared strings - " + e.toString());
            }
            try {
	            // Load styles source
	            this.stylesSource = (StylesSource)
	            	STYLES.load(getCorePart());
            } catch(Exception e) {
            	e.printStackTrace();
            	throw new IOException("Unable to load styles - " + e.toString());
            }
            
            // Load individual sheets
            for (CTSheet ctSheet : this.workbook.getSheets().getSheetArray()) {
                PackagePart part = getPackagePart(ctSheet);
                if (part == null) {
                	log.log(POILogger.WARN, "Sheet with name " + ctSheet.getName() + " and r:id " + ctSheet.getId()+ " was defined, but didn't exist in package, skipping");
                    continue;
                }
                WorksheetDocument worksheetDoc = WorksheetDocument.Factory.parse(part.getInputStream());
                XSSFSheet sheet = new XSSFSheet(ctSheet, worksheetDoc.getWorksheet(), this);
                this.sheets.add(sheet);
            }
        } catch (XmlException e) {
            throw new IOException(e.toString());
        } catch (InvalidFormatException e) {
            throw new IOException(e.toString());
        }
    }

    protected CTWorkbook getWorkbook() {
        return this.workbook;
    }

    /**
     * Get the PackagePart corresponding to a given sheet.
     * 
     * @param ctSheet The sheet
     * @return A PackagePart, or null if no matching part found.
     * @throws InvalidFormatException
     */
    private PackagePart getPackagePart(CTSheet ctSheet) throws InvalidFormatException {
        PackageRelationship rel = this.getCorePart().getRelationship(ctSheet.getId());
        if (rel == null) {
            log.log(POILogger.WARN, "No relationship found for sheet " + ctSheet.getId() + " - core part has " + this.getCorePart().getRelationships().size() + " relations defined");
            return null;
        }
        return getTargetPart(rel);
    }
    
    public int addPicture(byte[] pictureData, int format) {
        // TODO Auto-generated method stub
        return 0;
    }

    public int addSSTString(String string) {
        // TODO Auto-generated method stub
        return 0;
    }

    public Sheet cloneSheet(int sheetNum) {
        XSSFSheet srcSheet = sheets.get(sheetNum);
        String srcName = getSheetName(sheetNum);
        if (srcSheet != null) {
            XSSFSheet clonedSheet = srcSheet.cloneSheet();

            sheets.add(clonedSheet);
            CTSheet newcts = this.workbook.getSheets().addNewSheet();
            newcts.set(clonedSheet.getSheet());
            
            int i = 1;
            while (true) {
                //Try and find the next sheet name that is unique
                String name = srcName;
                String index = Integer.toString(i++);
                if (name.length() + index.length() + 2 < 31) {
                    name = name + "("+index+")";
                } else {
                    name = name.substring(0, 31 - index.length() - 2) + "(" +index + ")";
                }

                //If the sheet name is unique, then set it otherwise move on to the next number.
                if (getSheetIndex(name) == -1) {
                    setSheetName(sheets.size() - 1, name);
                    break;
                }
            }
            return clonedSheet;
        }
        return null;
    }

    public CellStyle createCellStyle() {
    	return new XSSFCellStyle(stylesSource);
    }

    public DataFormat createDataFormat() {
    	return getCreationHelper().createDataFormat();
    }

    public Font createFont() {
        // TODO Auto-generated method stub
        return null;
    }

    public Name createName() {
        // TODO Auto-generated method stub
        return null;
    }

    public Sheet createSheet() {
        return createSheet(null);
    }

    public Sheet createSheet(String sheetname) {
        return createSheet(sheetname, null);
    }
    
    public Sheet createSheet(String sheetname, CTWorksheet worksheet) {
        CTSheet sheet = addSheet(sheetname);
        XSSFWorksheet wrapper = new XSSFWorksheet(sheet, worksheet, this);
        this.sheets.add(wrapper);
        return wrapper;
    }
    
    public Sheet createDialogsheet(String sheetname, CTDialogsheet dialogsheet) {
    	  CTSheet sheet = addSheet(sheetname);
    	  XSSFDialogsheet wrapper = new XSSFDialogsheet(sheet, dialogsheet, this);
    	  this.sheets.add(wrapper);
    	  return wrapper;
    }

	private CTSheet addSheet(String sheetname) {
		CTSheet sheet = workbook.getSheets().addNewSheet();
        if (sheetname != null) {
            sheet.setName(sheetname);
        }
		return sheet;
	}

    public void dumpDrawingGroupRecords(boolean fat) {
        // TODO Auto-generated method stub

    }

    public Font findFont(short boldWeight, short color, short fontHeight, String name, boolean italic, boolean strikeout, short typeOffset, byte underline) {
        // TODO Auto-generated method stub
        return null;
    }

    public List getAllEmbeddedObjects() {
        // TODO Auto-generated method stub
        return null;
    }

    public List<PictureData> getAllPictures() {
        // In OOXML pictures are referred to in sheets
        List<PictureData> pictures = new LinkedList<PictureData>();
        for (CTSheet ctSheet : this.workbook.getSheets().getSheetArray()) {
            try {
                PackagePart sheetPart = getPackagePart(ctSheet);
                if (sheetPart == null) {
                    continue;
                }
                PackageRelationshipCollection prc = sheetPart.getRelationshipsByType(DRAWINGS.getRelation());
                for (PackageRelationship rel : prc) {
                    PackagePart drawingPart = getTargetPart(rel);
                    PackageRelationshipCollection prc2 = drawingPart.getRelationshipsByType(IMAGES.getRelation());
                    for (PackageRelationship rel2 : prc2) {
                        PackagePart imagePart = getTargetPart(rel2);
                        XSSFPictureData pd = new XSSFPictureData(imagePart);
                        pictures.add(pd);
                    }
                }
            } catch (InvalidFormatException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        return pictures;
    }

    public boolean getBackupFlag() {
        // TODO Auto-generated method stub
        return false;
    }

    public byte[] getBytes() {
        // TODO Auto-generated method stub
        return null;
    }

    public CellStyle getCellStyleAt(short idx) {
        // TODO Auto-generated method stub
        return null;
    }

    public Palette getCustomPalette() {
        // TODO Auto-generated method stub
        return null;
    }

    public short getDisplayedTab() {
        // TODO Auto-generated method stub
        return 0;
    }

    public Font getFontAt(short idx) {
        // TODO Auto-generated method stub
        return null;
    }

    public Name getNameAt(int index) {
        // TODO Auto-generated method stub
        return null;
    }

    public int getNameIndex(String name) {
        // TODO Auto-generated method stub
        return 0;
    }

    public String getNameName(int index) {
        // TODO Auto-generated method stub
        return null;
    }

    public short getNumCellStyles() {
        // TODO Auto-generated method stub
        return 0;
    }

    public short getNumberOfFonts() {
        // TODO Auto-generated method stub
        return 0;
    }

    public int getNumberOfNames() {
        // TODO Auto-generated method stub
        return 0;
    }

    public int getNumberOfSheets() {
        return this.workbook.getSheets().sizeOfSheetArray();
    }

    public String getPrintArea(int sheetIndex) {
        // TODO Auto-generated method stub
        return null;
    }

    public String getSSTString(int index) {
        return getSharedStringSource().getSharedStringAt(index);
    }

    public short getSelectedTab() {
        short i = 0;
        for (XSSFSheet sheet : this.sheets) {
            if (sheet.isSelected()) {
                return i;
            }
            ++i;
        }
        return -1;
    }
    
    /**
     * Doesn't do anything - returns the same index
     * TODO - figure out if this is a ole2 specific thing, or
     *  if we need to do something proper here too!
     */
    public int getSheetIndexFromExternSheetIndex(int externSheetNumber) {
		return externSheetNumber;
	}

	public Sheet getSheet(String name) {
        CTSheet[] sheets = this.workbook.getSheets().getSheetArray();  
        for (int i = 0 ; i < sheets.length ; ++i) {
            if (name.equals(sheets[i].getName())) {
                return this.sheets.get(i);
            }
        }
        return null;
    }

    public Sheet getSheetAt(int index) {
        return this.sheets.get(index);
    }

    public int getSheetIndex(String name) {
        CTSheet[] sheets = this.workbook.getSheets().getSheetArray();  
        for (int i = 0 ; i < sheets.length ; ++i) {
            if (name.equals(sheets[i].getName())) {
                return i;
            }
        }
        return -1;
    }

    public int getSheetIndex(Sheet sheet) {
        return this.sheets.indexOf(sheet);
    }

    public String getSheetName(int sheet) {
        return this.workbook.getSheets().getSheetArray(sheet).getName();
    }

    public void insertChartRecord() {
        // TODO Auto-generated method stub

    }

    public void removeName(int index) {
        // TODO Auto-generated method stub

    }

    public void removeName(String name) {
        // TODO Auto-generated method stub

    }

    public void removePrintArea(int sheetIndex) {
        // TODO Auto-generated method stub

    }

    public void removeSheetAt(int index) {
        this.sheets.remove(index);
        this.workbook.getSheets().removeSheet(index);
    }

    public void setBackupFlag(boolean backupValue) {
        // TODO Auto-generated method stub

    }

    public void setDisplayedTab(short index) {
        // TODO Auto-generated method stub

    }

    public void setPrintArea(int sheetIndex, String reference) {
        // TODO Auto-generated method stub

    }

    public void setPrintArea(int sheetIndex, int startColumn, int endColumn, int startRow, int endRow) {
        // TODO Auto-generated method stub

    }

    public void setRepeatingRowsAndColumns(int sheetIndex, int startColumn, int endColumn, int startRow, int endRow) {
        // TODO Auto-generated method stub

    }

    /**
     * We only set one sheet as selected for compatibility with HSSF.
     */
    public void setSelectedTab(short index) {
        for (int i = 0 ; i < this.sheets.size() ; ++i) {
            XSSFSheet sheet = this.sheets.get(i);
            sheet.setSelected(i == index);
        }
    }

    public void setSheetName(int sheet, String name) {
        this.workbook.getSheets().getSheetArray(sheet).setName(name);
    }

    public void setSheetName(int sheet, String name, short encoding) {
        this.workbook.getSheets().getSheetArray(sheet).setName(name);
    }

    public void setSheetOrder(String sheetname, int pos) {
        int idx = getSheetIndex(sheetname);
        sheets.add(pos, sheets.remove(idx));
        // Reorder CTSheets
        XmlObject cts = this.workbook.getSheets().getSheetArray(idx).copy();
        this.workbook.getSheets().removeSheet(idx);
        CTSheet newcts = this.workbook.getSheets().insertNewSheet(pos);
        newcts.set(cts);
    }

    public void unwriteProtectWorkbook() {
        // TODO Auto-generated method stub

    }

    public void write(OutputStream stream) throws IOException {

        try {
            // Create a package referring the temp file.
            Package pkg = Package.create(stream);
            // Main part
            PackagePartName corePartName = PackagingURIHelper.createPartName("/xl/workbook.xml");
            // Create main part relationship
            pkg.addRelationship(corePartName, TargetMode.INTERNAL, PackageRelationshipTypes.CORE_DOCUMENT, "rId1");
            // Create main document part
            PackagePart corePart = pkg.createPart(corePartName, WORKBOOK.getContentType());
            OutputStream out;

            XmlOptions xmlOptions = new XmlOptions();
            // Requests use of whitespace for easier reading
            xmlOptions.setSavePrettyPrint();
            xmlOptions.setSaveOuter();
            xmlOptions.setUseDefaultNamespace();
            
            // Write out our sheets, updating the references
            //  to them in the main workbook as we go
            for (int i=0 ; i < this.getNumberOfSheets(); i++) {
            	int sheetNumber = (i+1);
            	XSSFSheet sheet = (XSSFSheet) this.getSheetAt(i);
                PackagePartName partName = PackagingURIHelper.createPartName("/xl/worksheets/sheet" + sheetNumber + ".xml");
                PackageRelationship rel =
                	 corePart.addRelationship(partName, TargetMode.INTERNAL, WORKSHEET.getRelation(), "rSheet" + sheetNumber);
                PackagePart part = pkg.createPart(partName, WORKSHEET.getContentType());
                 
                // XXX This should not be needed, but apparently the setSaveOuter call above does not work in XMLBeans 2.2
                xmlOptions.setSaveSyntheticDocumentElement(new QName(CTWorksheet.type.getName().getNamespaceURI(), "worksheet"));
                out = part.getOutputStream();
                sheet.save(out, xmlOptions);
                out.close();
                 
                // Update our internal reference for the package part
                workbook.getSheets().getSheetArray(i).setId(rel.getId());
                workbook.getSheets().getSheetArray(i).setSheetId(sheetNumber);
            }
             
            // Write shared strings and styles
            if(sharedStringSource != null) {
	             SharedStringsTable sst = (SharedStringsTable)sharedStringSource;
	             SHARED_STRINGS.save(sst, corePart);
            }
            if(stylesSource != null) {
	             StylesTable st = (StylesTable)stylesSource;
	             STYLES.save(st, corePart);
            }

            // Now we can write out the main Workbook, with
            //  the correct references to the other parts
            out = corePart.getOutputStream();
            // XXX This should not be needed, but apparently the setSaveOuter call above does not work in XMLBeans 2.2
            xmlOptions.setSaveSyntheticDocumentElement(new QName(CTWorkbook.type.getName().getNamespaceURI(), "workbook"));
            workbook.save(out, xmlOptions);
            out.close();
             
            //  All done
            pkg.close();
        } catch (InvalidFormatException e) {
            // TODO: replace with more meaningful exception
            throw new RuntimeException(e);
        }
    }

    public void writeProtectWorkbook(String password, String username) {
        // TODO Auto-generated method stub

    }

    public SharedStringSource getSharedStringSource() {
        return this.sharedStringSource;
    }
    protected void setSharedStringSource(SharedStringSource sharedStringSource) {
        this.sharedStringSource = sharedStringSource;
    }
    
    public StylesSource getStylesSource() {
    	return this.stylesSource;
    }
    protected void setStylesSource(StylesSource stylesSource) {
    	this.stylesSource = stylesSource;
    }

    public CreationHelper getCreationHelper() {
    	return new XSSFCreationHelper(this);
    }
}
