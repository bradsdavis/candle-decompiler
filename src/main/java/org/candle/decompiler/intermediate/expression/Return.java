package org.candle.decompiler.intermediate.expression;

import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;

import org.apache.bcel.generic.InstructionHandle;


public class Return extends Expression {

	private Expression child = null;
	
	public Return(InstructionHandle instructionHandle, Expression child) {
		super(instructionHandle);
		this.child = child;
	}
	
	public Return(InstructionHandle instructionHandle) {
		super(instructionHandle);
	}
	@Override
	public void write(Writer writer) throws IOException {
		writer.append("return");
		if(child != null) {
			writer.append(" ");
			child.write(writer);
		}
	}
	
	public Expression getChild() {
		return child;
	}

	@Override
	public Set<Expression> nestedExpression() {
		Set<Expression> expressions = new HashSet<Expression>(2);
		if(child!=null) {
			expressions.add(child);
		}
		return expressions;
	}
}
