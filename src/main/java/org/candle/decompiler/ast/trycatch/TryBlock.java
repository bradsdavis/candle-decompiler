package org.candle.decompiler.ast.trycatch;

import java.util.ArrayList;
import java.util.List;

import org.apache.bcel.generic.CodeExceptionGen;
import org.apache.bcel.generic.InstructionHandle;
import org.candle.decompiler.ast.Block;

public class TryBlock extends Block {

	private final CodeExceptionGen codeExceptionGen;
	
	public TryBlock(CodeExceptionGen codeExceptionGen) {
		this.codeExceptionGen = codeExceptionGen;
	}
	
	public CodeExceptionGen getCodeExceptionGen() {
		return codeExceptionGen;
	}
	
	@Override
	public InstructionHandle getInstruction() {
		return codeExceptionGen.getStartPC();
	}
	
	@Override
	public int getStartBlockPosition() {
		return codeExceptionGen.getStartPC().getPosition();
	}

	@Override
	public int getEndBlockPosition() {
		return codeExceptionGen.getEndPC().getPosition();
	}
	

	public boolean within(InstructionHandle ih) {
		int position = ih.getPosition();

		if(position < getStartBlockPosition() || position > getEndBlockPosition()) {
			return false;
		}
		
		return true;
	}
	
	
	@Override
	public String generateSource() {
		final String indent = buildIndent();
		List<Block> catchBlocks = new ArrayList<Block>(); 
		
		StringBuilder builder = new StringBuilder();
		
		builder.append(indent);
		builder.append("try ");
		builder.append("{");
		
		for(Block child : children) {
			if(child instanceof CatchBlock) {
				catchBlocks.add(child);
				//skip these until end.
				continue;
			}
			builder.append(Block.NL);
			builder.append(child.generateSource());
		}
		builder.append(Block.NL);
		builder.append(indent);
		builder.append("}");
		builder.append(Block.NL);
		
		for(Block catchBlock : catchBlocks) {
			builder.append(catchBlock.generateSource());
		}
		
		return builder.toString();
	}
}
