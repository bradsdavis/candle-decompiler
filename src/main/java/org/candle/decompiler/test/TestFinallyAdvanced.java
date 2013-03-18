package org.candle.decompiler.test;


public class TestFinallyAdvanced {

	public static void main(String[] args) {
		boolean a = System.currentTimeMillis() < 0;
		
		while(true) {
			try {
				System.out.println("1");
			}
			catch(RuntimeException e) {
				if(a) {
					break;
				}
			}
			/*
			finally {
				if(a) {
					break;
				}
				System.out.println("2");
			}*/
			
			System.out.println("3");
		}
	}
}
