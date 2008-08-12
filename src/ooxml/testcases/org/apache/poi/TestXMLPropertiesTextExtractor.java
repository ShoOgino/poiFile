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
package org.apache.poi;

import java.io.File;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxml4j.opc.Package;

import junit.framework.TestCase;

public class TestXMLPropertiesTextExtractor extends TestCase {
	private String dirname;
	
	protected void setUp() throws Exception {
		dirname = System.getProperty("OOXML.testdata.path");
		assertTrue( (new File(dirname)).exists() );
	}

	public void testCore() throws Exception {
		org.openxml4j.opc.Package pkg = Package.open(
				(new File(dirname, "ExcelWithAttachments.xlsx")).toString()
		);
		XSSFWorkbook wb = new XSSFWorkbook(pkg);
		
		POIXMLPropertiesTextExtractor ext = new POIXMLPropertiesTextExtractor(wb);
		ext.getText();
		
		// Now check
		String text = ext.getText();
		String cText = ext.getCorePropertiesText();
		
		assertTrue(text.contains("LastModifiedBy = Yury Batrakov"));
		assertTrue(cText.contains("LastModifiedBy = Yury Batrakov"));
	}
	
	public void testExtended() throws Exception {
		org.openxml4j.opc.Package pkg = Package.open(
				(new File(dirname, "ExcelWithAttachments.xlsx")).toString()
		);
		XSSFWorkbook wb = new XSSFWorkbook(pkg);
		
		POIXMLPropertiesTextExtractor ext = new POIXMLPropertiesTextExtractor(wb);
		ext.getText();
		
		// Now check
		String text = ext.getText();
		String eText = ext.getExtendedPropertiesText();
		System.out.println(eText);
		
		assertTrue(text.contains("Application = Microsoft Excel"));
		assertTrue(text.contains("Company = Mera"));
		assertTrue(eText.contains("Application = Microsoft Excel"));
		assertTrue(eText.contains("Company = Mera"));
	}
	
	public void testCustom() throws Exception {
		// TODO!
	}
}
