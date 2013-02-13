package org.candle.decompiler.test;

public class TestArrayAssignment {

	public void testSwitch() throws Exception {
		TestArrayAssignment[] anewarray = new TestArrayAssignment[] { null, null, new TestArrayAssignment()};
		
		int[] arrayEx = new int[3];
		arrayEx[0] = 1;
		arrayEx[1] = 2;
		arrayEx[2] = 3;

		int m = 0;
		int[] compressed = new int[] { (m+1), 2, 3, 4 };
		
		byte[] byteArray = new byte[] { 100 };
		
		int twoD[][]= new int[4][5];
		double threeD[][][] = new double[3][4][5];
		byte fourD[][][][] = new byte[3][4][5][6];
		
		
		byte fourDThreeSet[][][][] = new byte[3][4][5][];
	}
}
