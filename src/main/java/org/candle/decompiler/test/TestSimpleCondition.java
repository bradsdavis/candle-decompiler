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
		int[] testArray = new int[2];
		
		for(int i = 0, j=testArray.length; i<j; i++) {
			LOG.info(testArray[i]);
		}
		/*for(int x : testArray) {
			LOG.info("Enhanced For: "+x);
		}*/
	}
	
	public int testReturnInteger() {
		Integer someInt = 1;
		
		return someInt;
	}
	
	public void testCastDoubleFloat() {
		double doubleVal = 2d;
		float someFloat = (float)doubleVal;
		
		fieldVal = fieldVal + "x";
		fieldInt += 1;
	}
	
	public void testCondition(String condition) {
		final String unique = "unique";
		Integer someInt = Integer.valueOf(5);
		String x = "y";
		int testInt = 0;
		double testDouble = 0d;
		float testFloat = 0f;
		
		
		
		int negatedInt = testInt * -1;
		int negatedInt2 = ~testInt;
		int negatedInt3 = testInt ^ 5;
		
		int remainder = testInt%2;
		
		int devision = testInt / 2;
		
		int multiMath = (((testInt / 2) + 3) %2);
		
		if(condition.equals("test"))
		{
			System.out.println("test: "+condition);
			
			if(!condition.endsWith("another")) {
				System.out.println("another: " + condition);
				
			}
			else {
				System.out.println("Else clause.");
				
				for(int j=0; j<10; j++) {
					System.out.println("Value of J: "+j+"!!");
				}
			}
			
			
			
			
			
			int i=0;
			while(i < 5) {
				System.out.println("Testing While: "+i);
				i++;
				testInt++;
			}
			

			for(int j = 5; j>0; j-=2) {
				LOG.info("Logging J: "+j);
			}
			
		}
	}
	
	public void testTryCatch(String condition) 
	{
		final String unique = "unique";
		Integer someInt = new Integer(5);
		String x = "y";
		int testInt = 0;
		double testDouble = 0d;
		float testFloat = 0f;
		
		int negatedInt = testInt * -1;
		int negatedInt2 = ~testInt;
		int negatedInt3 = testInt ^ 5;
		

		LOG.debug(unique + x);
	
		try {
			LOG.info("Static Call 1.");
			
			if(unique instanceof String) {
				LOG.info("Instanceof Expression!");
			}
			
			if(unique != null || someInt == null) {
				LOG.info("Testing Null!!");
			}
			
			try {
				LOG.info("Nested Try");
			}
			catch(IllegalMonitorStateException e) {
				LOG.info("Nested Catch.");
			}
			
			LOG.info("Static Call 2.");
		}
		catch(Exception e) {
			LOG.error("Error Call 1!!", e);
			LOG.error("Error Call 2!!", e);
			LOG.error("Error Call 3!!", e);
			testInt++;
			testDouble++;
			testFloat++;
		}
	}
}
