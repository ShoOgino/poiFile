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


import org.apache.poi.stress.*;
import org.apache.tools.ant.DirectoryScanner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 *  This is an integration test which performs various actions on all stored test-files and tries
 *  to reveal problems which are introduced, but not covered (yet) by unit tests. 
 * 
 *  This test looks for any file under the test-data directory and tries to do some useful 
 *  processing with it based on it's type.
 * 
 *  The test is implemented as a junit {@link Parameterized} test, which leads
 *  to one test-method call for each file (currently around 950 files are handled). 
 * 
 *  There is a a mapping of extension to implementations of the interface 
 *  {@link FileHandler} which defines how the file is loaded and which actions are 
 *  tried with the file. 
 *  
 *  The test can be expanded by adding more actions to the FileHandlers, this automatically
 *  applies the action to any such file in our test-data repository.
 *  
 *  There is also a list of files that should actually fail.
 *    
 *  Note: It is also a test-failure if a file that is expected to fail now actually works, 
 *  i.e. if a bug was fixed in POI itself, the file should be removed from the expected-failures 
 *  here as well! This is to ensure that files that should not work really do not work, e.g.  
 *  that we do not remove expected sanity checks.
 */
@RunWith(Parameterized.class)
public class TestAllFiles {
    private static final File ROOT_DIR = new File("test-data");

    static final String[] SCAN_EXCLUDES = new String[] { "**/.svn/**", "lost+found" };
    
    // map file extensions to the actual mappers
    static final Map<String, FileHandler> HANDLERS = new HashMap<String, FileHandler>();
    static {
        // Excel
        HANDLERS.put(".xls", new HSSFFileHandler());
        HANDLERS.put(".xlsx", new XSSFFileHandler());
        HANDLERS.put(".xlsm", new XSSFFileHandler());
        HANDLERS.put(".xltx", new XSSFFileHandler());
        HANDLERS.put(".xlsb", new XSSFFileHandler());

        // Word
        HANDLERS.put(".doc", new HWPFFileHandler());
        HANDLERS.put(".docx", new XWPFFileHandler());
        HANDLERS.put(".dotx", new XWPFFileHandler());
        HANDLERS.put(".docm", new XWPFFileHandler());

        // OpenXML4J files
        HANDLERS.put(".ooxml", new OPCFileHandler());		// OPCPackage
        HANDLERS.put(".zip", new OPCFileHandler());      // OPCPackage

        // Powerpoint
        HANDLERS.put(".ppt", new HSLFFileHandler());
        HANDLERS.put(".pptx", new XSLFFileHandler());
        HANDLERS.put(".pptm", new XSLFFileHandler());
        HANDLERS.put(".ppsm", new XSLFFileHandler());
        HANDLERS.put(".ppsx", new XSLFFileHandler());
        HANDLERS.put(".thmx", new XSLFFileHandler());
        HANDLERS.put(".potx", new XSLFFileHandler());

        // Outlook
        HANDLERS.put(".msg", new HSMFFileHandler());

        // Publisher
        HANDLERS.put(".pub", new HPBFFileHandler());

        // Visio - binary
        HANDLERS.put(".vsd", new HDGFFileHandler());
        
        // Visio - ooxml
        HANDLERS.put(".vsdm", new XDGFFileHandler());
        HANDLERS.put(".vsdx", new XDGFFileHandler());
        HANDLERS.put(".vssm", new XDGFFileHandler());
        HANDLERS.put(".vssx", new XDGFFileHandler());
        HANDLERS.put(".vstm", new XDGFFileHandler());
        HANDLERS.put(".vstx", new XDGFFileHandler());

        // Visio - not handled yet
        HANDLERS.put(".vst", new NullFileHandler());
        HANDLERS.put(".vss", new NullFileHandler());

        // POIFS
        HANDLERS.put(".ole2", new POIFSFileHandler());

        // Microsoft Admin Template?
        HANDLERS.put(".adm", new HPSFFileHandler());

        // Microsoft TNEF
        HANDLERS.put(".dat", new HMEFFileHandler());

        // TODO: are these readable by some of the formats?
        HANDLERS.put(".shw", new NullFileHandler());
        HANDLERS.put(".zvi", new NullFileHandler());
        HANDLERS.put(".mpp", new NullFileHandler());
        HANDLERS.put(".qwp", new NullFileHandler());
        HANDLERS.put(".wps", new NullFileHandler());
        HANDLERS.put(".bin", new NullFileHandler());
        HANDLERS.put(".xps", new NullFileHandler());
        HANDLERS.put(".sldprt", new NullFileHandler());
        HANDLERS.put(".mdb", new NullFileHandler());
        HANDLERS.put(".vml", new NullFileHandler());

        // ignore some file types, images, other formats, ...
        HANDLERS.put(".txt", new NullFileHandler());
        HANDLERS.put(".pdf", new NullFileHandler());
        HANDLERS.put(".rtf", new NullFileHandler());
        HANDLERS.put(".gif", new NullFileHandler());
        HANDLERS.put(".html", new NullFileHandler());
        HANDLERS.put(".png", new NullFileHandler());
        HANDLERS.put(".wmf", new NullFileHandler());
        HANDLERS.put(".emf", new NullFileHandler());
        HANDLERS.put(".dib", new NullFileHandler());
        HANDLERS.put(".svg", new NullFileHandler());
        HANDLERS.put(".pict", new NullFileHandler());
        HANDLERS.put(".jpg", new NullFileHandler());
        HANDLERS.put(".jpeg", new NullFileHandler());
        HANDLERS.put(".tif", new NullFileHandler());
        HANDLERS.put(".tiff", new NullFileHandler());
        HANDLERS.put(".wav", new NullFileHandler());
        HANDLERS.put(".pfx", new NullFileHandler());
        HANDLERS.put(".xml", new NullFileHandler());
        HANDLERS.put(".csv", new NullFileHandler());
        HANDLERS.put(".ods", new NullFileHandler());
        // VBA source files
        HANDLERS.put(".vba", new NullFileHandler());
        HANDLERS.put(".bas", new NullFileHandler());
        HANDLERS.put(".frm", new NullFileHandler());
        HANDLERS.put(".frx", new NullFileHandler()); //binary
        HANDLERS.put(".cls", new NullFileHandler());

        // map some files without extension
        HANDLERS.put("spreadsheet/BigSSTRecord", new NullFileHandler());
        HANDLERS.put("spreadsheet/BigSSTRecord2", new NullFileHandler());
        HANDLERS.put("spreadsheet/BigSSTRecord2CR1", new NullFileHandler());
        HANDLERS.put("spreadsheet/BigSSTRecord2CR2", new NullFileHandler());
        HANDLERS.put("spreadsheet/BigSSTRecord2CR3", new NullFileHandler());
        HANDLERS.put("spreadsheet/BigSSTRecord2CR4", new NullFileHandler());
        HANDLERS.put("spreadsheet/BigSSTRecord2CR5", new NullFileHandler());
        HANDLERS.put("spreadsheet/BigSSTRecord2CR6", new NullFileHandler());
        HANDLERS.put("spreadsheet/BigSSTRecord2CR7", new NullFileHandler());
        HANDLERS.put("spreadsheet/BigSSTRecordCR", new NullFileHandler());
        HANDLERS.put("spreadsheet/test_properties1", new NullFileHandler());
    }

    private static final Set<String> hashSet(String... a) {
        return new HashSet<String>(Arrays.asList(a));
    }

    // Old Word Documents where we can at least extract some text
    private static final Set<String> OLD_FILES = hashSet(
        "document/Bug49933.doc",
        "document/Bug51944.doc",
        "document/Word6.doc",
        "document/Word6_sections.doc",
        "document/Word6_sections2.doc",
        "document/Word95.doc",
        "document/word95err.doc",
        "hpsf/TestMickey.doc",
        "document/52117.doc"
    );

    private static final Set<String> EXPECTED_FAILURES = hashSet(
        // password protected files
        "spreadsheet/password.xls",
        "spreadsheet/protected_passtika.xlsx",
        "spreadsheet/51832.xls",
        "document/PasswordProtected.doc",
        "slideshow/Password_Protected-hello.ppt",
        "slideshow/Password_Protected-56-hello.ppt",
        "slideshow/Password_Protected-np-hello.ppt",
        "slideshow/cryptoapi-proc2356.ppt",
        //"document/bug53475-password-is-pass.docx",
        //"document/bug53475-password-is-solrcell.docx",
        "spreadsheet/xor-encryption-abc.xls",
        "spreadsheet/35897-type4.xls",
        //"poifs/protect.xlsx",
        //"poifs/protected_sha512.xlsx",
        //"poifs/extenxls_pwd123.xlsx",
        //"poifs/protected_agile.docx",
        "spreadsheet/58616.xlsx",
        "poifs/60320-protected.xlsx",

        // TODO: fails XMLExportTest, is this ok?
        "spreadsheet/CustomXMLMapping-singleattributenamespace.xlsx",
        "spreadsheet/55864.xlsx",
        "spreadsheet/57890.xlsx",

        // TODO: these fail now with some NPE/file read error because we now try to compute every value via Cell.toString()!
        "spreadsheet/44958.xls",
        "spreadsheet/44958_1.xls",
        "spreadsheet/testArraysAndTables.xls",

        // TODO: good to ignore?
        "spreadsheet/sample-beta.xlsx",

        // This is actually a spreadsheet!
        "hpsf/TestRobert_Flaherty.doc",

        // some files that are broken, eg Word 95, ...
        "spreadsheet/43493.xls",
        "spreadsheet/46904.xls",
        "document/Bug50955.doc",
        "document/57843.doc",
        "slideshow/PPT95.ppt",
        "openxml4j/OPCCompliance_CoreProperties_DCTermsNamespaceLimitedUseFAIL.docx",
        "openxml4j/OPCCompliance_CoreProperties_DoNotUseCompatibilityMarkupFAIL.docx",
        "openxml4j/OPCCompliance_CoreProperties_LimitedXSITypeAttribute_NotPresentFAIL.docx",
        "openxml4j/OPCCompliance_CoreProperties_LimitedXSITypeAttribute_PresentWithUnauthorizedValueFAIL.docx",
        "openxml4j/OPCCompliance_CoreProperties_OnlyOneCorePropertiesPartFAIL.docx",
        "openxml4j/OPCCompliance_CoreProperties_UnauthorizedXMLLangAttributeFAIL.docx",
        "openxml4j/OPCCompliance_DerivedPartNameFAIL.docx",
        "openxml4j/invalid.xlsx",
        "spreadsheet/54764-2.xlsx",   // see TestXSSFBugs.bug54764()
        "spreadsheet/54764.xlsx",     // see TestXSSFBugs.bug54764()
        "spreadsheet/Simple.xlsb",
        "poifs/unknown_properties.msg", // POIFS properties corrupted
        "poifs/only-zero-byte-streams.ole2", // No actual contents
        "spreadsheet/poc-xmlbomb.xlsx",  // contains xml-entity-expansion
        "spreadsheet/poc-shared-strings.xlsx",  // contains shared-string-entity-expansion
        "spreadsheet/60255_extra_drawingparts.xlsx", // Non-drawing drawing
        
        // old Excel files, which we only support simple text extraction of
        "spreadsheet/testEXCEL_2.xls",
        "spreadsheet/testEXCEL_3.xls",
        "spreadsheet/testEXCEL_4.xls",
        "spreadsheet/testEXCEL_5.xls",
        "spreadsheet/testEXCEL_95.xls",
        "spreadsheet/59074.xls",
        "spreadsheet/60284.xls",
        
        // OOXML Strict is not yet supported, see bug #57699
        "spreadsheet/SampleSS.strict.xlsx",
        "spreadsheet/SimpleStrict.xlsx",
        "spreadsheet/sample.strict.xlsx",
        "spreadsheet/57914.xlsx",

        // non-TNEF files
        "ddf/Container.dat",
        "ddf/47143.dat",

        // sheet cloning errors
        "spreadsheet/47813.xlsx",
        "spreadsheet/56450.xls",
        "spreadsheet/57231_MixedGasReport.xls",
        "spreadsheet/OddStyleRecord.xls",
        "spreadsheet/WithChartSheet.xlsx",
        "spreadsheet/chart_sheet.xlsx",
        "spreadsheet/SimpleScatterChart.xlsx",
        "spreadsheet/ConditionalFormattingSamples.xls"
    );

    private static final Set<String> IGNORED = hashSet(
        // need JDK8+ - https://bugs.openjdk.java.net/browse/JDK-8038081
        "slideshow/42474-2.ppt",
        // OPC handler works / XSSF handler fails
        "spreadsheet/57181.xlsm"
    );
    
    @Parameters(name="{index}: {0} using {1}")
    public static Iterable<Object[]> files() {
        DirectoryScanner scanner = new DirectoryScanner();
        scanner.setBasedir(ROOT_DIR);
        scanner.setExcludes(SCAN_EXCLUDES);

        scanner.scan();

        System.out.println("Handling " + scanner.getIncludedFiles().length + " files");

        List<Object[]> files = new ArrayList<Object[]>();
        for(String file : scanner.getIncludedFiles()) {
            file = file.replace('\\', '/'); // ... failures/handlers lookup doesn't work on windows otherwise
            if (IGNORED.contains(file)) {
                System.out.println("Ignoring " + file);
                continue;
            }
            FileHandler handler = HANDLERS.get(getExtension(file));
            files.add(new Object[] { file, handler });
            
            // for some file-types also run OPCFileHandler
            if(handler instanceof XSSFFileHandler ||
                handler instanceof XWPFFileHandler ||
                handler instanceof XSLFFileHandler ||
                handler instanceof XDGFFileHandler) {
                files.add(new Object[] { file, HANDLERS.get(".ooxml") });
            }
        }

        return files;
    }

    @SuppressWarnings("DefaultAnnotationParam")
    @Parameter(value=0)
    public String file;

    @Parameter(value=1)
    public FileHandler handler;

    @Test
    public void testAllFiles() throws Exception {
        System.out.println("Reading " + file + " with " + handler.getClass());
        assertNotNull("Unknown file extension for file: " + file + ": " + getExtension(file), handler);
        File inputFile = new File(ROOT_DIR, file);

        try {
            InputStream stream = new BufferedInputStream(new FileInputStream(inputFile), 64*1024);
            try {
                handler.handleFile(stream);

                assertFalse("Expected to fail for file " + file + " and handler " + handler + ", but did not fail!", 
                        OLD_FILES.contains(file));
            } finally {
                stream.close();
            }

            handler.handleExtracting(inputFile);

            // special cases where docx-handling breaks, but OPCPackage handling works
            boolean ignoredOPC = (file.endsWith(".docx") || file.endsWith(".xlsx") ||
                        file.endsWith(".xlsb") || file.endsWith(".pptx")) &&
                    handler instanceof OPCFileHandler;

            assertFalse("Expected to fail for file " + file + " and handler " + handler + ", but did not fail!", 
                EXPECTED_FAILURES.contains(file) && !ignoredOPC);
        } catch (OldFileFormatException e) {
            // for old word files we should still support extracting text
            if(OLD_FILES.contains(file)) {
                handler.handleExtracting(inputFile);
            } else {
                // check if we expect failure for this file
                if(!EXPECTED_FAILURES.contains(file) && !AbstractFileHandler.EXPECTED_EXTRACTOR_FAILURES.contains(file)) {
                    System.out.println("Failed: " + file);
                    throw new Exception("While handling " + file, e);
                }
            }
        } catch (Exception e) {
            // check if we expect failure for this file
            if(!EXPECTED_FAILURES.contains(file) && !AbstractFileHandler.EXPECTED_EXTRACTOR_FAILURES.contains(file)) {
                System.out.println("Failed: " + file);
                throw new Exception("While handling " + file, e);
            }
        }

        // let some file handlers do additional stuff
        handler.handleAdditional(inputFile);
    }

    static String getExtension(String file) {
        int pos = file.lastIndexOf('.');
        if(pos == -1 || pos == file.length()-1) {
            return file;
        }

        return file.substring(pos).toLowerCase(Locale.ROOT);
    }

    private static class NullFileHandler implements FileHandler {
        @Override
        public void handleFile(InputStream stream) throws Exception {
        }

        @Override
        public void handleExtracting(File file) throws Exception {
        }

        @Override
        public void handleAdditional(File file) throws Exception {
        }
    }
}
