package org.candle.decompiler.ast.conditional;

import java.io.IOException;
import java.io.Writer;

import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.InstructionHandle;
import org.candle.decompiler.ast.Block;
import org.candle.decompiler.intermediate.code.ConditionalIntermediate;

public class IfBlock extends ConditionalBlock {

	private final ConditionalIntermediate conditional; 
	private int endBlockPosition;
	private final int startBlockPosition;
	private int targetBlockPosition;
	
	public IfBlock(ConditionalIntermediate conditional) {
		this.conditional = conditional;
		
		BranchHandle handle = (BranchHandle)conditional.getInstruction();
		this.startBlockPosition = handle.getPosition();
		this.endBlockPosition = handle.getTarget().getPosition();
		this.targetBlockPosition = handle.getTarget().getPosition();
	}
	
	public IfBlock(ConditionalIntermediate conditional, int startPosition, int endPosition, int targetPosition) {
		this.startBlockPosition = startPosition;
		this.endBlockPosition = endPosition;
		this.targetBlockPosition = targetPosition;
		
		this.conditional = conditional;
	}
	
	public ConditionalIntermediate getConditional() {
		return conditional;
	}
	 
	@Override
	public void write(Writer builder) throws IOException {
		final String indent = buildIndent();
		
		Block elseBlock = null;
		
		builder.append(indent);
		builder.append("if(");
		
		conditional.getExpression().write(builder);
		builder.append(") ");
		
		builder.append("{");
		for(Block child : children) {
			if(child instanceof ElseBlock) {
				elseBlock = child;
				continue;
			}
			
			builder.append(Block.NL);
			child.write(builder);
		}
		builder.append(Block.NL);
		builder.append(indent);
		builder.append("}");
		builder.append(Block.NL);
		
		if(elseBlock != null) {
			elseBlock.write(builder);
		}
	}

	@Override
	public InstructionHandle getInstruction() {
		return conditional.getInstruction();
	}
	
	@Override
	public int getStartBlockPosition() {
		return startBlockPosition;
	}
	
	@Override
	public int getEndBlockPosition() {
		return endBlockPosition;
	}
	
	public void setEndBlockPosition(int endBlockPosition) {
		this.endBlockPosition = endBlockPosition;
	}
	
	public void setTargetBlockPosition(int targetBlockPosition) {
		this.targetBlockPosition = targetBlockPosition;
	}
	
	@Override
	public boolean within(InstructionHandle ih) {
		return (ih.getPosition() > getStartBlockPosition() && ih.getPosition() < getEndBlockPosition());
	}
	
	@Override
	public String toString() {
		return "IfBlock: "+conditional.toString() + "["+getStartBlockPosition()+" -> "+getEndBlockPosition()+"]";
	}

	@Override
	public int getTargetBlockPosition() {
		return targetBlockPosition;
	}
}
