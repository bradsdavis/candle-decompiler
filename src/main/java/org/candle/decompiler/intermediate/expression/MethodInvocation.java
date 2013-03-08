package org.candle.decompiler.intermediate.expression;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import org.apache.bcel.generic.InstructionHandle;


public class MethodInvocation extends Expression {

	protected final Expression target;
	protected final String methodName;
	protected final List<Expression> parameters;

	public MethodInvocation(InstructionHandle instructionHandle, Expression target, String methodName, List<Expression> params) {
		super(instructionHandle);
		this.target = target;
		this.methodName = methodName;
		this.parameters = params;
	}
	
	public Expression getTarget() {
		return target;
	}
	
	public String getMethodName() {
		return methodName;
	}
	
	public List<Expression> getParameters() {
		return parameters;
	}
	
	@Override
	public void write(Writer val) throws IOException {
		target.write(val);
		val.append(".").append(methodName);
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
