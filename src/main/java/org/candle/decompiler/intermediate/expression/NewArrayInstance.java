package org.candle.decompiler.intermediate.expression;

import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;

import org.apache.bcel.classfile.Utility;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.Type;
import org.apache.commons.lang.StringUtils;

public class NewArrayInstance extends NewInstance {

	protected final Expression count;
	
	public NewArrayInstance(InstructionHandle instructionHandle, Type type, Expression count) {
		super(instructionHandle, type);
		this.count = count;
	}

	@Override
	public void write(Writer builder) throws IOException {
		builder.append("new ");
		
		String signature = Utility.signatureToString(type.getSignature());
		signature = StringUtils.removeEnd(signature, "[]");
		builder.append(signature);
		
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
