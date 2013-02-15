package org.candle.decompiler.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TestConditionsBlock {

	private static final Log LOG = LogFactory.getLog(TestConditionsBlock.class);
	
	public void testMultiOrCondition() throws Exception {
		int i=3;
		int j=5;
		int k=5;
		
		if(i == 5 || i == 4) {
			System.out.println("I < 3");
		}
		else {
			System.out.println("Not first condition.");
		}
		
		if(i == 5 && i == 4) {
			System.out.println("I < 3");
		}
		
		
		
		int[] testArray = new int[]{};
		for(int x : testArray) {
			System.out.println(x);
		} 
		
		if(i==3 || j==5 || k==1 && k < 10) {
			
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
