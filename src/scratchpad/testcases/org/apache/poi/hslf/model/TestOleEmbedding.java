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

package org.apache.poi.hslf.model;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.awt.geom.Rectangle2D;
import java.io.*;
import java.util.List;

import org.apache.poi.POIDataSamples;
import org.apache.poi.hslf.usermodel.*;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.util.IOUtils;
import org.apache.poi.sl.usermodel.PictureData.PictureType;
import org.junit.Test;

public final class TestOleEmbedding {
    private static POIDataSamples _slTests = POIDataSamples.getSlideShowInstance();
    /**
     * Tests support for OLE objects.
     *
     * @throws Exception if an error occurs.
     */
    @Test
    public void testOleEmbedding2003() throws Exception {
        HSLFSlideShowImpl slideShow = new HSLFSlideShowImpl(_slTests.openResourceAsStream("ole2-embedding-2003.ppt"));
        // Placeholder EMFs for clients that don't support the OLE components.
        List<HSLFPictureData> pictures = slideShow.getPictureData();
        assertEquals("Should be two pictures", 2, pictures.size());
        //assertDigestEquals("Wrong data for picture 1", "8d1fbadf4814f321bb1ccdd056e3c788", pictures[0].getData());
        //assertDigestEquals("Wrong data for picture 2", "987a698e83559cf3d38a0deeba1cc63b", pictures[1].getData());

        // Actual embedded objects.
        HSLFObjectData[] objects = slideShow.getEmbeddedObjects();
        assertEquals("Should be two objects", 2, objects.length);
        //assertDigestEquals("Wrong data for objecs 1", "0d1fcc61a83de5c4894dc0c88e9a019d", objects[0].getData());
        //assertDigestEquals("Wrong data for object 2", "b323604b2003a7299c77c2693b641495", objects[1].getData());
    }

    @Test
    public void testOLEShape() throws Exception {
        HSLFSlideShow ppt = new HSLFSlideShow(_slTests.openResourceAsStream("ole2-embedding-2003.ppt"));

        HSLFSlide slide = ppt.getSlides().get(0);
        int cnt = 0;
        for (HSLFShape sh : slide.getShapes()) {
            if(sh instanceof OLEShape){
                cnt++;
                OLEShape ole = (OLEShape)sh;
                HSLFObjectData data = ole.getObjectData();
                if("Worksheet".equals(ole.getInstanceName())){
                    //Voila! we created a workbook from the embedded OLE data
                    HSSFWorkbook wb = new HSSFWorkbook(data.getData());
                    HSSFSheet sheet = wb.getSheetAt(0);
                    //verify we can access the xls data
                    assertEquals(1, sheet.getRow(0).getCell(0).getNumericCellValue(), 0);
                    assertEquals(1, sheet.getRow(1).getCell(0).getNumericCellValue(), 0);
                    assertEquals(2, sheet.getRow(2).getCell(0).getNumericCellValue(), 0);
                    assertEquals(3, sheet.getRow(3).getCell(0).getNumericCellValue(), 0);
                    assertEquals(8, sheet.getRow(5).getCell(0).getNumericCellValue(), 0);
                    wb.close();
                } else if ("Document".equals(ole.getInstanceName())){
                    //creating a HWPF document
                    HWPFDocument doc = new HWPFDocument(data.getData());
                    String txt = doc.getRange().getParagraph(0).text();
                    assertEquals("OLE embedding is thoroughly unremarkable.\r", txt);
                }
            }

        }
        assertEquals("Expected 2 OLE shapes", 2, cnt);
    }
    
    @Test
    public void testEmbedding() throws Exception {
    	HSLFSlideShowImpl _hslfSlideShow = HSLFSlideShowImpl.create();
    	HSLFSlideShow ppt = new HSLFSlideShow(_hslfSlideShow);
    	
    	File pict = POIDataSamples.getSlideShowInstance().getFile("clock.jpg");
    	HSLFPictureData pictData = ppt.addPicture(pict, PictureType.JPEG);
    	
    	InputStream is = POIDataSamples.getSpreadSheetInstance().openResourceAsStream("Employee.xls");
    	POIFSFileSystem poiData1 = new POIFSFileSystem(is);
    	is.close();
    	
    	int oleObjectId1 = ppt.addEmbed(poiData1);
    	
    	HSLFSlide slide1 = ppt.createSlide();
    	OLEShape oleShape1 = new OLEShape(pictData);
    	oleShape1.setObjectID(oleObjectId1);
    	slide1.addShape(oleShape1);
    	oleShape1.setAnchor(new Rectangle2D.Double(100,100,100,100));
    	
    	// add second slide with different order in object creation
    	HSLFSlide slide2 = ppt.createSlide();
    	OLEShape oleShape2 = new OLEShape(pictData);

        is = POIDataSamples.getSpreadSheetInstance().openResourceAsStream("SimpleWithImages.xls");
        POIFSFileSystem poiData2 = new POIFSFileSystem(is);
        is.close();
    	
        int oleObjectId2 = ppt.addEmbed(poiData2);

        oleShape2.setObjectID(oleObjectId2);
        slide2.addShape(oleShape2);
        oleShape2.setAnchor(new Rectangle2D.Double(100,100,100,100));
        
    	ByteArrayOutputStream bos = new ByteArrayOutputStream();
    	ppt.write(bos);
    	
    	ppt = new HSLFSlideShow(new ByteArrayInputStream(bos.toByteArray()));
    	OLEShape comp = (OLEShape)ppt.getSlides().get(0).getShapes().get(0);
    	byte compData[] = IOUtils.toByteArray(comp.getObjectData().getData());
    	
    	bos.reset();
    	poiData1.writeFilesystem(bos);
    	byte expData[] = bos.toByteArray();
    	
    	assertArrayEquals(expData, compData);
    	
    	poiData1.close();
    	poiData2.close();
    	
    }
}
