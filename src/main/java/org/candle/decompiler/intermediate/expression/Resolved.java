package org.candle.decompiler.intermediate.expression;

import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;

import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.Type;


public class Resolved extends Expression implements TypedExpression {

	private final Type type;
	private final String value;
	
	public Resolved(InstructionHandle instructionHandle, Type type, String value) {
		super(instructionHandle);
		this.type = type;
		this.value = value;
	}
	
	@Override
	public void write(Writer writer) throws IOException {
		writer.append(value);
	}
	
	public Type getType() {
		return type;
	}
	
	public String getValue() {
		return value;
	}
	
	@Override
	public Set<Expression> nestedExpression() {
		System.out.println("Calling nested on RESOLVED!");
		Set<Expression> expressions = new HashSet<Expression>(2);
		return expressions;
	}
}
