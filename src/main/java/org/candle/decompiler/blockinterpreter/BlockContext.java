package org.candle.decompiler.blockinterpreter;

import java.util.HashMap;
import java.util.Map;

import org.apache.bcel.generic.CodeExceptionGen;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.MethodGen;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.candle.decompiler.ast.Block;
import org.candle.decompiler.ast.ClassBlock;
import org.candle.decompiler.ast.MethodBlock;

public class BlockContext {
	private static final Log LOG = LogFactory.getLog(BlockContext.class);
	
	private final Map<InstructionHandle, CodeExceptionGen> tryBlockStart;
	private final Map<InstructionHandle, CodeExceptionGen> catchBlockStart;
	
	private final ClassBlock classBlock;
	private final MethodGen methodGen;
	private final Block methodBlock;
	
	
	private Block currentBlock;
	
	public BlockContext(MethodGen methodGen, ClassBlock clazz, MethodBlock method) {
		this.methodGen = methodGen;
		this.classBlock = clazz;
		this.methodBlock = method;
		
		this.tryBlockStart = new HashMap<InstructionHandle, CodeExceptionGen>();
		this.catchBlockStart = new HashMap<InstructionHandle, CodeExceptionGen>();
		
		for(CodeExceptionGen ceg : methodGen.getExceptionHandlers()) {
			//map the try and catch.
			tryBlockStart.put(ceg.getStartPC(), ceg);
			catchBlockStart.put(ceg.getHandlerPC(), ceg);
		}
	}

	public Map<InstructionHandle, CodeExceptionGen> getTryBlockStart() {
		return tryBlockStart;
	}
	
	public Map<InstructionHandle, CodeExceptionGen> getCatchBlockStart() {
		return catchBlockStart;
	}
	
	public ClassBlock getClassBlock() {
		return classBlock;
	}
	
	public void setCurrentBlock(Block currentBlock) {
		this.currentBlock = currentBlock;
	}
	
	public Block getCurrentBlock() {
		return currentBlock;
	}
	
	public void setCurrentBlockByHandle(InstructionHandle handle) {
		//search down.
		Block target = findBlockByHandle(methodBlock, handle);
		
		if(target == null) {
			throw new IllegalStateException("Target not found.");
		}
		this.setCurrentBlock(target);
	}
	
	public Block findBlockByHandle(Block block, InstructionHandle handle) {
		//start at root.
		if(block.getInstruction() == handle) {
			return block;
		}
		else {
			if(block.getChildren() != null) {
				for(Block child : block.getChildren()) {
					Block test = findBlockByHandle(child, handle);
					
					if(test != null && test.getInstruction() == handle) {
						return test;
					}
				}
			}
		}
		
		return null;
	}
	
	public MethodGen getMethodGen() {
		return methodGen;
	}
	
	public void moveUp() {
		if(this.getCurrentBlock().getParent() != null) {
			this.setCurrentBlock(this.getCurrentBlock().getParent());
		}
		else {
			LOG.warn("Called moveUp, but there is no parent.");
		}
	}
}
