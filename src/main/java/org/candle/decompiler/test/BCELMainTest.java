package org.candle.decompiler.test;

import org.candle.decompiler.Decompiler;



public class BCELMainTest {

	  public static void main(String[] argv) throws ClassNotFoundException
	  {
		  //String className = "org.candle.decompiler.test.TestSimpleCondition";
		  String className = "org.candle.decompiler.test.TestSimpleLoops";
		  //String className = "org.candle.decompiler.test.TestTryCatch";
		  //String className = "org.candle.decompiler.test.TestConditionsBlock";
		  
		  Decompiler decompiler = new Decompiler();

		  decompiler.decompile(className);
	  }
}
