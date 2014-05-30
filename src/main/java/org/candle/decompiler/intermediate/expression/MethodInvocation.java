package org.candle.decompiler.intermediate.expression;

import java.io.IOException;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;

import org.apache.bcel.generic.InstructionHandle;


public class MethodInvocation extends Expression {

	protected Expression target = null;
	protected String methodName;
	protected List<Expression> parameters;

	public MethodInvocation(InstructionHandle instructionHandle, Expression target, String methodName, List<Expression> params) {
		super(instructionHandle);
		this.setTarget(target);
		this.methodName = methodName;
		this.setParameters(params);
	}
	
	public void setTarget(Expression target) {
		if(this.target != null) {
			this.target.setParent(null);
		}
		this.target = target;
		
		if(target != null) {
			this.target.setParent(this);
		}
	}
	
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	
	public void setParameters(List<Expression> p) {
		if(this.parameters != null) {
			//unset all previous parents
			for(Expression exp : this.parameters) {
				exp.setParent(null);
			}
		}
		
		if(p == null) {
			this.parameters = new LinkedList<Expression>();
		}
		else {
			this.parameters = new LinkedList<Expression>(p);
			
			for(Expression exp : this.parameters) {
				exp.setParent(this);
			}
		}
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
	
	@Override
	public void visit(ASTListener listener) {
		listener.accept(this);
		if(getParameters() != null) {
			for(Expression exp : getParameters()) {
				listener.accept(exp);
			}
		}
	}
}
