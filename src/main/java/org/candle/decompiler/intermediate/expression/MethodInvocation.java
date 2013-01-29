package org.candle.decompiler.intermediate.expression;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
	
	@Override
	public String generateSource() {
		StringBuilder val = new StringBuilder(target.generateSource());
		val.append(".").append(methodName);
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
	
	@Override
	public String toString() {
		return generateSource();
	}
	
	@Override
	public Set<Expression> nestedExpression() {
		Set<Expression> expressions = new HashSet<Expression>(2);
		expressions.add(target);
		expressions.addAll(parameters);
		
		return expressions;
	}
	
}
