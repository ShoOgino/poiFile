/*
 * Created on May 6, 2005
 *
 */
package org.apache.poi.hssf.record.formula.functions;

import org.apache.poi.hssf.record.formula.eval.BlankEval;
import org.apache.poi.hssf.record.formula.eval.ErrorEval;
import org.apache.poi.hssf.record.formula.eval.Eval;
import org.apache.poi.hssf.record.formula.eval.NumberEval;
import org.apache.poi.hssf.record.formula.eval.NumericValueEval;
import org.apache.poi.hssf.record.formula.eval.ValueEval;
import org.apache.poi.hssf.record.formula.eval.ValueEvalToNumericXlator;

/**
 * @author Amol S. Deshmukh &lt; amolweb at ya hoo dot com &gt;
 *  
 */
public class Atan2 extends NumericFunction {
    
    private static final ValueEvalToNumericXlator NUM_XLATOR = 
        new ValueEvalToNumericXlator((short)
                ( ValueEvalToNumericXlator.BOOL_IS_PARSED 
                | ValueEvalToNumericXlator.EVALUATED_REF_BOOL_IS_PARSED
                | ValueEvalToNumericXlator.EVALUATED_REF_STRING_IS_PARSED
                | ValueEvalToNumericXlator.REF_BOOL_IS_PARSED
                | ValueEvalToNumericXlator.STRING_IS_PARSED
                ));

    protected ValueEvalToNumericXlator getXlator() {
        return NUM_XLATOR;
    }

    public Eval evaluate(Eval[] operands, int srcRow, short srcCol) {
        double d0 = 0;
        double d1 = 0;
        ValueEval retval = null;
        
        switch (operands.length) {
        default:
            break;
        case 2:
            ValueEval ve = singleOperandEvaluate(operands[0], srcRow, srcCol);
            if (ve instanceof NumericValueEval) {
                NumericValueEval ne = (NumericValueEval) ve;
                d0 = ne.getNumberValue();
            }
            else if (ve instanceof BlankEval) {
                // do nothing
            }
            else {
                retval = ErrorEval.NUM_ERROR;
            }
            
            if (retval == null) {
                ve = singleOperandEvaluate(operands[1], srcRow, srcCol);
                if (ve instanceof NumericValueEval) {
                    NumericValueEval ne = (NumericValueEval) ve;
                    d1 = ne.getNumberValue();
                }
                else if (ve instanceof BlankEval) {
                    // do nothing
                }
                else {
                    retval = ErrorEval.NUM_ERROR;
                }
            }
        }
        
        if (retval == null) {
            double d = (d0 == d1 && d1 == 0) ? Double.NaN : Math.atan2(d1, d0);
            retval = (Double.isNaN(d) || Double.isInfinite(d)) ? (ValueEval) ErrorEval.VALUE_INVALID : new NumberEval(d);
        }
        return retval;
    }

}
