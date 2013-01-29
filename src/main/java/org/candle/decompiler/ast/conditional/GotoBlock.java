package org.candle.decompiler.ast.conditional;

import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.InstructionHandle;
import org.candle.decompiler.ast.Block;

public class GotoBlock extends Block {

	private final BranchHandle instruction;
	
	public GotoBlock(BranchHandle instruction) {
		this.instruction = instruction;
	}
	
	@Override
	public InstructionHandle getInstruction() {
		return instruction;
	}

	@Override
	public String generateSource() {
		StringBuilder builder = new StringBuilder("goto ");
		builder.append(super.generateSource());
		
		return builder.toString();
	}
	
	@Override
	public int getStartBlockPosition() {
		return getInstruction().getPosition();
	}
	
	@Override
	public int getEndBlockPosition() {
		return this.instruction.getTarget().getPrev().getPosition();
	}
}
