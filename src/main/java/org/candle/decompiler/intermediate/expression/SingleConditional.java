package org.candle.decompiler.intermediate.expression;

import java.io.IOException;
import java.io.Writer;

import org.apache.bcel.generic.InstructionHandle;

public class SingleConditional extends ConditionalExpression {

	private Expression expression;
	private boolean negated;
	
	public SingleConditional(InstructionHandle instructionHandle, Expression expression, boolean negated) {
		super(instructionHandle);
		this.expression = expression;
		this.negated = negated;
	}
	
	public SingleConditional(InstructionHandle instructionHandle, Expression left) {
		this(instructionHandle, left, false);
	}
	
	public Expression getExpression() {
		return expression;
	}
	
	public void setExpression(Expression expression) {
		this.expression = expression;
	}
	
	@Override
	public void visit(ASTListener listener) {
		listener.accept(this);
		listener.accept(getExpression());
	}

	@Override
	public void write(Writer val) throws IOException {
		
		//check to see whether to negate... 
		if(negated) {
			val.append("!(");
		}

		expression.write(val);
		
		//close parentheses around negate...
		if(negated) {
			val.append(")");
		}
	}

	@Override
	public void negate() {
		this.negated = !negated;
	}
	
}
