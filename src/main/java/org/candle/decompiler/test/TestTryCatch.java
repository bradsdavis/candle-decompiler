package org.candle.decompiler.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TestTryCatch {

	private static final Log LOG = LogFactory.getLog(TestTryCatch.class);
	
	public void testTryCatch() {
		try {
			for(int i=0; i<5; i++) {
				System.out.println(i);
			}
		}
		catch(IllegalStateException e) {
			System.err.println("Exception!!");
		}
		catch(IllegalAccessError e) {
			System.err.println("Another !!");
		}
		finally {
			System.out.println("Finally!!");
		}
		
		System.out.println("After block.");
	}
}
