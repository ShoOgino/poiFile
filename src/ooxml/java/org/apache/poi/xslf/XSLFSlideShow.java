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
package org.apache.poi.xslf;

import java.io.IOException;

import org.apache.poi.POIXMLDocument;
import org.apache.xmlbeans.XmlException;
import org.openxml4j.exceptions.InvalidFormatException;
import org.openxml4j.exceptions.OpenXML4JException;
import org.openxml4j.opc.Package;
import org.openxml4j.opc.PackagePart;
import org.openxml4j.opc.PackageRelationshipCollection;
import org.openxmlformats.schemas.presentationml.x2006.main.CTNotesSlide;
import org.openxmlformats.schemas.presentationml.x2006.main.CTPresentation;
import org.openxmlformats.schemas.presentationml.x2006.main.CTSlide;
import org.openxmlformats.schemas.presentationml.x2006.main.CTSlideIdList;
import org.openxmlformats.schemas.presentationml.x2006.main.CTSlideIdListEntry;
import org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMaster;
import org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMasterIdList;
import org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMasterIdListEntry;
import org.openxmlformats.schemas.presentationml.x2006.main.NotesDocument;
import org.openxmlformats.schemas.presentationml.x2006.main.PresentationDocument;
import org.openxmlformats.schemas.presentationml.x2006.main.SldDocument;
import org.openxmlformats.schemas.presentationml.x2006.main.SldMasterDocument;

/**
 * Experimental class to do low level processing
 *  of pptx files.
 *  
 * If you are using these low level classes, then you
 *  will almost certainly need to refer to the OOXML
 *  specifications from
 *  http://www.ecma-international.org/publications/standards/Ecma-376.htm
 * 
 * WARNING - APIs expected to change rapidly
 */
public class XSLFSlideShow extends POIXMLDocument {
	public static final String MAIN_CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.presentationml.presentation.main+xml";
	public static final String NOTES_CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.presentationml.notesSlide+xml";
	public static final String SLIDE_CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.presentationml.slide+xml";
	public static final String SLIDE_LAYOUT_RELATION_TYPE = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/slideLayout";
	public static final String NOTES_RELATION_TYPE = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/notesSlide";

	private PresentationDocument presentationDoc;
	
	public XSLFSlideShow(Package container) throws OpenXML4JException, IOException, XmlException {
		super(container);
		
		presentationDoc =
			PresentationDocument.Factory.parse(getCorePart().getInputStream());
	}
	public XSLFSlideShow(String file) throws OpenXML4JException, IOException, XmlException {
		this(openPackage(file));
	}
	
	/**
	 * Returns the low level presentation base object
	 */
	public CTPresentation getPresentation() {
		return presentationDoc.getPresentation();
	}
	
	/**
	 * Returns the references from the presentation to its
	 *  slides.
	 * You'll need these to figure out the slide ordering,
	 *  and to get at the actual slides themselves
	 */
	public CTSlideIdList getSlideReferences() {
		return getPresentation().getSldIdLst();
	}
	/**
	 * Returns the references from the presentation to its
	 *  slide masters.
	 * You'll need these to get at the actual slide 
	 *  masters themselves
	 */
	public CTSlideMasterIdList getSlideMasterReferences() {
		return getPresentation().getSldMasterIdLst();
	}
	
	/**
	 * Returns the low level slide master object from
	 *  the supplied slide master reference
	 */
	public CTSlideMaster getSlideMaster(CTSlideMasterIdListEntry master) throws IOException, XmlException {
		try {
			PackagePart masterPart =
				getTargetPart(getCorePart().getRelationship(master.getId2()));
				
			SldMasterDocument masterDoc =
				SldMasterDocument.Factory.parse(masterPart.getInputStream());
			return masterDoc.getSldMaster();
		} catch(InvalidFormatException e) {
			throw new XmlException(e);
		}
	}
	
	/**
	 * Returns the low level slide object from
	 *  the supplied slide reference
	 */
	public CTSlide getSlide(CTSlideIdListEntry slide) throws IOException, XmlException {
		try {
			PackagePart slidePart =
				getTargetPart(getCorePart().getRelationship(slide.getId2()));
			SldDocument slideDoc =
				SldDocument.Factory.parse(slidePart.getInputStream());
			return slideDoc.getSld();
		} catch(InvalidFormatException e) {
			throw new XmlException(e);
		}
	}
	
	/**
	 * Returns the low level notes object for the given
	 *  slide, as found from the supplied slide reference
	 */
	public CTNotesSlide getNotes(CTSlideIdListEntry slide) throws IOException, XmlException {
		PackageRelationshipCollection notes;
		try {
			PackagePart slidePart =
				getTargetPart(getCorePart().getRelationship(slide.getId2()));
		
			notes = slidePart.getRelationshipsByType(NOTES_RELATION_TYPE);
		} catch(InvalidFormatException e) {
			throw new IllegalStateException(e);
		}
		
		if(notes.size() == 0) {
			// No notes for this slide
			return null;
		}
		if(notes.size() > 1) {
			throw new IllegalStateException("Expecting 0 or 1 notes for a slide, but found " + notes.size());
		}
		
		try {
			PackagePart notesPart =
				getTargetPart(notes.getRelationship(0));
			NotesDocument notesDoc =
				NotesDocument.Factory.parse(notesPart.getInputStream());
			
			return notesDoc.getNotes();
		} catch(InvalidFormatException e) {
			throw new IllegalStateException(e);
		}
	}
}
