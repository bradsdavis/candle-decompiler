package org.candle.decompiler.ast.conditional;

import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.InstructionHandle;
import org.candle.decompiler.ast.Block;
import org.candle.decompiler.intermediate.code.GoToIntermediate;

public class ElseBlock extends Block {

	private final GoToIntermediate line;
	
	public ElseBlock(GoToIntermediate line) {
		this.line = line;
	}
	
	@Override
	public String generateSource() {
		final String indent = buildIndent();
		StringBuilder builder = new StringBuilder();
		
		builder.append(indent);
		builder.append("else ");
		builder.append("{");
		
		for(Block child : children) {
			builder.append(NL);
			builder.append(child.generateSource());
		}
		builder.append(NL);
		builder.append(indent);
		builder.append("}");
		
		return builder.toString();
	}
	
	@Override
	public int countPathToRoot() {
		return getParent().countPathToRoot();
	}

	@Override
	public InstructionHandle getInstruction() {
		return line.getInstruction();
	}
	
	@Override
	public int getStartBlockPosition() {
		return getInstruction().getPosition();
	}
	
	@Override
	public int getEndBlockPosition() {
		return ((BranchHandle)line.getInstruction()).getTarget().getPosition();
	}
}
