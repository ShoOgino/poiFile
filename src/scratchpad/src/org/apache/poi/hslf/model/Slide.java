
/* ====================================================================
   Copyright 2002-2004   Apache Software Foundation

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
        


package org.apache.poi.hslf.model;

import java.util.Vector;

import org.apache.poi.hslf.record.PPDrawing;
import org.apache.poi.hslf.record.SlideAtom;
import org.apache.poi.hslf.record.SlideListWithText.SlideAtomsSet;

/**
 * This class represents a slide in a PowerPoint Document. It allows 
 *  access to the text within, and the layout. For now, it only does
 *  the text side of things though
 *
 * @author Nick Burch
 */

public class Slide extends Sheet
{
  private int _refSheetNo;  
  private int _sheetNo;
  private int _slideNo;
  private org.apache.poi.hslf.record.Slide _slide;
  private SlideAtomsSet _atomSet;
  private TextRun[] _runs;
  private TextRun[] _otherRuns; // Any from the PPDrawing, shouldn't really be any though
  private Notes _notes; // usermodel needs to set this

  /**
   * Constructs a Slide from the Slide record, and the SlideAtomsSet
   *  containing the text.
   * Initialises TextRuns, to provide easier access to the text
   *
   * @param slide the Slide record we're based on
   * @param notes the Notes sheet attached to us
   * @param atomSet the SlideAtomsSet to get the text from
   */
  public Slide(org.apache.poi.hslf.record.Slide slide, Notes notes, SlideAtomsSet atomSet, int slideIdentifier, int slideNumber) {
	_slide = slide;
	_notes = notes;
	_atomSet = atomSet;
	_refSheetNo = slide.getSheetId();
	_sheetNo = slideIdentifier;
	_slideNo = slideNumber;

	// Grab the TextRuns from the PPDrawing
	_otherRuns = findTextRuns(_slide.getPPDrawing());

	// For the text coming in from the SlideAtomsSet:
	// Build up TextRuns from pairs of TextHeaderAtom and
	//  one of TextBytesAtom or TextCharsAtom
	Vector textRuns = new Vector();
	if(_atomSet != null) {
		findTextRuns(_atomSet.getSlideRecords(),textRuns);
	} else {
		// No text on the slide, must just be pictures
	}

	// Build an array, more useful than a vector
	_runs = new TextRun[textRuns.size()+_otherRuns.length];
	// Grab text from SlideListWithTexts entries
	int i=0;
	for(i=0; i<textRuns.size(); i++) {
		_runs[i] = (TextRun)textRuns.get(i);
	}
	// Grab text from slide's PPDrawing
	for(int k=0; k<_otherRuns.length; i++, k++) {
		_runs[i] = _otherRuns[k];
	}
  }
  
  /**
   * Create a new Slide instance
   * @param sheetNumber The internal number of the sheet, as used by PersistPtrHolder
   * @param slideNumber The user facing number of the sheet
   */
  public Slide(int sheetNumber, int sheetRefId, int slideNumber){
	_slide = new org.apache.poi.hslf.record.Slide();
	_refSheetNo = sheetRefId;
	_sheetNo = sheetNumber;
	_slideNo = slideNumber;
  }

  /**
   * Sets the Notes that are associated with this. Updates the
   *  references in the records to point to the new ID
   */
  public void setNotes(Notes notes) {
	_notes = notes;

	// Update the Slide Atom's ID of where to point to
	SlideAtom sa = _slide.getSlideAtom();

	if(notes == null) {
		// Set to 0
		sa.setNotesID(0);
	} else {
		// Set to the value from the notes' sheet id
		sa.setNotesID(notes._getSheetNumber());
	}
  }


  // Accesser methods follow

  /**
   * Returns an array of all the TextRuns found
   */
  public TextRun[] getTextRuns() { return _runs; }

  /**
   * Returns the (internal, RefID based) sheet number, as used 
   *  to in PersistPtr stuff.
   */
  public int _getSheetRefId() { return _refSheetNo; }
  /**
   * Returns the (internal, SlideIdentifier based) sheet number
   * @see getSlideNumber()
   */
  public int _getSheetNumber() { return _sheetNo; }
  
  /**
   * Returns the (public facing) page number of this slide
   */
  public int getSlideNumber() { return _slideNo; }
  
  /**
   * Returns the underlying slide record
   */
  public org.apache.poi.hslf.record.Slide getSlideRecord() { return _slide; }

  /**
   * Returns the Notes Sheet for this slide, or null if there isn't one
   */
  public Notes getNotesSheet() { return _notes; }
  
  protected PPDrawing getPPDrawing() { return _slide.getPPDrawing(); }
} 
