package org.candle.decompiler.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TestSimpleCondition {
	
	private String fieldVal = "hello world";
	private int fieldInt = 5;
	
	private static int CONSTI = 5;
	public static int CONSTJ = 7;
	private static final Log LOG = LogFactory.getLog(TestSimpleCondition.class);
	
	private final int example;
	
	public TestSimpleCondition() {
		example = 1;
	}
	
	public void testArrayCreation() {
		int i=3;
		int j=5;
		int k=5;
		
		if(i==3 || j==5 || k==1 && k < 10) {
			LOG.info("Nested or block;");

			if(i == 10) {
				LOG.info("Nested If!");
			}
			else if(i ==5) {
				LOG.info("Nested Else If 5!");
			}
			else if(k == 6 && i != 3) {
				LOG.info("Nested Else If 6!");
			}
			else {
				if(k == 2) {
					LOG.info("Nested Else statement.");
				}
				
				LOG.info("Nested Else statement.");
				LOG.info("Nested Else statement.");
				LOG.info("Nested Else statement.");
			}
			
			LOG.info("End of If/ElseIf/Else");
		}
		
		if(i == 6) {
			LOG.info("Breaking Thought?");
		}
		else {
			LOG.info("Else Main");
		}
		int y = 5;
		LOG.info("Another!!"+y);
	}
}
