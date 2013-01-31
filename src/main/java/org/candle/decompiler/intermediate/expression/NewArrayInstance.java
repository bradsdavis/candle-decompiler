package org.candle.decompiler.intermediate.expression;

import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;

import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.Type;

public class NewArrayInstance extends NewInstance {

	private final Expression count;
	
	public NewArrayInstance(InstructionHandle instructionHandle, Type type, Expression count) {
		super(instructionHandle, type);
		this.count = count;
	}

	@Override
	public void write(Writer builder) throws IOException {
		super.write(builder);
		builder.append("[");
		count.write(builder);
		builder.append("]");
	}
	

	@Override
	public Set<Expression> nestedExpression() {
		Set<Expression> expressions = new HashSet<Expression>(2);
		expressions.add(count);
		return expressions;
	}
	
}
