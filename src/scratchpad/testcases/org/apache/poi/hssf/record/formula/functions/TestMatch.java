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

package org.apache.poi.hssf.record.formula.functions;

import junit.framework.TestCase;

import org.apache.poi.hssf.record.formula.AreaPtg;
import org.apache.poi.hssf.record.formula.eval.Area2DEval;
import org.apache.poi.hssf.record.formula.eval.BoolEval;
import org.apache.poi.hssf.record.formula.eval.ErrorEval;
import org.apache.poi.hssf.record.formula.eval.Eval;
import org.apache.poi.hssf.record.formula.eval.NumberEval;
import org.apache.poi.hssf.record.formula.eval.NumericValueEval;
import org.apache.poi.hssf.record.formula.eval.StringEval;
import org.apache.poi.hssf.record.formula.eval.ValueEval;

/**
 * Test cases for MATCH()
 * 
 * @author Josh Micich
 */
public final class TestMatch extends TestCase {
	/** less than or equal to */
	private static final NumberEval MATCH_LARGEST_LTE = new NumberEval(1);
	private static final NumberEval MATCH_EXACT = new NumberEval(0);
	/** greater than or equal to */
	private static final NumberEval MATCH_SMALLEST_GTE = new NumberEval(-1);
	
	
	private static Eval invokeMatch(Eval lookup_value, Eval lookup_array, Eval match_type) {
		Eval[] args = { lookup_value, lookup_array, match_type, };
		return new Match().evaluate(args, -1, (short)-1);
	}
	private static void confirmInt(int expected, Eval actualEval) {
		if(!(actualEval instanceof NumericValueEval)) {
			fail("Expected numeric result");
		}
		NumericValueEval nve = (NumericValueEval)actualEval;
		assertEquals(expected, nve.getNumberValue(), 0);
	}
	
	public void testSimpleNumber() {
		
		ValueEval[] values = {
			new NumberEval(4),	
			new NumberEval(5),	
			new NumberEval(10),	
			new NumberEval(10),	
			new NumberEval(25),	
		};
		
		AreaPtg areaPtg = new AreaPtg("A1:A5");
		Area2DEval ae = new Area2DEval(areaPtg, values);
		
		
		confirmInt(2, invokeMatch(new NumberEval(5), ae, MATCH_LARGEST_LTE));
		confirmInt(2, invokeMatch(new NumberEval(5), ae, MATCH_EXACT));
		confirmInt(4, invokeMatch(new NumberEval(10), ae, MATCH_LARGEST_LTE));
		confirmInt(3, invokeMatch(new NumberEval(10), ae, MATCH_EXACT));
		confirmInt(4, invokeMatch(new NumberEval(20), ae, MATCH_LARGEST_LTE));
		assertEquals(ErrorEval.NA, invokeMatch(new NumberEval(20), ae, MATCH_EXACT));
	}
	
	public void testReversedNumber() {
		
		ValueEval[] values = {
			new NumberEval(25),	
			new NumberEval(10),	
			new NumberEval(10),	
			new NumberEval(10),	
			new NumberEval(4),	
		};
		
		AreaPtg areaPtg = new AreaPtg("A1:A5");
		Area2DEval ae = new Area2DEval(areaPtg, values);
		
		
		confirmInt(2, invokeMatch(new NumberEval(10), ae, MATCH_SMALLEST_GTE));
		confirmInt(2, invokeMatch(new NumberEval(10), ae, MATCH_EXACT));
		confirmInt(4, invokeMatch(new NumberEval(9), ae, MATCH_SMALLEST_GTE));
		confirmInt(1, invokeMatch(new NumberEval(20), ae, MATCH_SMALLEST_GTE));
		assertEquals(ErrorEval.NA, invokeMatch(new NumberEval(20), ae, MATCH_EXACT));
		assertEquals(ErrorEval.NA, invokeMatch(new NumberEval(26), ae, MATCH_SMALLEST_GTE));
	}
	
	public void testSimpleString() {
		
		ValueEval[] values = {
			new StringEval("Albert"),	
			new StringEval("Charles"),	
			new StringEval("Ed"),	
			new StringEval("Greg"),	
			new StringEval("Ian"),	
		};
		
		AreaPtg areaPtg = new AreaPtg("A1:A5");
		Area2DEval ae = new Area2DEval(areaPtg, values);
		
		// Note String comparisons are case insensitive
		confirmInt(3, invokeMatch(new StringEval("Ed"), ae, MATCH_LARGEST_LTE));
		confirmInt(3, invokeMatch(new StringEval("eD"), ae, MATCH_LARGEST_LTE));
		confirmInt(3, invokeMatch(new StringEval("Ed"), ae, MATCH_EXACT));
		confirmInt(3, invokeMatch(new StringEval("ed"), ae, MATCH_EXACT));
		confirmInt(4, invokeMatch(new StringEval("Hugh"), ae, MATCH_LARGEST_LTE));
		assertEquals(ErrorEval.NA, invokeMatch(new StringEval("Hugh"), ae, MATCH_EXACT));
	}
	
	public void testSimpleBoolean() {
		
		ValueEval[] values = {
				BoolEval.FALSE,	
				BoolEval.FALSE,	
				BoolEval.TRUE,	
				BoolEval.TRUE,	
		};
		
		AreaPtg areaPtg = new AreaPtg("A1:A4");
		Area2DEval ae = new Area2DEval(areaPtg, values);
		
		// Note String comparisons are case insensitive
		confirmInt(2, invokeMatch(BoolEval.FALSE, ae, MATCH_LARGEST_LTE));
		confirmInt(1, invokeMatch(BoolEval.FALSE, ae, MATCH_EXACT));
		confirmInt(4, invokeMatch(BoolEval.TRUE, ae, MATCH_LARGEST_LTE));
		confirmInt(3, invokeMatch(BoolEval.TRUE, ae, MATCH_EXACT));
	}
	
	public void testHeterogeneous() {
		
		ValueEval[] values = {
				new NumberEval(4),	
				BoolEval.FALSE,	
				new NumberEval(5),
				new StringEval("Albert"),	
				BoolEval.FALSE,	
				BoolEval.TRUE,	
				new NumberEval(10),	
				new StringEval("Charles"),	
				new StringEval("Ed"),	
				new NumberEval(10),	
				new NumberEval(25),	
				BoolEval.TRUE,	
				new StringEval("Ed"),	
		};
		
		AreaPtg areaPtg = new AreaPtg("A1:A13");
		Area2DEval ae = new Area2DEval(areaPtg, values);
		
		assertEquals(ErrorEval.NA, invokeMatch(new StringEval("Aaron"), ae, MATCH_LARGEST_LTE));
		
		confirmInt(5, invokeMatch(BoolEval.FALSE, ae, MATCH_LARGEST_LTE));
		confirmInt(2, invokeMatch(BoolEval.FALSE, ae, MATCH_EXACT));
		confirmInt(3, invokeMatch(new NumberEval(5), ae, MATCH_LARGEST_LTE));
		confirmInt(3, invokeMatch(new NumberEval(5), ae, MATCH_EXACT));
		
		confirmInt(8, invokeMatch(new StringEval("CHARLES"), ae, MATCH_EXACT));
		
		confirmInt(4, invokeMatch(new StringEval("Ben"), ae, MATCH_LARGEST_LTE));
		
		confirmInt(13, invokeMatch(new StringEval("ED"), ae, MATCH_LARGEST_LTE));
		confirmInt(9, invokeMatch(new StringEval("ED"), ae, MATCH_EXACT));
			
		confirmInt(13, invokeMatch(new StringEval("Hugh"), ae, MATCH_LARGEST_LTE));
		assertEquals(ErrorEval.NA, invokeMatch(new StringEval("Hugh"), ae, MATCH_EXACT));
		
		confirmInt(11, invokeMatch(new NumberEval(30), ae, MATCH_LARGEST_LTE));
		confirmInt(12, invokeMatch(BoolEval.TRUE, ae, MATCH_LARGEST_LTE));
	}
	
}
