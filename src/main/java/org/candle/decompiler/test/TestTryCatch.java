package org.candle.decompiler.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TestTryCatch {

	private static final Log LOG = LogFactory.getLog(TestTryCatch.class);
	
	public void testTryCatch() {
		int i = 5;
		
		try {
			System.out.println("Inner try");
			for(int j=0; j<5; j++) {
				System.out.println("X");
			}
				
			//System.out.println("here");
		}
		catch(IllegalArgumentException e) {
			System.err.println("Illegal Arg Exception!!"+e.getMessage());
		}
		catch(IllegalStateException e) {
			System.err.println("Exception!!"+e.getMessage());
		}
		finally {
			System.out.println("Finally!!");
			if(i>2) {
				System.out.println("Greater!!");
			}
			else {
				System.out.println("Less!!");
			}
		}
		System.out.println("After block.");
	}
}
