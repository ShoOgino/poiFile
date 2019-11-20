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

package org.apache.poi.hssf.record.chart;

import org.apache.poi.hssf.record.RecordInputStream;
import org.apache.poi.hssf.record.StandardRecord;
import org.apache.poi.util.BitField;
import org.apache.poi.util.BitFieldFactory;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;

/**
 * The text record is used to define text stored on a chart.
 */
public final class TextRecord extends StandardRecord {
    public final static short      sid                             = 0x1025;
    
    private static final BitField dataLabelPlacement            = BitFieldFactory.getInstance(0x000F);
    private static final BitField autoColor                     = BitFieldFactory.getInstance(0x0001);
    private static final BitField showKey                       = BitFieldFactory.getInstance(0x0002);
    private static final BitField showValue                     = BitFieldFactory.getInstance(0x0004);
    private static final BitField vertical                      = BitFieldFactory.getInstance(0x0008);
    private static final BitField autoGeneratedText             = BitFieldFactory.getInstance(0x0010);
    private static final BitField generated                     = BitFieldFactory.getInstance(0x0020);
    private static final BitField autoLabelDeleted              = BitFieldFactory.getInstance(0x0040);
    private static final BitField autoBackground                = BitFieldFactory.getInstance(0x0080);
    private static final BitField rotation                      = BitFieldFactory.getInstance(0x0700);

    private static final BitField showCategoryLabelAsPercentage = BitFieldFactory.getInstance(0x0800);
    private static final BitField showValueAsPercentage         = BitFieldFactory.getInstance(0x1000);
    private static final BitField showBubbleSizes               = BitFieldFactory.getInstance(0x2000);
    private static final BitField showLabel                     = BitFieldFactory.getInstance(0x4000);
    
    
    private  byte       field_1_horizontalAlignment;
    public final static byte        HORIZONTAL_ALIGNMENT_LEFT      = 1;
    public final static byte        HORIZONTAL_ALIGNMENT_CENTER    = 2;
    public final static byte        HORIZONTAL_ALIGNMENT_BOTTOM    = 3;
    public final static byte        HORIZONTAL_ALIGNMENT_JUSTIFY   = 4;
    private  byte       field_2_verticalAlignment;
    public final static byte        VERTICAL_ALIGNMENT_TOP         = 1;
    public final static byte        VERTICAL_ALIGNMENT_CENTER      = 2;
    public final static byte        VERTICAL_ALIGNMENT_BOTTOM      = 3;
    public final static byte        VERTICAL_ALIGNMENT_JUSTIFY     = 4;
    private  short      field_3_displayMode;
    public final static short       DISPLAY_MODE_TRANSPARENT       = 1;
    public final static short       DISPLAY_MODE_OPAQUE            = 2;
    private  int        field_4_rgbColor;
    private  int        field_5_x;
    private  int        field_6_y;
    private  int        field_7_width;
    private  int        field_8_height;
    private  short      field_9_options1;
    public final static short  ROTATION_NONE                  = 0;
    public final static short  ROTATION_TOP_TO_BOTTOM         = 1;
    public final static short  ROTATION_ROTATED_90_DEGREES    = 2;
    public final static short  ROTATION_ROTATED_90_DEGREES_CLOCKWISE = 3;
    private  short      field_10_indexOfColorValue;
    private  short      field_11_options2;
    public final static short  DATA_LABEL_PLACEMENT_CHART_DEPENDENT = 0;
    public final static short  DATA_LABEL_PLACEMENT_OUTSIDE   = 1;
    public final static short  DATA_LABEL_PLACEMENT_INSIDE    = 2;
    public final static short  DATA_LABEL_PLACEMENT_CENTER    = 3;
    public final static short  DATA_LABEL_PLACEMENT_AXIS      = 4;
    public final static short  DATA_LABEL_PLACEMENT_ABOVE     = 5;
    public final static short  DATA_LABEL_PLACEMENT_BELOW     = 6;
    public final static short  DATA_LABEL_PLACEMENT_LEFT      = 7;
    public final static short  DATA_LABEL_PLACEMENT_RIGHT     = 8;
    public final static short  DATA_LABEL_PLACEMENT_AUTO      = 9;
    public final static short  DATA_LABEL_PLACEMENT_USER_MOVED = 10;
    private  short      field_12_textRotation;


    public TextRecord()
    {

    }

    public TextRecord(RecordInputStream in)
    {
        field_1_horizontalAlignment    = in.readByte();
        field_2_verticalAlignment      = in.readByte();
        field_3_displayMode            = in.readShort();
        field_4_rgbColor               = in.readInt();
        field_5_x                      = in.readInt();
        field_6_y                      = in.readInt();
        field_7_width                  = in.readInt();
        field_8_height                 = in.readInt();
        field_9_options1               = in.readShort();
        field_10_indexOfColorValue     = in.readShort();
        field_11_options2              = in.readShort();
        field_12_textRotation          = in.readShort();
    }

    public String toString()
    {
        StringBuilder buffer = new StringBuilder();

        buffer.append("[TEXT]\n");
        buffer.append("    .horizontalAlignment  = ")
            .append("0x").append(HexDump.toHex(  getHorizontalAlignment ()))
            .append(" (").append( getHorizontalAlignment() ).append(" )");
        buffer.append(System.getProperty("line.separator")); 
        buffer.append("    .verticalAlignment    = ")
            .append("0x").append(HexDump.toHex(  getVerticalAlignment ()))
            .append(" (").append( getVerticalAlignment() ).append(" )");
        buffer.append(System.getProperty("line.separator")); 
        buffer.append("    .displayMode          = ")
            .append("0x").append(HexDump.toHex(  getDisplayMode ()))
            .append(" (").append( getDisplayMode() ).append(" )");
        buffer.append(System.getProperty("line.separator")); 
        buffer.append("    .rgbColor             = ")
            .append("0x").append(HexDump.toHex(  getRgbColor ()))
            .append(" (").append( getRgbColor() ).append(" )");
        buffer.append(System.getProperty("line.separator")); 
        buffer.append("    .x                    = ")
            .append("0x").append(HexDump.toHex(  getX ()))
            .append(" (").append( getX() ).append(" )");
        buffer.append(System.getProperty("line.separator")); 
        buffer.append("    .y                    = ")
            .append("0x").append(HexDump.toHex(  getY ()))
            .append(" (").append( getY() ).append(" )");
        buffer.append(System.getProperty("line.separator")); 
        buffer.append("    .width                = ")
            .append("0x").append(HexDump.toHex(  getWidth ()))
            .append(" (").append( getWidth() ).append(" )");
        buffer.append(System.getProperty("line.separator")); 
        buffer.append("    .height               = ")
            .append("0x").append(HexDump.toHex(  getHeight ()))
            .append(" (").append( getHeight() ).append(" )");
        buffer.append(System.getProperty("line.separator")); 
        buffer.append("    .options1             = ")
            .append("0x").append(HexDump.toHex(  getOptions1 ()))
            .append(" (").append( getOptions1() ).append(" )");
        buffer.append(System.getProperty("line.separator")); 
        buffer.append("         .autoColor                = ").append(isAutoColor()).append('\n'); 
        buffer.append("         .showKey                  = ").append(isShowKey()).append('\n'); 
        buffer.append("         .showValue                = ").append(isShowValue()).append('\n'); 
        buffer.append("         .vertical                 = ").append(isVertical()).append('\n'); 
        buffer.append("         .autoGeneratedText        = ").append(isAutoGeneratedText()).append('\n'); 
        buffer.append("         .generated                = ").append(isGenerated()).append('\n'); 
        buffer.append("         .autoLabelDeleted         = ").append(isAutoLabelDeleted()).append('\n'); 
        buffer.append("         .autoBackground           = ").append(isAutoBackground()).append('\n'); 
            buffer.append("         .rotation                 = ").append(getRotation()).append('\n'); 
        buffer.append("         .showCategoryLabelAsPercentage     = ").append(isShowCategoryLabelAsPercentage()).append('\n'); 
        buffer.append("         .showValueAsPercentage     = ").append(isShowValueAsPercentage()).append('\n'); 
        buffer.append("         .showBubbleSizes          = ").append(isShowBubbleSizes()).append('\n'); 
        buffer.append("         .showLabel                = ").append(isShowLabel()).append('\n'); 
        buffer.append("    .indexOfColorValue    = ")
            .append("0x").append(HexDump.toHex(  getIndexOfColorValue ()))
            .append(" (").append( getIndexOfColorValue() ).append(" )");
        buffer.append(System.getProperty("line.separator")); 
        buffer.append("    .options2             = ")
            .append("0x").append(HexDump.toHex(  getOptions2 ()))
            .append(" (").append( getOptions2() ).append(" )");
        buffer.append(System.getProperty("line.separator")); 
            buffer.append("         .dataLabelPlacement       = ").append(getDataLabelPlacement()).append('\n'); 
        buffer.append("    .textRotation         = ")
            .append("0x").append(HexDump.toHex(  getTextRotation ()))
            .append(" (").append( getTextRotation() ).append(" )");
        buffer.append(System.getProperty("line.separator")); 

        buffer.append("[/TEXT]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        out.writeByte(field_1_horizontalAlignment);
        out.writeByte(field_2_verticalAlignment);
        out.writeShort(field_3_displayMode);
        out.writeInt(field_4_rgbColor);
        out.writeInt(field_5_x);
        out.writeInt(field_6_y);
        out.writeInt(field_7_width);
        out.writeInt(field_8_height);
        out.writeShort(field_9_options1);
        out.writeShort(field_10_indexOfColorValue);
        out.writeShort(field_11_options2);
        out.writeShort(field_12_textRotation);
    }

    protected int getDataSize() {
        return 1 + 1 + 2 + 4 + 4 + 4 + 4 + 4 + 2 + 2 + 2 + 2;
    }

    public short getSid()
    {
        return sid;
    }

    public Object clone() {
        TextRecord rec = new TextRecord();
    
        rec.field_1_horizontalAlignment = field_1_horizontalAlignment;
        rec.field_2_verticalAlignment = field_2_verticalAlignment;
        rec.field_3_displayMode = field_3_displayMode;
        rec.field_4_rgbColor = field_4_rgbColor;
        rec.field_5_x = field_5_x;
        rec.field_6_y = field_6_y;
        rec.field_7_width = field_7_width;
        rec.field_8_height = field_8_height;
        rec.field_9_options1 = field_9_options1;
        rec.field_10_indexOfColorValue = field_10_indexOfColorValue;
        rec.field_11_options2 = field_11_options2;
        rec.field_12_textRotation = field_12_textRotation;
        return rec;
    }




    /**
     * Get the horizontal alignment field for the Text record.
     *
     * @return  One of 
     *        HORIZONTAL_ALIGNMENT_LEFT
     *        HORIZONTAL_ALIGNMENT_CENTER
     *        HORIZONTAL_ALIGNMENT_BOTTOM
     *        HORIZONTAL_ALIGNMENT_JUSTIFY
     */
    public byte getHorizontalAlignment()
    {
        return field_1_horizontalAlignment;
    }

    /**
     * Set the horizontal alignment field for the Text record.
     *
     * @param field_1_horizontalAlignment
     *        One of 
     *        HORIZONTAL_ALIGNMENT_LEFT
     *        HORIZONTAL_ALIGNMENT_CENTER
     *        HORIZONTAL_ALIGNMENT_BOTTOM
     *        HORIZONTAL_ALIGNMENT_JUSTIFY
     */
    public void setHorizontalAlignment(byte field_1_horizontalAlignment)
    {
        this.field_1_horizontalAlignment = field_1_horizontalAlignment;
    }

    /**
     * Get the vertical alignment field for the Text record.
     *
     * @return  One of 
     *        VERTICAL_ALIGNMENT_TOP
     *        VERTICAL_ALIGNMENT_CENTER
     *        VERTICAL_ALIGNMENT_BOTTOM
     *        VERTICAL_ALIGNMENT_JUSTIFY
     */
    public byte getVerticalAlignment()
    {
        return field_2_verticalAlignment;
    }

    /**
     * Set the vertical alignment field for the Text record.
     *
     * @param field_2_verticalAlignment
     *        One of 
     *        VERTICAL_ALIGNMENT_TOP
     *        VERTICAL_ALIGNMENT_CENTER
     *        VERTICAL_ALIGNMENT_BOTTOM
     *        VERTICAL_ALIGNMENT_JUSTIFY
     */
    public void setVerticalAlignment(byte field_2_verticalAlignment)
    {
        this.field_2_verticalAlignment = field_2_verticalAlignment;
    }

    /**
     * Get the display mode field for the Text record.
     *
     * @return  One of 
     *        DISPLAY_MODE_TRANSPARENT
     *        DISPLAY_MODE_OPAQUE
     */
    public short getDisplayMode()
    {
        return field_3_displayMode;
    }

    /**
     * Set the display mode field for the Text record.
     *
     * @param field_3_displayMode
     *        One of 
     *        DISPLAY_MODE_TRANSPARENT
     *        DISPLAY_MODE_OPAQUE
     */
    public void setDisplayMode(short field_3_displayMode)
    {
        this.field_3_displayMode = field_3_displayMode;
    }

    /**
     * Get the rgbColor field for the Text record.
     */
    public int getRgbColor()
    {
        return field_4_rgbColor;
    }

    /**
     * Set the rgbColor field for the Text record.
     */
    public void setRgbColor(int field_4_rgbColor)
    {
        this.field_4_rgbColor = field_4_rgbColor;
    }

    /**
     * Get the x field for the Text record.
     */
    public int getX()
    {
        return field_5_x;
    }

    /**
     * Set the x field for the Text record.
     */
    public void setX(int field_5_x)
    {
        this.field_5_x = field_5_x;
    }

    /**
     * Get the y field for the Text record.
     */
    public int getY()
    {
        return field_6_y;
    }

    /**
     * Set the y field for the Text record.
     */
    public void setY(int field_6_y)
    {
        this.field_6_y = field_6_y;
    }

    /**
     * Get the width field for the Text record.
     */
    public int getWidth()
    {
        return field_7_width;
    }

    /**
     * Set the width field for the Text record.
     */
    public void setWidth(int field_7_width)
    {
        this.field_7_width = field_7_width;
    }

    /**
     * Get the height field for the Text record.
     */
    public int getHeight()
    {
        return field_8_height;
    }

    /**
     * Set the height field for the Text record.
     */
    public void setHeight(int field_8_height)
    {
        this.field_8_height = field_8_height;
    }

    /**
     * Get the options1 field for the Text record.
     */
    public short getOptions1()
    {
        return field_9_options1;
    }

    /**
     * Set the options1 field for the Text record.
     */
    public void setOptions1(short field_9_options1)
    {
        this.field_9_options1 = field_9_options1;
    }

    /**
     * Get the index of color value field for the Text record.
     */
    public short getIndexOfColorValue()
    {
        return field_10_indexOfColorValue;
    }

    /**
     * Set the index of color value field for the Text record.
     */
    public void setIndexOfColorValue(short field_10_indexOfColorValue)
    {
        this.field_10_indexOfColorValue = field_10_indexOfColorValue;
    }

    /**
     * Get the options2 field for the Text record.
     */
    public short getOptions2()
    {
        return field_11_options2;
    }

    /**
     * Set the options2 field for the Text record.
     */
    public void setOptions2(short field_11_options2)
    {
        this.field_11_options2 = field_11_options2;
    }

    /**
     * Get the text rotation field for the Text record.
     */
    public short getTextRotation()
    {
        return field_12_textRotation;
    }

    /**
     * Set the text rotation field for the Text record.
     */
    public void setTextRotation(short field_12_textRotation)
    {
        this.field_12_textRotation = field_12_textRotation;
    }

    /**
     * Sets the auto color field value.
     * true = automaticly selected colour, false = user-selected
     */
    public void setAutoColor(boolean value)
    {
        field_9_options1 = autoColor.setShortBoolean(field_9_options1, value);
    }

    /**
     * true = automaticly selected colour, false = user-selected
     * @return  the auto color field value.
     */
    public boolean isAutoColor()
    {
        return autoColor.isSet(field_9_options1);
    }

    /**
     * Sets the show key field value.
     * true = draw legend
     */
    public void setShowKey(boolean value)
    {
        field_9_options1 = showKey.setShortBoolean(field_9_options1, value);
    }

    /**
     * true = draw legend
     * @return  the show key field value.
     */
    public boolean isShowKey()
    {
        return showKey.isSet(field_9_options1);
    }

    /**
     * Sets the show value field value.
     * false = text is category label
     */
    public void setShowValue(boolean value)
    {
        field_9_options1 = showValue.setShortBoolean(field_9_options1, value);
    }

    /**
     * false = text is category label
     * @return  the show value field value.
     */
    public boolean isShowValue()
    {
        return showValue.isSet(field_9_options1);
    }

    /**
     * Sets the vertical field value.
     * true = text is vertical
     */
    public void setVertical(boolean value)
    {
        field_9_options1 = vertical.setShortBoolean(field_9_options1, value);
    }

    /**
     * true = text is vertical
     * @return  the vertical field value.
     */
    public boolean isVertical()
    {
        return vertical.isSet(field_9_options1);
    }

    /**
     * Sets the auto generated text field value.
     * 
     */
    public void setAutoGeneratedText(boolean value)
    {
        field_9_options1 = autoGeneratedText.setShortBoolean(field_9_options1, value);
    }

    /**
     * 
     * @return  the auto generated text field value.
     */
    public boolean isAutoGeneratedText()
    {
        return autoGeneratedText.isSet(field_9_options1);
    }

    /**
     * Sets the generated field value.
     * 
     */
    public void setGenerated(boolean value)
    {
        field_9_options1 = generated.setShortBoolean(field_9_options1, value);
    }

    /**
     * 
     * @return  the generated field value.
     */
    public boolean isGenerated()
    {
        return generated.isSet(field_9_options1);
    }

    /**
     * Sets the auto label deleted field value.
     * 
     */
    public void setAutoLabelDeleted(boolean value)
    {
        field_9_options1 = autoLabelDeleted.setShortBoolean(field_9_options1, value);
    }

    /**
     * 
     * @return  the auto label deleted field value.
     */
    public boolean isAutoLabelDeleted()
    {
        return autoLabelDeleted.isSet(field_9_options1);
    }

    /**
     * Sets the auto background field value.
     * 
     */
    public void setAutoBackground(boolean value)
    {
        field_9_options1 = autoBackground.setShortBoolean(field_9_options1, value);
    }

    /**
     * 
     * @return  the auto background field value.
     */
    public boolean isAutoBackground()
    {
        return autoBackground.isSet(field_9_options1);
    }

    /**
     * Sets the rotation field value.
     * 
     */
    public void setRotation(short value)
    {
        field_9_options1 = rotation.setShortValue(field_9_options1, value);
    }

    /**
     * 
     * @return  the rotation field value.
     */
    public short getRotation()
    {
        return rotation.getShortValue(field_9_options1);
    }

    /**
     * Sets the show category label as percentage field value.
     * 
     */
    public void setShowCategoryLabelAsPercentage(boolean value)
    {
        field_9_options1 = showCategoryLabelAsPercentage.setShortBoolean(field_9_options1, value);
    }

    /**
     * 
     * @return  the show category label as percentage field value.
     */
    public boolean isShowCategoryLabelAsPercentage()
    {
        return showCategoryLabelAsPercentage.isSet(field_9_options1);
    }

    /**
     * Sets the show value as percentage field value.
     * 
     */
    public void setShowValueAsPercentage(boolean value)
    {
        field_9_options1 = showValueAsPercentage.setShortBoolean(field_9_options1, value);
    }

    /**
     * 
     * @return  the show value as percentage field value.
     */
    public boolean isShowValueAsPercentage()
    {
        return showValueAsPercentage.isSet(field_9_options1);
    }

    /**
     * Sets the show bubble sizes field value.
     * 
     */
    public void setShowBubbleSizes(boolean value)
    {
        field_9_options1 = showBubbleSizes.setShortBoolean(field_9_options1, value);
    }

    /**
     * 
     * @return  the show bubble sizes field value.
     */
    public boolean isShowBubbleSizes()
    {
        return showBubbleSizes.isSet(field_9_options1);
    }

    /**
     * Sets the show label field value.
     * 
     */
    public void setShowLabel(boolean value)
    {
        field_9_options1 = showLabel.setShortBoolean(field_9_options1, value);
    }

    /**
     * 
     * @return  the show label field value.
     */
    public boolean isShowLabel()
    {
        return showLabel.isSet(field_9_options1);
    }

    /**
     * Sets the data label placement field value.
     * 
     */
    public void setDataLabelPlacement(short value)
    {
        field_11_options2 = dataLabelPlacement.setShortValue(field_11_options2, value);
    }

    /**
     * 
     * @return  the data label placement field value.
     */
    public short getDataLabelPlacement()
    {
        return dataLabelPlacement.getShortValue(field_11_options2);
    }
}
