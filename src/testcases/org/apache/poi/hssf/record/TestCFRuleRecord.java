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

package org.apache.poi.hssf.record;

import junit.framework.TestCase;

import org.apache.poi.hssf.record.cf.BorderFormatting;
import org.apache.poi.hssf.record.cf.FontFormatting;
import org.apache.poi.hssf.record.cf.PatternFormatting;
import org.apache.poi.hssf.util.HSSFColor;

/**
 * Tests the serialization and deserialization of the TestCFRuleRecord
 * class works correctly.  
 *
 * @author Dmitriy Kumshayev 
 */
public class TestCFRuleRecord
        extends TestCase
{

    public TestCFRuleRecord(String name)
    {
        super(name);
    }

    public void testCreateCFRuleRecord () 
    {
    	CFRuleRecord record = new CFRuleRecord();
    	testCFRuleRecord(record);
    	
    	// Serialize
    	byte [] serializedRecord = record.serialize();

    	// Strip header
    	byte [] recordData = new byte[serializedRecord.length-4];
   		System.arraycopy(serializedRecord, 4, recordData, 0, recordData.length);
   		
   		// Deserialize
    	record = new CFRuleRecord(new TestcaseRecordInputStream(CFRuleRecord.sid, (short)recordData.length, recordData));
    	
    	// Serialize again
		byte[] output = record.serialize();
    	
		// Compare
		assertEquals("Output size", recordData.length+4, output.length); //includes sid+recordlength
		
		for (int i = 0; i < recordData.length;i++) 
		{
			assertEquals("CFRuleRecord doesn't match", recordData[i], output[i+4]);
		}
    }

	private void testCFRuleRecord(CFRuleRecord record)
	{
		FontFormatting fontFormatting = new FontFormatting();
    	testFontFormattingAccessors(fontFormatting);
    	assertFalse(record.containsFontFormattingBlock());
    	record.setFontFormatting(fontFormatting);
    	assertTrue(record.containsFontFormattingBlock());
    	
    	BorderFormatting borderFormatting = new BorderFormatting();
    	testBorderFormattingAccessors(borderFormatting);
    	assertFalse(record.containsBorderFormattingBlock());
    	record.setBorderFormatting(borderFormatting);
    	assertTrue(record.containsBorderFormattingBlock());
    	
    	assertFalse(record.isLeftBorderModified());
    	record.setLeftBorderModified(true);
    	assertTrue(record.isLeftBorderModified());
    	
    	assertFalse(record.isRightBorderModified());
    	record.setRightBorderModified(true);
    	assertTrue(record.isRightBorderModified());
    	
    	assertFalse(record.isTopBorderModified());
    	record.setTopBorderModified(true);
    	assertTrue(record.isTopBorderModified());
    	
    	assertFalse(record.isBottomBorderModified());
    	record.setBottomBorderModified(true);
    	assertTrue(record.isBottomBorderModified());
    	
    	assertFalse(record.isTopLeftBottomRightBorderModified());
    	record.setTopLeftBottomRightBorderModified(true);
    	assertTrue(record.isTopLeftBottomRightBorderModified());
    	
    	assertFalse(record.isBottomLeftTopRightBorderModified());
    	record.setBottomLeftTopRightBorderModified(true);
    	assertTrue(record.isBottomLeftTopRightBorderModified());
    	
    	
    	PatternFormatting patternFormatting = new PatternFormatting();
    	testPatternFormattingAccessors(patternFormatting);
    	assertFalse(record.containsPatternFormattingBlock());
    	record.setPatternFormatting(patternFormatting);
    	assertTrue(record.containsPatternFormattingBlock());
    	
    	assertFalse(record.isPatternBackgroundColorModified());
    	record.setPatternBackgroundColorModified(true);
    	assertTrue(record.isPatternBackgroundColorModified());
    	
    	assertFalse(record.isPatternColorModified());
    	record.setPatternColorModified(true);
    	assertTrue(record.isPatternColorModified());

    	assertFalse(record.isPatternStyleModified());
    	record.setPatternStyleModified(true);
    	assertTrue(record.isPatternStyleModified());
	}

	private void testPatternFormattingAccessors(PatternFormatting patternFormatting)
	{
		patternFormatting.setFillBackgroundColor(HSSFColor.GREEN.index);
		assertEquals(HSSFColor.GREEN.index,patternFormatting.getFillBackgroundColor());

		patternFormatting.setFillForegroundColor(HSSFColor.INDIGO.index);
		assertEquals(HSSFColor.INDIGO.index,patternFormatting.getFillForegroundColor());
		
		patternFormatting.setFillPattern(PatternFormatting.DIAMONDS);
		assertEquals(PatternFormatting.DIAMONDS,patternFormatting.getFillPattern());
		
	}
	
	private void testBorderFormattingAccessors(BorderFormatting borderFormatting)
	{
		borderFormatting.setBackwardDiagonalOn(false);
		assertFalse(borderFormatting.isBackwardDiagonalOn());
		borderFormatting.setBackwardDiagonalOn(true);
		assertTrue(borderFormatting.isBackwardDiagonalOn());
		
		borderFormatting.setBorderBottom(BorderFormatting.BORDER_DOTTED);
		assertEquals(BorderFormatting.BORDER_DOTTED, borderFormatting.getBorderBottom());
		
		borderFormatting.setBorderDiagonal(BorderFormatting.BORDER_MEDIUM);
		assertEquals(BorderFormatting.BORDER_MEDIUM, borderFormatting.getBorderDiagonal());
		
		borderFormatting.setBorderLeft(BorderFormatting.BORDER_MEDIUM_DASH_DOT_DOT);
		assertEquals(BorderFormatting.BORDER_MEDIUM_DASH_DOT_DOT, borderFormatting.getBorderLeft());

		borderFormatting.setBorderRight(BorderFormatting.BORDER_MEDIUM_DASHED);
		assertEquals(BorderFormatting.BORDER_MEDIUM_DASHED, borderFormatting.getBorderRight());

		borderFormatting.setBorderTop(BorderFormatting.BORDER_HAIR);
		assertEquals(BorderFormatting.BORDER_HAIR, borderFormatting.getBorderTop());

		borderFormatting.setBottomBorderColor(HSSFColor.AQUA.index);
		assertEquals(HSSFColor.AQUA.index, borderFormatting.getBottomBorderColor());

		borderFormatting.setDiagonalBorderColor(HSSFColor.RED.index);
		assertEquals(HSSFColor.RED.index, borderFormatting.getDiagonalBorderColor());

		assertFalse(borderFormatting.isForwardDiagonalOn());
		borderFormatting.setForwardDiagonalOn(true);
		assertTrue(borderFormatting.isForwardDiagonalOn());

		borderFormatting.setLeftBorderColor(HSSFColor.BLACK.index);
		assertEquals(HSSFColor.BLACK.index, borderFormatting.getLeftBorderColor());

		borderFormatting.setRightBorderColor(HSSFColor.BLUE.index);
		assertEquals(HSSFColor.BLUE.index, borderFormatting.getRightBorderColor());

		borderFormatting.setTopBorderColor(HSSFColor.GOLD.index);
		assertEquals(HSSFColor.GOLD.index, borderFormatting.getTopBorderColor());
	}
    
    
	private void testFontFormattingAccessors(FontFormatting fontFormatting)
	{
		// Check for defaults
    	assertFalse(fontFormatting.isEscapementTypeModified());
    	assertFalse(fontFormatting.isFontCancellationModified());
    	assertFalse(fontFormatting.isFontCondenseModified());
    	assertFalse(fontFormatting.isFontOutlineModified());
    	assertFalse(fontFormatting.isFontShadowModified());
    	assertFalse(fontFormatting.isFontStyleModified());
    	assertFalse(fontFormatting.isUnderlineTypeModified());
    	
    	assertFalse(fontFormatting.isBold());
    	assertFalse(fontFormatting.isCondenseOn());
    	assertFalse(fontFormatting.isItalic());
    	assertFalse(fontFormatting.isOutlineOn());
    	assertFalse(fontFormatting.isShadowOn());
    	assertFalse(fontFormatting.isStruckout());

    	assertEquals(0, fontFormatting.getEscapementType());
    	assertEquals(-1, fontFormatting.getFontColorIndex());
    	assertEquals(-1, fontFormatting.getFontHeight());
    	assertEquals(400, fontFormatting.getFontWeight());
    	assertEquals(0, fontFormatting.getUnderlineType());
    	
    	fontFormatting.setBold(true);
    	assertTrue(fontFormatting.isBold());
    	fontFormatting.setBold(false);
    	assertFalse(fontFormatting.isBold());
    	
    	fontFormatting.setCondense(true);
    	assertTrue(fontFormatting.isCondenseOn());
    	fontFormatting.setCondense(false);
    	assertFalse(fontFormatting.isCondenseOn());
    	
    	fontFormatting.setEscapementType(FontFormatting.SS_SUB);
    	assertEquals(FontFormatting.SS_SUB, fontFormatting.getEscapementType());
    	fontFormatting.setEscapementType(FontFormatting.SS_SUPER);
    	assertEquals(FontFormatting.SS_SUPER, fontFormatting.getEscapementType());
    	fontFormatting.setEscapementType(FontFormatting.SS_NONE);
    	assertEquals(FontFormatting.SS_NONE, fontFormatting.getEscapementType());
    	
    	fontFormatting.setEscapementTypeModified(false);
    	assertFalse(fontFormatting.isEscapementTypeModified());
    	fontFormatting.setEscapementTypeModified(true);
    	assertTrue(fontFormatting.isEscapementTypeModified());

    	fontFormatting.setFontCancellationModified(false);
    	assertFalse(fontFormatting.isFontCancellationModified());
    	fontFormatting.setFontCancellationModified(true);
    	assertTrue(fontFormatting.isFontCancellationModified());
    	
    	fontFormatting.setFontColorIndex((short)10);
    	assertEquals(10,fontFormatting.getFontColorIndex());

    	fontFormatting.setFontCondenseModified(false);
    	assertFalse(fontFormatting.isFontCondenseModified());
    	fontFormatting.setFontCondenseModified(true);
    	assertTrue(fontFormatting.isFontCondenseModified());
    	
    	fontFormatting.setFontHeight((short)100);
    	assertEquals(100,fontFormatting.getFontHeight());
    	
    	fontFormatting.setFontOutlineModified(false);
    	assertFalse(fontFormatting.isFontOutlineModified());
    	fontFormatting.setFontOutlineModified(true);
    	assertTrue(fontFormatting.isFontOutlineModified());

    	fontFormatting.setFontShadowModified(false);
    	assertFalse(fontFormatting.isFontShadowModified());
    	fontFormatting.setFontShadowModified(true);
    	assertTrue(fontFormatting.isFontShadowModified());

    	fontFormatting.setFontStyleModified(false);
    	assertFalse(fontFormatting.isFontStyleModified());
    	fontFormatting.setFontStyleModified(true);
    	assertTrue(fontFormatting.isFontStyleModified());

    	fontFormatting.setItalic(false);
    	assertFalse(fontFormatting.isItalic());
    	fontFormatting.setItalic(true);
    	assertTrue(fontFormatting.isItalic());

    	fontFormatting.setOutline(false);
    	assertFalse(fontFormatting.isOutlineOn());
    	fontFormatting.setOutline(true);
    	assertTrue(fontFormatting.isOutlineOn());

    	fontFormatting.setShadow(false);
    	assertFalse(fontFormatting.isShadowOn());
    	fontFormatting.setShadow(true);
    	assertTrue(fontFormatting.isShadowOn());
    	
    	fontFormatting.setStrikeout(false);
    	assertFalse(fontFormatting.isStruckout());
    	fontFormatting.setStrikeout(true);
    	assertTrue(fontFormatting.isStruckout());
    	
    	fontFormatting.setUnderlineType(FontFormatting.U_DOUBLE_ACCOUNTING);
    	assertEquals(FontFormatting.U_DOUBLE_ACCOUNTING, fontFormatting.getUnderlineType());

    	fontFormatting.setUnderlineTypeModified(false);
    	assertFalse(fontFormatting.isUnderlineTypeModified());
    	fontFormatting.setUnderlineTypeModified(true);
    	assertTrue(fontFormatting.isUnderlineTypeModified());
	}
    
    
    public static void main(String[] ignored_args)
	{
		System.out.println("Testing org.apache.poi.hssf.record.CFRuleRecord");
		junit.textui.TestRunner.run(TestCFRuleRecord.class);
	}
    
}
