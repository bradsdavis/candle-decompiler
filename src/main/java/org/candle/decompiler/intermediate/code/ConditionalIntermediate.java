package org.candle.decompiler.intermediate.code;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.Set;

import org.apache.bcel.generic.InstructionHandle;
import org.candle.decompiler.blockinterpreter.Visitor;
import org.candle.decompiler.intermediate.expression.ConditionalExpression;
import org.candle.decompiler.intermediate.expression.Expression;

public class ConditionalIntermediate extends AbstractIntermediate {

	private ConditionalExpression expression;
	private AbstractIntermediate trueTarget;
	private AbstractIntermediate falseTarget;
	
	public ConditionalIntermediate(InstructionHandle instruction, ConditionalExpression expression) {
		super(instruction);
		this.expression = expression;
	}
	
	public void negate() {
		expression.negate();
		
		//swap true and false.
		AbstractIntermediate t = trueTarget;
		
		this.trueTarget = falseTarget;
		this.falseTarget = t;
	}
	
	public void setTrueTarget(AbstractIntermediate trueTarget) {
		this.trueTarget = trueTarget;
	}
	
	public void setFalseTarget(AbstractIntermediate falseTarget) {
		this.falseTarget = falseTarget;
	}
	
	public AbstractIntermediate getTrueTarget() {
		return trueTarget;
	}
	
	public AbstractIntermediate getFalseTarget() {
		return falseTarget;
	}
	
	public ConditionalExpression getExpression() {
		return expression;
	}
	
	public void setExpression(ConditionalExpression expression) {
		this.expression = expression;
	}
	
	@Override
	public String toString() {
		StringWriter sw = new StringWriter();
		try {
			this.expression.write(sw);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Conditional: "+sw.toString();
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visitAbstractLine(this);
		visitor.visitConditionalLine(this);
	}

	@Override
	public Set<Expression> nestedExpression() {
		Set<Expression> nested = new HashSet<Expression>();
		nested.add(expression);
		return nested;
	}

}
