
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
package org.apache.poi.hslf.usermodel;

import junit.framework.TestCase;
import org.apache.poi.hslf.HSLFSlideShow;
import org.apache.poi.hslf.model.*;
import org.apache.poi.hslf.model.Shape;

import java.io.*;
import java.util.HashSet;
import java.util.HashMap;
import java.util.ArrayList;
import java.awt.*;

/**
 * Testcases for bugs entered in bugzilla
 * the Test name contains the bugzilla bug id
 *
 * @author Yegor Kozlov
 */
public class TestBugs extends TestCase {
    protected String cwd = System.getProperty("HSLF.testdata.path");

    /**
     * Bug 41384: Array index wrong in record creation
     */
    public void test41384() throws Exception {
        FileInputStream is = new FileInputStream(new File(cwd, "41384.ppt"));
        HSLFSlideShow hslf = new HSLFSlideShow(is);
        is.close();

        SlideShow ppt = new SlideShow(hslf);
        assertTrue("No Exceptions while reading file", true);

        assertEquals(1, ppt.getSlides().length);

        PictureData[] pict = ppt.getPictureData();
        assertEquals(2, pict.length);
        assertEquals(Picture.JPEG, pict[0].getType());
        assertEquals(Picture.JPEG, pict[1].getType());
    }

    /**
     * First fix from Bug 42474: NPE in RichTextRun.isBold()
     * when the RichTextRun comes from a Notes model object
     */
    public void test42474_1() throws Exception {
        FileInputStream is = new FileInputStream(new File(cwd, "42474-1.ppt"));
        HSLFSlideShow hslf = new HSLFSlideShow(is);
        is.close();

        SlideShow ppt = new SlideShow(hslf);
        assertTrue("No Exceptions while reading file", true);
        assertEquals(2, ppt.getSlides().length);

        TextRun txrun;
        Notes notes;

        notes = ppt.getSlides()[0].getNotesSheet();
        assertNotNull(notes);
        txrun = notes.getTextRuns()[0];
        assertEquals("Notes-1", txrun.getRawText());
        assertEquals(false, txrun.getRichTextRuns()[0].isBold());

        //notes for the second slide are in bold
        notes = ppt.getSlides()[1].getNotesSheet();
        assertNotNull(notes);
        txrun = notes.getTextRuns()[0];
        assertEquals("Notes-2", txrun.getRawText());
        assertEquals(true, txrun.getRichTextRuns()[0].isBold());

    }

    /**
     * Second fix from Bug 42474: Incorrect matching of notes to slides
     */
    public void test42474_2() throws Exception {
        FileInputStream is = new FileInputStream(new File(cwd, "42474-2.ppt"));
        HSLFSlideShow hslf = new HSLFSlideShow(is);
        is.close();

        SlideShow ppt = new SlideShow(hslf);

        //map slide number and starting phrase of its notes
        HashMap notesMap = new HashMap();
        notesMap.put(new Integer(4), "For  decades before calculators");
        notesMap.put(new Integer(5), "Several commercial applications");
        notesMap.put(new Integer(6), "There are three variations of LNS that are discussed here");
        notesMap.put(new Integer(7), "Although multiply and square root are easier");
        notesMap.put(new Integer(8), "The bus Z is split into Z_H and Z_L");

        Slide[] slide = ppt.getSlides();
        for (int i = 0; i < slide.length; i++) {
            Integer slideNumber = new Integer(slide[i].getSlideNumber());
            Notes notes = slide[i].getNotesSheet();
            if (notesMap.containsKey(slideNumber)){
                assertNotNull(notes);
                String text = notes.getTextRuns()[0].getRawText();
                String startingPhrase = (String)notesMap.get(slideNumber);
                assertTrue("Notes for slide " + slideNumber + " must start with " +
                        startingPhrase , text.startsWith(startingPhrase));
            }
        }
    }

    /**
     * Bug 42485: All TextBoxes inside ShapeGroups have null TextRuns
     */
    public void test42485 () throws Exception {
        FileInputStream is = new FileInputStream(new File(cwd, "42485.ppt"));
        HSLFSlideShow hslf = new HSLFSlideShow(is);
        is.close();

        SlideShow ppt = new SlideShow(hslf);
        Shape[] shape = ppt.getSlides()[0].getShapes();
        for (int i = 0; i < shape.length; i++) {
            if(shape[i] instanceof ShapeGroup){
                ShapeGroup  group = (ShapeGroup)shape[i];
                Shape[] sh = group.getShapes();
                for (int j = 0; j < sh.length; j++) {
                    if( sh[j] instanceof TextBox){
                        TextBox txt = (TextBox)sh[j];
                        assertNotNull(txt.getTextRun());
                    }
                }
            }
        }
    }

    /**
     * Bug 42484: NullPointerException from ShapeGroup.getAnchor()
     */
    public void test42484 () throws Exception {
        FileInputStream is = new FileInputStream(new File(cwd, "42485.ppt")); //test file is the same as for bug 42485
        HSLFSlideShow hslf = new HSLFSlideShow(is);
        is.close();

        SlideShow ppt = new SlideShow(hslf);
        Shape[] shape = ppt.getSlides()[0].getShapes();
        for (int i = 0; i < shape.length; i++) {
            if(shape[i] instanceof ShapeGroup){
                ShapeGroup  group = (ShapeGroup)shape[i];
                assertNotNull(group.getAnchor());
                Shape[] sh = group.getShapes();
                for (int j = 0; j < sh.length; j++) {
                    assertNotNull(sh[j].getAnchor());
                }
            }
        }
        assertTrue("No Exceptions while reading file", true);
    }

    /**
     * Bug 41381: Exception from Slide.getMasterSheet() on a seemingly valid PPT file
     */
    public void test41381() throws Exception {
        FileInputStream is = new FileInputStream(new File(cwd, "alterman_security.ppt"));
        HSLFSlideShow hslf = new HSLFSlideShow(is);
        is.close();

        SlideShow ppt = new SlideShow(hslf);
        assertTrue("No Exceptions while reading file", true);

        assertEquals(1, ppt.getSlidesMasters().length);
        assertEquals(1, ppt.getTitleMasters().length);
        Slide[] slide = ppt.getSlides();
        for (int i = 0; i < slide.length; i++) {
            MasterSheet master = slide[i].getMasterSheet();
            if (i == 0) assertTrue(master instanceof TitleMaster); //the first slide follows TitleMaster
            else assertTrue(master instanceof SlideMaster);
        }
    }

    /**
     * Bug 42486:  Failure parsing a seemingly valid PPT
     */
    public void test42486 () throws Exception {
        FileInputStream is = new FileInputStream(new File(cwd, "42486.ppt"));
        HSLFSlideShow hslf = new HSLFSlideShow(is);
        is.close();

        SlideShow ppt = new SlideShow(hslf);
        Slide[] slide = ppt.getSlides();
        for (int i = 0; i < slide.length; i++) {
            Shape[] shape = slide[i].getShapes();
        }
        assertTrue("No Exceptions while reading file", true);

    }

    /**
     * Bug 42524:  NPE in Shape.getShapeType()
     */
    public void test42524 () throws Exception {
        FileInputStream is = new FileInputStream(new File(cwd, "42486.ppt")); //test file is the same as for Bug 42486
        HSLFSlideShow hslf = new HSLFSlideShow(is);
        is.close();

        SlideShow ppt = new SlideShow(hslf);
        //walk down the tree and see if there were no errors while reading
        Slide[] slide = ppt.getSlides();
        for (int i = 0; i < slide.length; i++) {
            Shape[] shape = slide[i].getShapes();
            for (int j = 0; j < shape.length; j++) {
                assertNotNull(shape[j].getShapeName());
                if (shape[j] instanceof ShapeGroup){
                    ShapeGroup group = (ShapeGroup)shape[j];
                    Shape[] comps = group.getShapes();
                    for (int k = 0; k < comps.length; k++) {
                        assertNotNull(comps[k].getShapeName());
                   }
                }
            }

        }
        assertTrue("No Exceptions while reading file", true);

    }

    /**
     * Bug 42520:  NPE in Picture.getPictureData()
     */
    public void test42520 () throws Exception {
        FileInputStream is = new FileInputStream(new File(cwd, "42520.ppt")); //test file is the same as for Bug 42486
        HSLFSlideShow hslf = new HSLFSlideShow(is);
        is.close();

        SlideShow ppt = new SlideShow(hslf);

        //test case from the bug report
        ShapeGroup shapeGroup = (ShapeGroup)ppt.getSlides()[11].getShapes()[10];
        Picture picture = (Picture)shapeGroup.getShapes()[0];
        picture.getPictureData();

        //walk down the tree and see if there were no errors while reading
        Slide[] slide = ppt.getSlides();
        for (int i = 0; i < slide.length; i++) {
            Shape[] shape = slide[i].getShapes();
            for (int j = 0; j < shape.length; j++) {
              if (shape[j] instanceof ShapeGroup){
                    ShapeGroup group = (ShapeGroup)shape[j];
                    Shape[] comps = group.getShapes();
                    for (int k = 0; k < comps.length; k++) {
                        Shape comp = comps[k];
                        if (comp instanceof Picture){
                            PictureData pict = ((Picture)comp).getPictureData();
                        }
                    }
                }
            }

        }
        assertTrue("No Exceptions while reading file", true);

    }

    /**
     * Bug 38256:  RuntimeException: Couldn't instantiate the class for type with id 0.
     * ( also fixed followup: getTextRuns() returns no text )
     */
    public void test38256 () throws Exception {
        FileInputStream is = new FileInputStream(new File(cwd, "38256.ppt"));
        SlideShow ppt = new SlideShow(is);
        is.close();
         
        assertTrue("No Exceptions while reading file", true);

        Slide[] slide = ppt.getSlides();
        assertEquals(1, slide.length);
        TextRun[] runs = slide[0].getTextRuns();
        assertEquals(4, runs.length);

        HashSet txt = new HashSet();
        txt.add("\u201CHAPPY BIRTHDAY SCOTT\u201D");
        txt.add("Have a HAPPY DAY");
        txt.add("PS Nobody is allowed to hassle Scott TODAY\u2026");
        txt.add("Drinks will be in the Boardroom at 5pm today to celebrate Scott\u2019s B\u2019Day\u2026  See you all there!");

        for (int i = 0; i < runs.length; i++) {
            String text = runs[i].getRawText();
            assertTrue(text, txt.contains(text));
        }

    }

    /**
     * Bug 38256:  RuntimeException: Couldn't instantiate the class for type with id 0.
     * ( also fixed followup: getTextRuns() returns no text )
     */
    public void test43781 () throws Exception {
        FileInputStream is = new FileInputStream(new File(cwd, "43781.ppt"));
        SlideShow ppt = new SlideShow(is);
        is.close();

        assertTrue("No Exceptions while reading file", true);

        Slide slide = ppt.getSlides()[0];
        TextRun[] tr1 = slide.getTextRuns();

        ArrayList lst = new ArrayList();
        Shape[] shape = slide.getShapes();
        for (int i = 0; i < shape.length; i++) {
            if( shape[i] instanceof TextBox){
                TextRun textRun = ((TextBox)shape[i]).getTextRun();
                if(textRun != null) lst.add(textRun);
            }

        }
        TextRun[] tr2 = new TextRun[lst.size()];
        lst.toArray(tr2);

        assertEquals(tr1.length, tr2.length);
        for (int i = 0; i < tr1.length; i++) {
            assertEquals(tr1[i].getText(), tr2[i].getText());
        }
    }
}
