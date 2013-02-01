package org.candle.decompiler.intermediate.code;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.bcel.generic.InstructionHandle;
import org.candle.decompiler.intermediate.expression.ConditionalExpression;
import org.candle.decompiler.intermediate.visitor.IntermediateVisitor;

public class ConditionalIntermediate extends AbstractIntermediate {

	protected ConditionalExpression expression;
	protected AbstractIntermediate trueTarget;
	protected AbstractIntermediate falseTarget;
	
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
	public void accept(IntermediateVisitor visitor) {
		visitor.visitAbstractLine(this);
		visitor.visitConditionalLine(this);
	}
}
