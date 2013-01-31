package org.candle.decompiler.ast;

import java.io.IOException;
import java.io.Writer;

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
	public void write(Writer builder) throws IOException {
		final String indent = buildIndent(); 
		
		builder.append(indent);
		line.getExpression().write(builder);
		builder.append(";");
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
