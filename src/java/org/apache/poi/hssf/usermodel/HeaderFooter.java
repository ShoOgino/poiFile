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

/**
 * Common class for {@link HSSFHeader} and
 *  {@link HSSFFooter}.
 */
public abstract class HeaderFooter {
	protected String left;
	protected String center;
	protected String right;
	
	protected HeaderFooter(String text) {
		while (text != null && text.length() > 1) {
		    int pos = text.length();
		    switch (text.substring(1, 2).charAt(0)) {
			    case 'L' :
				if (text.indexOf("&C") >= 0) {
				    pos = Math.min(pos, text.indexOf("&C"));
				} 
				if (text.indexOf("&R") >= 0) {
				    pos = Math.min(pos, text.indexOf("&R"));
				} 
				left = text.substring(2, pos);
				text = text.substring(pos);
				break;
		    case 'C' : 
				if (text.indexOf("&L") >= 0) {
				    pos = Math.min(pos, text.indexOf("&L"));
				} 
				if (text.indexOf("&R") >= 0) {
				    pos = Math.min(pos, text.indexOf("&R"));
				} 
				center = text.substring(2, pos);
				text = text.substring(pos);
				break;
		    case 'R' : 
				if (text.indexOf("&C") >= 0) {
				    pos = Math.min(pos, text.indexOf("&C"));
				} 
				if (text.indexOf("&L") >= 0) {
				    pos = Math.min(pos, text.indexOf("&L"));
				} 
				right = text.substring(2, pos);
				text = text.substring(pos);
				break;
		    default: 
		    	text = null;
		    }
		}
	}
	
    /**
     * Get the left side of the header or footer.
     * @return The string representing the left side.
     */
    public String getLeft() {
		return left;
	}
    public abstract void setLeft( String newLeft );

    /**
     * Get the center of the header or footer.
     * @return The string representing the center.
     */
    public String getCenter() {
    	return center;
    }
    public abstract void setCenter( String newCenter );

    /**
     * Get the right side of the header or footer.
     * @return The string representing the right side.
     */
    public String getRight() {
    	return right;
    }
    public abstract void setRight( String newRight );

    

    /**
     * Returns the string that represents the change in font size.
     *
     * @param size the new font size
     * @return The special string to represent a new font size
     */
    public static String fontSize( short size )
    {
        return "&" + size;
    }

    /**
     * Returns the string that represents the change in font.
     *
     * @param font  the new font
     * @param style the fonts style
     * @return The special string to represent a new font size
     */
    public static String font( String font, String style )
    {
        return "&\"" + font + "," + style + "\"";
    }

    /**
     * Returns the string representing the current page number
     *
     * @return The special string for page number
     */
    public static String page()
    {
        return "&P";
    }

    /**
     * Returns the string representing the number of pages.
     *
     * @return The special string for the number of pages
     */
    public static String numPages()
    {
        return "&N";
    }

    /**
     * Returns the string representing the current date
     *
     * @return The special string for the date
     */
    public static String date()
    {
        return "&D";
    }

    /**
     * Returns the string representing the current time
     *
     * @return The special string for the time
     */
    public static String time()
    {
        return "&T";
    }

    /**
     * Returns the string representing the current file name
     *
     * @return The special string for the file name
     */
    public static String file()
    {
        return "&F";
    }

    /**
     * Returns the string representing the current tab (sheet) name
     *
     * @return The special string for tab name
     */
    public static String tab()
    {
        return "&A";
    }

    /**
     * Returns the string representing the start underline
     *
     * @return The special string for start underline
     */
    public static String startUnderline()
    {
        return "&U";
    }

    /**
     * Returns the string representing the end underline
     *
     * @return The special string for end underline
     */
    public static String endUnderline()
    {
        return "&U";
    }

    /**
     * Returns the string representing the start double underline
     *
     * @return The special string for start double underline
     */
    public static String startDoubleUnderline()
    {
        return "&E";
    }

    /**
     * Returns the string representing the end double underline
     *
     * @return The special string for end double underline
     */
    public static String endDoubleUnderline()
    {
        return "&E";
    }
}
