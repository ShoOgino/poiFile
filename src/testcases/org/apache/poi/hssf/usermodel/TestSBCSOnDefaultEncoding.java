/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2002 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation" and
 *    "Apache POI" must not be used to endorse or promote products
 *    derived from this software without prior written permission. For
 *    written permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 *    "Apache POI", nor may "Apache" appear in their name, without
 *    prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */
package org.apache.poi.hssf.usermodel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;

import junit.framework.TestCase;
import sun.io.Converters;

/**
 * 
 * Test SBCS records works
 * 
 * CAUTION:This may works only under Sun JRE1.4,
 * because there are some dirty way to emulate special env:P
 * 
 * @author Toshiaki Kamoshida(kamoshida.toshiaki at future dot co dot jp)
 */
public class TestSBCSOnDefaultEncoding extends TestCase {

	/**
	 * Constructor for TestSBCSOverDefaultEncoding.
	 * @param arg0
	 */
	public TestSBCSOnDefaultEncoding(String arg0) {
		super(arg0);
	}

	/**
	 * Change Default Encoding on JVM(with dirty way :D)
	 */
	private void setDefaultEncoding(String enc)throws Exception{
		System.setProperty("file.encoding",enc);
		Converters.resetDefaultEncodingName();//CAUTION!
	}
	
	public void testHeaderRecord()throws Exception{
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		setDefaultEncoding("ISO-8859-1");

		HSSFWorkbook src = new HSSFWorkbook();
		HSSFSheet sheet   = src.createSheet();
		HSSFHeader header = sheet.getHeader();
		header.setCenter("ABC123");
		src.write(out);

		setDefaultEncoding("UTF-16");

		HSSFWorkbook dst = new HSSFWorkbook(new ByteArrayInputStream(out.toByteArray()));
		sheet  = dst.getSheetAt(0);
		header = sheet.getHeader();
		assertEquals("ABC123",header.getCenter());
	}

	public void testFooterRecord()throws Exception{
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		setDefaultEncoding("ISO-8859-1");

		HSSFWorkbook src  = new HSSFWorkbook();
		HSSFSheet sheet   = src.createSheet();
		HSSFFooter footer = sheet.getFooter();
		footer.setCenter("ABC123");
		src.write(out);

		setDefaultEncoding("UTF-16");

		HSSFWorkbook dst = new HSSFWorkbook(new ByteArrayInputStream(out.toByteArray()));
		sheet  = dst.getSheetAt(0);
		footer = sheet.getFooter();
		assertEquals("ABC123",footer.getCenter());
	}

	public void testBoundSheetRecord()throws Exception{
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		setDefaultEncoding("ISO-8859-1");

		HSSFWorkbook src  = new HSSFWorkbook();
		src.createSheet();
		src.setSheetName(0,"ABC123");
		src.write(out);

		setDefaultEncoding("UTF-16");

		HSSFWorkbook dst = new HSSFWorkbook(new ByteArrayInputStream(out.toByteArray()));
		assertEquals("ABC123",dst.getSheetName(0));
	}

	public void testNameRecord()throws Exception{
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		setDefaultEncoding("ISO-8859-1");

		HSSFWorkbook src  = new HSSFWorkbook();
		HSSFName name =  src.createName();
		name.setNameName("ABC123");
		src.write(out);

		setDefaultEncoding("UTF-16");

		HSSFWorkbook dst = new HSSFWorkbook(new ByteArrayInputStream(out.toByteArray()));
		name = dst.getNameAt(dst.getNumberOfNames()-1);
		assertEquals("ABC123",name.getNameName());
	}

}
