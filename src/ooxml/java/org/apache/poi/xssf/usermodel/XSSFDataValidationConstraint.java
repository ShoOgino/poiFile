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
package org.apache.poi.xssf.usermodel;

import java.util.Arrays;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationType;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationOperator.Enum;

/**
 * @author <a href="rjankiraman@emptoris.com">Radhakrishnan J</a>
 *
 */
public class XSSFDataValidationConstraint implements DataValidationConstraint {
    /**
     * Excel validation constraints with static lists are delimited with optional whitespace and the Windows List Separator,
     * which is typically comma, but can be changed by users.  POI will just assume comma.
     */
    private static final String LIST_SEPARATOR = ",";
    private static final Pattern LIST_SPLIT_REGEX = Pattern.compile("\\s*" + LIST_SEPARATOR + "\\s*");
    private static final String QUOTE = "\"";
    
	private String formula1;
	private String formula2;
	private int validationType = -1;
	private int operator = -1;
	private String[] explicitListOfValues;

	public XSSFDataValidationConstraint(String[] explicitListOfValues) {
		if( explicitListOfValues==null || explicitListOfValues.length==0) {
			throw new IllegalArgumentException("List validation with explicit values must specify at least one value");
		}
		this.validationType = ValidationType.LIST;
		setExplicitListValues(explicitListOfValues);
		
		validate();
	}
	
	public XSSFDataValidationConstraint(int validationType,String formula1) {
		super();
		setFormula1(formula1);
		this.validationType = validationType;
		validate();
	}



	public XSSFDataValidationConstraint(int validationType, int operator,String formula1) {
		super();
		setFormula1(formula1);
		this.validationType = validationType;
		this.operator = operator;
		validate();
	}

	public XSSFDataValidationConstraint(int validationType, int operator,String formula1, String formula2) {
		super();
		setFormula1(formula1);
		setFormula2(formula2);
		this.validationType = validationType;
		this.operator = operator;
		
		validate();
		
		//FIXME: Need to confirm if this is not a formula.
		// empirical testing shows Excel saves explicit lists surrounded by double quotes, 
		// range formula expressions can't start with quotes (I think - anyone have a creative counter example?)
		if( ValidationType.LIST==validationType
				&& formula1 != null
				&& formula1.startsWith(QUOTE)
				&& formula1.endsWith(QUOTE) ) {
            final String formulaWithoutQuotes = formula1.substring(1, formula1.length()-1);
			explicitListOfValues = LIST_SPLIT_REGEX.split(formulaWithoutQuotes);
		}
	}

	/**
	 * If validation type is {@link org.apache.poi.ss.usermodel.DataValidationConstraint.ValidationType#LIST}, returns list of literal values.
	 * Otherwise returns <code>null</code>.
	 * @see org.apache.poi.ss.usermodel.DataValidationConstraint#getExplicitListValues()
	 */
	public String[] getExplicitListValues() {
		return explicitListOfValues;
	}

	/* (non-Javadoc)
	 * @see org.apache.poi.ss.usermodel.DataValidationConstraint#getFormula1()
	 */
	public String getFormula1() {
		return formula1;
	}

	/* (non-Javadoc)
	 * @see org.apache.poi.ss.usermodel.DataValidationConstraint#getFormula2()
	 */
	public String getFormula2() {
		return formula2;
	}

	/* (non-Javadoc)
	 * @see org.apache.poi.ss.usermodel.DataValidationConstraint#getOperator()
	 */
	public int getOperator() {
		return operator;
	}

	/* (non-Javadoc)
	 * @see org.apache.poi.ss.usermodel.DataValidationConstraint#getValidationType()
	 */
	public int getValidationType() {
		return validationType;
	}

	/* (non-Javadoc)
	 * @see org.apache.poi.ss.usermodel.DataValidationConstraint#setExplicitListValues(java.lang.String[])
	 */
	public void setExplicitListValues(String[] explicitListValues) {
		this.explicitListOfValues = explicitListValues;
		if( explicitListOfValues!=null && explicitListOfValues.length > 0 ) {
			StringBuilder builder = new StringBuilder("\"");
			for (int i = 0; i < explicitListValues.length; i++) {
				String string = explicitListValues[i];
				if( builder.length() > 1) {
					builder.append(LIST_SEPARATOR);
				}
				builder.append(string);
			}
			builder.append("\"");
			setFormula1(builder.toString());			
		}
	}

	/* (non-Javadoc)
	 * @see org.apache.poi.ss.usermodel.DataValidationConstraint#setFormula1(java.lang.String)
	 */
	public void setFormula1(String formula1) {
		this.formula1 = removeLeadingEquals(formula1);
	}

	protected static String removeLeadingEquals(String formula1) {
		return isFormulaEmpty(formula1) ? formula1 : formula1.charAt(0)=='=' ? formula1.substring(1) : formula1;
	}

	/* (non-Javadoc)
	 * @see org.apache.poi.ss.usermodel.DataValidationConstraint#setFormula2(java.lang.String)
	 */
	public void setFormula2(String formula2) {
		this.formula2 = removeLeadingEquals(formula2);
	}

	/* (non-Javadoc)
	 * @see org.apache.poi.ss.usermodel.DataValidationConstraint#setOperator(int)
	 */
	public void setOperator(int operator) {
		this.operator = operator;
	}

	public void validate() {
		if (validationType==ValidationType.ANY) {
			return;
		}
		
		if (validationType==ValidationType.LIST ) {
			if (isFormulaEmpty(formula1)) {
				throw new IllegalArgumentException("A valid formula or a list of values must be specified for list validation.");
			}
		} else  {
			if( isFormulaEmpty(formula1) ) {
				throw new IllegalArgumentException("Formula is not specified. Formula is required for all validation types except explicit list validation.");
			}
			
			if( validationType!= ValidationType.FORMULA ) {
				if (operator==-1) {
					throw new IllegalArgumentException("This validation type requires an operator to be specified.");
				} else if (( operator==OperatorType.BETWEEN || operator==OperatorType.NOT_BETWEEN) && isFormulaEmpty(formula2)) {
					throw new IllegalArgumentException("Between and not between comparisons require two formulae to be specified.");
				}
			}
		}
	}

	protected static boolean isFormulaEmpty(String formula1) {
		return formula1 == null || formula1.trim().length()==0;
	}
	
	public String prettyPrint() {
		StringBuilder builder = new StringBuilder();
		STDataValidationType.Enum vt = XSSFDataValidation.validationTypeMappings.get(validationType);
		Enum ot = XSSFDataValidation.operatorTypeMappings.get(operator);
		builder.append(vt);
		builder.append(' ');
		if (validationType!=ValidationType.ANY) {
			if (validationType != ValidationType.LIST
					&& validationType != ValidationType.ANY
					&& validationType != ValidationType.FORMULA) {
				builder.append(LIST_SEPARATOR).append(ot).append(", ");
			}

			if (validationType == ValidationType.LIST && explicitListOfValues != null) {
				builder.append(QUOTE).append(Arrays.asList(explicitListOfValues)).append(QUOTE).append(' ');
			} else {
				builder.append(QUOTE).append(formula1).append(QUOTE).append(' ');
			}
			if (formula2 != null) {
				builder.append(QUOTE).append(formula2).append(QUOTE).append(' ');
			}
		}
		return builder.toString();
	}
}
