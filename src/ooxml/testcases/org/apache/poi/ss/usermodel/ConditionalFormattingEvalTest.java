package org.apache.poi.ss.usermodel;


import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.formula.ConditionalFormattingEvaluator;
import org.apache.poi.ss.formula.EvaluationConditionalFormatRule;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.XSSFTestDataSamples;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConditionalFormattingEvalTest {

    private XSSFWorkbook wb;
    private Sheet sheet;
    private XSSFFormulaEvaluator formulaEval;
    private ConditionalFormattingEvaluator cfe;
    private CellReference ref;
    private List<EvaluationConditionalFormatRule> rules;

    @Before
    public void openWB() {
        wb = XSSFTestDataSamples.openSampleWorkbook("ConditionalFormattingSamples.xlsx");
        formulaEval = new XSSFFormulaEvaluator(wb);
        cfe = new ConditionalFormattingEvaluator(wb, formulaEval);
    }
    
    @After
    public void closeWB() {
        formulaEval = null;
        cfe = null;
        ref = null;
        rules = null;
        try {
            if (wb != null) wb.close();
        } catch (IOException e) {
            // keep going, this shouldn't cancel things
            e.printStackTrace();
        }
    }

    @Test
    public void testFormattingEvaluation() {
        sheet = wb.getSheet("Products1");
        
        getRulesFor(12, 1);
        assertEquals("wrong # of rules for " + ref, 1, rules.size());
        assertEquals("wrong bg color for " + ref, "FFFFEB9C", getColor(rules.get(0).getRule().getPatternFormatting().getFillBackgroundColorColor()));
        assertFalse("should not be italic " + ref, rules.get(0).getRule().getFontFormatting().isItalic());
        
        getRulesFor(16, 3);
        assertEquals("wrong # of rules for " + ref, 1, rules.size());
        assertEquals("wrong bg color for " + ref, 0.7999816888943144d, getTint(rules.get(0).getRule().getPatternFormatting().getFillBackgroundColorColor()), 0.000000000000001);
        
        getRulesFor(12, 3);
        assertEquals("wrong # of rules for " + ref, 0, rules.size());
        
        sheet = wb.getSheet("Products2");
        
        getRulesFor(15,1);
        assertEquals("wrong # of rules for " + ref, 1, rules.size());
        assertEquals("wrong bg color for " + ref, "FFFFEB9C", getColor(rules.get(0).getRule().getPatternFormatting().getFillBackgroundColorColor()));

        getRulesFor(20,3);
        assertEquals("wrong # of rules for " + ref, 0, rules.size());

        // now change a cell value that's an input for the rules
        Cell cell = sheet.getRow(1).getCell(6);
        cell.setCellValue("Dairy");
        formulaEval.notifyUpdateCell(cell);
        cell = sheet.getRow(4).getCell(6);
        cell.setCellValue(500);
        formulaEval.notifyUpdateCell(cell);
        // need to throw away all evaluations, since we don't know how value changes may have affected format formulas
        cfe.clearAllCachedValues();
        
        // test that the conditional validation evaluations changed
        getRulesFor(15,1);
        assertEquals("wrong # of rules for " + ref, 0, rules.size());
        
        getRulesFor(20,3);
        assertEquals("wrong # of rules for " + ref, 1, rules.size());
        assertEquals("wrong bg color for " + ref, 0.7999816888943144d, getTint(rules.get(0).getRule().getPatternFormatting().getFillBackgroundColorColor()), 0.000000000000001);
        
        getRulesFor(20,1);
        assertEquals("wrong # of rules for " + ref, 1, rules.size());
        assertEquals("wrong bg color for " + ref, "FFFFEB9C", getColor(rules.get(0).getRule().getPatternFormatting().getFillBackgroundColorColor()));
        
        sheet = wb.getSheet("Book tour");
        
        getRulesFor(8,2);
        assertEquals("wrong # of rules for " + ref, 1, rules.size());
        
    }
    
    private List<EvaluationConditionalFormatRule> getRulesFor(int row, int col) {
        ref = new CellReference(sheet.getSheetName(), row, col, false, false);
        return rules = cfe.getConditionalFormattingForCell(ref);
    }
    
    private String getColor(Color color) {
        final XSSFColor c = XSSFColor.toXSSFColor(color);
        return c.getARGBHex();
    }

    private double getTint(Color color) {
        final XSSFColor c = XSSFColor.toXSSFColor(color);
        return c.getTint();
    }
}
