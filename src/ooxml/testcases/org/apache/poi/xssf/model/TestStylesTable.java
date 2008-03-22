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

package org.apache.poi.xssf.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import junit.framework.TestCase;

public class TestStylesTable extends TestCase {
	private File xml;
	
	protected void setUp() throws Exception {
		xml = new File(
				System.getProperty("HSSF.testdata.path") +
				File.separator + "Formatting.xlsx"
		);
		assertTrue(xml.exists());
	}

	public void testCreateNew() throws Exception {
		StylesTable st = new StylesTable();
		
		// Check defaults
		assertNotNull(st._getRawStylesheet());
		assertEquals(1, st._getXfsSize());
		assertEquals(1, st._getStyleXfsSize());
		assertEquals(0, st._getNumberFormatSize());
	}
	
	public void testCreateSaveLoad() throws Exception {
		StylesTable st = new StylesTable();
		
		assertNotNull(st._getRawStylesheet());
		assertEquals(1, st._getXfsSize());
		assertEquals(1, st._getStyleXfsSize());
		assertEquals(0, st._getNumberFormatSize());
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		st.writeTo(baos);
		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		st = new StylesTable(bais);
		
		assertNotNull(st._getRawStylesheet());
		assertEquals(1, st._getXfsSize());
		assertEquals(1, st._getStyleXfsSize());
		assertEquals(0, st._getNumberFormatSize());
	}
	
	public void testLoadExisting() throws Exception {
		XSSFWorkbook workbook = new XSSFWorkbook(xml.toString());
		assertNotNull(workbook.getStylesSource());
		
		StylesTable st = (StylesTable)workbook.getStylesSource();
		
		doTestExisting(st);
	}
	public void testLoadSaveLoad() throws Exception {
		XSSFWorkbook workbook = new XSSFWorkbook(xml.toString());
		assertNotNull(workbook.getStylesSource());
		
		StylesTable st = (StylesTable)workbook.getStylesSource();
		doTestExisting(st);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		st.writeTo(baos);
		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		st = new StylesTable(bais);
		doTestExisting(st);
	}
	public void doTestExisting(StylesTable st) throws Exception {
		// Check contents
		assertNotNull(st._getRawStylesheet());
		assertEquals(11, st._getXfsSize());
		assertEquals(1, st._getStyleXfsSize());
		assertEquals(8, st._getNumberFormatSize());
		
		assertEquals(2, st._getFontsSize());
		assertEquals(2, st._getFillsSize());
		assertEquals(1, st._getBordersSize());
		
		assertEquals("yyyy/mm/dd", st.getNumberFormatAt(165));
		assertEquals("yy/mm/dd", st.getNumberFormatAt(167));
		
		assertNotNull(st.getStyleAt(0));
		assertNotNull(st.getStyleAt(1));
		assertNotNull(st.getStyleAt(2));
		
		assertEquals(0, st.getStyleAt(0).getDataFormat());
		assertEquals(14, st.getStyleAt(1).getDataFormat());
		assertEquals(0, st.getStyleAt(2).getDataFormat());
		assertEquals(165, st.getStyleAt(3).getDataFormat());
		
		assertEquals("yyyy/mm/dd", st.getStyleAt(3).getDataFormatString());
	}
	
	public void testPopulateNew() throws Exception {
		StylesTable st = new StylesTable();
		
		assertNotNull(st._getRawStylesheet());
		assertEquals(1, st._getXfsSize());
		assertEquals(1, st._getStyleXfsSize());
		assertEquals(0, st._getNumberFormatSize());
		
		long nf1 = st.putNumberFormat("yyyy-mm-dd");
		long nf2 = st.putNumberFormat("yyyy-mm-DD");
		assertEquals(nf1, st.putNumberFormat("yyyy-mm-dd"));
		
		st.putStyle(new XSSFCellStyle(st));
		
		// Save and re-load
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		st.writeTo(baos);
		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		st = new StylesTable(bais);
		
		assertNotNull(st._getRawStylesheet());
		assertEquals(2, st._getXfsSize());
		assertEquals(1, st._getStyleXfsSize());
		assertEquals(2, st._getNumberFormatSize());
		
		assertEquals("yyyy-mm-dd", st.getNumberFormatAt(nf1));
		assertEquals(nf1, st.putNumberFormat("yyyy-mm-dd"));
		assertEquals(nf2, st.putNumberFormat("yyyy-mm-DD"));
	}
	
	public void testPopulateExisting() throws Exception {
		XSSFWorkbook workbook = new XSSFWorkbook(xml.toString());
		assertNotNull(workbook.getStylesSource());
		
		StylesTable st = (StylesTable)workbook.getStylesSource();
		assertEquals(11, st._getXfsSize());
		assertEquals(1, st._getStyleXfsSize());
		assertEquals(8, st._getNumberFormatSize());
		
		long nf1 = st.putNumberFormat("YYYY-mm-dd");
		long nf2 = st.putNumberFormat("YYYY-mm-DD");
		assertEquals(nf1, st.putNumberFormat("YYYY-mm-dd"));
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		st.writeTo(baos);
		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		st = new StylesTable(bais);
		
		assertEquals(11, st._getXfsSize());
		assertEquals(1, st._getStyleXfsSize());
		assertEquals(10, st._getNumberFormatSize());
		
		assertEquals("YYYY-mm-dd", st.getNumberFormatAt(nf1));
		assertEquals(nf1, st.putNumberFormat("YYYY-mm-dd"));
		assertEquals(nf2, st.putNumberFormat("YYYY-mm-DD"));
	}
}
