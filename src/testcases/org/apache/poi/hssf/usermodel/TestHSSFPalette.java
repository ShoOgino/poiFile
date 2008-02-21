
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
        
package org.apache.poi.hssf.usermodel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import junit.framework.TestCase;
import org.apache.poi.hssf.record.PaletteRecord;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.util.TempFile;

/**
 * @author Brian Sanders (bsanders at risklabs dot com)
 */
public class TestHSSFPalette extends TestCase
{
    private PaletteRecord palette;
    private HSSFPalette hssfPalette;
    
    public TestHSSFPalette(String name)
    {
        super(name);
    }
    
    public void setUp()
    {
        palette = new PaletteRecord();
        hssfPalette = new HSSFPalette(palette);
    }
    
    /**
     * Verifies that a custom palette can be created, saved, and reloaded
     */
    public void testCustomPalette() throws IOException
    {
        //reading sample xls
        String dir = System.getProperty("HSSF.testdata.path");
        File sample = new File(dir + "/Simple.xls");
        assertTrue("Simple.xls exists and is readable", sample.canRead());
        FileInputStream fis = new FileInputStream(sample);
        HSSFWorkbook book = new HSSFWorkbook(fis);
        fis.close();
        
        //creating custom palette
        HSSFPalette palette = book.getCustomPalette();
        palette.setColorAtIndex((short) 0x12, (byte) 101, (byte) 230, (byte) 100);
        palette.setColorAtIndex((short) 0x3b, (byte) 0, (byte) 255, (byte) 52);
        
        //writing to disk; reading in and verifying palette
        File temp = TempFile.createTempFile("testCustomPalette", ".xls");
        FileOutputStream fos = new FileOutputStream(temp);
        book.write(fos);
        fos.close();
        
        fis = new FileInputStream(temp);
        book = new HSSFWorkbook(fis);
        fis.close();
        
        palette = book.getCustomPalette();
        HSSFColor color = palette.getColor(HSSFColor.CORAL.index);  //unmodified
        assertNotNull("Unexpected null in custom palette (unmodified index)", color);
        short[] expectedRGB = HSSFColor.CORAL.triplet;
        short[] actualRGB = color.getTriplet();
        String msg = "Expected palette position to remain unmodified";
        assertEquals(msg, expectedRGB[0], actualRGB[0]);
        assertEquals(msg, expectedRGB[1], actualRGB[1]);
        assertEquals(msg, expectedRGB[2], actualRGB[2]);
        
        color = palette.getColor((short) 0x12);
        assertNotNull("Unexpected null in custom palette (modified)", color);
        actualRGB = color.getTriplet();
        msg = "Expected palette modification to be preserved across save";
        assertEquals(msg, (short) 101, actualRGB[0]);
        assertEquals(msg, (short) 230, actualRGB[1]);
        assertEquals(msg, (short) 100, actualRGB[2]);
    }
    
    /**
     * Uses the palette from cell stylings
     */
    public void testPaletteFromCellColours() throws Exception {
        String dir = System.getProperty("HSSF.testdata.path");
        File sample = new File(dir + "/SimpleWithColours.xls");
        assertTrue("SimpleWithColours.xls exists and is readable", sample.canRead());
        FileInputStream fis = new FileInputStream(sample);
        HSSFWorkbook book = new HSSFWorkbook(fis);
        fis.close();
    	
        HSSFPalette p = book.getCustomPalette();
        
        HSSFCell cellA = book.getSheetAt(0).getRow(0).getCell((short)0);
        HSSFCell cellB = book.getSheetAt(0).getRow(1).getCell((short)0);
        HSSFCell cellC = book.getSheetAt(0).getRow(2).getCell((short)0);
        HSSFCell cellD = book.getSheetAt(0).getRow(3).getCell((short)0);
        HSSFCell cellE = book.getSheetAt(0).getRow(4).getCell((short)0);
        
        // Plain
        assertEquals("I'm plain", cellA.getStringCellValue());
        assertEquals(64, cellA.getCellStyle().getFillForegroundColor());
        assertEquals(64, cellA.getCellStyle().getFillBackgroundColor());
        assertEquals(HSSFFont.COLOR_NORMAL, cellA.getCellStyle().getFont(book).getColor());
        assertEquals(0, cellA.getCellStyle().getFillPattern());
        assertEquals("0:0:0", p.getColor((short)64).getHexString());
        assertEquals(null, p.getColor((short)32767));
        
        // Red
        assertEquals("I'm red", cellB.getStringCellValue());
        assertEquals(64, cellB.getCellStyle().getFillForegroundColor());
        assertEquals(64, cellB.getCellStyle().getFillBackgroundColor());
        assertEquals(10, cellB.getCellStyle().getFont(book).getColor());
        assertEquals(0, cellB.getCellStyle().getFillPattern());
        assertEquals("0:0:0", p.getColor((short)64).getHexString());
        assertEquals("FFFF:0:0", p.getColor((short)10).getHexString());
        
        // Red + green bg
        assertEquals("I'm red with a green bg", cellC.getStringCellValue());
        assertEquals(11, cellC.getCellStyle().getFillForegroundColor());
        assertEquals(64, cellC.getCellStyle().getFillBackgroundColor());
        assertEquals(10, cellC.getCellStyle().getFont(book).getColor());
        assertEquals(1, cellC.getCellStyle().getFillPattern());
        assertEquals("0:FFFF:0", p.getColor((short)11).getHexString());
        assertEquals("FFFF:0:0", p.getColor((short)10).getHexString());
        
        // Pink with yellow
        assertEquals("I'm pink with a yellow pattern (none)", cellD.getStringCellValue());
        assertEquals(13, cellD.getCellStyle().getFillForegroundColor());
        assertEquals(64, cellD.getCellStyle().getFillBackgroundColor());
        assertEquals(14, cellD.getCellStyle().getFont(book).getColor());
        assertEquals(0, cellD.getCellStyle().getFillPattern());
        assertEquals("FFFF:FFFF:0", p.getColor((short)13).getHexString());
        assertEquals("FFFF:0:FFFF", p.getColor((short)14).getHexString());
        
        // Pink with yellow - full
        assertEquals("I'm pink with a yellow pattern (full)", cellE.getStringCellValue());
        assertEquals(13, cellE.getCellStyle().getFillForegroundColor());
        assertEquals(64, cellE.getCellStyle().getFillBackgroundColor());
        assertEquals(14, cellE.getCellStyle().getFont(book).getColor());
        assertEquals(0, cellE.getCellStyle().getFillPattern());
        assertEquals("FFFF:FFFF:0", p.getColor((short)13).getHexString());
        assertEquals("FFFF:0:FFFF", p.getColor((short)14).getHexString());
    }
    
    public void testFindSimilar() throws Exception {
    	HSSFWorkbook book = new HSSFWorkbook();
    	HSSFPalette p = book.getCustomPalette();
    	
    	
    	// Add a few edge colours in
    	p.setColorAtIndex((short)8, (byte)-1, (byte)0, (byte)0);
    	p.setColorAtIndex((short)9, (byte)0, (byte)-1, (byte)0);
    	p.setColorAtIndex((short)10, (byte)0, (byte)0, (byte)-1);
    	
    	// And some near a few of them
    	p.setColorAtIndex((short)11, (byte)-1, (byte)2, (byte)2);
    	p.setColorAtIndex((short)12, (byte)-2, (byte)2, (byte)10);
    	p.setColorAtIndex((short)13, (byte)-4, (byte)0, (byte)0);
    	p.setColorAtIndex((short)14, (byte)-8, (byte)0, (byte)0);
    	
    	assertEquals(
    			"FFFF:0:0", p.getColor((short)8).getHexString()
    	);
    	
    	// Now check we get the right stuff back
    	assertEquals(
    			p.getColor((short)8).getHexString(), 
    			p.findSimilarColor((byte)-1, (byte)0, (byte)0).getHexString()
    	);
    	assertEquals(
    			p.getColor((short)8).getHexString(), 
    			p.findSimilarColor((byte)-2, (byte)0, (byte)0).getHexString()
    	);
    	assertEquals(
    			p.getColor((short)8).getHexString(), 
    			p.findSimilarColor((byte)-1, (byte)1, (byte)0).getHexString()
    	);
    	assertEquals(
    			p.getColor((short)11).getHexString(), 
    			p.findSimilarColor((byte)-1, (byte)2, (byte)1).getHexString()
    	);
    	assertEquals(
    			p.getColor((short)12).getHexString(), 
    			p.findSimilarColor((byte)-1, (byte)2, (byte)10).getHexString()
    	);
    }
    
    /**
     * Verifies that the generated gnumeric-format string values match the
     * hardcoded values in the HSSFColor default color palette
     */
    public void testGnumericStrings()
    {
        compareToDefaults(new ColorComparator() {
            public void compare(HSSFColor expected, HSSFColor palette)
            {
                assertEquals(expected.getHexString(), palette.getHexString());
            }
        });
    }
    
    /**
     * Verifies that the palette handles invalid palette indexes
     */
    public void testBadIndexes()
    {
        //too small
        hssfPalette.setColorAtIndex((short) 2, (byte) 255, (byte) 255, (byte) 255);
        //too large
        hssfPalette.setColorAtIndex((short) 0x45, (byte) 255, (byte) 255, (byte) 255);
        
        //should still match defaults; 
        compareToDefaults(new ColorComparator() {
            public void compare(HSSFColor expected, HSSFColor palette)
            {
                short[] s1 = expected.getTriplet();
                short[] s2 = palette.getTriplet();
                assertEquals(s1[0], s2[0]);
                assertEquals(s1[1], s2[1]);
                assertEquals(s1[2], s2[2]);
            }
        });
    }
    
    private void compareToDefaults(ColorComparator c)
    {
        Map colors = HSSFColor.getIndexHash();
        Iterator it = colors.keySet().iterator();
        while (it.hasNext())
        {
            Number index = (Number) it.next();
            HSSFColor expectedColor = (HSSFColor) colors.get(index);
            HSSFColor paletteColor = hssfPalette.getColor(index.shortValue());
            c.compare(expectedColor, paletteColor);
        }
    }

    public void testAddColor() throws Exception
    {
        try
        {
            HSSFColor hssfColor = hssfPalette.addColor((byte)10,(byte)10,(byte)10);
            fail();
        }
        catch ( RuntimeException e )
        {
            // Failing because by default there are no colours left in the palette.
        }
    }

    private static interface ColorComparator
    {
        void compare(HSSFColor expected, HSSFColor palette);
    }
}
