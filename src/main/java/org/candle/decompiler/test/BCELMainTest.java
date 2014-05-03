package org.candle.decompiler.test;

import java.io.File;

import org.candle.decompiler.CandleDecompiler;
import org.candle.decompiler.DecompilerException;



public class BCELMainTest {

	  public static void main(String[] argv) throws DecompilerException
	  {
		  
		  //TODO: Fix TRY/CATCH/Finally
		  //Fix: Array Iterator
		  //Fix: Else clause in last switch statement.
		  
		  //String className = "org.candle.decompiler.test.TestSimpleCondition";
		  String className = "org.candle.decompiler.test.TestTryCatch";

		 // String className = "org.candle.decompiler.test.TestSwitch";
		  
		// String className = "org.candle.decompiler.test.TestConditionsBlock";
		 //String className = "org.candle.decompiler.test.TestSwitch";
		 // String className = "org.candle.decompiler.test.TestFinallyAdvanced";
		  
		  //works
		 // String className = "org.candle.decompiler.test.TestArrayAssignment";
		  //String className = "org.candle.decompiler.test.TestSimpleLoops";
			  
		  CandleDecompiler decompiler = new CandleDecompiler();
		  decompiler.decompile(className);
		  
		  //File clz = new File("/Users/bradsdavis/Projects/workspace/clzTest/ObjectGridProvider.class");
		  //decompiler.decompile(clz, new File("/Users/bradsdavis/Projects/workspace/clzTest/test.src"));
		  
		 File clz = new File("/Users/bradsdavis/Projects/workspace/clzTest/ClassLoaderAdapterCallback.class");
		 //decompiler.decompile(clz, new File("/Users/bradsdavis/Projects/workspace/clzTest/test.src"));
	  }
}
