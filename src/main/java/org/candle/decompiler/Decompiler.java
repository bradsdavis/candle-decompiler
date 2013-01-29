package org.candle.decompiler;

import org.apache.bcel.Repository;
import org.apache.bcel.classfile.JavaClass;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.candle.decompiler.ast.ClassBlock;
import org.candle.decompiler.ast.enhancer.RecursiveClassEnhancer;
import org.candle.decompiler.intermediate.ClassIntermediateVisitor;

public class Decompiler {
	private static final Log LOG = LogFactory.getLog(Decompiler.class);
	
	public void decompile(String className) throws ClassNotFoundException {
		LOG.info("Decompiling: "+className);
		JavaClass clazz  = Repository.lookupClass(className);
		
		ClassIntermediateVisitor civ = new ClassIntermediateVisitor(clazz);
		ClassBlock classBlock = civ.decompile();

		RecursiveClassEnhancer rce = new RecursiveClassEnhancer();
		rce.enhanceBlock(classBlock);
		
		//TODO: push out the source.  Change this to take an output stream / writer.
		LOG.info(classBlock.generateSource());
	}
	
}
