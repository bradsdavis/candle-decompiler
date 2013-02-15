package org.candle.decompiler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.apache.bcel.Repository;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;
import org.apache.commons.lang.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.candle.decompiler.ast.ClassBlock;
import org.candle.decompiler.ast.enhancer.RecursiveClassEnhancer;
import org.candle.decompiler.intermediate.ClassIntermediateVisitor;

public class CandleDecompiler {

	private static final Log LOG = LogFactory.getLog(CandleDecompiler.class);

	public void decompile(String clazzName) throws DecompilerException {
		Validate.isTrue(org.apache.commons.lang.StringUtils.isNotBlank(clazzName), "Classname is required, but not provided.");
		
		
		OutputStreamWriter osw = new OutputStreamWriter(System.out);
		try {
			this.decompile(clazzName, osw);
		}
		finally {
			//silently.
			if(osw != null) { 
				try {
					osw.flush();
					osw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}
		
	}
	
	public void decompile(String clazzName, Writer writer) throws DecompilerException {
		Validate.isTrue(org.apache.commons.lang.StringUtils.isNotBlank(clazzName), "Classname is required field, but was not provided.");
		Validate.notNull(writer, "Writer is required, but not provided.");
		
		JavaClass clz;
		try {
			clz = Repository.lookupClass(clazzName);
			decompile(clz, writer);
		} catch (ClassNotFoundException e) {
			throw new DecompilerException("Exception while decompiling.", e);
		}
		
	}
	
	public void decompile(File clz, File src) throws DecompilerException {
		Validate.notNull(clz, "Input file is required, but not provided.");
		Validate.notNull(src, "Output file is required, but not provided.");
		
		LOG.info("Processing: "+clz.getAbsolutePath());
		
		Writer writer = null;
		try {
			writer = new FileWriter(src);
			ClassParser cp = new ClassParser(clz.getAbsolutePath());
			decompile(cp.parse(), writer);
		}
		catch(Exception e) {
			throw new DecompilerException("Exception while decompiling.", e);
		}
		finally {
			if(writer != null) {
				try {
					writer.flush();
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void decompile(JavaClass clz, Writer writer) throws DecompilerException {
		Validate.notNull(clz, "Class is required, but not provided.");
		Validate.notNull(writer, "Writer is required, but not provided.");
		
		
		ClassIntermediateVisitor civ = new ClassIntermediateVisitor(clz);
		ClassBlock classBlock = civ.decompile();

		RecursiveClassEnhancer rce = new RecursiveClassEnhancer();
		rce.enhanceBlock(classBlock);
		
		try {
			classBlock.write(writer);
		} catch (IOException e) {
			throw new DecompilerException("Exception while decompiling.", e);
		}
		LOG.debug("Completed decompilation.");
	}
	
	
	
}
