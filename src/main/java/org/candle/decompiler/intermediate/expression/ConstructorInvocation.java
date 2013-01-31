package org.candle.decompiler.intermediate.expression;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import org.apache.bcel.generic.InstructionHandle;

public class ConstructorInvocation extends MethodInvocation {

	public ConstructorInvocation(InstructionHandle instructionHandle, Expression target, String methodName,
			List<Expression> params) {
		super(instructionHandle, target, methodName, params);
	}

	@Override
	public void write(Writer val) throws IOException {
		target.write(val);
		val.append("(");
		
		if(parameters!=null) {
			//for each parameter, serialize.
			for(int i=0, j=parameters.size(); i<j; i++) {
				//separate the parameters.
				if(i>0) {
					val.append(", ");
				}
				parameters.get(i).write(val);
			}
		}
		val.append(")");
	}
}
