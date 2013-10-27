package org.apache.poi.hssf.dev;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

public class TestEFBiffViewer extends BaseXLSIteratingTest {
	static {
		// Look at the output of the test for the detailed stacktrace of the failures...
		//EXCLUDED.add("");

		// these are likely ok to fail
		SILENT_EXCLUDED.add("XRefCalc.xls"); 
		SILENT_EXCLUDED.add("password.xls"); 
		SILENT_EXCLUDED.add("51832.xls"); 		// password
		SILENT_EXCLUDED.add("43493.xls");	// HSSFWorkbook cannot open it as well
	};
	
	@Override
	void runOneFile(String dir, String file, List<String> failed) throws IOException {
		PrintStream save = System.out;
		try {
			// redirect standard out during the test to avoid spamming the console with output
			System.setOut(new PrintStream(NULL_OUTPUT_STREAM));

			EFBiffViewer.main(new String[] { new File(dir, file).getAbsolutePath() });
		} finally {
			System.setOut(save);
		}
	}
}
