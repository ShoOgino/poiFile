/*
 *  ====================================================================
 *  The Apache Software License, Version 1.1
 *
 *  Copyright (c) 2000 The Apache Software Foundation.  All rights
 *  reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *  1. Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the
 *  distribution.
 *
 *  3. The end-user documentation included with the redistribution,
 *  if any, must include the following acknowledgment:
 *  "This product includes software developed by the
 *  Apache Software Foundation (http://www.apache.org/)."
 *  Alternately, this acknowledgment may appear in the software itself,
 *  if and wherever such third-party acknowledgments normally appear.
 *
 *  4. The names "Apache" and "Apache Software Foundation" must
 *  not be used to endorse or promote products derived from this
 *  software without prior written permission. For written
 *  permission, please contact apache@apache.org.
 *
 *  5. Products derived from this software may not be called "Apache",
 *  nor may "Apache" appear in their name, without prior written
 *  permission of the Apache Software Foundation.
 *
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 *  ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 *  USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 *  OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 *  SUCH DAMAGE.
 *  ====================================================================
 *
 *  This software consists of voluntary contributions made by many
 *  individuals on behalf of the Apache Software Foundation.  For more
 *  information on the Apache Software Foundation, please see
 *  <http://www.apache.org/>.
 */
package org.apache.poi.hpsf;

import org.apache.poi.util.HexDump;

/**
 * <p>This exception is thrown if HPSF encounters a variant type that isn't
 * supported yet. Although a variant type is unsupported the value can still be
 * retrieved using the {@link #getValue} method.</p>
 * 
 * <p>Obviously this class should disappear some day.</p>
 *
 * @author Rainer Klute <a
 * href="mailto:klute@rainer-klute.de">&lt;klute@rainer-klute.de&gt;</a>
 * @since 2003-08-05
 * @version $Id$
 */
public abstract class UnsupportedVariantTypeException extends HPSFException
{

    private Object value;

    private long variantType;



    /**
     * <p>Constructor.</p>
     * 
     * @param variantType The unsupported variant type
     * @param value The value who's variant type is not yet supported
     */
    public UnsupportedVariantTypeException(final long variantType,
                                           final Object value)
    {
        super("HPSF does not yet support the variant type " + variantType + 
              " (" + Variant.getVariantName(variantType) + ", " +
              HexDump.toHex((int) variantType) + "). If you want support for " +
              "this variant type in one of the next POI releases please " +
              "submit a request for enhancement (RFE) to " +
              "<http://nagoya.apache.org/bugzilla/>! Thank you!");
        this.variantType = variantType;
        this.value = value;
    }



    /**
     * <p>Returns the offending variant type.</p>
     *
     * @return the offending variant type.
     */
    public long getVariantType()
    {
        return variantType;
    }



    /**
     * <p>Return the value who's variant type is not yet supported.</p>
     *
     * @return the value who's variant type is not yet supported
     */
    public Object getValue()
    {
        return value;
    }

}
