package org.candle.decompiler.test;

public class TestSimpleLoops {

	public void testForLoop() {
		
		
		for(int i=0; i<5; i++) {
			System.out.println(i);
		}
		
		
		int[] testArray = new int[]{};
		for(int i : testArray) {
			System.out.println(i);
		}
		
		/*
		int k=0;
		int j=0;
		while(j < 5) {
			System.out.println("Test while.");
			j++;
			k++;
		}
		*/
	}
}
