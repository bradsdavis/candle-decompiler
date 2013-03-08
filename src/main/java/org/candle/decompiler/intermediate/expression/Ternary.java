package org.candle.decompiler.intermediate.expression;

import java.io.IOException;
import java.io.Writer;

import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.Type;
public class Ternary extends Expression {

	public Ternary(InstructionHandle instructionHandle,
			Type type, Expression logic, Expression left, Expression right) {
		super(instructionHandle);
		
		this.type = type;
		this.logic = logic;
		this.left = left;
		this.right = right;
	}

	private Type type;
	private Expression logic;
	private Expression left;
	private Expression right;
	
	@Override
	public void write(Writer writer) throws IOException {
		writer.write("(");
		logic.write(writer);
		writer.write(")");
		
		writer.write(" ? ");
		
		left.write(writer);
		
		writer.write(" : ");
		
		right.write(writer);
	}

}
