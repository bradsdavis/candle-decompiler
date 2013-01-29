package org.candle.decompiler.intermediate.code;

import java.util.HashSet;
import java.util.Set;

import org.apache.bcel.generic.InstructionHandle;
import org.candle.decompiler.blockinterpreter.Visitor;
import org.candle.decompiler.intermediate.expression.Expression;

public class StatementIntermediate extends AbstractIntermediate {

	private Expression expression;
	
	public StatementIntermediate(InstructionHandle instruction, Expression expression) {
		super(instruction);
		this.expression = expression;
	}
	
	public Expression getExpression() {
		return expression;
	}
	
	public void setExpression(Expression expression) {
		this.expression = expression;
	}
	
	@Override
	public String toString() {
		return expression.generateSource() + ";";
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visitAbstractLine(this);
		
		visitor.visitCompleteLine(this); 
	}
	
	@Override
	public Set<Expression> nestedExpression() {
		Set<Expression> nested = new HashSet<Expression>();
		nested.add(expression);
		return nested;
	}
}
