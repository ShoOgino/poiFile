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

package org.apache.poi.sl.draw;

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.text.AttributedCharacterIterator;
import java.text.AttributedCharacterIterator.Attribute;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.sl.usermodel.AutoNumberingScheme;
import org.apache.poi.sl.usermodel.Insets2D;
import org.apache.poi.sl.usermodel.PaintStyle;
import org.apache.poi.sl.usermodel.PlaceableShape;
import org.apache.poi.sl.usermodel.ShapeContainer;
import org.apache.poi.sl.usermodel.TextParagraph;
import org.apache.poi.sl.usermodel.TextParagraph.BulletStyle;
import org.apache.poi.sl.usermodel.TextParagraph.TextAlign;
import org.apache.poi.sl.usermodel.TextRun;
import org.apache.poi.sl.usermodel.TextRun.TextCap;
import org.apache.poi.sl.usermodel.TextShape;
import org.apache.poi.util.Units;

public class DrawTextParagraph implements Drawable {
    protected TextParagraph<?,?,?> paragraph;
    double x, y;
    protected List<DrawTextFragment> lines = new ArrayList<DrawTextFragment>();
    protected String rawText;
    protected DrawTextFragment bullet;
    protected int autoNbrIdx = 0;

    /**
     * the highest line in this paragraph. Used for line spacing.
     */
    protected double maxLineHeight;

    public DrawTextParagraph(TextParagraph<?,?,?> paragraph) {
        this.paragraph = paragraph;
    }

    public void setPosition(double x, double y) {
        // TODO: replace it, by applyTransform????
        this.x = x;
        this.y = y;
    }

    public double getY() {
        return y;
    }

    /**
     * Sets the auto numbering index of the handled paragraph
     * @param index the auto numbering index
     */
    public void setAutoNumberingIdx(int index) {
        autoNbrIdx = index;
    }
    
    public void draw(Graphics2D graphics){
        if (lines.isEmpty()) return;
        
        double penY = y;

        boolean firstLine = true;
        int indentLevel = paragraph.getIndentLevel();
        Double leftMargin = paragraph.getLeftMargin();
        if (leftMargin == null) {
            // if the marL attribute is omitted, then a value of 347663 is implied
            leftMargin = Units.toPoints(347663*indentLevel);
        }
        Double indent = paragraph.getIndent();
        if (indent == null) {
            indent = Units.toPoints(347663*indentLevel);
        }
        if (paragraph.getClass().getName().contains("HSLF")) {
            // special handling for HSLF
            indent -= leftMargin;
        }
        
//        Double rightMargin = paragraph.getRightMargin();
//        if (rightMargin == null) {
//            rightMargin = 0d;
//        }

        //The vertical line spacing
        Double spacing = paragraph.getLineSpacing();
        if (spacing == null) spacing = 100d;
        
        for(DrawTextFragment line : lines){
            double penX;

            if(firstLine) {
                if (!isEmptyParagraph()) {
                    // TODO: find out character style for empty, but bulleted/numbered lines
                    bullet = getBullet(graphics, line.getAttributedString().getIterator());
                }
                
                if (bullet != null){
                    bullet.setPosition(x+leftMargin+indent, penY);
                    bullet.draw(graphics);
                    // don't let text overlay the bullet and advance by the bullet width
                    double bulletWidth = bullet.getLayout().getAdvance() + 1;
                    penX = x + Math.max(leftMargin, leftMargin+indent+bulletWidth);
                } else {
                    penX = x + leftMargin;
                }
            } else {
                penX = x + leftMargin;
            }

            Rectangle2D anchor = DrawShape.getAnchor(graphics, paragraph.getParentShape());
            // Insets are already applied on DrawTextShape.drawContent
            // but (outer) anchor need to be adjusted
            Insets2D insets = paragraph.getParentShape().getInsets();
            double leftInset = insets.left;
            double rightInset = insets.right;

            TextAlign ta = paragraph.getTextAlign();
            if (ta == null) ta = TextAlign.LEFT;
            switch (ta) {
                case CENTER:
                    penX += (anchor.getWidth() - line.getWidth() - leftInset - rightInset - leftMargin) / 2;
                    break;
                case RIGHT:
                    penX += (anchor.getWidth() - line.getWidth() - leftInset - rightInset);
                    break;
                default:
                    break;
            }

            line.setPosition(penX, penY);
            line.draw(graphics);

            if(spacing > 0) {
                // If linespacing >= 0, then linespacing is a percentage of normal line height.
                penY += spacing*0.01* line.getHeight();
            } else {
                // negative value means absolute spacing in points
                penY += -spacing;
            }

            firstLine = false;
        }

        y = penY - y;
    }
    
    public float getFirstLineHeight() {
        return (lines.isEmpty()) ? 0 : lines.get(0).getHeight();
    }

    public float getLastLineHeight() {
        return (lines.isEmpty()) ? 0 : lines.get(lines.size()-1).getHeight();
    }

    public boolean isEmptyParagraph() {
        return (lines.isEmpty() || rawText.trim().isEmpty());
    }
    
    public void applyTransform(Graphics2D graphics) {
    }

    public void drawContent(Graphics2D graphics) {
    }

    /**
     * break text into lines, each representing a line of text that fits in the wrapping width
     *
     * @param graphics
     */
    protected void breakText(Graphics2D graphics){
        lines.clear();

        DrawFactory fact = DrawFactory.getInstance(graphics);
        StringBuilder text = new StringBuilder();
        AttributedString at = getAttributedString(graphics, text);
        boolean emptyParagraph = ("".equals(text.toString().trim()));

        AttributedCharacterIterator it = at.getIterator();
        LineBreakMeasurer measurer = new LineBreakMeasurer(it, graphics.getFontRenderContext());
        for (;;) {
            int startIndex = measurer.getPosition();

            double wrappingWidth = getWrappingWidth(lines.size() == 0, graphics) + 1; // add a pixel to compensate rounding errors
            // shape width can be smaller that the sum of insets (this was proved by a test file)
            if(wrappingWidth < 0) wrappingWidth = 1;

            int nextBreak = text.indexOf("\n", startIndex + 1);
            if (nextBreak == -1) nextBreak = it.getEndIndex();

            TextLayout layout = measurer.nextLayout((float)wrappingWidth, nextBreak, true);
            if (layout == null) {
                 // layout can be null if the entire word at the current position
                 // does not fit within the wrapping width. Try with requireNextWord=false.
                 layout = measurer.nextLayout((float)wrappingWidth, nextBreak, false);
            }

            if(layout == null) {
                // exit if can't break any more
                break;
            }

            int endIndex = measurer.getPosition();
            // skip over new line breaks (we paint 'clear' text runs not starting or ending with \n)
            if(endIndex < it.getEndIndex() && text.charAt(endIndex) == '\n'){
                measurer.setPosition(endIndex + 1);
            }

            TextAlign hAlign = paragraph.getTextAlign();
            if(hAlign == TextAlign.JUSTIFY || hAlign == TextAlign.JUSTIFY_LOW) {
                layout = layout.getJustifiedLayout((float)wrappingWidth);
            }

            AttributedString str = (emptyParagraph)
                ? null // we will not paint empty paragraphs
                : new AttributedString(it, startIndex, endIndex);
            DrawTextFragment line = fact.getTextFragment(layout, str);
            lines.add(line);

            maxLineHeight = Math.max(maxLineHeight, line.getHeight());

            if(endIndex == it.getEndIndex()) break;
        }

        rawText = text.toString();
    }

    protected DrawTextFragment getBullet(Graphics2D graphics, AttributedCharacterIterator firstLineAttr) {
        BulletStyle bulletStyle = paragraph.getBulletStyle();
        if (bulletStyle == null) return null;

        String buCharacter;
        AutoNumberingScheme ans = bulletStyle.getAutoNumberingScheme();
        if (ans != null) {
            buCharacter = ans.format(autoNbrIdx);
        } else {
            buCharacter = bulletStyle.getBulletCharacter();
        }
        if (buCharacter == null) return null;

        String buFont = bulletStyle.getBulletFont();
        if (buFont == null) buFont = paragraph.getDefaultFontFamily();
        assert(buFont != null);

        PlaceableShape<?,?> ps = getParagraphShape();
        PaintStyle fgPaintStyle = bulletStyle.getBulletFontColor();
        Paint fgPaint;
        if (fgPaintStyle == null) {
            fgPaint = (Paint)firstLineAttr.getAttribute(TextAttribute.FOREGROUND);
        } else {
            fgPaint = new DrawPaint(ps).getPaint(graphics, fgPaintStyle);
        }

        float fontSize = (Float)firstLineAttr.getAttribute(TextAttribute.SIZE);
        Double buSz = bulletStyle.getBulletFontSize();
        if (buSz == null) buSz = 100d; 
        if (buSz > 0) fontSize *= buSz* 0.01;
        else fontSize = (float)-buSz;

        
        AttributedString str = new AttributedString(buCharacter);
        str.addAttribute(TextAttribute.FOREGROUND, fgPaint);
        str.addAttribute(TextAttribute.FAMILY, buFont);
        str.addAttribute(TextAttribute.SIZE, fontSize);

        TextLayout layout = new TextLayout(str.getIterator(), graphics.getFontRenderContext());
        DrawFactory fact = DrawFactory.getInstance(graphics);
        return fact.getTextFragment(layout, str);
    }

    protected String getRenderableText(TextRun tr) {
        StringBuilder buf = new StringBuilder();
        TextCap cap = tr.getTextCap();
        String tabs = null;
        for (char c : tr.getRawText().toCharArray()) {
            if(c == '\t') {
                if (tabs == null) {
                    tabs = tab2space(tr);
                }
                buf.append(tabs);
                continue;
            }

            switch (cap) {
                case ALL: c = Character.toUpperCase(c); break;
                case SMALL: c = Character.toLowerCase(c); break;
                case NONE: break;
            }

            buf.append(c);
        }

        return buf.toString();
    }
    
    /**
     * Replace a tab with the effective number of white spaces.
     */
    private String tab2space(TextRun tr) {
        AttributedString string = new AttributedString(" ");
        String typeFace = tr.getFontFamily();
        if (typeFace == null) typeFace = "Lucida Sans";
        string.addAttribute(TextAttribute.FAMILY, typeFace);

        Double fs = tr.getFontSize();
        if (fs == null) fs = 12d;
        string.addAttribute(TextAttribute.SIZE, fs.floatValue());
        
        TextLayout l = new TextLayout(string.getIterator(), new FontRenderContext(null, true, true));
        double wspace = l.getAdvance();

        Double tabSz = paragraph.getDefaultTabSize();
        if (tabSz == null) tabSz = wspace*4;

        int numSpaces = (int)Math.ceil(tabSz / wspace);
        StringBuilder buf = new StringBuilder();
        for(int i = 0; i < numSpaces; i++) {
            buf.append(' ');
        }
        return buf.toString();
    }
    

    /**
     * Returns wrapping width to break lines in this paragraph
     *
     * @param firstLine whether the first line is breaking
     *
     * @return  wrapping width in points
     */
    protected double getWrappingWidth(boolean firstLine, Graphics2D graphics){
        // internal margins for the text box

        Insets2D insets = paragraph.getParentShape().getInsets();
        double leftInset = insets.left;
        double rightInset = insets.right;

        Rectangle2D anchor = DrawShape.getAnchor(graphics, paragraph.getParentShape());

        int indentLevel = paragraph.getIndentLevel();
        Double leftMargin = paragraph.getLeftMargin();
        if (leftMargin == null) {
            // if the marL attribute is omitted, then a value of 347663 is implied
            leftMargin = Units.toPoints(347663*(indentLevel+1));
        }
        Double indent = paragraph.getIndent();
        if (indent == null) {
            indent = Units.toPoints(347663*indentLevel);
        }
        Double rightMargin = paragraph.getRightMargin();
        if (rightMargin == null) {
            rightMargin = 0d;
        }

        double width;
        TextShape<?,?> ts = paragraph.getParentShape();
        if (!ts.getWordWrap()) {
            // if wordWrap == false then we return the advance to the right border of the sheet
            width = ts.getSheet().getSlideShow().getPageSize().getWidth() - anchor.getX();
        } else {
            width = anchor.getWidth() - leftInset - rightInset - leftMargin - rightMargin;
            if (firstLine) {
                if (bullet != null){
                    if (indent > 0) width -= indent;
                } else {
                    if (indent > 0) width -= indent; // first line indentation
                    else if (indent < 0) { // hanging indentation: the first line start at the left margin
                        width += leftMargin;
                    }
                }
            }
        }

        return width;
    }

    private static class AttributedStringData {
        Attribute attribute;
        Object value;
        int beginIndex, endIndex;
        AttributedStringData(Attribute attribute, Object value, int beginIndex, int endIndex) {
            this.attribute = attribute;
            this.value = value;
            this.beginIndex = beginIndex;
            this.endIndex = endIndex;
        }
    }
    
    /**
     * Helper method for paint style relative to bounds, e.g. gradient paint
     */
    @SuppressWarnings("rawtypes")
    private PlaceableShape<?,?> getParagraphShape() {
        PlaceableShape<?,?> ps = new PlaceableShape(){
            public ShapeContainer<?,?> getParent() { return null; }
            public Rectangle2D getAnchor() { return paragraph.getParentShape().getAnchor(); }
            public void setAnchor(Rectangle2D anchor) {}
            public double getRotation() { return 0; }
            public void setRotation(double theta) {}
            public void setFlipHorizontal(boolean flip) {}
            public void setFlipVertical(boolean flip) {}
            public boolean getFlipHorizontal() { return false; }
            public boolean getFlipVertical() { return false; }
        };
        return ps;
    }

    protected AttributedString getAttributedString(Graphics2D graphics, StringBuilder text){
        List<AttributedStringData> attList = new ArrayList<AttributedStringData>();
        if (text == null) text = new StringBuilder();

        PlaceableShape<?,?> ps = getParagraphShape();
        
        DrawFontManager fontHandler = (DrawFontManager)graphics.getRenderingHint(Drawable.FONT_HANDLER);

        for (TextRun run : paragraph){
            String runText = getRenderableText(run);
            // skip empty runs
            if (runText.isEmpty()) continue;

            int beginIndex = text.length();
            text.append(runText);
            int endIndex = text.length();

            PaintStyle fgPaintStyle = run.getFontColor();
            Paint fgPaint = new DrawPaint(ps).getPaint(graphics, fgPaintStyle);
            attList.add(new AttributedStringData(TextAttribute.FOREGROUND, fgPaint, beginIndex, endIndex));

            // user can pass an custom object to convert fonts
            String fontFamily = run.getFontFamily();
            @SuppressWarnings("unchecked")
            Map<String,String> fontMap = (Map<String,String>)graphics.getRenderingHint(Drawable.FONT_MAP);
            if (fontMap != null && fontMap.containsKey(fontFamily)) {
                fontFamily = fontMap.get(fontFamily);
            }
            if(fontHandler != null) {
                fontFamily = fontHandler.getRendererableFont(fontFamily, run.getPitchAndFamily());
            }
            if (fontFamily == null) {
                fontFamily = paragraph.getDefaultFontFamily();
            }
            attList.add(new AttributedStringData(TextAttribute.FAMILY, fontFamily, beginIndex, endIndex));

            Double fontSz = run.getFontSize();
            if (fontSz == null) fontSz = paragraph.getDefaultFontSize();
            attList.add(new AttributedStringData(TextAttribute.SIZE, fontSz.floatValue(), beginIndex, endIndex));

            if(run.isBold()) {
                attList.add(new AttributedStringData(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD, beginIndex, endIndex));
            }
            if(run.isItalic()) {
                attList.add(new AttributedStringData(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE, beginIndex, endIndex));
            }
            if(run.isUnderlined()) {
                attList.add(new AttributedStringData(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON, beginIndex, endIndex));
                attList.add(new AttributedStringData(TextAttribute.INPUT_METHOD_UNDERLINE, TextAttribute.UNDERLINE_LOW_TWO_PIXEL, beginIndex, endIndex));
            }
            if(run.isStrikethrough()) {
                attList.add(new AttributedStringData(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON, beginIndex, endIndex));
            }
            if(run.isSubscript()) {
                attList.add(new AttributedStringData(TextAttribute.SUPERSCRIPT, TextAttribute.SUPERSCRIPT_SUB, beginIndex, endIndex));
            }
            if(run.isSuperscript()) {
                attList.add(new AttributedStringData(TextAttribute.SUPERSCRIPT, TextAttribute.SUPERSCRIPT_SUPER, beginIndex, endIndex));
            }
        }

        // ensure that the paragraph contains at least one character
        // We need this trick to correctly measure text
        if (text.length() == 0) {
            Double fontSz = paragraph.getDefaultFontSize();
            text.append(" ");
            attList.add(new AttributedStringData(TextAttribute.SIZE, fontSz.floatValue(), 0, 1));
        }

        AttributedString string = new AttributedString(text.toString());
        for (AttributedStringData asd : attList) {
            string.addAttribute(asd.attribute, asd.value, asd.beginIndex, asd.endIndex);
        }

        return string;
    }


}
