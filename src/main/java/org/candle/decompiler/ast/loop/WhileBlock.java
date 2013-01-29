package org.candle.decompiler.ast.loop;

import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.InstructionHandle;
import org.candle.decompiler.ast.Block;
import org.candle.decompiler.intermediate.code.ConditionalIntermediate;

public class WhileBlock extends Block {

	
	private final ConditionalIntermediate conditional;
	
	public WhileBlock(ConditionalIntermediate conditional) {
		this.conditional = conditional;
	}
	
	public ConditionalIntermediate getConditional() {
		return conditional;
	}
	
	@Override
	public String generateSource() {
		final String indent = buildIndent();
		
		StringBuilder builder = new StringBuilder();
		builder.append(indent);
		builder.append("while(");
		builder.append(conditional.getExpression().generateSource());
		builder.append(") ");
		
		builder.append("{");
		for(Block child : children) {
			builder.append(NL);
			builder.append(child.generateSource());
		}
		builder.append(NL);
		builder.append(indent);
		builder.append("}");
		builder.append(NL);
		
		return builder.toString();
	}

	@Override
	public InstructionHandle getInstruction() {
		return conditional.getInstruction();
	}
	
	@Override
	public int getStartBlockPosition() {
		return conditional.getInstruction().getPosition();
	}
	
	@Override
	public int getEndBlockPosition() {
		BranchHandle branchHandle = ((BranchHandle)conditional.getInstruction());
		return branchHandle.getPosition();
	}
}
