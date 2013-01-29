package org.candle.decompiler.ast;

import org.apache.bcel.generic.InstructionHandle;
import org.candle.decompiler.intermediate.code.StatementIntermediate;

public class Statement extends Block {

	private final StatementIntermediate line;
	
	public Statement(Block parent, StatementIntermediate line) {
		this.parent = parent;
		this.line = line;
	}
	
	public StatementIntermediate getLine() {
		return line;
	}

	@Override
	public String generateSource() {
		final String indent = buildIndent(); 
		StringBuilder builder = new StringBuilder();
		
		
		builder.append(indent);
		builder.append(line.getExpression().generateSource());
		builder.append(";");
		
		return builder.toString();
	}
	
	@Override
	public InstructionHandle getInstruction() {
		return line.getInstruction();
	}
	
	@Override
	public int getStartBlockPosition() {
		return line.getInstruction().getPosition();
	}
	
	@Override
	public int getEndBlockPosition() {
		return line.getInstruction().getPosition();
	}
	
	@Override
	public String toString() {
		return line.toString();
	}
}
