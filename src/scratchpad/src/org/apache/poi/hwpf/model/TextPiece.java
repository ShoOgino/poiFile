/* ====================================================================
   Copyright 2002-2004   Apache Software Foundation

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */


package org.apache.poi.hwpf.model;


import java.io.UnsupportedEncodingException;
/**
 * Lightweight representation of a text piece.
 *
 * @author Ryan Ackley
 */

public class TextPiece extends PropertyNode implements Comparable
{
  private boolean _usesUnicode;

  private PieceDescriptor _pd;

  /**
   * @param start Offset in main document stream.
   * @param length The total length of the text in bytes. Note: 1 character
   *        does not necessarily refer to 1 byte.
   * @param unicode true if this text is unicode.
   */
  public TextPiece(int start, int end, byte[] text, PieceDescriptor pd)
    throws UnsupportedEncodingException
  {
     /** start - end is length on file. This is double the expected when its
     * unicode.*/
    super(start, end, new StringBuffer(new String(text, pd.isUnicode() ? "UTF-16LE" : "Cp1252")));
    _usesUnicode = pd.isUnicode();
    _pd = pd;
  }
  /**
   * @return If this text piece uses unicode
   */
   public boolean usesUnicode()
   {
      return _usesUnicode;
   }

   public PieceDescriptor getPieceDescriptor()
   {
     return _pd;
   }

   public StringBuffer getStringBuffer()
   {
     return (StringBuffer)_buf;
   }

   public void setStringBuffer(StringBuffer buf)
   {
     _buf = buf;
   }

   public byte[] getRawBytes()
   {
     try
     {
       return ((StringBuffer)_buf).toString().getBytes(_usesUnicode ?
           "UTF-16LE" : "Cp1252");
     }
     catch (UnsupportedEncodingException ignore)
     {
       // shouldn't ever happen considering we wouldn't have been able to
       // create the StringBuffer w/o getting this exception
       return ((StringBuffer)_buf).toString().getBytes();
     }

   }

   public String substring(int start, int end)
   {
     int denominator = _usesUnicode ? 2 : 1;

     return ((StringBuffer)_buf).substring(start/denominator, end/denominator);
   }

   public void adjustForDelete(int start, int length)
   {

   }

   public int characterLength()
   {
     return (getEnd() - getStart()) / (_usesUnicode ? 2 : 1);
   }

   public boolean equals(Object o)
   {
     if (limitsAreEqual(o))
     {
       TextPiece tp = (TextPiece)o;
       return getStringBuffer().toString().equals(tp.getStringBuffer().toString()) &&
              tp._usesUnicode == _usesUnicode && _pd.equals(tp._pd);
     }
     return false;
   }

}
