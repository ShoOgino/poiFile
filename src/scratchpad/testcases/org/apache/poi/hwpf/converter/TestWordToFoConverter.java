/*
 *  ====================================================================
 *    Licensed to the Apache Software Foundation (ASF) under one or more
 *    contributor license agreements.  See the NOTICE file distributed with
 *    this work for additional information regarding copyright ownership.
 *    The ASF licenses this file to You under the Apache License, Version 2.0
 *    (the "License"); you may not use this file except in compliance with
 *    the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 * ====================================================================
 */
package org.apache.poi.hwpf.converter;

import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import junit.framework.TestCase;
import org.apache.poi.POIDataSamples;
import org.apache.poi.hwpf.HWPFDocument;

/**
 * Test cases for {@link WordToFoConverter}
 * 
 * @author Sergey Vladimirov (vlsergey {at} gmail {dot} com)
 */
public class TestWordToFoConverter extends TestCase
{
    private static String getFoText( final String sampleFileName )
            throws Exception
    {
        HWPFDocument hwpfDocument = new HWPFDocument( POIDataSamples
                .getDocumentInstance().openResourceAsStream( sampleFileName ) );

        WordToFoConverter wordToFoConverter = new WordToFoConverter(
                DocumentBuilderFactory.newInstance().newDocumentBuilder()
                        .newDocument() );
        wordToFoConverter.processDocument( hwpfDocument );

        StringWriter stringWriter = new StringWriter();

        Transformer transformer = TransformerFactory.newInstance()
                .newTransformer();
        transformer.setOutputProperty( OutputKeys.INDENT, "yes" );
        transformer.transform(
                new DOMSource( wordToFoConverter.getDocument() ),
                new StreamResult( stringWriter ) );

        String result = stringWriter.toString();
        return result;
    }

    public void testDocumentProperties() throws Exception
    {
        String result = getFoText( "documentProperties.doc" );

        assertTrue( result
                .contains( "<dc:title xmlns:dc=\"http://purl.org/dc/elements/1.1/\">This is document title</dc:title>" ) );
        assertTrue( result
                .contains( "<pdf:Keywords xmlns:pdf=\"http://ns.adobe.com/pdf/1.3/\">This is document keywords</pdf:Keywords>" ) );
    }

    public void testEquation() throws Exception
    {
        final String sampleFileName = "equation.doc";
        String result = getFoText( sampleFileName );

        assertTrue( result
                .contains( "<!--Image link to '0.emf' can be here-->" ) );
    }

    public void testInnerTable() throws Exception
    {
        final String sampleFileName = "innertable.doc";
        String result = getFoText( sampleFileName );

        assertTrue( result
                .contains( "padding-end=\"0.0in\" padding-start=\"0.0in\" width=\"1.0770833in\"" ) );
    }

    public void testHyperlink() throws Exception
    {
        final String sampleFileName = "hyperlink.doc";
        String result = getFoText( sampleFileName );

        assertTrue( result
                .contains( "<fo:basic-link external-destination=\"http://testuri.org/\">" ) );
        assertTrue( result.contains( "Hyperlink text" ) );
    }

    public void testPageref() throws Exception
    {
        final String sampleFileName = "pageref.doc";
        String result = getFoText( sampleFileName );

        System.out.println( result );

        assertTrue( result
                .contains( "<fo:basic-link internal-destination=\"userref\">" ) );
        assertTrue( result.contains( "1" ) );
    }
}
