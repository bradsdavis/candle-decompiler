package org.candle.decompiler.intermediate.expression;

import java.util.List;

import org.apache.bcel.generic.InstructionHandle;

public class ConstructorInvocation extends MethodInvocation {

	public ConstructorInvocation(InstructionHandle instructionHandle, Expression target, String methodName,
			List<Expression> params) {
		super(instructionHandle, target, methodName, params);
	}

	@Override
	public String generateSource() {
		StringBuilder val = new StringBuilder(target.generateSource());
		val.append("(");
		
		if(parameters!=null) {
			//for each parameter, serialize.
			for(int i=0, j=parameters.size(); i<j; i++) {
				//separate the parameters.
				if(i>0) {
					val.append(", ");
				}
				String paramVal = parameters.get(i).generateSource();
				val.append(paramVal);
			}
		}
		
		val.append(")");
		return val.toString();
	}
}
