
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
        

package org.apache.poi.hwpf.sprm;



public class SprmUncompressor
{
  public SprmUncompressor()
  {
  }

  /**
   * Converts an int into a boolean. If the int is non-zero, it returns true.
   * Otherwise it returns false.
   *
   * @param x The int to convert.
   *
   * @return A boolean whose value depends on x.
   */
  public static boolean getFlag (int x)
  {
    if (x != 0)
    {
      return true;
    }
    else
    {
      return false;
    }
  }


}
