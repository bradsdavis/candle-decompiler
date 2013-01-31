package org.candle.decompiler.ast.loop;

import java.io.IOException;
import java.io.Writer;

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
	public void write(Writer builder) throws IOException {
		final String indent = buildIndent();
		
		builder.append(indent);
		builder.append("while(");
		
		conditional.getExpression().write(builder);
		
		builder.append(") ");
		
		builder.append("{");
		for(Block child : children) {
			builder.append(NL);
			child.write(builder);
		}
		builder.append(NL);
		builder.append(indent);
		builder.append("}");
		builder.append(NL);
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
