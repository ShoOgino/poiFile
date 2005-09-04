
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
        


package org.apache.poi.hslf.record;

import org.apache.poi.hslf.record.StyleTextPropAtom.*;

import junit.framework.TestCase;
import java.io.ByteArrayOutputStream;
import java.util.LinkedList;

/**
 * Tests that StyleTextPropAtom works properly
 *
 * @author Nick Burch (nick at torchbox dot com)
 */
public class TestStyleTextPropAtom extends TestCase {
	// From a real file: a paragraph with 4 different styles:
	// ??? (21)
	// ??? (17)
	// ??? (16)
	private byte[] data_a = new byte[] { 0, 0, 0xA1-256, 0x0F, 0x2A, 0, 0, 0,
      0x36, 00, 00, 00, 00, 00, 00, 00, 
	  00, 00, 0x15, 00, 00, 00, 00, 00,
      00, 00, 0x11, 00, 00, 00, 00, 00,
	  0x04, 00, 00, 00, 00, 0x05, 0x10, 00,
      00, 00, 00, 00, 0x04, 00, 0xFF-256, 0x33, 00, 0xFE-256
	};
	private int data_a_text_len = 54;

	// From a real file: a paragraph with text in 4 different styles:
	// left aligned+bold (30)
	// centre aligned+italic+blue (28)
	// right aligned+red (25)
	// left aligned+underlined+larger font size (97)
	private byte[] data_b = new byte[] { 
		00, 00, 0xA1-256, 0x0F, 0x80-256, 00, 
		00, 00, 0x1E, 00, 00, 00, 00, 00,
		00, 0x18, 00, 00, 00, 00, 0x50, 00,
		0x1C, 00, 00, 00, 00, 00, 00, 0x10,
	    00, 00, 0x50, 00, 0x19, 00, 00, 00,
	    00, 00, 00, 0x18, 00, 00, 02, 00,
	    0x50, 00, 0x61, 00, 00, 00, 00, 00,
	    00, 0x18, 00, 00, 00, 00, 0x50, 00,
	    0x1E, 00, 00, 00, 01, 00, 02, 00,
	    01, 00, 0x14, 00, 0x1C, 00, 00, 00,
	    02, 00, 06, 00, 02, 00, 0x14, 00,
	    00, 00, 00, 05, 0x19, 00, 00, 00,
	    00, 00, 06, 00, 0x14, 00, 0xFF-256, 0x33,
	    00, 0xFE-256, 0x60, 00, 00, 00, 04, 00,
	    03, 00, 04, 00, 01, 00, 0x18, 00,
	    01, 00, 00, 00, 04, 00, 07, 00,
	    04, 00, 01, 00, 0x18, 00, 0xFF-256, 0x33,
	    00, 0xFE-256
	};
	private int data_b_text_len = 0xB3;
	

    public void testRecordType() throws Exception {
		StyleTextPropAtom stpa = new StyleTextPropAtom(data_a,0,data_a.length);
		StyleTextPropAtom stpb = new StyleTextPropAtom(data_b,0,data_b.length);
		assertEquals(4001l, stpa.getRecordType());
		assertEquals(4001l, stpb.getRecordType());
	}


	public void testCharacterStyleCounts() throws Exception {
		StyleTextPropAtom stpa = new StyleTextPropAtom(data_a,0,data_a.length);
		StyleTextPropAtom stpb = new StyleTextPropAtom(data_b,0,data_b.length);

		// Set for the appropriate text sizes
		stpa.setParentTextSize(data_a_text_len);
		stpb.setParentTextSize(data_b_text_len);

		// In case A, there is a single styling of the characters
		assertEquals(3, stpa.getCharacterStyles().size());
		// In case B, there are 4 different stylings
		assertEquals(4, stpb.getCharacterStyles().size());
	}

	public void testParagraphStyleCounts() throws Exception {
		StyleTextPropAtom stpa = new StyleTextPropAtom(data_a,0,data_a.length);
		StyleTextPropAtom stpb = new StyleTextPropAtom(data_b,0,data_b.length);

		// Set for the appropriate text sizes
		stpa.setParentTextSize(data_a_text_len);
		stpb.setParentTextSize(data_b_text_len);

		// In case A, all has the same spacing and alignment
		assertEquals(1, stpa.getParagraphStyles().size());
		// In case B, all 4 sets have different alignments
		assertEquals(4, stpb.getParagraphStyles().size());
	}


	public void testCharacterStyleLengths() throws Exception {
		StyleTextPropAtom stpa = new StyleTextPropAtom(data_a,0,data_a.length);
		StyleTextPropAtom stpb = new StyleTextPropAtom(data_b,0,data_b.length);

		// Set for the appropriate text sizes
		stpa.setParentTextSize(data_a_text_len);
		stpb.setParentTextSize(data_b_text_len);

		// 54 chars, 21 + 17 + 16
		LinkedList a_ch_l = stpa.getCharacterStyles();
		TextPropCollection a_ch_1 = (TextPropCollection)a_ch_l.get(0);
		TextPropCollection a_ch_2 = (TextPropCollection)a_ch_l.get(1);
		TextPropCollection a_ch_3 = (TextPropCollection)a_ch_l.get(2);
		assertEquals(21, a_ch_1.getCharactersCovered());
		assertEquals(17, a_ch_2.getCharactersCovered());
		assertEquals(16, a_ch_3.getCharactersCovered());

		// 179 chars, 30 + 28 + 25
		LinkedList b_ch_l = stpb.getCharacterStyles();
		TextPropCollection b_ch_1 = (TextPropCollection)b_ch_l.get(0);
		TextPropCollection b_ch_2 = (TextPropCollection)b_ch_l.get(1);
		TextPropCollection b_ch_3 = (TextPropCollection)b_ch_l.get(2);
		TextPropCollection b_ch_4 = (TextPropCollection)b_ch_l.get(3);
		assertEquals(30, b_ch_1.getCharactersCovered());
		assertEquals(28, b_ch_2.getCharactersCovered());
		assertEquals(25, b_ch_3.getCharactersCovered());
		assertEquals(96, b_ch_4.getCharactersCovered());
	}


	public void testCharacterPropOrdering() throws Exception {
		StyleTextPropAtom stpb = new StyleTextPropAtom(data_b,0,data_b.length);
		stpb.setParentTextSize(data_b_text_len);

		LinkedList b_ch_l = stpb.getCharacterStyles();
		TextPropCollection b_ch_1 = (TextPropCollection)b_ch_l.get(0);
		TextPropCollection b_ch_2 = (TextPropCollection)b_ch_l.get(1);
		TextPropCollection b_ch_3 = (TextPropCollection)b_ch_l.get(2);
		TextPropCollection b_ch_4 = (TextPropCollection)b_ch_l.get(3);
		
		// In first set, we get a CharFlagsTextProp and a font.size
		assertEquals(2,b_ch_1.getTextPropList().size());
		TextProp tp_1_1 = (TextProp)b_ch_1.getTextPropList().get(0);
		TextProp tp_1_2 = (TextProp)b_ch_1.getTextPropList().get(1);
		assertEquals(true, tp_1_1 instanceof CharFlagsTextProp);
		assertEquals(true, tp_1_2 instanceof TextProp);
		assertEquals("font.size", tp_1_2.getName());
		assertEquals(20, tp_1_2.getValue());
		
		// In second set, we get a CharFlagsTextProp and a font.size and a font.color
		assertEquals(3,b_ch_2.getTextPropList().size());
		TextProp tp_2_1 = (TextProp)b_ch_2.getTextPropList().get(0);
		TextProp tp_2_2 = (TextProp)b_ch_2.getTextPropList().get(1);
		TextProp tp_2_3 = (TextProp)b_ch_2.getTextPropList().get(2);
		assertEquals(true, tp_2_1 instanceof CharFlagsTextProp);
		assertEquals(true, tp_2_2 instanceof TextProp);
		assertEquals(true, tp_2_3 instanceof TextProp);
		assertEquals("font.size", tp_2_2.getName());
		assertEquals("font.color", tp_2_3.getName());
		assertEquals(20, tp_2_2.getValue());
		
		// In third set, it's just a font.size and a font.color
		assertEquals(2,b_ch_3.getTextPropList().size());
		TextProp tp_3_1 = (TextProp)b_ch_3.getTextPropList().get(0);
		TextProp tp_3_2 = (TextProp)b_ch_3.getTextPropList().get(1);
		assertEquals(true, tp_3_1 instanceof TextProp);
		assertEquals(true, tp_3_2 instanceof TextProp);
		assertEquals("font.size", tp_3_1.getName());
		assertEquals("font.color", tp_3_2.getName());
		assertEquals(20, tp_3_1.getValue());
		
		// In fourth set, we get a CharFlagsTextProp and a font.index and a font.size
		assertEquals(3,b_ch_4.getTextPropList().size());
		TextProp tp_4_1 = (TextProp)b_ch_4.getTextPropList().get(0);
		TextProp tp_4_2 = (TextProp)b_ch_4.getTextPropList().get(1);
		TextProp tp_4_3 = (TextProp)b_ch_4.getTextPropList().get(2);
		assertEquals(true, tp_4_1 instanceof CharFlagsTextProp);
		assertEquals(true, tp_4_2 instanceof TextProp);
		assertEquals(true, tp_4_3 instanceof TextProp);
		assertEquals("font.index", tp_4_2.getName());
		assertEquals("font.size", tp_4_3.getName());
		assertEquals(24, tp_4_3.getValue());
	}

	public void testParagraphProps() throws Exception {
		StyleTextPropAtom stpb = new StyleTextPropAtom(data_b,0,data_b.length);
		stpb.setParentTextSize(data_b_text_len);
		
		LinkedList b_p_l = stpb.getParagraphStyles();
		TextPropCollection b_p_1 = (TextPropCollection)b_p_l.get(0);
		TextPropCollection b_p_2 = (TextPropCollection)b_p_l.get(1);
		TextPropCollection b_p_3 = (TextPropCollection)b_p_l.get(2);
		TextPropCollection b_p_4 = (TextPropCollection)b_p_l.get(3);
		
		// 1st is left aligned + normal line spacing
		assertEquals(2,b_p_1.getTextPropList().size());
		TextProp tp_1_1 = (TextProp)b_p_1.getTextPropList().get(0);
		TextProp tp_1_2 = (TextProp)b_p_1.getTextPropList().get(1);
		assertEquals(true, tp_1_1 instanceof TextProp);
		assertEquals(true, tp_1_2 instanceof TextProp);
		assertEquals("alignment", tp_1_1.getName());
		assertEquals("linespacing", tp_1_2.getName());
		assertEquals(0, tp_1_1.getValue());
		assertEquals(80, tp_1_2.getValue());
		
		// 2nd is centre aligned (default) + normal line spacing
		assertEquals(1,b_p_2.getTextPropList().size());
		TextProp tp_2_1 = (TextProp)b_p_2.getTextPropList().get(0);
		assertEquals(true, tp_2_1 instanceof TextProp);
		assertEquals(true, tp_1_2 instanceof TextProp);
		assertEquals("linespacing", tp_2_1.getName());
		assertEquals(80, tp_2_1.getValue());
		
		// 3rd is right aligned + normal line spacing
		assertEquals(2,b_p_3.getTextPropList().size());
		TextProp tp_3_1 = (TextProp)b_p_3.getTextPropList().get(0);
		TextProp tp_3_2 = (TextProp)b_p_3.getTextPropList().get(1);
		assertEquals(true, tp_3_1 instanceof TextProp);
		assertEquals(true, tp_3_2 instanceof TextProp);
		assertEquals("alignment", tp_3_1.getName());
		assertEquals("linespacing", tp_3_2.getName());
		assertEquals(2, tp_3_1.getValue());
		assertEquals(80, tp_3_2.getValue());
		
		// 4st is left aligned + normal line spacing (despite differing font)
		assertEquals(2,b_p_4.getTextPropList().size());
		TextProp tp_4_1 = (TextProp)b_p_4.getTextPropList().get(0);
		TextProp tp_4_2 = (TextProp)b_p_4.getTextPropList().get(1);
		assertEquals(true, tp_4_1 instanceof TextProp);
		assertEquals(true, tp_4_2 instanceof TextProp);
		assertEquals("alignment", tp_4_1.getName());
		assertEquals("linespacing", tp_4_2.getName());
		assertEquals(0, tp_4_1.getValue());
		assertEquals(80, tp_4_2.getValue());
	}

	public void testCharacterProps() throws Exception {
		StyleTextPropAtom stpb = new StyleTextPropAtom(data_b,0,data_b.length);
		stpb.setParentTextSize(data_b_text_len);

		LinkedList b_ch_l = stpb.getCharacterStyles();
		TextPropCollection b_ch_1 = (TextPropCollection)b_ch_l.get(0);
		TextPropCollection b_ch_2 = (TextPropCollection)b_ch_l.get(1);
		TextPropCollection b_ch_3 = (TextPropCollection)b_ch_l.get(2);
		TextPropCollection b_ch_4 = (TextPropCollection)b_ch_l.get(3);

		// 1st is bold
		CharFlagsTextProp cf_1_1 = (CharFlagsTextProp)b_ch_1.getTextPropList().get(0);
		assertEquals(true,cf_1_1.getSubValue(CharFlagsTextProp.BOLD_IDX));
		assertEquals(false,cf_1_1.getSubValue(CharFlagsTextProp.ITALIC_IDX));
		assertEquals(false,cf_1_1.getSubValue(CharFlagsTextProp.ENABLE_NUMBERING_1_IDX));
		assertEquals(false,cf_1_1.getSubValue(CharFlagsTextProp.ENABLE_NUMBERING_2_IDX));
		assertEquals(false,cf_1_1.getSubValue(CharFlagsTextProp.RELIEF_IDX));
		assertEquals(false,cf_1_1.getSubValue(CharFlagsTextProp.RESET_NUMBERING_IDX));
		assertEquals(false,cf_1_1.getSubValue(CharFlagsTextProp.SHADOW_IDX));
		assertEquals(false,cf_1_1.getSubValue(CharFlagsTextProp.STRIKETHROUGH_IDX));
		assertEquals(false,cf_1_1.getSubValue(CharFlagsTextProp.UNDERLINE_IDX));
		
		// 2nd is italic
		CharFlagsTextProp cf_2_1 = (CharFlagsTextProp)b_ch_2.getTextPropList().get(0);
		assertEquals(false,cf_2_1.getSubValue(CharFlagsTextProp.BOLD_IDX));
		assertEquals(true,cf_2_1.getSubValue(CharFlagsTextProp.ITALIC_IDX));
		assertEquals(false,cf_2_1.getSubValue(CharFlagsTextProp.ENABLE_NUMBERING_1_IDX));
		assertEquals(false,cf_2_1.getSubValue(CharFlagsTextProp.ENABLE_NUMBERING_2_IDX));
		assertEquals(false,cf_2_1.getSubValue(CharFlagsTextProp.RELIEF_IDX));
		assertEquals(false,cf_2_1.getSubValue(CharFlagsTextProp.RESET_NUMBERING_IDX));
		assertEquals(false,cf_2_1.getSubValue(CharFlagsTextProp.SHADOW_IDX));
		assertEquals(false,cf_2_1.getSubValue(CharFlagsTextProp.STRIKETHROUGH_IDX));
		assertEquals(false,cf_2_1.getSubValue(CharFlagsTextProp.UNDERLINE_IDX));

		// 3rd is normal, so lacks a CharFlagsTextProp
		assertFalse(b_ch_3.getTextPropList().get(0) instanceof CharFlagsTextProp);
		
		// 4th is underlinds
		CharFlagsTextProp cf_4_1 = (CharFlagsTextProp)b_ch_4.getTextPropList().get(0);
		assertEquals(false,cf_4_1.getSubValue(CharFlagsTextProp.BOLD_IDX));
		assertEquals(false,cf_4_1.getSubValue(CharFlagsTextProp.ITALIC_IDX));
		assertEquals(false,cf_4_1.getSubValue(CharFlagsTextProp.ENABLE_NUMBERING_1_IDX));
		assertEquals(false,cf_4_1.getSubValue(CharFlagsTextProp.ENABLE_NUMBERING_2_IDX));
		assertEquals(false,cf_4_1.getSubValue(CharFlagsTextProp.RELIEF_IDX));
		assertEquals(false,cf_4_1.getSubValue(CharFlagsTextProp.RESET_NUMBERING_IDX));
		assertEquals(false,cf_4_1.getSubValue(CharFlagsTextProp.SHADOW_IDX));
		assertEquals(false,cf_4_1.getSubValue(CharFlagsTextProp.STRIKETHROUGH_IDX));
		assertEquals(true,cf_4_1.getSubValue(CharFlagsTextProp.UNDERLINE_IDX));
	}


	public void testWrite() throws Exception {
		StyleTextPropAtom stpa = new StyleTextPropAtom(data_a,0,data_a.length);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		stpa.writeOut(baos);
		byte[] b = baos.toByteArray();

		assertEquals(data_a.length, b.length);
		for(int i=0; i<data_a.length; i++) {
			assertEquals(data_a[i],b[i]);
		}
	}

	public void testLoadWrite() throws Exception {
		StyleTextPropAtom stpa = new StyleTextPropAtom(data_a,0,data_a.length);
		stpa.setParentTextSize(data_a_text_len);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		stpa.writeOut(baos);
		byte[] b = baos.toByteArray();

		assertEquals(data_a.length, b.length);
		for(int i=0; i<data_a.length; i++) {
			assertEquals(data_a[i],b[i]);
		}
	}
}
