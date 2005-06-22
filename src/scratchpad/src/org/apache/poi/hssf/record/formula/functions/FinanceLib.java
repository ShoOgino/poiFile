/*
 * Created on May 21, 2005
 *
 */
package org.apache.poi.hssf.record.formula.functions;

/**
 * @author Amol S. Deshmukh &lt; amolweb at ya hoo dot com &gt;
 * 
 * 
 * This class is a functon library for common fiscal functions.
 * <b>Glossary of terms/abbreviations:</b>
 * <br/>
 * <ul>
 * <li><em>FV:</em> Future Value</li>
 * <li><em>PV:</em> Present Value</li>
 * <li><em>NPV:</em> Net Present Value</li>
 * <li><em>PMT:</em> (Periodic) Payment</li>
 * 
 * </ul>
 * For more info on the terms/abbreviations please use the references below 
 * (hyperlinks are subject to change):
 * </br>Online References:
 * <ol>
 * <li>GNU Emacs Calc 2.02 Manual: http://theory.uwinnipeg.ca/gnu/calc/calc_203.html</li>
 * <li>Yahoo Financial Glossary: http://biz.yahoo.com/f/g/nn.html#y</li>
 * <li>MS Excel function reference: http://office.microsoft.com/en-us/assistance/CH062528251033.aspx</li>
 * </ol>
 * <h3>Implementation Notes:</h3>
 * Symbols used in the formulae that follow:<br/>
 * <ul>
 * <li>p: present value</li>
 * <li>f: future value</li>
 * <li>n: number of periods</li>
 * <li>y: payment (in each period)</li>
 * <li>r: rate</li>
 * <li>^: the power operator (NOT the java bitwise XOR operator!)</li>
 * </ul>
 * [From MS Excel function reference] Following are some of the key formulas
 * that are used in this implementation:
 * <pre>
 * p(1+r)^n + y(1+rt)((1+r)^n-1)/r + f=0   ...{when r!=0}
 * ny + p + f=0                            ...{when r=0}
 * </pre>
 */
public final class FinanceLib {
    
    // constants for default values
    
    
    
    private FinanceLib() {}
    
    /**
     * Future value of an amount given the number of payments, rate, amount
     * of individual payment, present value and boolean value indicating whether
     * payments are due at the beginning of period 
     * (false => payments are due at end of period) 
     * @param r rate
     * @param n num of periods
     * @param y pmt per period
     * @param f future value
     * @param t type (true=pmt at end of period, false=pmt at begining of period)
     * @return
     */
    public static double fv(double r, double n, double y, double p, boolean t) {
        double retval = 0;
        if (r == 0) {
            retval = -1*(p+(n*y));
        }
        else {
            double r1 = r + 1;
            retval =((1-Math.pow(r1, n)) * (t ? r1 : 1) * y ) / r  
                      - 
                   p*Math.pow(r1, n);
        }
        return retval;
    }
    
    /**
     * Present value of an amount given the number of future payments, rate, amount
     * of individual payment, future value and boolean value indicating whether
     * payments are due at the beginning of period 
     * (false => payments are due at end of period) 
     * @param r
     * @param n
     * @param y
     * @param f
     * @param t
     * @return
     */
    public static double pv(double r, double n, double y, double f, boolean t) {
        double retval = 0;
        if (r == 0) {
            retval = -1*((n*y)+f);
        }
        else {
            double r1 = r + 1;
            retval =(( ( 1 - Math.pow(r1, n) ) / r ) * (t ? r1 : 1)  * y - f)
                     /
                    Math.pow(r1, n);
        }
        return retval;
    }
    
    /**
     * calculates the Net Present Value of a principal amount
     * given the discount rate and a sequence of cash flows 
     * (supplied as an array). If the amounts are income the value should 
     * be positive, else if they are payments and not income, the 
     * value should be negative.
     * @param r
     * @param cfs cashflow amounts
     * @return
     */
    public static double npv(double r, double[] cfs) {
        double npv = 0;
        double r1 = r + 1;
        double trate = r1;
        for (int i=0, iSize=cfs.length; i<iSize; i++) {
            npv += cfs[i] / trate;
            trate *= r1;
        }
        return npv;
    }
    
    /**
     * 
     * @param r
     * @param n
     * @param p
     * @param f
     * @param t
     * @return
     */
    public static double pmt(double r, double n, double p, double f, boolean t) {
        double retval = 0;
        if (r == 0) {
            retval = -1*(f+p)/n;
        }
        else {
        double r1 = r + 1;
        retval = ( f + p * Math.pow(r1, n) ) * r 
                  / 
               ((t ? r1 : 1) * (1 - Math.pow(r1, n)));
        }
        return retval;
    }

    /**
     * 
     * @param r
     * @param n
     * @param p
     * @param f
     * @param t
     * @return
     */
    public static double nper(double r, double y, double p, double f, boolean t) {
        double retval = 0;
        if (r == 0) {
            retval = -1 * (f + p) / y;
        } else {
            double r1 = r + 1;
            double ryr = (t ? r1 : 1) * y / r;
            double a1 = ((ryr - f) < 0)
                    ? Math.log(f - ryr)
                    : Math.log(ryr - f);
            double a2 = ((ryr - f) < 0)
                    ? Math.log(-p - ryr)
                    : Math.log(p + ryr);
            double a3 = Math.log(r1);
            retval = (a1 - a2) / a3;
        }
        return retval;
    }
    

}
