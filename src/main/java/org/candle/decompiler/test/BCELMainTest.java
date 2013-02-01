package org.candle.decompiler.test;

import org.candle.decompiler.CandleDecompiler;
import org.candle.decompiler.DecompilerException;



public class BCELMainTest {

	  public static void main(String[] argv) throws DecompilerException
	  {
		  //String className = "org.candle.decompiler.test.TestSimpleCondition";
		  //String className = "org.candle.decompiler.test.TestSimpleLoops";
		  //String className = "org.candle.decompiler.test.TestTryCatch";
		  String className = "org.candle.decompiler.test.TestConditionsBlock";
		  
		  CandleDecompiler decompiler = new CandleDecompiler();
		  decompiler.decompile(className);
		  
		  //File clz = new File("/Users/bradsdavis/Projects/workspace/clzTest/ObjectGridProvider.class");
		  //decompiler.decompile(clz, new File("/Users/bradsdavis/Projects/workspace/clzTest/test.src"));
	  }
}
