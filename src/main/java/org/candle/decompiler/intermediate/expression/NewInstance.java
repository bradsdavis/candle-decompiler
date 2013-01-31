package org.candle.decompiler.intermediate.expression;

import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;

import org.apache.bcel.classfile.Utility;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.Type;


public class NewInstance extends ObjectType {

	public NewInstance(InstructionHandle instructionHandle, Type type) {
		super(instructionHandle, type);
	}
	
	@Override
	public void write(Writer writer) throws IOException {
		writer.append("new ");
		
		String signature = Utility.signatureToString(type.getSignature());
		writer.append(signature);
	}
	

	@Override
	public Set<Expression> nestedExpression() {
		Set<Expression> expressions = new HashSet<Expression>(2);
		return expressions;
	}

}
