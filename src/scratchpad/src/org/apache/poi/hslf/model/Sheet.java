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

package org.apache.poi.hslf.model;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ddf.EscherContainerRecord;
import org.apache.poi.ddf.EscherDgRecord;
import org.apache.poi.ddf.EscherDggRecord;
import org.apache.poi.ddf.EscherRecord;
import org.apache.poi.hslf.record.CString;
import org.apache.poi.hslf.record.ColorSchemeAtom;
import org.apache.poi.hslf.record.EscherTextboxWrapper;
import org.apache.poi.hslf.record.MasterTextPropAtom;
import org.apache.poi.hslf.record.OEPlaceholderAtom;
import org.apache.poi.hslf.record.PPDrawing;
import org.apache.poi.hslf.record.Record;
import org.apache.poi.hslf.record.RecordContainer;
import org.apache.poi.hslf.record.RecordTypes;
import org.apache.poi.hslf.record.RoundTripHFPlaceholder12;
import org.apache.poi.hslf.record.SheetContainer;
import org.apache.poi.hslf.record.StyleTextProp9Atom;
import org.apache.poi.hslf.record.StyleTextPropAtom;
import org.apache.poi.hslf.record.TextBytesAtom;
import org.apache.poi.hslf.record.TextCharsAtom;
import org.apache.poi.hslf.record.TextHeaderAtom;
import org.apache.poi.hslf.record.TextRulerAtom;
import org.apache.poi.hslf.record.TextSpecInfoAtom;
import org.apache.poi.hslf.usermodel.SlideShow;
import org.apache.poi.sl.usermodel.ShapeContainer;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;

/**
 * This class defines the common format of "Sheets" in a powerpoint
 * document. Such sheets could be Slides, Notes, Master etc
 *
 * @author Nick Burch
 * @author Yegor Kozlov
 */

public abstract class Sheet implements ShapeContainer<HSLFShape> {
	private static POILogger logger = POILogFactory.getLogger(Sheet.class);

    /**
     * The <code>SlideShow</code> we belong to
     */
    private SlideShow _slideShow;

    /**
     * Sheet background
     */
    private Background _background;

    /**
     * Record container that holds sheet data.
     * For slides it is org.apache.poi.hslf.record.Slide,
     * for notes it is org.apache.poi.hslf.record.Notes,
     * for slide masters it is org.apache.poi.hslf.record.SlideMaster, etc.
     */
    private SheetContainer _container;

    private int _sheetNo;

    public Sheet(SheetContainer container, int sheetNo) {
        _container = container;
        _sheetNo = sheetNo;
    }

    /**
     * Returns an array of all the TextRuns in the sheet.
     */
    public abstract TextRun[] getTextRuns();

    /**
     * Returns the (internal, RefID based) sheet number, as used
     * to in PersistPtr stuff.
     */
    public int _getSheetRefId() {
        return _container.getSheetId();
    }

    /**
     * Returns the (internal, SlideIdentifier based) sheet number, as used
     * to reference this sheet from other records.
     */
    public int _getSheetNumber() {
        return _sheetNo;
    }

    /**
     * Fetch the PPDrawing from the underlying record
     */
    protected PPDrawing getPPDrawing() {
        return _container.getPPDrawing();
    }

    /**
     * Fetch the SlideShow we're attached to
     */
    public SlideShow getSlideShow() {
        return _slideShow;
    }

    /**
     * Return record container for this sheet
     */
    public SheetContainer getSheetContainer() {
        return _container;
    }

    /**
     * Set the SlideShow we're attached to.
     * Also passes it on to our child RichTextRuns
     */
    public void setSlideShow(SlideShow ss) {
        _slideShow = ss;
        TextRun[] trs = getTextRuns();
        if (trs != null) {
            for (int i = 0; i < trs.length; i++) {
                trs[i].supplySlideShow(_slideShow);
            }
        }
    }


    /**
     * For a given PPDrawing, grab all the TextRuns
     */
    public static TextRun[] findTextRuns(PPDrawing ppdrawing) {
        final List<TextRun> runsV = new ArrayList<TextRun>();
        final EscherTextboxWrapper[] wrappers = ppdrawing.getTextboxWrappers();
        for (int i = 0; i < wrappers.length; i++) {
            int s1 = runsV.size();

            // propagate parents to parent-aware records
            RecordContainer.handleParentAwareRecords(wrappers[i]);
            findTextRuns(wrappers[i], runsV);
            int s2 = runsV.size();
            if (s2 != s1){
                TextRun t = runsV.get(runsV.size()-1);
                t.setShapeId(wrappers[i].getShapeId());
            }
        }
        return runsV.toArray(new TextRun[runsV.size()]);
    }
    /**
     * Scans through the supplied record array, looking for
     * a TextHeaderAtom followed by one of a TextBytesAtom or
     * a TextCharsAtom. Builds up TextRuns from these
     *
     * @param records the records to build from
     * @param found   vector to add any found to
     */
    protected static void findTextRuns(final Record[] records, final List<TextRun> found) {
    	findTextRuns(records, found, null); 
    }
    /**
     * Scans through the supplied record array, looking for
     * a TextHeaderAtom followed by one of a TextBytesAtom or
     * a TextCharsAtom. Builds up TextRuns from these
     *
     * @param wrapper an EscherTextboxWrapper
     * @param found   vector to add any found to
     */
    protected static void findTextRuns(final EscherTextboxWrapper wrapper, final List<TextRun> found) {
    	findTextRuns(wrapper.getChildRecords(), found, wrapper.getStyleTextProp9Atom());
    }
    /**
     * Scans through the supplied record array, looking for
     * a TextHeaderAtom followed by one of a TextBytesAtom or
     * a TextCharsAtom. Builds up TextRuns from these
     *
     * @param records the records to build from
     * @param found   vector to add any found to
     * @param styleTextProp9Atom a StyleTextProp9Atom with numbered lists info
     */
    protected static void findTextRuns(final Record[] records, final List<TextRun> found, final StyleTextProp9Atom styleTextProp9Atom) {
        for (int i = 0, slwtIndex=0; i < (records.length - 1); i++) {
            if (records[i] instanceof TextHeaderAtom) {
                TextHeaderAtom tha = (TextHeaderAtom) records[i];
                StyleTextPropAtom stpa = null;
                TextRun trun = null;
                Record next = null;
                Record subs = null;
                
                // See what follows the TextHeaderAtom
                next = records[i+1];
                if (i < records.length - 2) {
                    subs = records[i+2];
                }
                
                // Is the next record one we need to skip over?
                if (subs != null) {
                    if (next instanceof TextRulerAtom ||
                        next instanceof MasterTextPropAtom ||
                        next instanceof TextSpecInfoAtom) {
                        // Ignore this one, check the one after
                        next = subs;
                        if (i < records.length - 3) {
                            subs = records[i+3];
                        } else {
                            subs = null;
                        }
                    }
                }
                
                // Is the subsequent record a style one?
                if (subs != null && subs instanceof StyleTextPropAtom) {
                    stpa = (StyleTextPropAtom)subs;
                }
                
                // Now, check if the next record is one to record
                if (next instanceof TextCharsAtom) {
                    TextCharsAtom tca = (TextCharsAtom)next;
                    trun = new TextRun(tha, tca, stpa);
                } else if (next instanceof TextBytesAtom) {
                    TextBytesAtom tba = (TextBytesAtom)next;
                    trun = new TextRun(tha, tba, stpa);
                } else if (next instanceof StyleTextPropAtom) {
                    stpa = (StyleTextPropAtom)next;
                } else if (next instanceof TextHeaderAtom) {
                    // Seems to be a mostly, but not completely deleted block of
                    //  text. Only the header remains, which isn't useful alone 
                    // Skip on to the next TextHeaderAtom
                    continue;
                } else {
                    logger.log(POILogger.ERROR, "Found a TextHeaderAtom not followed by a TextBytesAtom or TextCharsAtom: Followed by " + next.getRecordType());
                }

                if (trun != null) {
                    List<Record> lst = new ArrayList<Record>();
                    for (int j = i; j < records.length; j++) {
                        if(j > i && records[j] instanceof TextHeaderAtom) break;
                        lst.add(records[j]);
                    }
                    Record[] recs = new Record[lst.size()];
                    lst.toArray(recs);
                    trun._records = recs;
                    trun.setIndex(slwtIndex);
                    trun.setStyleTextProp9Atom(styleTextProp9Atom);
                    found.add(trun);
                    i++;
                } else {
                    // Not a valid one, so skip on to next and look again
                }
                slwtIndex++;
            }
        }
    }

    /**
     * Returns all shapes contained in this Sheet
     *
     * @return all shapes contained in this Sheet (Slide or Notes)
     */
    public HSLFShape[] getShapes() {
        List<HSLFShape> shapeList = getShapeList();
        return shapeList.toArray(new HSLFShape[shapeList.size()]);
    }

    /**
     * Add a new Shape to this Slide
     *
     * @param shape - the Shape to add
     */
    public void addShape(HSLFShape shape) {
        PPDrawing ppdrawing = getPPDrawing();

        EscherContainerRecord dgContainer = (EscherContainerRecord) ppdrawing.getEscherRecords()[0];
        EscherContainerRecord spgr = (EscherContainerRecord) HSLFShape.getEscherChild(dgContainer, EscherContainerRecord.SPGR_CONTAINER);
        spgr.addChildRecord(shape.getSpContainer());

        shape.setSheet(this);
        shape.setShapeId(allocateShapeId());
        shape.afterInsert(this);
    }

    /**
     * Allocates new shape id for the new drawing group id.
     *
     * @return a new shape id.
     */
    public int allocateShapeId()
    {
        EscherDggRecord dgg = _slideShow.getDocumentRecord().getPPDrawingGroup().getEscherDggRecord();
        EscherDgRecord dg = _container.getPPDrawing().getEscherDgRecord();

        dgg.setNumShapesSaved( dgg.getNumShapesSaved() + 1 );

        // Add to existing cluster if space available
        for (int i = 0; i < dgg.getFileIdClusters().length; i++)
        {
            EscherDggRecord.FileIdCluster c = dgg.getFileIdClusters()[i];
            if (c.getDrawingGroupId() == dg.getDrawingGroupId() && c.getNumShapeIdsUsed() != 1024)
            {
                int result = c.getNumShapeIdsUsed() + (1024 * (i+1));
                c.incrementShapeId();
                dg.setNumShapes( dg.getNumShapes() + 1 );
                dg.setLastMSOSPID( result );
                if (result >= dgg.getShapeIdMax())
                    dgg.setShapeIdMax( result + 1 );
                return result;
            }
        }

        // Create new cluster
        dgg.addCluster( dg.getDrawingGroupId(), 0, false );
        dgg.getFileIdClusters()[dgg.getFileIdClusters().length-1].incrementShapeId();
        dg.setNumShapes( dg.getNumShapes() + 1 );
        int result = (1024 * dgg.getFileIdClusters().length);
        dg.setLastMSOSPID( result );
        if (result >= dgg.getShapeIdMax())
            dgg.setShapeIdMax( result + 1 );
        return result;
    }

    /**
     * Removes the specified shape from this sheet.
     *
     * @param shape shape to be removed from this sheet, if present.
     * @return <tt>true</tt> if the shape was deleted.
     */
    public boolean removeShape(HSLFShape shape) {
        PPDrawing ppdrawing = getPPDrawing();

        EscherContainerRecord dg = (EscherContainerRecord) ppdrawing.getEscherRecords()[0];
        EscherContainerRecord spgr = null;

        for (Iterator<EscherRecord> it = dg.getChildIterator(); it.hasNext();) {
            EscherRecord rec = it.next();
            if (rec.getRecordId() == EscherContainerRecord.SPGR_CONTAINER) {
                spgr = (EscherContainerRecord) rec;
                break;
            }
        }
        if(spgr == null) {
            return false;
        }

        List<EscherRecord> lst = spgr.getChildRecords();
        boolean result = lst.remove(shape.getSpContainer());
        spgr.setChildRecords(lst);
        return result;
    }

    /**
     * Called by SlideShow ater a new sheet is created
     */
    public void onCreate(){

    }

    /**
     * Return the master sheet .
     */
    public abstract MasterSheet getMasterSheet();

    /**
     * Color scheme for this sheet.
     */
    public ColorSchemeAtom getColorScheme() {
        return _container.getColorScheme();
    }

    /**
     * Returns the background shape for this sheet.
     *
     * @return the background shape for this sheet.
     */
    public Background getBackground() {
        if (_background == null) {
            PPDrawing ppdrawing = getPPDrawing();

            EscherContainerRecord dg = (EscherContainerRecord) ppdrawing.getEscherRecords()[0];
            EscherContainerRecord spContainer = null;

            for (Iterator<EscherRecord> it = dg.getChildIterator(); it.hasNext();) {
                EscherRecord rec = it.next();
                if (rec.getRecordId() == EscherContainerRecord.SP_CONTAINER) {
                    spContainer = (EscherContainerRecord) rec;
                    break;
                }
            }
            _background = new Background(spContainer, null);
            _background.setSheet(this);
        }
        return _background;
    }

    public void draw(Graphics2D graphics){

    }

    /**
     * Subclasses should call this method and update the array of text runs
     * when a text shape is added
     *
     * @param shape
     */
    protected void onAddTextShape(TextShape shape) {

    }

    /**
     * Return placeholder by text type
     *
     * @param type  type of text, See {@link org.apache.poi.hslf.record.TextHeaderAtom}
     * @return  <code>TextShape</code> or <code>null</code>
     */
    public TextShape getPlaceholderByTextType(int type){
        HSLFShape[] shape = getShapes();
        for (int i = 0; i < shape.length; i++) {
            if(shape[i] instanceof TextShape){
                TextShape tx = (TextShape)shape[i];
                TextRun run = tx.getTextRun();
                if(run != null && run.getRunType() == type){
                    return tx;
                }
            }
        }
        return null;
    }

    /**
     * Search text placeholer by its type
     *
     * @param type  type of placeholder to search. See {@link org.apache.poi.hslf.record.OEPlaceholderAtom}
     * @return  <code>TextShape</code> or <code>null</code>
     */
    public TextShape getPlaceholder(int type){
        HSLFShape[] shape = getShapes();
        for (int i = 0; i < shape.length; i++) {
            if(shape[i] instanceof TextShape){
                TextShape tx = (TextShape)shape[i];
                int placeholderId = 0;
                OEPlaceholderAtom oep = tx.getPlaceholderAtom();
                if(oep != null) {
                    placeholderId = oep.getPlaceholderId();
                } else {
                    //special case for files saved in Office 2007
                    RoundTripHFPlaceholder12 hldr = tx.getClientDataRecord(RecordTypes.RoundTripHFPlaceholder12.typeID);
                    if(hldr != null) placeholderId = hldr.getPlaceholderId();
                }
                if(placeholderId == type){
                    return tx;
                }
            }
        }
        return null;
    }

    /**
     * Return programmable tag associated with this sheet, e.g. <code>___PPT12</code>.
     *
     * @return programmable tag associated with this sheet.
     */
    public String getProgrammableTag(){
        String tag = null;
        RecordContainer progTags = (RecordContainer)
                getSheetContainer().findFirstOfType(
                            RecordTypes.ProgTags.typeID
        );
        if(progTags != null) {
            RecordContainer progBinaryTag = (RecordContainer)
                progTags.findFirstOfType(
                        RecordTypes.ProgBinaryTag.typeID
            );
            if(progBinaryTag != null) {
                CString binaryTag = (CString)
                    progBinaryTag.findFirstOfType(
                            RecordTypes.CString.typeID
                );
                if(binaryTag != null) tag = binaryTag.getText();
            }
        }

        return tag;

    }

    public Iterator<HSLFShape> iterator() {
        return getShapeList().iterator();
    }


    /**
     * Returns all shapes contained in this Sheet
     *
     * @return all shapes contained in this Sheet (Slide or Notes)
     */
    protected List<HSLFShape> getShapeList() {
        PPDrawing ppdrawing = getPPDrawing();

        EscherContainerRecord dg = (EscherContainerRecord) ppdrawing.getEscherRecords()[0];
        EscherContainerRecord spgr = null;

        for (Iterator<EscherRecord> it = dg.getChildIterator(); it.hasNext();) {
            EscherRecord rec = it.next();
            if (rec.getRecordId() == EscherContainerRecord.SPGR_CONTAINER) {
                spgr = (EscherContainerRecord) rec;
                break;
            }
        }
        if (spgr == null) {
            throw new IllegalStateException("spgr not found");
        }

        List<HSLFShape> shapeList = new ArrayList<HSLFShape>();
        Iterator<EscherRecord> it = spgr.getChildIterator();
        if (it.hasNext()) {
            // skip first item
            it.next();
        }
        for (; it.hasNext();) {
            EscherContainerRecord sp = (EscherContainerRecord) it.next();
            HSLFShape sh = ShapeFactory.createShape(sp, null);
            sh.setSheet(this);
            shapeList.add(sh);
        }

        return shapeList;
    }

}
