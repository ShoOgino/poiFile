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

package org.apache.poi.hslf.examples;

import org.apache.poi.hslf.usermodel.*;
import org.apache.poi.hslf.model.*;

import java.io.FileOutputStream;
import java.awt.*;

/**
 * Demonstrates how to create hyperlinks in PowerPoint presentations
 *
 * @author Yegor Kozlov
 */
public final class CreateHyperlink {

    public static void main(String[] args) throws Exception {
        HSLFSlideShow ppt = new HSLFSlideShow();

        HSLFSlide slideA = ppt.createSlide();
        HSLFSlide slideB = ppt.createSlide();
        HSLFSlide slideC = ppt.createSlide();

        // link to a URL
        HSLFTextBox textBox1 = new HSLFTextBox();
        textBox1.setText("Apache POI");
        textBox1.setAnchor(new Rectangle(100, 100, 200, 50));

        String text = textBox1.getText();
        HSLFHyperlink link = new HSLFHyperlink();
        link.setAddress("http://www.apache.org");
        link.setTitle(textBox1.getText());
        int linkId = ppt.addHyperlink(link);

        // apply link to the text
        textBox1.setHyperlink(linkId, 0, text.length());

        slideA.addShape(textBox1);

        // link to another slide
        HSLFTextBox textBox2 = new HSLFTextBox();
        textBox2.setText("Go to slide #3");
        textBox2.setAnchor(new Rectangle(100, 300, 200, 50));

        HSLFHyperlink link2 = new HSLFHyperlink();
        link2.setAddress(slideC);
        ppt.addHyperlink(link2);

        // apply link to the whole shape
        textBox2.setHyperlink(link2);

        slideA.addShape(textBox2);

        FileOutputStream out = new FileOutputStream("hyperlink.ppt");
        ppt.write(out);
        out.close();

   }
}
