
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
        


package org.apache.poi.hslf.usermodel;


import junit.framework.TestCase;
import org.apache.poi.hslf.*;
import org.apache.poi.hslf.model.*;

/**
 * Tests that SlideShow returns Sheets which have the right text in them
 *
 * @author Nick Burch (nick at torchbox dot com)
 */
public class TestSheetText extends TestCase {
	// SlideShow primed on the test data
	private SlideShow ss;

    public TestSheetText() throws Exception {
		String dirname = System.getProperty("HSLF.testdata.path");
		String filename = dirname + "/basic_test_ppt_file.ppt";
		HSLFSlideShow hss = new HSLFSlideShow(filename);
		ss = new SlideShow(hss);
    }

    public void testSheetOne() throws Exception {
		Sheet slideOne = ss.getSlides()[0];

		String[] expectText = new String[] {"This is a test title","This is a test subtitle\nThis is on page 1"};
		assertEquals(expectText.length, slideOne.getTextRuns().length);
		for(int i=0; i<expectText.length; i++) {
			assertEquals(expectText[i], slideOne.getTextRuns()[i].getText());
		}
    }

	public void testSheetTwo() throws Exception {
		Sheet slideTwo = ss.getSlides()[1];
		String[] expectText = new String[] {"This is the title on page 2","This is page two\nIt has several blocks of text\nNone of them have formatting"};
		assertEquals(expectText.length, slideTwo.getTextRuns().length);
		for(int i=0; i<expectText.length; i++) {
			assertEquals(expectText[i], slideTwo.getTextRuns()[i].getText());
		}
	}
}
